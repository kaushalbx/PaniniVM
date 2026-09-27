package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        if (siyut != null) return siyut.varnas.firstOrNull() == Vyanjana.SA

        val yasut = context.terms.firstOrNull { it.id == "yasut" } ?: return false
        val gana = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.gana != null }?.gana
        val stemFormationComplete = when (gana) {
            DhatuGana.CURADI -> context.terms.any { it.id == "shap" }
            DhatuGana.RUDHADI -> context.droppedTerms.any { it.id == "shnam" }
            DhatuGana.ADADI, DhatuGana.JUHOTYADI -> context.allEffectiveTerms.any { it.id == "shap" }
            else -> context.terms.any {
                it.id in setOf("shap", "shyan", "shnu", "sha", "tanadi-u", "shna")
            }
        }
        return stemFormationComplete && yasut.varnas.lastOrNull() == Vyanjana.SA
    }

    override fun apply(context: DerivationState): DerivationChange {
        val siyut = context.terms.firstOrNull { it.id == "siyut" }
        if (siyut != null) {
            return DerivationChange(
                context.replaceWholeAffix(
                    siyut.id,
                    siyut.varnas.drop(1),
                    sutra,
                    WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()),
                ),
                "7.2.79 removes the initial स् of सीय्.",
            )
        }

        val yasut = context.terms.first { it.id == "yasut" }
        val replacement = if (context.terms.last().matchesUpadesha("झि")) {
            listOf(Vyanjana.YA)
        } else {
            yasut.varnas.dropLast(1)
        }
        return DerivationChange(
            context.replaceWholeAffix(
                yasut.id,
                replacement,
                sutra,
                WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()),
            ),
            "7.2.79 removes the non-final स् of यास्.",
        )
    }
}
