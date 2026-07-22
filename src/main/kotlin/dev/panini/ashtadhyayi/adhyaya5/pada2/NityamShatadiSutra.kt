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
    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.hasTamat()) return false
        val datIndex = context.datIndex()
        return datIndex > 0 && context.terms[datIndex - 1].compoundHeadUpadesha in PuranaNumeralClasses.shatadiHeads
    }

    override fun apply(context: DerivationState): DerivationChange {
        return context.insertTamat(sutra, "$text: डट् में नित्य तमट् आगम।")
    }
}
