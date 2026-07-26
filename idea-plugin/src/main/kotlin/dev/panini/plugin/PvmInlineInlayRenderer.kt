package dev.panini.plugin

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints

class PvmInlineInlayRenderer(val surfaceText: String) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int {
        val fontMetrics = inlay.editor.component.getFontMetrics(inlay.editor.colorsScheme.getFont(EditorFontType.PLAIN))
        return fontMetrics.stringWidth(" ➔ $surfaceText") + 24
    }

    override fun calcHeightInPixels(inlay: Inlay<*>): Int {
        return inlay.editor.lineHeight
    }

    override fun paint(inlay: Inlay<*>, g: Graphics, r: Rectangle, textAttributes: TextAttributes) {
        val g2 = g.create() as? Graphics2D ?: return
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

            val editor = inlay.editor
            g2.font = editor.colorsScheme.getFont(EditorFontType.PLAIN)

            val isDark = UIUtil.isUnderDarcula()
            // Muted Kotlin-style parameter hint color without background or border
            val textColor = if (isDark) Color(140, 140, 145) else Color(110, 110, 115)

            g2.color = textColor
            g2.drawString(" ➔ $surfaceText", r.x + 16, r.y + editor.ascent)
        } finally {
            g2.dispose()
        }
    }
}
