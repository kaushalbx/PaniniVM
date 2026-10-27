package dev.panini.plugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.util.concurrency.AppExecutorUtil
import dev.panini.execution.PvmUktiSadhaka
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.Ukti
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * PvmEditorInlayListener updates inline hints dynamically as the user edits .pvm files.
 * Offloads all CPU-heavy Sandhi and VM computations to background thread pool tasks to maintain editor responsiveness.
 */
class PvmEditorInlayListener : EditorFactoryListener {

    private companion object {
        const val UPDATE_DELAY_MS = 300L
    }

    private val documentListeners = ConcurrentHashMap<Editor, DocumentListener>()
    private val pendingComputations = ConcurrentHashMap<Editor, Future<*>>()

    private data class InlaySnapshot(
        val isPvmFile: Boolean,
        val document: ProgramDocument?,
        val text: String,
        val modificationStamp: Long,
    )

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor

        ApplicationManager.getApplication().invokeLater {
            if (!editor.isDisposed) {
                scheduleInlayUpdate(editor)
            }
        }

        val listener = object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                ApplicationManager.getApplication().invokeLater {
                    if (!editor.isDisposed) {
                        scheduleInlayUpdate(editor)
                    }
                }
            }
        }
        editor.document.addDocumentListener(listener)
        documentListeners[editor] = listener
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        pendingComputations.remove(editor)?.cancel(true)
        val listener = documentListeners.remove(editor)
        if (listener != null) {
            editor.document.removeDocumentListener(listener)
        }
    }

    private fun scheduleInlayUpdate(editor: Editor) {
        val project = editor.project ?: return
        if (project.isDisposed) return

        // Debounce rapid edits and interrupt work that has not yet completed.
        pendingComputations.remove(editor)?.cancel(true)

        val snapshot = runReadAction {
            val psiFile = if (project.isDisposed) null
            else PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
            val pvmFile = psiFile as? PvmFile
            InlaySnapshot(
                isPvmFile = pvmFile != null,
                document = pvmFile?.canonicalDocument(),
                text = editor.document.text,
                modificationStamp = editor.document.modificationStamp,
            )
        }
        if (!snapshot.isPvmFile) return

        val text = snapshot.text
        val modificationStamp = snapshot.modificationStamp

        val future = AppExecutorUtil.getAppScheduledExecutorService().schedule({
            if (Thread.currentThread().isInterrupted) return@schedule

            val inlineInlayEntries = snapshot.document?.let { canonicalDocument ->
                PvmInlayHints.compute(text, canonicalDocument) { Thread.currentThread().isInterrupted }
            }.orEmpty()

            // Once background computations are complete, schedule visual rendering on EDT
            ApplicationManager.getApplication().invokeLater {
                if (editor.isDisposed) return@invokeLater
                if (editor.document.modificationStamp != modificationStamp) return@invokeLater

                // Dispose previous inline inlays so live edits replace stale sentence hints
                val existingInlineInlays = editor.inlayModel.getInlineElementsInRange(0, editor.document.textLength)
                for (inlay in existingInlineInlays) {
                    if (inlay.renderer is PvmInlineInlayRenderer) {
                        inlay.dispose()
                    }
                }

                // Add fresh inline inlays after Danda at end of each sentence
                for (entry in inlineInlayEntries) {
                    val offset = entry.first.coerceIn(0, editor.document.textLength)
                    editor.inlayModel.addInlineElement(
                        offset,
                        true, // relatesToPreceding
                        PvmInlineInlayRenderer(entry.second)
                    )
                }

                editor.contentComponent.repaint()
            }
        }, UPDATE_DELAY_MS, TimeUnit.MILLISECONDS)

        pendingComputations[editor] = future
    }
}

/** Pure derivation previews: this path must never invoke [dev.panini.execution.PaniniVM]. */
internal object PvmInlayHints {
    fun compute(
        source: String,
        document: ProgramDocument,
        isCanceled: () -> Boolean = { false },
    ): List<Pair<Int, String>> {
        val sadhaka = PvmUktiSadhaka()
        return document.items.zip(document.itemSpans).mapNotNull { (item, span) ->
            if (isCanceled()) return emptyList()
            if (item !is Ukti || span.endExclusive > source.length) return@mapNotNull null
            val itemSource = source.substring(span.start, span.endExclusive)
            val surface = runCatching { sadhaka.sadhayaLine(itemSource) }.getOrDefault("")
            val display = surface.replace("॥", "").replace("।", "")
                .replace(Regex("\\s+"), " ").trim()
            display.takeIf(String::isNotBlank)?.let { span.endExclusive to it }
        }
    }
}
