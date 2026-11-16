package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.4.46 अचो रहाभ्यां द्वे — a yar after र् or ह् that follows a vowel is optionally doubled
 * (वा from 8.4.45): कर्तरि → कर्त्तरि, अन्तर्धौ → अन्तर्द्धौ (8.4.53 then gives jaś).
 * 8.4.49 शरोऽचि: a śar before a vowel is not doubled (वर्षः). A yar already beside a savarṇa
 * consonant has been doubled, so the rule does not repeat on its own output.
 */
object AchoRahabhyamDveSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.46", text = "अचो रहाभ्यां द्वे",
    hindiExplanation = "अच् से परे रेफ या हकार के बाद यर् को विकल्प से द्वित्व होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840046,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun targets(state: DerivationState): List<LocatedVarna> {
        if (state.terms.any { it.itProcessingPending || it.itDesignations.isNotEmpty() || it.deferredItDesignations.isNotEmpty() }) return emptyList()
        val engine = Ashtadhyayi.pratyaharaEngine
        val sounds = state.terms.flatMap { term -> term.varnas.mapIndexed { index, varna -> LocatedVarna(term.id, index, varna) } }
        val atJoin = state.isJoinOfCompletedPadas()
        return (2..sounds.lastIndex).filter { i ->
            val target = sounds[i].varna
            val next = sounds.getOrNull(i + 1)?.varna
            (!atJoin || state.isJoinSite(listOfNotNull(sounds[i - 2], sounds[i - 1], sounds[i], sounds.getOrNull(i + 1)).map { it.termId to it.index })) &&
                engine.contains(Pratyahara.AC, sounds[i - 2].varna) &&
                sounds[i - 1].varna in setOf(Vyanjana.RA, Vyanjana.HA) &&
                engine.contains(Pratyahara.YAR, target) &&
                !(engine.contains(Pratyahara.SHAR, target) && next != null && engine.contains(Pratyahara.AC, next)) &&
                (next == null || !Varnamala.areSavarna(target, next))
        }.map { sounds[it] }
    }
    // Doubling is an option a speaker may take; the default keeps the stated forms (जुह्वतु, कर्तरि).
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = targets(context).isNotEmpty()
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val occurrences = targets(state)
        require(occurrences.isNotEmpty())
        return occurrences.map { double(state, it) } + DerivationChange(state, "8.4.46 declined.", applied = false)
    }
    private fun double(state: DerivationState, at: LocatedVarna) = DerivationChange(
        state.insertTermVarnas(at.termId, at.index, listOf(at.varna), sutra),
        "8.4.46 doubles ${at.varna.devanagari} after र्/ह् following a vowel.")
}
