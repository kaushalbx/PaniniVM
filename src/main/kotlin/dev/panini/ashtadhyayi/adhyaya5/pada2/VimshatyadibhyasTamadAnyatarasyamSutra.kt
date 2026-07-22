package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.56: विंशत्यादिभ्यस्तमडन्यतरस्याम् — optional तमट् augment of ordinal डट्. */
object VimshatyadibhyasTamadAnyatarasyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.56",
    text = "विंशत्यादिभ्यस्तमडन्यतरस्याम्",
    hindiExplanation = "विंशति आदि संख्याओं के पूरणार्थक डट् प्रत्यय में विकल्प से तमट् आगम होता है।",
    type = SutraType.VIBHASHA,
    chapter = 5,
    pada = 2,
    optional = true,
    kramaValue = 520056,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.terms.any { it.upadesha == "तमट्" }) return false
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        if (datIndex <= 0) return false
        val base = context.terms[datIndex - 1]
        return base.upadesha in setOf(
            "विंशति", "त्रिंशत्", "चत्वारिंशत्", "पञ्चाशत्",
            "षष्टि", "सप्तति", "अशीति", "नवति",
        )
    }

    override fun apply(context: DerivationState): DerivationChange {
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        val tamat = DerivationTerm(
            id = "purana_tamat",
            surface = "तम",
            kind = TermKind.AGAMA,
            upadesha = "तमट्",
        )
        val terms = context.terms.toMutableList().apply { add(datIndex, tamat) }
        return DerivationChange(context.copy(terms = terms), "$text: डट् में तमट् आगम।")
    }
}
