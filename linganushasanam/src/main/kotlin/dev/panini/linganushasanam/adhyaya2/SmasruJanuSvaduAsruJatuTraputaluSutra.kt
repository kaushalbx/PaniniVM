package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 54: श्मश्रुजानुस्वाद्वश्रुजतुत्रपुतालूनि नपुंसके । */
object SmasruJanuSvaduAsruJatuTraputaluSutra : LinganushasanaSutra(
    number = "54",
    text = "श्मश्रुजानुस्वाद्वश्रुजतुत्रपुतालूनि नपुंसके ।",
    hindiExplanation = "श्मश्रु, जानु, स्वादु, अश्रु, जतु, त्रपु और तालु नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("श्मश्रु", "जानु", "स्वादु", "अश्रु", "जतु", "त्रपु", "तालु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

