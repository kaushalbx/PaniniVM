package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 147: शङ्कुः पुंसि । (केचित् पाठे नैत्तसूत्रम्) */
object SankuPumsiSutra : LinganushasanaSutra(
    number = "147", text = "शङ्कुः पुंसि", hindiExplanation = "स्वीकृत पाठ में शङ्कु पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS), priority = 50,
) {
    private val members = setOf("शङ्कु")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

