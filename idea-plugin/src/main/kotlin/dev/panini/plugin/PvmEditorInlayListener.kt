package dev.panini.plugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import dev.panini.execution.PvmUktiSadhaka

class PvmEditorInlayListener : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return
        if (file.extension != "pvm") return

        val parentDisposable = (editor as? Disposable) ?: return

        updateInlays(editor)

        editor.document.addDocumentListener(object : DocumentListener {
            override fun documentChanged(event: DocumentEvent) {
                updateInlays(editor)
            }
        }, parentDisposable)
    }

    private fun updateInlays(editor: Editor) {
        val sadhaka = PvmUktiSadhaka()
        val text = editor.document.text
        if (text.isBlank()) return

        val lines = text.lines()
        var currentOffset = 0

        // Clear previous PVM block inlays to avoid duplicate line gap elements
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
    }
}
