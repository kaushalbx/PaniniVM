package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 152: ब्रह्मन्पुंसि च । (केचित् पाठे ब्रह्म इत्येव) */
object BrahmanPumsiCaSutra : LinganushasanaSutra(
    number = "152", text = "ब्रह्मन्पुंसि च । (केचित् पाठे ब्रह्म इत्येव)", hindiExplanation = "ब्रह्मन्/ब्रह्म पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("ब्रह्मन्", "ब्रह्म")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

