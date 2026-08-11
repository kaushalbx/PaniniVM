package dev.panini.plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.panini.execution.PvmScript
import dev.panini.execution.SamjnaScriptValidator
import dev.panini.execution.SamjnaDiagnosticSeverity
import dev.panini.vyakaranam.parser.PaniniParser

class PvmAnnotator : Annotator {
    private val paniniParser = PaniniParser()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiFile || element !is PvmFile) return

        val text = try {
            element.text
        } catch (_: Throwable) {
            return
        }
        if (text.isBlank()) return

        // A .pvm file is a script, not one grammatical utterance. Procedure
        // declarations legitimately contain several danda-delimited sentences,
        // so validate the script structure before falling back to a single-
        // utterance diagnostic for incomplete or malformed editor text.
        if (runCatching { PvmScript.parse(text) }.isSuccess) {
            SamjnaScriptValidator.validate(text).forEach { diagnostic ->
                val start = diagnostic.offset.coerceIn(0, text.length)
                val end = (start + diagnostic.length).coerceIn(start, text.length)
                val annotation = holder.newAnnotation(
                    if (diagnostic.severity == SamjnaDiagnosticSeverity.WARNING) HighlightSeverity.WARNING else HighlightSeverity.ERROR,
                    diagnostic.message,
                )
                    .range(TextRange(start, end))
                diagnostic.replacement?.let { replacement ->
                    annotation.withFix(PvmCanonicalNumeralStemQuickFix(TextRange(start, end), replacement))
                }
                annotation.create()
            }
            return
        }

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
        } catch (_: Throwable) {
            // Ignore syntax validation errors during live editing
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
