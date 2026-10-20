package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 136: छदिः स्त्रियामेव । */
object ChadisStriyamEvaSutra : LinganushasanaSutra(
    number = "136", text = "छदिः स्त्रियामेव", hindiExplanation = "छदिस् केवल स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("छदिस्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

