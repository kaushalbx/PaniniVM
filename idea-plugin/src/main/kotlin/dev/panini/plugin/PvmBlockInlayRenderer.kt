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
        return fontMetrics.stringWidth(" ➔ $surfaceText") + 16
    }

    override fun calcHeightInPixels(inlay: Inlay<*>): Int {
        return inlay.editor.lineHeight
    }

    override fun paint(inlay: Inlay<*>, g: Graphics, r: Rectangle, textAttributes: TextAttributes) {
        val editor = inlay.editor
        g.color = Color(46, 125, 50, 30) // Subtle green background block gap
        g.fillRoundRect(r.x, r.y + 2, r.width, r.height - 4, 6, 6)

        g.color = Color(46, 125, 50)
        g.font = editor.colorsScheme.getFont(EditorFontType.BOLD)
        g.drawString(" ➔ $surfaceText", r.x + 8, r.y + editor.ascent)
    }
}
