package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.102: lengthens an a-final aṅga before a yañ-initial sup affix. */
object SupiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.102",
    text = "सुपि च",
    hindiExplanation = "यञादि सुप् के परे अकारान्त अङ्ग का अकार दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730102,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        
        // Must be a-ending stem and sup affix starting with Yañ
        val isAEnding = dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface)
        val firstChar = affix.surface.firstOrNull() ?: return false

        val isSupEnvironment = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        return isAEnding && isSupEnvironment && (isYan(firstChar) || affix.upadesha in completePadaAffixes)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val affix = terms.last()
        val oldChar = stem.surface.last()
        val newSurface = if (affix.upadesha == "ङि") {
            if (oldChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
                stem.surface + "े"
            } else {
                stem.surface.dropLast(1) + "े"
            }
        } else if (oldChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
            stem.surface + "ा"
        } else {
            stem.surface.dropLast(1) + "ा"
        }
        val changedState = if (affix.upadesha in completePadaAffixes) {
            val completedSurface = if (affix.upadesha == "ङि") newSurface else newSurface + affix.surface
            context.copy(
                terms = terms.dropLast(2) + stem.copy(surface = completedSurface),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            )
        } else {
            context.copy(
                terms = terms.dropLast(2) + stem.copy(surface = newSurface) + affix,
                stage = DerivationStage.ANGAKARYA
            )
        }
        
        return DerivationChange(
            state = changedState.addSubstitution(VarnaSubstitution(stem.id, oldChar, "ा", sutra)),
            explanation = "7.3.102: Lengthened final 'a' to 'ā' before yañ-initial sup."
        )
    }

    private fun isYan(c: Char): Boolean = c in setOf('य', 'व', 'र', 'ल', 'ञ', 'म', 'ङ', 'ण', 'न')

    private val completePadaAffixes = setOf("भ्याम्", "ङे", "ङि")
}
