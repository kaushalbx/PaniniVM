package dev.panini.plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.panini.analysis.FrameDiagnostic
import dev.panini.analysis.FrameDiagnosticSeverity
import dev.panini.analysis.KriyaFrame
import dev.panini.execution.PrakriyaScriptValidator
import dev.panini.execution.PrakriyaDiagnosticSeverity
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

class PvmAnnotator : Annotator {
    private val paniniParser = PaniniParser()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiFile || element !is PvmFile) return

        val text = element.text
        if (text.isBlank()) return

        // A .pvm file is a script, not one grammatical utterance. Prakriya
        // declarations legitimately contain several danda-delimited sentences,
        // so validate the script structure before falling back to a single-
        // utterance diagnostic for incomplete or malformed editor text.
        val canonicalDocument = element.canonicalDocument()
        if (canonicalDocument != null) {
            highlightSemanticIdentifiers(
                element,
                holder,
                canonicalDocument.items.flatMap { item ->
                    item.canonicalLocatedPadas().filterIsInstance<SubantaPada>().mapNotNull {
                        (it as SourceLocatedNode).sourceSpan
                    }
                },
                PvmSyntaxHighlighter.NOMINAL_STEM,
                setOf(PvmTokenTypes.IDENTIFIER),
            )
            highlightSemanticIdentifiers(
                element, holder, PvmSymbolResolver.prakriyaDeclarationSpans(canonicalDocument),
                PvmSyntaxHighlighter.PRAKRIYA_DECLARATION,
            )
            highlightSemanticIdentifiers(
                element, holder, PvmSymbolResolver.prakriyaReferenceSpans(canonicalDocument),
                PvmSyntaxHighlighter.PRAKRIYA_REFERENCE,
            )
            highlightSemanticIdentifiers(
                element, holder, PvmSymbolResolver.domainDeclarationSpans(canonicalDocument),
                PvmSyntaxHighlighter.DOMAIN_DECLARATION,
            )
            highlightSemanticIdentifiers(
                element, holder, PvmSymbolResolver.domainReferenceSpans(canonicalDocument),
                PvmSyntaxHighlighter.DOMAIN_REFERENCE,
            )
            highlightSemanticIdentifiers(
                element,
                holder,
                PsiTreeUtil.findChildrenOfType(element, PvmPrakriyaParameterPsi::class.java).map {
                    dev.panini.vyakaranam.ast.DocumentSourceSpan(it.textOffset, it.textRange.endOffset)
                },
                PvmSyntaxHighlighter.PRAKRIYA_PARAMETER,
            )
            highlightSemanticIdentifiers(
                element,
                holder,
                PsiTreeUtil.findChildrenOfType(element, PvmInvocationArgumentPsi::class.java)
                    .mapNotNull { argument ->
                        val resolved = argument.resolvedPrakriyaArgument() ?: return@mapNotNull null
                        (resolved.argument.pada as? SourceLocatedNode)?.sourceSpan
                    },
                PvmSyntaxHighlighter.PRAKRIYA_ARGUMENT,
            )
            highlightSemanticIdentifiers(
                element,
                holder,
                PvmPrakriyaCallSemantics.namedParameterReferenceSpans(element) +
                    PvmPrakriyaCallSemantics.bodyParameterReferenceSpans(element),
                PvmSyntaxHighlighter.PRAKRIYA_PARAMETER_REFERENCE,
            )
            annotateUnresolvedProjectReferences(element, canonicalDocument, holder)
            annotateDiagnostics(holder, text, PrakriyaScriptValidator.validate(text, canonicalDocument))
            annotateKriyaFrameDiagnostics(element, holder)
            return
        }

        // Parsing may fail after syntax recognition with a typed semantic
        // diagnostic. Preserve that canonical code and span instead of reducing
        // it to a generic live-parser error.
        val semanticDiagnostics = runCatching { PrakriyaScriptValidator.validate(text) }
            .getOrDefault(emptyList())
            .filter { it.code != "INVALID_SOURCE" || it.replacement != null }
        annotateDiagnostics(holder, text, semanticDiagnostics)
        if (semanticDiagnostics.any { it.severity == PrakriyaDiagnosticSeverity.ERROR }) return

        // ANTLR live syntax error highlighting for malformed/incomplete text.
        try {
            val syntaxErrors = paniniParser.validate(text)
            for (error in syntaxErrors) {
                val offset = calculateOffset(text, error.line, error.column)
                val length = (error.offendingText?.length ?: 1).coerceAtLeast(1)
                val start = offset.coerceIn(0, text.length)
                val end = (start + length).coerceIn(start, text.length)

                holder.newAnnotation(HighlightSeverity.ERROR, "PaniniVM syntax error: ${error.message}")
                    .range(TextRange(start, end))
                    .create()
            }
        } catch (_: Exception) {
            // Ignore syntax validation errors during live editing
        }
    }

    /** Render the analyzer's own kāraka/kriyā diagnostics at canonical AST spans. */
    private fun annotateKriyaFrameDiagnostics(
        file: PvmFile,
        holder: AnnotationHolder,
    ) {
        val emitted = mutableSetOf<Triple<dev.panini.analysis.FrameDiagnosticCode, Int, Int>>()
        for (analysis in file.canonicalGrammarAnalyses()) {
            ProgressManager.checkCanceled()
            for (frame in analysis.frames) {
                for (diagnostic in frame.diagnostics) {
                    val span = diagnosticSpan(frame, diagnostic) ?: continue
                    if (!emitted.add(Triple(diagnostic.code, span.start, span.endExclusive))) continue
                    holder.newAnnotation(diagnostic.severity.highlightSeverity(), diagnostic.message)
                        .range(TextRange(span.start, span.endExclusive))
                        .create()
                }
            }
            for (diagnostic in analysis.diagnostics) {
                val frame = analysis.frames.firstOrNull { it.vakya.sourceText == diagnostic.sourceText }
                    ?: continue
                val span = diagnosticSpan(frame, diagnostic) ?: continue
                if (!emitted.add(Triple(diagnostic.code, span.start, span.endExclusive))) continue
                holder.newAnnotation(diagnostic.severity.highlightSeverity(), diagnostic.message)
                    .range(TextRange(span.start, span.endExclusive))
                    .create()
            }
        }
    }

    private fun diagnosticSpan(frame: KriyaFrame, diagnostic: FrameDiagnostic): DocumentSourceSpan? {
        val exact = buildList {
            frame.relations.forEach { add(it.participant.pada) }
            frame.kriya?.analysis?.pada?.let(::add)
        }.filter { it.sourceText == diagnostic.sourceText }
            .mapNotNull { (it as? SourceLocatedNode)?.sourceSpan }
        if (exact.isNotEmpty()) return exact.minByOrNull { it.endExclusive - it.start }

        if (diagnostic.sourceText != frame.vakya.sourceText) return null
        val spans = buildList {
            frame.relations.forEach { relation ->
                (relation.participant.pada as? SourceLocatedNode)?.sourceSpan?.let(::add)
            }
            (frame.kriya?.analysis?.pada as? SourceLocatedNode)?.sourceSpan?.let(::add)
        }
        return if (spans.isEmpty()) null else DocumentSourceSpan(
            spans.minOf(DocumentSourceSpan::start),
            spans.maxOf(DocumentSourceSpan::endExclusive),
        )
    }

    private fun FrameDiagnosticSeverity.highlightSeverity(): HighlightSeverity = when (this) {
        FrameDiagnosticSeverity.ERROR -> HighlightSeverity.ERROR
        FrameDiagnosticSeverity.WARNING -> HighlightSeverity.WARNING
        FrameDiagnosticSeverity.INFO -> HighlightSeverity.INFORMATION
    }

    private fun annotateUnresolvedProjectReferences(
        file: PvmFile,
        document: dev.panini.vyakaranam.ast.ProgramDocument,
        holder: AnnotationHolder,
    ) {
        val knownNames = PvmProjectFileIndex.knownOperationStems(file)
        PvmSymbolResolver.prakriyaReferenceSpans(document).forEach { span ->
            ProgressManager.checkCanceled()
            val identity = PvmSymbolResolver.prakriyaReferenceIdentityAt(document, span.start, knownNames)
                ?: return@forEach
            if (resolvePrakriyaTargets(file, identity).isNotEmpty()) return@forEach
            val qualified = identity.second?.let { "$it :: ${identity.first}" } ?: identity.first
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Unresolved PaniniVM prakriyā '$qualified'.",
            ).range(TextRange(span.start, span.endExclusive)).create()
        }
    }

    private fun annotateDiagnostics(
        holder: AnnotationHolder,
        text: String,
        diagnostics: List<dev.panini.execution.PrakriyaDiagnostic>,
    ) {
        diagnostics.forEach { diagnostic ->
            val start = diagnostic.offset.coerceIn(0, text.length)
            val end = (start + diagnostic.length).coerceIn(start, text.length)
            val range = TextRange(start, end)
            val annotation = holder.newAnnotation(
                if (diagnostic.severity == PrakriyaDiagnosticSeverity.WARNING) {
                    HighlightSeverity.WARNING
                } else {
                    HighlightSeverity.ERROR
                },
                diagnostic.message,
            ).range(range)
            diagnostic.corrections.forEach { correction ->
                val correctionRange = TextRange(
                    correction.span.start.coerceIn(0, text.length),
                    correction.span.endExclusive.coerceIn(0, text.length),
                )
                annotation.withFix(PvmSourceReplacementQuickFix(
                    correctionRange,
                    correction.replacement,
                    correction.description,
                ))
            }
            diagnostic.replacement?.takeIf { diagnostic.corrections.isEmpty() }?.let { replacement ->
                annotation.withFix(PvmSourceReplacementQuickFix(range, replacement, diagnostic.message))
            }
            diagnostic.missingParameters.forEach { parameter ->
                annotation.withFix(PvmSupplyMissingArgumentQuickFix(range.endOffset, parameter))
            }
            annotation.create()
        }
    }

    private fun highlightSemanticIdentifiers(
        file: PvmFile,
        holder: AnnotationHolder,
        spans: List<dev.panini.vyakaranam.ast.DocumentSourceSpan>,
        attributes: com.intellij.openapi.editor.colors.TextAttributesKey,
        acceptedTokenTypes: Set<com.intellij.psi.tree.IElementType> = PvmTokenTypes.SEMANTIC_SYMBOLS,
    ) {
        spans.forEach { span ->
            var leaf = file.findElementAt(span.start)
            while (leaf != null && leaf.textRange.startOffset < span.endExclusive) {
                if (leaf.node.elementType in acceptedTokenTypes) {
                    val range = leaf.textRange.intersection(TextRange(span.start, span.endExclusive))
                    if (range != null && !range.isEmpty) {
                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .range(range)
                            .textAttributes(attributes)
                            .create()
                    }
                }
                leaf = PsiTreeUtil.nextLeaf(leaf)
            }
        }
    }

    private fun calculateOffset(text: String, line: Int, column: Int): Int {
        var currentLine = 1
        var offset = 0
        for (i in text.indices) {
            if (currentLine == line) {
                return (offset + column).coerceAtMost(text.length)
            }
            if (text[i] == '\n') {
                currentLine++
                offset = i + 1
            }
        }
        return offset.coerceAtMost(text.length)
    }
}
