package dev.panini.plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.panini.execution.PvmUktiSadhaka
import dev.panini.vyakaranam.parser.PaniniParser

class PvmAnnotator : Annotator {
    private val paniniParser = PaniniParser()
    private val sadhaka = PvmUktiSadhaka()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiFile || element !is PvmFile) return

        val text = try {
            element.text
        } catch (_: Throwable) {
            return
        }
        if (text.isBlank()) return

        // 1. ANTLR Live Syntax Error Highlighting
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

        // 2. Add Block Inlay Elements in Gaps Directly Above Each Statement Line
        try {
            val project = holder.currentAnnotationSession.file.project
            val editor: Editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return

            val lines = text.lines()
            var currentOffset = 0

            // Clear previous PvmBlockInlayRenderer elements to avoid duplicate line gap elements
            val existingInlays = editor.inlayModel.getBlockElementsInRange(0, text.length)
            for (inlay in existingInlays) {
                if (inlay.renderer is PvmBlockInlayRenderer) {
                    inlay.dispose()
                }
            }

            for (line in lines) {
                val trimmed = line.trim()
                val lineEnd = currentOffset + line.length

                if (trimmed.isNotEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("#")) {
                    try {
                        val surface = sadhaka.sadhayaLine(trimmed)
                        if (surface.isNotBlank()) {
                            editor.inlayModel.addBlockElement(
                                currentOffset,
                                true, // relatesToPreceding
                                true, // showAbove: Creates vertical line gap and renders ON TOP of the line!
                                0,    // priority
                                PvmBlockInlayRenderer(surface)
                            )
                        }
                    } catch (_: Throwable) {
                        // Ignore incomplete line expressions during typing
                    }
                }

                currentOffset = lineEnd + 1
            }
        } catch (_: Throwable) {
            // Thread safety guarantee
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
