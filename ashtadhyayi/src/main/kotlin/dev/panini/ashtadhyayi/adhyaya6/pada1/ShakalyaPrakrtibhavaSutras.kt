package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.toHrasva
import dev.panini.sutra.*

/** The vowel sandhis a prakṛtibhāva leaves unapplied, as 6.1.125 blocks them. */
private val vowelSandhis = setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.101", "6.1.109")

/** A pada-final vowel and the vowel opening the next pada, unless the left is already pragṛhya. */
private fun DerivationState.vowelBoundary(): Triple<DerivationTerm, Svara, Svara>? {
    if (stage == DerivationStage.FINAL || terms.size < 2) return null
    val left = terms[terms.size - 2]
    val right = terms.last()
    val last = left.varnas.lastOrNull() as? Svara ?: return null
    val first = right.varnas.firstOrNull() as? Svara ?: return null
    if (!isPadaFinalVarna(left, left.varnas.lastIndex)) return null
    if (samjnas.any { it.targetId == left.id && it.samjna == Samjna.PRAGRHYA }) return null
    return Triple(left, last, first)
}

/** Keeps both vowels with the left one shortened (हरस्वश्च) and blocks the vowel sandhis. */
private fun DerivationState.shortenAndKeep(left: DerivationTerm, sutra: String): DerivationState {
    val last = left.varnas.last() as Svara
    var state = if (last.toHrasva() == last) this
        else replaceTermVarna(left.id, left.varnas.lastIndex, listOf(last.toHrasva()), sutra)
    val right = state.terms.last()
    for (rule in vowelSandhis) state = state.blockAtBoundary(rule, left.id, right.id, sutra)
    return state.copy(stage = DerivationStage.FINAL)
}

/**
 * 6.1.127 इकोऽसवर्णे शाकल्यस्य ह्रस्वश्च — for Śākalya a pada-final ik before a dissimilar vowel
 * stays, shortened: चक्रि अत्र beside चक्र्यत्र, कुमारि अत्र beside कुमार्यत्र. It is an opinion, so the
 * ordinary yaṇ (6.1.77) stays the default and the hiatus is the alternative.
 */
object IkoSavarneShakalyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.127", text = "इकोऽसवर्णे शाकल्यस्य ह्रस्वश्च",
    hindiExplanation = "शाकल्य के मत में पदान्त इक् को असवर्ण अच् परे प्रकृतिभाव और ह्रस्व होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 1, optional = true, kramaValue = 610127,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI, blocks = vowelSandhis, priority = SutraPriority.APAVADA,
), DerivationSutra {
    override val appliesByDefault = false
    private fun target(state: DerivationState): DerivationTerm? {
        val (left, last, first) = state.vowelBoundary() ?: return null
        return left.takeIf { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IK, last) && !Varnamala.areSavarna(last, first) }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        DerivationChange(state.shortenAndKeep(requireNotNull(target(state)), sutra),
            "6.1.127 keeps the pada-final ik before a dissimilar vowel, shortened (Śākalya)."),
        DerivationChange(state, "6.1.127 declined.", applied = false))
}

/**
 * 6.1.128 ऋत्यकः — a pada-final ak before ऋ optionally stays, shortened (वा, Śākalya, from 6.1.127):
 * ब्रह्म ऋषिः beside ब्रह्मर्षिः. The guṇa of 6.1.87 stays the default.
 */
object RtyakahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.128", text = "ऋत्यकः",
    hindiExplanation = "ह्रस्व ऋकार परे होने पर पदान्त अक् को विकल्प से प्रकृतिभाव और ह्रस्व होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 1, optional = true, kramaValue = 610128,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI, blocks = vowelSandhis, priority = SutraPriority.APAVADA,
), DerivationSutra {
    override val appliesByDefault = false
    private fun target(state: DerivationState): DerivationTerm? {
        val (left, last, first) = state.vowelBoundary() ?: return null
        return left.takeIf { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AK, last) && first == Svara.R }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        DerivationChange(state.shortenAndKeep(requireNotNull(target(state)), sutra),
            "6.1.128 keeps the pada-final ak before ऋ, shortened."),
        DerivationChange(state, "6.1.128 declined.", applied = false))
}
