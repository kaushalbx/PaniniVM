package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 57: रुत्वन्तः. */
object RutvantaSutra : LinganushasanaSutra(
    number = "57",
    text = "रुत्वन्तः",
    hindiExplanation = "रु अथवा तु अन्त वाले शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.endsWithVarnas("रु") || context.pratipadika.endsWithVarnas("तु")
}
