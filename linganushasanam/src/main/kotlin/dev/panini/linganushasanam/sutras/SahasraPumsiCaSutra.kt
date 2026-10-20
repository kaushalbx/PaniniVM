package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 148: सहस्रः पुंसि च । (केचित् पाठे पुंसि च इति स्थाने क्वचित् इति पाठः) */
object SahasraPumsiCaSutra : LinganushasanaSutra(
    number = "148", text = "सहस्रः क्वचित्", hindiExplanation = "सहस्र पुंलिङ्ग तथा नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("सहस्र")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

