package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignationRemap
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.4.100: itaśca.
 * In a Nit lakāra, the final short 'i' of a Parasmaipada suffix is dropped.
 */
object ItasCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.100",
    text = "इतश्च",
    hindiExplanation = "ङित् लकार (लङ् आदि) के परस्मैपद प्रत्ययों के अन्त्य इकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340100,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        if (lastTerm.matchesUpadesha("मिप्")) return false // 3.4.101 takes priority

        val isNit = context.effectiveContext.rupa.lakara in setOf(
            Lakara.LANG, Lakara.LRNG, Lakara.LUNG, Lakara.LING,
        )
        // In luṅ, 3.1.43 must first introduce cli so that its explicit
        // whole-affix substitute (sic/kṣa/etc.) can undergo its own lifecycle.
        if (context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.allEffectiveTerms.none { it.upadesha in setOf("च्लि", "सिच्", "क्स", "चङ्", "अङ्", "चिण्") }
        ) return false
        // The rule applies only to the nine Parasmaipada tiṅ endings. It
        // therefore removes the surviving इ in ति and सि, but cannot target
        // the Ātmanepada वहि termination.
        val isParasmaipadaTing = lastTerm.upadesha in setOf(
            "तिप्",
            "तस्",
            "झि",
            "सिप्",
            "थस्",
            "थ",
            "मिप्",
            "वस्",
            "मस्",
        )
        // After the झि initial-it lopa, 7.1.3's अन्ति is joined to the
        // aṅga and the original झि remains in droppedTerms. That resulting
        // final इ is still the Parasmaipada tiṅ इ governed by this sūtra.
        val isJhiJoinedToAnga = context.droppedTerms.any { it.matchesUpadesha("झि") }

        return isNit && (isParasmaipadaTing || isJhiJoinedToAnga) && lastTerm.surface.endsWith('ि')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val newSurface = lastTerm.surface.dropLast(1) + '्'
        val replaced = if (lastTerm.kind == TermKind.PRATYAYA) {
            val remaps = (lastTerm.itDesignations + lastTerm.deferredItDesignations).map { designation ->
                ItDesignationRemap(
                    oldStart = designation.start,
                    oldEndExclusive = designation.endExclusive,
                    newStart = designation.start,
                    newEndExclusive = designation.endExclusive,
                )
            }
            context.replaceWholeAffix(
                lastTerm.id,
                newSurface,
                sutra,
                dev.panini.derivation.WholeAffixDesignationPolicy.PreserveAndRemap(remaps),
            )
        } else {
            // When 7.1.3 has already joined the jhi outcome to the aṅga,
            // this is a varṇa operation on that aṅga rather than an affix replacement.
            context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
        }
        return DerivationChange(
            state = replaced.copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.100: Dropped final short 'i' of Parasmaipada suffix."
        )
    }
}
