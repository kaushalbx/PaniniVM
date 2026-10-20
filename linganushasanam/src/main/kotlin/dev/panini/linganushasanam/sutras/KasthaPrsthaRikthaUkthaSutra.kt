package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 71: काष्ठपृष्ठसिक्थोक्थानि नपुंसके । */
object KasthaPrsthaRikthaUkthaSutra : LinganushasanaSutra(
    number = "71",
    text = "काष्ठपृष्ठसिक्थोक्थानि नपुंसके",
    hindiExplanation = "काष्ठ, पृष्ठ, सिक्थ और उक्थ नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("काष्ठ", "पृष्ठ", "सिक्थ", "उक्थ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

