package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 132: सङ्ख्यापूर्वा रात्रिः. */
object SankhyaPurvaRatriSutra : LinganushasanaSutra(
    number = "132", text = "संख्यापूर्वा रात्रिः", hindiExplanation = "संख्यापूर्वक रात्रि-समास पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS), priority = 10,
) { override fun matches(context: LingaRuleContext): Boolean = context.numericalValue != null && context.pratipadika.endsWithVarnas("रात्रि") }
