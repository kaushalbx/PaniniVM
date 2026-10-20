package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 44: त्रिविष्टपत्रिभुवने नपुंसके । (त्रिपिष्टप इति पाठान्तरः) */
object TrivistapaTribhuvanaNapumsakaSutra : LinganushasanaSutra(
    number = "44",
    text = "त्रिविष्टपत्रिभुवने नपुंसके",
    hindiExplanation = "त्रिविष्टप/त्रिपिष्टप और त्रिभुवन नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("त्रिविष्टप", "त्रिपिष्टप", "त्रिभुवन")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

