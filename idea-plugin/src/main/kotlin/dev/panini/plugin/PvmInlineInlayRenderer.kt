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
        return fontMetrics.stringWidth(surfaceText) + 24
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
            g2.font = editor.colorsScheme.getFont(EditorFontType.BOLD)

            val isDark = UIUtil.isUnderDarcula()
            // Code block chip badge background (matching inline code highlighting)
            val chipBgColor = if (isDark) Color(60, 63, 65) else Color(230, 235, 240)
            val chipBorderColor = if (isDark) Color(85, 88, 90) else Color(200, 205, 210)
            val textColor = if (isDark) Color(129, 199, 132) else Color(27, 94, 32)

            val badgeY = r.y + 2
            val badgeHeight = r.height - 4

            // Draw rounded code block chip background
            g2.color = chipBgColor
            g2.fillRoundRect(r.x + 8, badgeY, r.width - 12, badgeHeight, 6, 6)

            // Draw chip border
            g2.color = chipBorderColor
            g2.drawRoundRect(r.x + 8, badgeY, r.width - 12, badgeHeight, 6, 6)

            // Draw surface text without arrow
            g2.color = textColor
            g2.drawString(surfaceText, r.x + 14, badgeY + editor.ascent - 2)
        } finally {
            g2.dispose()
        }
    }
}
