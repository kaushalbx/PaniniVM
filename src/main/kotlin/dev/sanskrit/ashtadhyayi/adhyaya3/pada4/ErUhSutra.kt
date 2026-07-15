package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType
import dev.sanskrit.sutra.SutraPriority

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
            context.replaceTerm(affix.id, affix.copy(surface = replacement)).copy(stage = DerivationStage.PADA_FORMED),
            "3.4.86 replaces तिप् with तु in loṭ.",
        )
    }
}
