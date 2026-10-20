package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object ArghaPathiMathiRbhuksiSutra : LinganushasanaSutra(
    number = "115",
    text = "अर्धपथिमथ्यृभुक्षिस्तम्बनितम्बपूगाः",
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("अर्ध", "पथि", "मथि", "ऋभुक्षि", "स्तम्ब", "नितम्ब", "पूग")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
