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
        val fontMetrics = inlay.editor.component.getFontMetrics(inlay.editor.colorsScheme.getFont(EditorFontType.BOLD))
        return fontMetrics.stringWidth(" ➔ $surfaceText") + 16
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
            val font = editor.colorsScheme.getFont(EditorFontType.BOLD)
            g2.font = font

            val isDark = UIUtil.isUnderDarcula()
            val bgPillColor = if (isDark) Color(46, 125, 50, 45) else Color(232, 245, 233, 220)
            val borderColor = if (isDark) Color(76, 175, 80, 120) else Color(129, 199, 132, 180)
            val textColor = if (isDark) Color(129, 199, 132) else Color(27, 94, 32)

            val badgeY = r.y + 2
            val badgeHeight = r.height - 4

            // Draw inline pill background after Danda
            g2.color = bgPillColor
            g2.fillRoundRect(r.x + 4, badgeY, r.width - 8, badgeHeight, 8, 8)

            // Draw inline pill border
            g2.color = borderColor
            g2.drawRoundRect(r.x + 4, badgeY, r.width - 8, badgeHeight, 8, 8)

            // Draw text
            g2.color = textColor
            g2.drawString(" ➔ $surfaceText", r.x + 10, badgeY + editor.ascent - 2)
        } finally {
            g2.dispose()
        }
    }
}
