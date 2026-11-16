package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

private fun DerivationTerm.rootUpadesha(): String? = sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha }

/**
 * 8.4.15 हिनुमीना — after an upasarga's र्/ष्, the न of हिनु (हि + श्नु) and of मीना (मीञ् + श्ना)
 * becomes ण: प्र + हिनोति → प्रहिणोति, प्र + मीनाति → प्रमीणाति. Roots by Dhātupāṭha upadeśa.
 */
object HinumiNaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.15", text = "हिनुमीना",
    hindiExplanation = "उपसर्ग के र्/ष् से परे हिनु और मीना के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840015,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val roots = mapOf("हि" to Vyanjana.HA, "मीञ्" to Vyanjana.MA)

    private fun target(state: DerivationState): Pair<String, Int>? = (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
        val verb = state.terms[i + 1]
        val initial = roots[verb.rootUpadesha()] ?: return@firstNotNullOfOrNull null
        // The form begins with the root (or अट् + root); its vikaraṇa न is the first न after it.
        val start = if (verb.varnas.firstOrNull() == initial) 0
            else if (verb.varnas.firstOrNull() == dev.panini.shiksha.Svara.A && verb.varnas.getOrNull(1) == initial) 1
            else return@firstNotNullOfOrNull null
        val nasal = (start + 1..verb.varnas.lastIndex).firstOrNull { verb.varnas[it] == Vyanjana.NA }
        UpasargadAsamasePiSutra.nasalAfterUpasarga(state, state.terms[i], verb, nasal)?.let { verb.id to it }
    }

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (id, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.15: the न of हिनु/मीना becomes ण after the upasarga.")
    }
}

/**
 * 8.4.19 अनितेः — after an upasarga's र्/ष्, the न of अन् (अनँ) becomes ण: प्र + अनिति → प्राणिति,
 * परा + अनिति → पराणिति. The vowel join (6.1.101) comes first, as 8.4 is asiddha to 6.1; the merged word
 * is known by the dropped verb's root and the upasarga it was joined to.
 */
object AnitehSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.19", text = "अनितेः",
    hindiExplanation = "उपसर्ग के र्/ष् से परे अन् धातु के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840019,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private const val ROOT = "अनँ"

    /** The root अन्'s न after an upasarga's र्/ष्, whether or not it ends the pada. */
    internal fun rootNasal(state: DerivationState): Pair<String, Int>? {
        // Unmerged: upasarga + verb form beginning with the root (or अट् + root).
        (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
            val verb = state.terms[i + 1]
            if (verb.rootUpadesha() != ROOT) return@firstNotNullOfOrNull null
            val nasal = (0..1).firstOrNull { verb.varnas.getOrNull(it) == Vyanjana.NA }
            UpasargadAsamasePiSutra.nasalAfterUpasarga(state, state.terms[i], verb, nasal)?.let { verb.id to it }
        }?.let { return it }
        // Merged by a vowel ekādeśa: the upasarga term now carries the verb.
        if (state.droppedTerms.none { it.rootUpadesha() == ROOT }) return null
        val word = state.terms.firstOrNull { term ->
            state.samjnas.any { it.targetId == term.id && it.samjna == Samjna.UPASARGA }
        } ?: return null
        val prefixLength = word.upadesha.toVarnas().size
        val trigger = (0 until minOf(prefixLength, word.varnas.size)).lastOrNull {
            word.varnas[it] == Vyanjana.RA || word.varnas[it] == Vyanjana.SSA
        } ?: return null
        // The root's न is the first consonant after the coalesced vowel.
        val nasal = (trigger + 1..word.varnas.lastIndex).firstOrNull { word.varnas[it] is Vyanjana } ?: return null
        if (word.varnas[nasal] != Vyanjana.NA) return null
        return word.id to nasal
    }

    internal fun isPadaFinal(state: DerivationState, at: Pair<String, Int>): Boolean {
        val terms = state.terms
        val term = terms.first { it.id == at.first }
        return at.second == term.varnas.lastIndex && terms.last().id == term.id
    }

    private fun target(state: DerivationState) = rootNasal(state)?.takeUnless { isPadaFinal(state, it) }

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (id, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.19: the न of अन् becomes ण after the upasarga.")
    }
}
