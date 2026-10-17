package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.hasCurrentAffix
import dev.panini.core.SupAffix
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.55: ṣaṭ-caturbhyaś ca.
 * Adds the augment 'nuṭ' before the genitive plural affix 'ām' after 'ṣaṭ' designated stems and 'catur'.
 */
object SatCaturbhyascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.55",
    text = "षट्चतुर्भ्यश्च",
    hindiExplanation = "षट् संज्ञक शब्दों और 'चतुर्' के बाद आम् को नुट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710055,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (context.effectiveContext.samasaType == SamasaType.BAHUVRIHI) return false
        val isCaturOrShat = stem.upadesha == "चतुर्" || stem.compoundHeadUpadesha == "चतुर्" ||
            context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SHAT }

        return isCaturOrShat && affix.sourceSupAffix == SupAffix.AM_6 && affix.hasCurrentAffix(SupAffix.AM_6) &&
            affix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            sutra !in affix.establishedBySutras &&
            context.allEffectiveTerms.none { it.upadesha == "नुँट्" && it.augmentTargetId == affix.id }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val nut = DerivationTerm(
            id = "${affix.id}-nut",
            surface = "नुँट्",
            kind = TermKind.AGAMA,
            upadesha = "नुँट्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = affix.id,
            mergeIntoAugmentTarget = true,
        )

        return DerivationChange(
            state = context.addTerm(nut),
            explanation = "7.1.55 introduces raw नुँट् before आम् after catur/ṣaṭ."
        )
    }
}
