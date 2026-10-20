package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 71: काष्ठपृष्ठरिक्थोक्थानि नपुंसके ।(क्वचित् रिक्थ स्थाने सिक्थ इति पाठभेदः) */
object KasthaPrsthaRikthaUkthaSutra : LinganushasanaSutra(
    number = "71",
    text = "काष्ठपृष्ठरिक्थोक्थानि नपुंसके ।(क्वचित् रिक्थ स्थाने सिक्थ इति पाठभेदः)",
    hindiExplanation = "काष्ठ, पृष्ठ, रिक्थ/सिक्थ और उक्थ नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("काष्ठ", "पृष्ठ", "रिक्थ", "सिक्थ", "उक्थ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

