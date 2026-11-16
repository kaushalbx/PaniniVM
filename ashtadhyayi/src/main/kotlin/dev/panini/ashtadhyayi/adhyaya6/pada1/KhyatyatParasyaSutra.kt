package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 6.1.112 ख्यत्यात्परस्य — the अ of ङसि and ङस् after ख्य and त्य (खि, ति under yaṇ) becomes उ:
 * सखि + ङस् → सख्युः; पति + ङस् → पत्युः. 6.1.77 would merge stem and ending, so this rule (later,
 * hence preferred by 1.4.2) changes the ending while खि/ति still stand apart; yaṇ then gives ख्यु/त्यु.
 * Only a stem that is neither घि nor नदी keeps इ for yaṇ: हरि and भूपति take guṇa (7.3.111) instead.
 */
object KhyatyatParasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.112", text = "ख्यत्यात्परस्य",
    hindiExplanation = "ख्य और त्य (खि, ति के यणादेश) से परे ङसि और ङस् के अ को उ होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610112,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.VOWEL_SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? {
        for (index in 0 until state.terms.lastIndex) {
            val stem = state.terms[index]
            val affix = state.terms[index + 1]
            if (affix.sourceSupAffix !in setOf(SupAffix.NGASI, SupAffix.NGAS)) continue
            if (affix.varnas.firstOrNull() != Svara.A || affix.itProcessingPending) continue
            val tail = stem.varnas.takeLast(2)
            if (tail != listOf(Vyanjana.KHA, Svara.I) && tail != listOf(Vyanjana.TA, Svara.I)) continue
            if (state.samjnas.any { it.targetId == stem.id && it.samjna in setOf(Samjna.GHI, Samjna.NADI) }) continue
            return affix
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val affix = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(affix.id, 0, listOf(Svara.U), sutra),
            "6.1.112 replaces the अ of ङसि/ङस् with उ after ख्य/त्य.")
    }
}
