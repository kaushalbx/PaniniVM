package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.SpeechBoundary
import dev.panini.derivation.firstVarnaAfterTerm
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.3.15: khar-avasānayor visarjanīyaḥ.
 * Word-final 'r' (repha) is replaced by visarga before a khar sound
 * or at the end of a derivation (avasāna).
 */
object KharavasanayorVisarjaniyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.15",
    text = "खरवसानयोर्विसर्जनीयः",
    hindiExplanation = "पदान्त 'र्' के स्थान पर विसर्ग होता है यदि बाद में 'खर्' वर्ण हो या अवसान (विराम) हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830015,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.VISARJANIYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = targetTerm(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(targetTerm(context))

        return DerivationChange(
            state = context.replaceTermVarna(target.id, target.varnas.lastIndex, listOf(Ayogavaha.VISARGA), number)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.3.15 replaces the eligible final sound with visarga before khar or explicit avasāna."
        )
    }

    private fun targetTerm(context: DerivationState): DerivationTerm? = context.terms.firstOrNull { term ->
        val isRutva = term.varnas.lastOrNull() == Vyanjana.RA && context.substitutions.any { substitution ->
            substitution.targetId == term.id && substitution.sutra in setOf("8.2.66", "8.3.7") &&
                (substitution.sourceVarnaIndex == null || substitution.sourceVarnaIndex == term.varnas.lastIndex)
        }
        val isSuffixalSha = term.varnas.lastOrNull() == Vyanjana.SSA && term.kind == TermKind.PRATYAYA &&
            term.upadeshaVarnas.lastOrNull() == Vyanjana.SA
        // 8.3.15 says repha, not only repha produced by 8.2.66. A completed
        // pada licenses lexical r too; this includes a newly completed sup/tiṅ
        // combination, not just a previously formed external input. An arbitrary
        // internal r without the established designation still does not qualify.
        val isCompletedPadaRepha = term.varnas.lastOrNull() == Vyanjana.RA &&
            context.samjnas.any { it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.PADA }
        val next = context.firstVarnaAfterTerm(term.id)
        val environment = if (next == null) context.effectiveContext.finalBoundary == SpeechBoundary.AVASANA
            else Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, next)
        (isRutva || isCompletedPadaRepha || isSuffixalSha) && environment
    }
}
