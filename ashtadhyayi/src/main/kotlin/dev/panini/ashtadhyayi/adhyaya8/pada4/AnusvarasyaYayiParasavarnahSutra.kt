package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.58: anusvārasya yayi parasavarṇaḥ.
 * Anusvāra is replaced by a sound homogeneous with the following sound (parasavarṇa)
 * if that sound is in the 'yay' pratyāhāra (all consonants except ś, ṣ, s, h).
 */
object AnusvarasyaYayiParasavarnahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.58",
    text = "अनुस्वारस्य ययि परसवर्णः",
    hindiExplanation = "यय् वर्ण परे होने पर अनुस्वार के स्थान पर परसवर्ण (बाद वाले वर्ण का सवर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840058,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findTarget(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = findTarget(context) ?: return DerivationChange(context, "8.4.58: Target anusvāra not found.")
        val substitute = nasalFor(target.follower)

        return DerivationChange(
            state = context.replaceTermVarna(target.term.id, target.varnaIndex, listOf(substitute), sutra),
            explanation = "8.4.58: Replaced Anusvāra with nasal parasavarṇa '${substitute.devanagari}'."
        )
    }

    private fun findTarget(context: DerivationState): Target? {
        val positions = context.terms.flatMap { term -> term.varnas.indices.map { index -> term to index } }
        for (position in 0 until positions.lastIndex) {
            val (term, index) = positions[position]
            if (term.varnas[index] != Ayogavaha.ANUSVARA) continue
            val follower = positions[position + 1].first.varnas[positions[position + 1].second]
            if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, follower)) return Target(term, index, follower)
        }
        return null
    }

    private fun nasalFor(follower: Varna): Vyanjana = when (follower) {
        Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA -> Vyanjana.NGA
        Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA, Vyanjana.NYA -> Vyanjana.NYA
        Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA, Vyanjana.NNA -> Vyanjana.NNA
        Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA -> Vyanjana.NA
        Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA, Vyanjana.MA -> Vyanjana.MA
        else -> Vyanjana.NA
    }

    private data class Target(val term: dev.panini.derivation.DerivationTerm, val varnaIndex: Int, val follower: Varna)
}
