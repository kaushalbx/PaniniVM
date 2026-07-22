package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.57: नित्यं शतादिमासार्धमाससंवत्सराच्च — obligatory तमट् after शत etc. */
object NityamShatadiSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.57",
    text = "नित्यं शतादिमासार्धमाससंवत्सराच्च",
    hindiExplanation = "शत आदि संख्याओं के पूरणार्थक डट् में तमट् आगम नित्य होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520057,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("5.2.56"),
), DerivationSutra {
    private val shatadiHeads = setOf("शत", "सहस्र", "अयुत", "लक्ष", "प्रयुत", "कोटि")

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.terms.any { it.upadesha == "तमट्" }) return false
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        return datIndex > 0 && context.terms[datIndex - 1].upadesha in shatadiHeads
    }

    override fun apply(context: DerivationState): DerivationChange {
        val datIndex = context.terms.indexOfLast { it.upadesha == "डट्" }
        val tamat = DerivationTerm("purana_tamat", "तम", TermKind.AGAMA, upadesha = "तमट्")
        val terms = context.terms.toMutableList().apply { add(datIndex, tamat) }
        return DerivationChange(context.copy(terms = terms), "$text: डट् में नित्य तमट् आगम।")
    }
}
