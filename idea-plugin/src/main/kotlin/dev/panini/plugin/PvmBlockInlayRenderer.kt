package dev.panini.plugin

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

class PvmBlockInlayRenderer(val surfaceText: String) : EditorCustomElementRenderer {
    override fun calcWidthInPixels(inlay: Inlay<*>): Int {
        val fontMetrics = inlay.editor.component.getFontMetrics(inlay.editor.colorsScheme.getFont(EditorFontType.PLAIN))
        return fontMetrics.stringWidth(" ➔ $surfaceText") + 20
    }

    override fun calcHeightInPixels(inlay: Inlay<*>): Int {
        // Extra line height gap above sentence
        return inlay.editor.lineHeight + 12
    }

    override fun paint(inlay: Inlay<*>, g: Graphics, r: Rectangle, textAttributes: TextAttributes) {
        val editor = inlay.editor

        // Slate gray text color for sentence above the line
        g.color = Color(128, 128, 128)
        g.font = editor.colorsScheme.getFont(EditorFontType.ITALIC)
        g.drawString(" ➔ $surfaceText", r.x + 8, r.y + editor.ascent + 4)
    }
}
