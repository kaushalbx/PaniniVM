package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.2.79: liṅaḥ salopo 'nantyasya. Removes the non-final स् of यास्. */
object LingasSalopoAnantyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.79",
    text = "लिङः सलोपोऽनन्त्यस्य",
    hindiExplanation = "लिङ् में यास् का अन्त्येतर स् लोप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720079,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val yasut = context.terms.firstOrNull { it.id == "yasut" } ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.terms.any { it.id == "shap" } &&
            yasut.surface.endsWith("स्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val yasut = context.terms.first { it.id == "yasut" }
        return DerivationChange(
            context.replaceTerm(yasut.id, yasut.copy(surface = yasut.surface.dropLast(1))),
            "7.2.79 removes the non-final स् of यास्.",
        )
    }
}
