package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val isAEnding = dev.panini.shiksha.Varnamala.endsWithA(stem.surface)
        val firstChar = affix.surface.firstOrNull() ?: return false

        val isSupEnvironment = affix.id.startsWith("sup-") && context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        return isAEnding && affix.upadesha !in setOf("टा", "ओस्", "अम्", "सुँ", "सु") && isSupEnvironment &&
            (isYan(firstChar) || affix.upadesha in completePadaAffixes)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val affix = terms.last()
        val oldChar = stem.surface.last()
        val newSurface = if (affix.upadesha == "ङि") {
            if (oldChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
                stem.surface + "े"
            } else {
                stem.surface.dropLast(1) + "े"
            }
        } else if (oldChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
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

    /** यण् is य्, व्, र्, ल्; the nasals do not license 7.3.102. */
    private fun isYan(c: Char): Boolean = c in setOf('य', 'व', 'र', 'ल')

    private val completePadaAffixes = setOf("भ्याम्", "ङे", "ङि")
}
