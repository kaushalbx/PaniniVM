package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
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
        // आभीक्ष्ण्य is repetition, not every भाव: णमुल् is given only when that sense is asked for.
        (context.stage == dev.panini.derivation.DerivationStage.INITIAL || context.stage == dev.panini.derivation.DerivationStage.PRATYAYA_SELECTED) &&
        context.samjnas.any { it.samjna == dev.panini.shiksha.Samjna.NAMUL } &&
        context.terms.any { it.kind == TermKind.DHATU } &&
        context.terms.none { it.kind == TermKind.PRATYAYA && !dev.panini.derivation.SanadiAffixes.contains(it.upadesha) }

    override fun apply(context: DerivationState): DerivationChange {
        val namul = DerivationTerm("namul", "णमुँल्", TermKind.PRATYAYA, upadesha = "णमुल्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(namul),
            explanation = "3.4.22 prescribes णमुल् repetitive action gerund affix in ābhīkṣṇya.",
        )
    }
}
