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

class PvmBlockInlayRenderer(val surfaceText: String) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int {
        val fontMetrics = inlay.editor.component.getFontMetrics(inlay.editor.colorsScheme.getFont(EditorFontType.BOLD))
        return fontMetrics.stringWidth("✨ $surfaceText") + 28
    }

    override fun calcHeightInPixels(inlay: Inlay<*>): Int {
        // Increased spacious vertical line gap above source code
        return inlay.editor.lineHeight + 22
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
            val bgPillColor = if (isDark) Color(46, 125, 50, 50) else Color(232, 245, 233, 230)
            val borderColor = if (isDark) Color(76, 175, 80, 140) else Color(129, 199, 132, 190)
            val textColor = if (isDark) Color(129, 199, 132) else Color(27, 94, 32)

            val badgeY = r.y + 6
            val badgeHeight = r.height - 10

            // Draw rounded pill background
            g2.color = bgPillColor
            g2.fillRoundRect(r.x + 4, badgeY, r.width - 8, badgeHeight, 10, 10)

            // Draw pill border
            g2.color = borderColor
            g2.drawRoundRect(r.x + 4, badgeY, r.width - 8, badgeHeight, 10, 10)

            // Draw Sanskrit surface text
            g2.color = textColor
            g2.drawString("✨ $surfaceText", r.x + 14, badgeY + editor.ascent - 2)
        } finally {
            g2.dispose()
        }
    }
}
