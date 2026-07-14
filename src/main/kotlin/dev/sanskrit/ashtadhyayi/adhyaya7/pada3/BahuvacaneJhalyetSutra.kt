package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.103: substitutes ए for final अ before plural झल-initial sup affix. */
object BahuvacaneJhalyetSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.103",
    text = "बहुवचने झल्येत्",
    hindiExplanation = "झलादि बहुवचन सुप् के परे अकारान्त अङ्ग का अकार एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730103,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 7.3.103 applies to a-final aṅgas; feminine ā-stems retain their ā.
        val isAEnding = dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface) &&
            !dev.sanskrit.shiksha.Varnamala.endsWithAA(stem.surface)
        val firstChar = affix.surface.firstOrNull() ?: return false
        
        val isPlural = HasMorphosyntax(vacana = Vacana.BAHUVACANA).matches(context)
        
        return affix.upadesha != "शि" &&
            isAEnding && isPlural && isJhal(firstChar) &&
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val oldChar = stem.surface.last()
        val newSurface = if (oldChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
            stem.surface + "े"
        } else {
            stem.surface.dropLast(1) + "े"
        }
        
        val changedState = if (terms.last().upadesha == "भ्यस्") {
            context.copy(
                terms = terms.dropLast(2) + stem.copy(surface = newSurface + terms.last().surface),
                droppedTerms = context.droppedTerms + terms.last().copy(surface = ""),
                stage = DerivationStage.PADA_FORMED,
            )
        } else {
            context.copy(
                terms = terms.dropLast(2) + stem.copy(surface = newSurface) + terms.last(),
                stage = DerivationStage.ANGAKARYA
            )
        }

        return DerivationChange(
            state = changedState.addSubstitution(VarnaSubstitution(stem.id, oldChar, "े", sutra)),
            explanation = "7.3.103: Substituted 'e' for final 'a' before plural jhal-initial sup."
        )
    }

    private fun isJhal(c: Char): Boolean = c in setOf(
        'झ', 'भ', 'घ', 'ढ', 'ध', 'ज', 'ब', 'ग', 'ड', 'द', 'ख', 'फ', 'छ', 'ठ', 'थ', 'च', 'ट', 'त', 'क', 'प', 'श', 'ष', 'स', 'ह'
    )
}
