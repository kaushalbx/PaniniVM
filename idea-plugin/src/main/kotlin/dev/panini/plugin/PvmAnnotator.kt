package dev.panini.plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
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

        // 1. ANTLR Live Syntax Error Highlighting (Safe)
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

        // 2. Safe Sentence Surface Form Conjugation Annotation (Only on success)
        try {
            val vm = PaniniVM()
            val lines = text.lines()
            var currentOffset = 0

            for (line in lines) {
                val trimmed = line.trim()
                val lineEnd = currentOffset + line.length

                if (trimmed.isNotEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("#")) {
                    try {
                        val res = vm.eval(trimmed)
                        if (res is ExecutionResult.Success && res.value.isNotBlank()) {
                            val lineRange = TextRange((lineEnd - 1).coerceAtLeast(currentOffset), lineEnd.coerceAtMost(text.length))
                            holder.newAnnotation(HighlightSeverity.INFORMATION, "Conjugated Surface: ${res.value}")
                                .range(lineRange)
                                .afterEndOfLine()
                                .create()
                        }
                    } catch (_: Throwable) {
                        // Conjugation not ready or incomplete line - safely ignore
                    }
                }

                currentOffset = lineEnd + 1
            }
        } catch (_: Throwable) {
            // Swallow VM evaluation errors to guarantee editor performance and file opening safety
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
