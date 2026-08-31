package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.86: er uḥ. Replaces loṭ तिप् with तु. */
object ErUhSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.86",
    text = "एरुः",
    hindiExplanation = "लोट् के तिप् के स्थान पर तु आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340086,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private const val sourceEnding = "तिप्"
    private const val replacement = "तु"

    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        return (context.stage == DerivationStage.PADA_FORMED && affix.surface != replacement ||
            context.stage == DerivationStage.IT_PROCESSED && affix.surface == replacement) &&
            context.effectiveContext.rupa.lakara == Lakara.LOT &&
            affix.upadesha == sourceEnding
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            context.replaceWholeAffix(affix.id, replacement, sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume).copy(stage = DerivationStage.PADA_FORMED),
            "3.4.86 replaces तिप् with तु in loṭ.",
        )
    }
}
