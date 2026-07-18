package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.2.79: liṅaḥ salopo 'nantyasya. Removes the non-final स् of a liṅ augment. */
object LingasSalopoAnantyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.79",
    text = "लिङः सलोपोऽनन्त्यस्य",
    hindiExplanation = "लिङ् में आगम का अन्त्येतर स् लोप होता है।",
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
        if (context.effectiveContext.rupa.lakara != Lakara.LING) return false
        val siyut = context.terms.firstOrNull { it.id == "siyut" }
        if (siyut != null) return siyut.surface.startsWith('स')

        val yasut = context.terms.firstOrNull { it.id == "yasut" } ?: return false
        val gana = context.terms.firstOrNull { it.kind == TermKind.DHATU }?.gana
        val stemFormationComplete = when (gana) {
            Gana.CURADI -> context.terms.any { it.id == "shap" }
            Gana.RUDHADI -> context.droppedTerms.any { it.id == "shnam" }
            else -> context.terms.any {
                it.id in setOf("shap", "shyan", "shnu", "sha", "tanadi-u", "shna")
            }
        }
        return stemFormationComplete && yasut.surface.endsWith("स्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val siyut = context.terms.firstOrNull { it.id == "siyut" }
        if (siyut != null) {
            return DerivationChange(
                context.replaceTerm(siyut.id, siyut.copy(surface = siyut.surface.drop(1))),
                "7.2.79 removes the initial स् of सीय्.",
            )
        }

        val yasut = context.terms.first { it.id == "yasut" }
        return DerivationChange(
            context.replaceTerm(yasut.id, yasut.copy(surface = yasut.surface.removeSuffix("स्"))),
            "7.2.79 removes the non-final स् of यास्.",
        )
    }
}
