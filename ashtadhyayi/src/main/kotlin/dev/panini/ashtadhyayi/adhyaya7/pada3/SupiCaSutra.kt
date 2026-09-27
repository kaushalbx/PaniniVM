package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.replaceVarna
import dev.panini.shiksha.toDevanagari
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
        val isAEnding = stem.varnas.lastOrNull() == Svara.A
        val firstVarna = affix.varnas.firstOrNull() ?: return false

        val isSupEnvironment = affix.id.startsWith("sup-") && context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        return isAEnding && affix.upadesha !in setOf("टा", "ओस्", "अम्", "सुँ", "सु") && isSupEnvironment &&
            (isYan(firstVarna) || affix.upadesha in completePadaAffixes)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val affix = terms.last()
        val replacement = if (affix.upadesha == "ङि") Svara.E else Svara.AA
        val newVarnas = stem.varnas.replaceVarna(stem.varnas.lastIndex, listOf(replacement))
        val newSurface = newVarnas.toDevanagari()
        val changedState = if (affix.upadesha in completePadaAffixes) {
            val completedSurface = if (affix.upadesha == "ङि") newSurface else (newVarnas + affix.varnas).toDevanagari()
            context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, completedSurface, Svara.A, listOf(replacement), sutra,
            ).copy(stage = DerivationStage.PADA_FORMED)
        } else {
            context.substituteTermSurface(stem.id, newSurface, Svara.A, listOf(replacement), sutra)
                .copy(stage = DerivationStage.ANGAKARYA)
        }

        return DerivationChange(
            state = changedState,
            explanation = "7.3.102: Lengthened final 'a' to 'ā' before yañ-initial sup."
        )
    }

    /** यण् is य्, व्, र्, ल्; the nasals do not license 7.3.102. */
    private fun isYan(varna: Varna): Boolean = varna in setOf(Vyanjana.YA, Vyanjana.VA, Vyanjana.RA, Vyanjana.LA)

    private val completePadaAffixes = setOf("भ्याम्")
}
