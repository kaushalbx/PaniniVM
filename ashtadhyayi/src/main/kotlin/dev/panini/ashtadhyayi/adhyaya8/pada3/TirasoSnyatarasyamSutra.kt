package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

internal val kupuStops = setOf(Vyanjana.KA, Vyanjana.KHA, Vyanjana.PA, Vyanjana.PHA)

/**
 * A visarga before ku/pu that comes from the word's own स् (8.2.66 in this derivation), not from
 * an affix. A word given already ending in visarga has no such evidence, so nothing is guessed.
 */
internal fun DerivationState.ownSVisargaBeforeKupu(): DerivationTerm? {
    for (index in 0 until terms.lastIndex) {
        val left = terms[index]
        val right = terms.drop(index + 1).firstOrNull { it.varnas.isNotEmpty() } ?: continue
        if (left.varnas.lastOrNull() != Ayogavaha.VISARGA || right.varnas.first() !in kupuStops) continue
        if (left.sourceSupAffix != null || left.sourceSuffixUpadeshas.isNotEmpty()) continue
        if (substitutions.none { it.sutra == "8.2.66" && it.targetId == left.id }) continue
        return left
    }
    return null
}

/**
 * 8.3.42 तिरसोऽन्यतरस्याम् — the visarga of तिरस् optionally becomes स् before ku/pu:
 * तिरस्कृत्वा beside तिरःकृत्वा (and 8.3.37's तिरᳵकृत्वा). Known by the word's own स्.
 */
object TirasoSnyatarasyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.42", text = "तिरसोऽन्यतरस्याम्",
    hindiExplanation = "तिरस् के विसर्ग को कवर्ग-पवर्ग परे विकल्प से सकार होता है।",
    type = SutraType.ANYATARASYAM, chapter = 8, pada = 3, optional = true, kramaValue = 830042,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private val tira = listOf(Vyanjana.TA, Svara.I, Vyanjana.RA, Svara.A, Ayogavaha.VISARGA)
    private fun target(state: DerivationState) =
        state.ownSVisargaBeforeKupu()?.takeIf { it.varnas == tira }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val left = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(left.id, left.varnas.lastIndex, listOf(Vyanjana.SA), sutra),
            "8.3.42 optionally replaces the visarga of तिरस् with स् before ku/pu."),
            DerivationChange(state, "8.3.42 declined.", applied = false))
    }
}
