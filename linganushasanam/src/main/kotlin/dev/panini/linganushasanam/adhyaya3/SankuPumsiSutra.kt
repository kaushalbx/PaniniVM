package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 149: शङ्कुः पुंसि । (केचित् पाठे नैत्तसूत्रम्) */
object SankuPumsiSutra : LinganushasanaSutra(
    number = "149", text = "शङ्कुः पुंसि । (केचित् पाठे नैत्तसूत्रम्)", hindiExplanation = "स्वीकृत पाठ में शङ्कु पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("शङ्कु")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

