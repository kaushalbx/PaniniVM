package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.14: ḍho ḍhe lopaḥ.
 * The sound 'ḍh' is elided when immediately followed by another 'ḍh'.
 */
object DhoDheLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.14",
    text = "ढो ढे लोपः",
    hindiExplanation = "ढ-कार का ढ-कार परे रहते लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830014,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            context.terms[i].varnas.lastOrNull() == Vyanjana.DDHA &&
                context.terms[i + 1].varnas.firstOrNull() == Vyanjana.DDHA
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            context.terms[i].varnas.lastOrNull() == Vyanjana.DDHA &&
                context.terms[i + 1].varnas.firstOrNull() == Vyanjana.DDHA
        }

        val targetTerm = context.terms[targetIndex]
        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, targetTerm.varnas.lastIndex, emptyList(), sutra),
            explanation = "8.3.14: Elided 'ḍh' before another 'ḍh'."
        )
    }
}
