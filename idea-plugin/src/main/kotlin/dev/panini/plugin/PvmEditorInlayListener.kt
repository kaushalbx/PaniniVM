package dev.panini.plugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.psi.PsiDocumentManager
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmUktiSadhaka
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

/**
 * PvmEditorInlayListener updates inline hints dynamically as the user edits .pvm files.
 * Offloads all CPU-heavy Sandhi and VM computations to background thread pool tasks to maintain editor responsiveness.
 */
class PvmEditorInlayListener : EditorFactoryListener {

    private val documentListeners = ConcurrentHashMap<Editor, DocumentListener>()
    private val pendingComputations = ConcurrentHashMap<Editor, Future<*>>()

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor

        ApplicationManager.getApplication().invokeLater {
            if (!editor.isDisposed) {
                updateInlays(editor)
            }
        }

        val listener = object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                ApplicationManager.getApplication().invokeLater {
                    if (!editor.isDisposed) {
                        updateInlays(editor)
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
            runCatching {
                editor.document.removeDocumentListener(listener)
            }
        }
    }

    private fun updateInlays(editor: Editor) {
        val project = editor.project ?: return
        if (project.isDisposed) return

        // Cancel previous pending task for this editor to avoid redundant calculations
        pendingComputations.remove(editor)?.cancel(true)

        // Verify the document is a PvmFile before doing any compute-heavy operations
        val isPvm = runReadAction {
            if (project.isDisposed) false
            else {
                val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
                psiFile is PvmFile
            }
        }
        if (!isPvm) return

        val text = try {
            editor.document.text
        } catch (_: Throwable) {
            return
        }
        if (text.isBlank()) return

        val future = ApplicationManager.getApplication().executeOnPooledThread {
            if (Thread.currentThread().isInterrupted) return@executeOnPooledThread

            val sadhaka = PvmUktiSadhaka()
            val vm = PaniniVM()
            val lines = text.lines()
            var currentOffset = 0

            val virtualFile = editor.virtualFile
            if (virtualFile != null && virtualFile.extension == "pvm") {
                runCatching {
                    val txtFile = File(virtualFile.path.substringBeforeLast('.') + ".txt")
                    txtFile.writeText(sadhaka.sadhayaScript(text) + "\n")
                }
            }

            val inlineInlayEntries = mutableListOf<Pair<Int, String>>()

            for (line in lines) {
                if (Thread.currentThread().isInterrupted) return@executeOnPooledThread
                val trimmed = line.trim()
                val lineEnd = currentOffset + line.length

                val codeWithoutComment = when {
                    trimmed.startsWith("#") || trimmed.startsWith("//") -> ""
                    trimmed.contains("#") -> trimmed.substringBefore("#").trim()
                    trimmed.contains("//") -> trimmed.substringBefore("//").trim()
                    else -> trimmed
                }

                if (codeWithoutComment.isNotEmpty()) {
                    val lineHasDanda = codeWithoutComment.contains("।") || codeWithoutComment.contains("॥")

                    var surface = try { sadhaka.sadhayaLine(codeWithoutComment) } catch (_: Throwable) { "" }
                    if (surface.isBlank() || surface == codeWithoutComment) {
                        val res = try { vm.eval(codeWithoutComment) } catch (_: Throwable) { null }
                        if (res is ExecutionResult.Success && res.value.isNotBlank()) {
                            surface = res.value
                        }
                    }

                    if (surface.isNotBlank()) {
                        val displayHint = if (!lineHasDanda) {
                            surface
                                .replace("॥", "")
                                .replace("।", "")
                                .replace(Regex("\\s+"), " ")
                                .trim()
                        } else {
                            surface.trim()
                        }

                        if (displayHint.isNotBlank()) {
                            inlineInlayEntries.add(Pair(lineEnd, displayHint))
                        }
                    }
                }

                currentOffset = lineEnd + 1
            }

            // Once background computations are complete, schedule visual rendering on EDT
            ApplicationManager.getApplication().invokeLater {
                if (editor.isDisposed) return@invokeLater

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

                try {
                    editor.contentComponent.repaint()
                } catch (_: Throwable) {
                }
            }
        }

        pendingComputations[editor] = future
    }
}
