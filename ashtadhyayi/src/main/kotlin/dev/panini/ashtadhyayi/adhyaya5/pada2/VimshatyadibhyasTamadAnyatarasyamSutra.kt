package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.none { it.samjna == Samjna.PURANA }) return false
        if (context.hasTamat()) return false
        val datIndex = context.datIndex()
        if (datIndex <= 0) return false
        val base = context.terms[datIndex - 1]
        return base.compoundHeadUpadesha in PuranaNumeralClasses.vimshatyadiHeads
    }

    override fun apply(context: DerivationState): DerivationChange {
        return context.insertTamat(sutra, "$text: डट् में तमट् आगम।")
    }
}
