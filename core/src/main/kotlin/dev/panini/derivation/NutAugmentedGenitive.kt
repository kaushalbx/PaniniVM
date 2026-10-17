package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

/** Genitive plural nām must come from an actual placed, processed nuṭ, not
 * an unrelated lexical string or a nuṭ belonging to another affix. */
fun DerivationState.hasNutAugmentedGenitivePlural(affix: DerivationTerm): Boolean =
    affix.sourceSupAffix == SupAffix.AM_6 && affix.hasCurrentAffix(SupAffix.AM_6) &&
        affix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
        affix.varnas == listOf(Vyanjana.NA, Svara.AA, Vyanjana.MA) &&
        allEffectiveTerms.any { augment ->
            augment.kind == TermKind.AGAMA && augment.upadesha == "नुँट्" &&
                augment.augmentTargetId == affix.id && augment.mergedIntoTermId == affix.id &&
                augment.droppedBySutra == "1.1.46"
        }
