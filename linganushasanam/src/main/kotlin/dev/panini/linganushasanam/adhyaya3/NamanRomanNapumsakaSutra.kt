package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 153: नामरोमणी नपुंसके । */
object NamanRomanNapumsakaSutra : LinganushasanaSutra(
    number = "153", text = "नामरोमणी नपुंसके ।", hindiExplanation = "नामन् और रोमन् नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("नामन्", "रोमन्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

