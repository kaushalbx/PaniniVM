package dev.panini.plugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.PvmUktiSadhaka

class PvmEditorInlayListener : EditorFactoryListener {

    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor

        ApplicationManager.getApplication().invokeLater {
            if (!editor.isDisposed) {
                updateInlays(editor)
            }
        }

        val parentDisposable = (editor as? Disposable) ?: return
        editor.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                ApplicationManager.getApplication().invokeLater {
                    if (!editor.isDisposed) {
                        updateInlays(editor)
                    }
                }
            }
        }, parentDisposable)
    }

    private fun updateInlays(editor: Editor) {
        val text = try {
            editor.document.text
        } catch (_: Throwable) {
            return
        }
        if (text.isBlank()) return

        val sadhaka = PvmUktiSadhaka()
        val vm = PaniniVM()
        val lines = text.lines()
        var currentOffset = 0

        val blockInlayEntries = mutableListOf<Pair<Int, String>>()
        val inlineInlayEntries = mutableListOf<Pair<Int, String>>()

        for (line in lines) {
            val trimmed = line.trim()
            val lineEnd = currentOffset + line.length

            if (trimmed.isNotEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("#")) {
                var surface = try { sadhaka.sadhayaLine(trimmed) } catch (_: Throwable) { "" }
                if (surface.isBlank() || surface == trimmed) {
                    val res = try { vm.eval(trimmed) } catch (_: Throwable) { null }
                    if (res is ExecutionResult.Success && res.value.isNotBlank()) {
                        surface = res.value
                    }
                }

                if (surface.isNotBlank()) {
                    blockInlayEntries.add(Pair(currentOffset, surface))
                    inlineInlayEntries.add(Pair(lineEnd, surface))
                }
            }

            currentOffset = lineEnd + 1
        }

        if (editor.isDisposed) return

        // Clear previous block and inline PVM inlays
        val existingBlockInlays = editor.inlayModel.getBlockElementsInRange(0, editor.document.textLength)
        for (inlay in existingBlockInlays) {
            if (inlay.renderer is PvmBlockInlayRenderer) {
                inlay.dispose()
            }
        }

        val existingInlineInlays = editor.inlayModel.getInlineElementsInRange(0, editor.document.textLength)
        for (inlay in existingInlineInlays) {
            if (inlay.renderer is PvmInlineInlayRenderer) {
                inlay.dispose()
            }
        }

        // Add block inlays above each statement line
        for (entry in blockInlayEntries) {
            val offset = entry.first.coerceIn(0, editor.document.textLength)
            editor.inlayModel.addBlockElement(
                offset,
                true, // relatesToPreceding
                true, // showAbove: Creates vertical line gap and renders ON TOP of line!
                0,    // priority
                PvmBlockInlayRenderer(entry.second)
            )
        }

        // Add inline inlays after Danda / end of line
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
