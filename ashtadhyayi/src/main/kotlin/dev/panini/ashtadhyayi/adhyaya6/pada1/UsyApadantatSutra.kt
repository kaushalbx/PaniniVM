package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/**
 * 6.1.96 उस्यपदान्तात् — a non-pada-final अ/आ and the उ of उस् (जुस्) take the later sound as
 * their single substitute (पररूप, from 6.1.94): अ दा उस् → अदुः, अ स्था उस् → अस्थुः.
 * An apavāda to 6.1.87 and 6.1.101.
 */
object UsyApadantatSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.96", text = "उस्यपदान्तात्",
    hindiExplanation = "अपदान्त अवर्ण से परे उस् का उकार हो तो पररूप एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610096,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.87", "6.1.88", "6.1.101"),
), DerivationSutra {
    private fun pair(state: DerivationState) = state.terms.zipWithNext().lastOrNull { (left, right) ->
        right.kind == TermKind.PRATYAYA && right.surface == "उस्" &&
            (right.upadesha == "जुस्" || right.upadesha == "झि" || right.sourceTingAffix != null) &&
            left.varnas.lastOrNull() in setOf(Svara.A, Svara.AA) &&
            state.samjnas.none { it.targetId == left.id && it.samjna == Samjna.PADA }
    }

    override fun matches(context: DerivationState) = pair(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, _) = requireNotNull(pair(context))
        return DerivationChange(
            context.deleteTermVarnas(left.id, left.varnas.lastIndex, 1, sutra),
            "6.1.96 takes the उ of उस् as the single substitute for अ/आ + उ.",
        )
    }
}
