package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.22 आभीक्ष्ण्ये णमुल् च.
 * Prescribes ṇamul adverbial repetitive gerund affix after verbal roots when repetition (ābhīkṣṇya) is expressed.
 */
object AbhikshnyeNamulCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.22", text = "आभीक्ष्ण्ये णमुल् च",
    hindiExplanation = "क्रिया की निरन्तरता या पौनःपुन्य (आभीक्ष्ण्य) अर्थ में धातु से 'णमुल्' (अम्) प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340022,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "णमुल्" }

    override fun apply(context: DerivationState): DerivationChange {
        val namul = DerivationTerm("namul", "णमुँल्", TermKind.PRATYAYA, upadesha = "णमुल्", itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(namul),
            explanation = "3.4.22 prescribes णमुल् repetitive action gerund affix in ābhīkṣṇya.",
        )
    }
}
