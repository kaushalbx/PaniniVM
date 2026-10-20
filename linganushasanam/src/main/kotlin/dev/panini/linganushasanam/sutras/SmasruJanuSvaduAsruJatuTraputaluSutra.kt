package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 54: श्मश्रुजानुवसुस्वाद्वश्रुजतुत्रयपुतालूनि नपुंसके । */
object SmasruJanuSvaduAsruJatuTraputaluSutra : LinganushasanaSutra(
    number = "54",
    text = "श्मश्रुजानुवसुस्वाद्वश्रुजतुत्रयपुतालूनि नपुंसके",
    hindiExplanation = "श्मश्रु, जानु, वसु, स्वादु, अश्रु, जतु, त्रयपु और तालु नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("श्मश्रु", "जानु", "वसु", "स्वादु", "अश्रु", "जतु", "त्रयपु", "तालु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

