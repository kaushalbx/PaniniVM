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

        val inlayEntries = mutableListOf<Pair<Int, String>>()

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
                    inlayEntries.add(Pair(currentOffset, surface))
                }
            }

            currentOffset = lineEnd + 1
        }

        if (editor.isDisposed) return

        // Remove old block inlays
        val existingInlays = editor.inlayModel.getBlockElementsInRange(0, editor.document.textLength)
        for (inlay in existingInlays) {
            if (inlay.renderer is PvmBlockInlayRenderer) {
                inlay.dispose()
            }
        }

        // Add block inlays in line gaps above each statement line
        for (entry in inlayEntries) {
            val offset = entry.first.coerceIn(0, editor.document.textLength)
            editor.inlayModel.addBlockElement(
                offset,
                true, // relatesToPreceding
                true, // showAbove: Creates vertical line gap and renders ON TOP of line!
                0,    // priority
                PvmBlockInlayRenderer(entry.second)
            )
        }

        try {
            editor.contentComponent.repaint()
        } catch (_: Throwable) {
        }
    }
}
