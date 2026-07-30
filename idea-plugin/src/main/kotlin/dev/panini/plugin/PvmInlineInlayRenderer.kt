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

    companion object {
        private val resolvedFontName: String by lazy {
            try {
                val ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                val families = ge.availableFontFamilyNames.toSet()
                val preferred = listOf("Nirmala UI", "Mangal", "Sanskrit Text", "Arial Unicode MS")
                preferred.firstOrNull { it in families } ?: "SansSerif"
            } catch (_: Throwable) {
                "SansSerif"
            }
        }
    }

    private fun getHintFont(inlay: Inlay<*>): java.awt.Font {
        val editor = inlay.editor
        val baseFont = editor.colorsScheme.getFont(EditorFontType.PLAIN)
        val hintFontSize = (baseFont.size * 0.9f).coerceAtLeast(10f)
        return java.awt.Font(resolvedFontName, java.awt.Font.PLAIN, hintFontSize.toInt())
    }

    override fun calcWidthInPixels(inlay: Inlay<*>): Int {
        val fontMetrics = inlay.editor.component.getFontMetrics(getHintFont(inlay))
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
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

            val editor = inlay.editor
            val hintFont = getHintFont(inlay)
            g2.font = hintFont

            val isDark = UIUtil.isUnderDarcula()
            // Code block chip badge background (matching inline code highlighting)
            val chipBgColor = if (isDark) {
                Color(60, 63, 65, 100) // ~40% opacity
            } else {
                Color(230, 235, 240, 100)
            }

            val chipBorderColor = if (isDark) {
                Color(255, 255, 255, 30) // Subtle outline (~12% opacity)
            } else {
                Color(0, 0, 0, 30)
            }

            val textColor = if (isDark) {
                Color(141, 141, 141) // Muted grey text
            } else {
                Color(158, 158, 158)
            }

            val badgeY = r.y + 2
            val badgeHeight = r.height - 4

            // Draw rounded code block chip background
            g2.color = chipBgColor
            g2.fillRoundRect(r.x + 8, badgeY, r.width - 12, badgeHeight, 6, 6)

            // Draw chip border
            g2.color = chipBorderColor
            g2.drawRoundRect(r.x + 8, badgeY, r.width - 12, badgeHeight, 6, 6)

            // Draw surface text vertically centered inside the badge
            g2.color = textColor
            val fontMetrics = g2.fontMetrics
            val textY = badgeY + (badgeHeight - fontMetrics.height) / 2 + fontMetrics.ascent
            g2.drawString(surfaceText, r.x + 14, textY)
        } finally {
            g2.dispose()
        }
    }
}
