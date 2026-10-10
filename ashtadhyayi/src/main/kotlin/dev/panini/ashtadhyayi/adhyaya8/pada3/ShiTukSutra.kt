package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 8.3.31: Optional t augmentation at the end of a n-final pada before ś. */
object ShiTukSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.31", text = "शि तुक्",
    hindiExplanation = "पदान्त नकार के बाद शकार परे होने पर विकल्प से तुक् आगम होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = true, kramaValue = 830031,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(context: DerivationState): Int? = (0 until context.terms.lastIndex).firstOrNull { index ->
        val left = context.terms[index]
        left.varnas.lastOrNull() == Vyanjana.NA &&
            context.terms[index + 1].varnas.firstOrNull() == Vyanjana.SHA &&
            context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA }
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val left = context.terms[requireNotNull(target(context))]
        return DerivationChange(
            context.insertTermVarnas(left.id, left.varnas.size, listOf(Vyanjana.TA), sutra),
            "8.3.31 optionally adds त् to the end of a न्-final pada before श्; उ is for pronunciation and क् is इत्.",
        )
    }
}
