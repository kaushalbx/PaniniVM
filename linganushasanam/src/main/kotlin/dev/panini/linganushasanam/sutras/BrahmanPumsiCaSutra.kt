package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 150: ब्रह्मन्पुंसि च । (केचित् पाठे ब्रह्म इत्येव) */
object BrahmanPumsiCaSutra : LinganushasanaSutra(
    number = "150", text = "ब्रह्मन् पुंसि च", hindiExplanation = "ब्रह्मन्/ब्रह्म पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS), priority = 50,
) {
    private val members = setOf("ब्रह्मन्", "ब्रह्म")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

