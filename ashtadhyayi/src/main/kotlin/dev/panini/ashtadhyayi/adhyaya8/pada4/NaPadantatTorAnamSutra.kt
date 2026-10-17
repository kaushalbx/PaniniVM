package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** Following sto is protected only from the preceding pada-final retroflex stop. */
object NaPadantatTorAnamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.42", text = "न पदान्ताट्टोरनाम्",
    hindiExplanation = "पदान्त टवर्ग से परे स्तु का ष्टुत्व नहीं होता, नाम् को छोड़कर।",
    type = SutraType.NISHEDHA, chapter = 8, pada = 4, optional = false, kramaValue = 840042,
    role = SutraRole.Nishedha, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI, blocks = setOf("8.4.41"),
), DerivationSutra {
    override val advancesTripadiFrontier = false
    private val retroflexStops = setOf(Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA, Vyanjana.NNA)
    private val stu = setOf(Vyanjana.SA, Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)
    private fun target(state: DerivationState): Pair<String, String>? {
        for ((index, left) in state.terms.withIndex()) {
            if (left.varnas.lastOrNull() !in retroflexStops ||
                !state.isPadaFinalVarna(left, left.varnas.lastIndex)) continue
            val right = state.terms.drop(index + 1).firstOrNull { it.varnas.isNotEmpty() } ?: continue
            if (right.varnas.first() !in stu || state.isBlockedAtBoundary("8.4.41", left.id, right.id)) continue
            // Nāṃ is augmented genitive plural ām, not any word spelled nām.
            if (right.sourceSupAffix == SupAffix.AM_6 && right.varnas.first() == Vyanjana.NA) continue
            return left.id to right.id
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(target(context))
        return DerivationChange(context.blockAtBoundary("8.4.41", left, right, sutra),
            "8.4.42 prevents retroflexion of following sto from a pada-final retroflex stop, except augmented genitive plural nam.")
    }
}
