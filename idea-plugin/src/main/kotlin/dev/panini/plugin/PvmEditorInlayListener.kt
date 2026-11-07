package dev.panini.plugin

import dev.panini.analysis.karakaLabel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.JBTextArea
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.ui.JBUI
import dev.panini.execution.PvmUktiSadhaka
import dev.panini.execution.PrakriyaArgumentBindingKind
import dev.panini.execution.PrakriyaArgumentResolution
import dev.panini.execution.PrakriyaInvocationArgumentResolver
import dev.panini.execution.PrakriyaRegistry
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.registerDeclarations
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.UktiAnalysis
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.SourceLocatedNode
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
    private val mouseListeners = ConcurrentHashMap<Editor, EditorMouseListener>()
    private val pendingComputations = ConcurrentHashMap<Editor, Future<*>>()

    private data class InlaySnapshot(
        val isPvmFile: Boolean,
        val document: ProgramDocument?,
        val text: String,
        val modificationStamp: Long,
        val sourceFile: String?,
        val projectDocuments: List<Pair<String?, ProgramDocument>>,
        val grammarAnalyses: List<UktiAnalysis>,
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

        val mouseListener = object : EditorMouseListener {
            override fun mouseClicked(event: EditorMouseEvent) {
                val inlay = editor.inlayModel.getElementAt(event.mouseEvent.point) ?: return
                val renderer = inlay.renderer as? PvmInlineInlayRenderer ?: return
                val explanation = renderer.explanation ?: return
                // Swing's HTML renderer does not consistently shape Devanagari combining marks.
                // A plain text component keeps the Unicode text intact and uses the selected font directly.
                val textArea = JBTextArea(explanation).apply {
                    font = PvmUiFonts.forText(editor.component.font, explanation)
                    isEditable = false
                    isFocusable = false
                    isOpaque = false
                    lineWrap = true
                    wrapStyleWord = true
                    columns = 48
                    border = JBUI.Borders.empty(8, 10)
                }
                JBPopupFactory.getInstance()
                    .createComponentPopupBuilder(textArea, null)
                    .setRequestFocus(false)
                    .setResizable(false)
                    .setMovable(false)
                    .createPopup()
                    .show(RelativePoint(event.mouseEvent))
            }
        }
        editor.addEditorMouseListener(mouseListener)
        mouseListeners[editor] = mouseListener
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        pendingComputations.remove(editor)?.cancel(true)
        val listener = documentListeners.remove(editor)
        if (listener != null) {
            editor.document.removeDocumentListener(listener)
        }
        mouseListeners.remove(editor)?.let(editor::removeEditorMouseListener)
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
                sourceFile = pvmFile?.virtualFile?.url,
                projectDocuments = pvmFile?.let { anchor ->
                    PvmProjectFileIndex.documents(anchor).map { (file, document) ->
                        file.virtualFile?.url to document
                    }
                }.orEmpty(),
                grammarAnalyses = pvmFile?.canonicalGrammarAnalyses().orEmpty(),
            )
        }
        if (!snapshot.isPvmFile) return

        val text = snapshot.text
        val modificationStamp = snapshot.modificationStamp

        val future = AppExecutorUtil.getAppScheduledExecutorService().schedule({
            if (Thread.currentThread().isInterrupted) return@schedule

            val inlineInlayEntries = snapshot.document?.let { canonicalDocument ->
                val registry = PrakriyaRegistry().also { registry ->
                    snapshot.projectDocuments.forEach { (sourceFile, document) ->
                        registry.registerDeclarations(PvmScript.fromDocument(document), sourceFile)
                    }
                }
                PvmInlayHints.compute(
                    text,
                    canonicalDocument,
                    registry,
                    snapshot.sourceFile,
                    snapshot.grammarAnalyses,
                ) { Thread.currentThread().isInterrupted }
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
                    val offset = entry.offset.coerceIn(0, editor.document.textLength)
                    editor.inlayModel.addInlineElement(
                        offset,
                        true, // relatesToPreceding
                        PvmInlineInlayRenderer(entry.text, entry.explanation, entry.attributesKey)
                    )
                }

                editor.contentComponent.repaint()
            }
        }, UPDATE_DELAY_MS, TimeUnit.MILLISECONDS)

        pendingComputations[editor] = future
    }
}

/** Pure derivation previews: this path must never invoke [dev.panini.execution.PaniniVM]. */
internal data class PvmInlayHint(
    val offset: Int,
    val text: String,
    val explanation: String? = null,
    val attributesKey: com.intellij.openapi.editor.colors.TextAttributesKey =
        PvmSyntaxHighlighter.INLAY_SURFACE,
)

internal object PvmInlayHints {
    fun compute(
        source: String,
        document: ProgramDocument,
        registry: PrakriyaRegistry? = null,
        callerSourceFile: String? = null,
        grammarAnalyses: List<UktiAnalysis>? = null,
        isCanceled: () -> Boolean = { false },
    ): List<PvmInlayHint> {
        val sadhaka = PvmUktiSadhaka()
        val surfaceHints = document.items.zip(document.itemSpans).mapNotNull { (item, span) ->
            if (isCanceled()) return emptyList()
            if (item !is Ukti || span.endExclusive > source.length) return@mapNotNull null
            val itemSource = source.substring(span.start, span.endExclusive)
            val surface = runCatching { sadhaka.sadhayaLine(itemSource) }.getOrDefault("")
            val display = surface.replace("॥", "").replace("।", "")
                .replace(Regex("\\s+"), " ").trim()
            display.takeIf(String::isNotBlank)?.let {
                PvmInlayHint(
                    span.endExclusive,
                    it,
                    attributesKey = PvmSyntaxHighlighter.INLAY_SURFACE,
                )
            }
        }
        if (registry == null || isCanceled()) return surfaceHints

        val knownOperations = registry.all().mapTo(linkedSetOf()) { it.nameStem }
        val sentences = PvmScript.fromDocument(document).flatMap { statement ->
                when (statement) {
                    is PvmScriptStatement.Sentence -> listOf(statement)
                    is PvmScriptStatement.PrakriyaDefinition -> statement.body
                    else -> emptyList()
                }
            }
        val semanticHints = buildList {
            VyakaranamExecutionAdapter.nonKriyaWords(document).filter { it.start >= 0 }.forEach { add(PvmInlayHint(it.start, "${it.reading} =", it.text, PvmSyntaxHighlighter.INLAY_KARAKA)) }
            for (sentence in sentences) {
                if (isCanceled()) return emptyList()
                val ukti = sentence.ukti ?: continue
                val parameterHintSpans = mutableSetOf<dev.panini.vyakaranam.ast.DocumentSourceSpan>()
                val analysis = VyakaranamExecutionAdapter.analyzeProcedureCall(ukti, knownOperations)
                    ?: grammarAnalyses?.firstOrNull { it.ukti === ukti }
                    ?: runCatching { VyakaranamExecutionAdapter.analyzeAsWritten(ukti) }.getOrNull()
                analysis?.frames?.forEach { frame ->
                    val span = (frame.kriya?.analysis?.pada as? SourceLocatedNode)?.sourceSpan
                        ?: return@forEach
                    add(PvmInlayHint(
                        span.start,
                        "क्रिया =",
                        kriyaExplanation(frame),
                        PvmSyntaxHighlighter.INLAY_KRIYA,
                    ))
                }
                val invocation = sentence.ukti?.let {
                    runCatching {
                        registry.detectInvocation(it, callerSourceFile, staticAnalysis = true)
                    }.getOrNull()
                }
                if (invocation != null) {
                    val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation, knownOperations)
                        as? PrakriyaArgumentResolution.Success ?: continue
                    resolution.arguments.forEach { resolved ->
                        if (resolved.bindingKind != PrakriyaArgumentBindingKind.POSITIONAL) {
                            return@forEach
                        }
                        val parameter = resolved.parameter ?: return@forEach
                        val span = (resolved.argument.pada as? SourceLocatedNode)?.sourceSpan
                            ?: return@forEach
                        parameterHintSpans += span
                        add(PvmInlayHint(
                            span.start,
                            "${parameter.nameStem}:",
                            buildString {
                                appendLine("प्रक्रिया-मानम्: ${parameter.nameStem}")
                                appendLine("प्रकारः: ${parameter.type.sanskritName}")
                                append("प्रक्रिया: ${invocation.kriya.nameStem}")
                            },
                            PvmSyntaxHighlighter.INLAY_PARAMETER,
                        ))
                    }
                }

                analysis?.frames?.flatMap { frame -> frame.relations.map { frame to it } }?.forEach { (frame, relation) ->
                    val label = frame.karakaLabel(relation) ?: return@forEach
                    val span = (relation.participant.pada as? SourceLocatedNode)?.sourceSpan
                        ?: return@forEach
                    if (span in parameterHintSpans) return@forEach
                    add(PvmInlayHint(
                        span.start,
                        "$label =",
                        buildString {
                            val padaSource = source.substring(span.start, span.endExclusive)
                            appendLine("पदम्: $padaSource")
                            appendLine("कारकम्: $label")
                            frameFor(analysis.frames, relation.kriyaId)?.let { frame ->
                                appendLine("क्रिया: ${frame.kriya?.analysis?.pada?.dhatu?.mulaDhatu.orEmpty()}")
                            }
                            if (relation.evidence.isNotEmpty()) {
                                appendLine("अष्टाध्यायी-प्रमाणम्:")
                                relation.evidence.forEachIndexed { index, evidence ->
                                    append("${evidence.sutra} ${evidence.text} — ${evidence.reason}")
                                    if (index != relation.evidence.lastIndex) appendLine()
                                }
                            }
                        }.trimEnd(),
                        PvmSyntaxHighlighter.INLAY_KARAKA,
                    ))
                }
            }
        }.distinct()
        return (semanticHints + surfaceHints).sortedBy(PvmInlayHint::offset)
    }

    private fun frameFor(frames: List<KriyaFrame>, id: dev.panini.analysis.KriyaId): KriyaFrame? =
        frames.firstOrNull { it.id == id }

    private fun kriyaExplanation(frame: KriyaFrame): String = buildString {
        val kriya = frame.kriya ?: return@buildString
        val pada = kriya.analysis.pada
        appendLine("क्रिया: ${pada.sourceText}")
        appendLine("धातुः: ${kriya.dhatu?.upadesha ?: pada.dhatu.mulaDhatu}")
        append("प्रयोगः: ${frame.prayoga.name}")
    }
}
