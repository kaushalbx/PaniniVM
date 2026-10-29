package dev.panini.plugin

import java.awt.Font
import java.awt.GraphicsEnvironment

/** Selects a font that can render the complete Devanagari string, including combining marks. */
internal object PvmUiFonts {
    private val preferredFamilies = listOf(
        "Nirmala UI",
        "Sanskrit Text",
        "Mangal",
        "Noto Sans Devanagari",
        "Noto Serif Devanagari",
        Font.SANS_SERIF,
    )

    fun forText(base: Font, text: String, size: Float = base.size2D): Font {
        val derivedBase = base.deriveFont(base.style, size)
        val containsDevanagari = text.any { it.code in 0x0900..0x097F }
        if (!containsDevanagari && derivedBase.canDisplayUpTo(text) == -1) return derivedBase

        val available = runCatching {
            GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames.toSet()
        }.getOrDefault(emptySet())
        val preferred = preferredFamilies.asSequence()
            .filter { it == Font.SANS_SERIF || it in available }
            .map { Font(it, base.style, size.toInt()).deriveFont(size) }
            .firstOrNull { it.canDisplayUpTo(text) == -1 }
        return preferred
            ?: derivedBase.takeIf { it.canDisplayUpTo(text) == -1 }
            ?: Font(Font.SANS_SERIF, base.style, size.toInt()).deriveFont(size)
    }
}
