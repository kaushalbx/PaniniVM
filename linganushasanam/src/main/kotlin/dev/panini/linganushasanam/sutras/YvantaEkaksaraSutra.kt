package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 12: य्वन्तमेकाक्षरम्. */
object YvantaEkaksaraSutra : LinganushasanaSutra(
    number = "12",
    text = "य्वन्तमेकाक्षरम्",
    hindiExplanation = "इ/ई अथवा उ/ऊ अन्त वाला एकाक्षर शब्द स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 10,
) {
    private val finalVowels = setOf(Svara.I, Svara.II, Svara.U, Svara.UU)

    override fun matches(context: LingaRuleContext): Boolean =
        context.syllableCount == 1 && context.pratipadika.toVarnas().lastOrNull() in finalVowels
}
