package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.*

/**
 * 8.4.47 अनचि च — a yar after a vowel and not before a vowel is optionally doubled
 * (वा from 8.4.45): दध्यत्र → दद्ध्यत्र (8.4.53). A yar already beside a savarṇa consonant has
 * been doubled, so the rule does not repeat on its own output.
 *
 * The options are every eligible yar doubled, or none (the default, so paradigms keep their
 * stated forms). Only a yar before a consonant is taken: a word-final one has no follower yet.
 * The opinions of 8.4.50–8.4.52 (no doubling in long clusters, nowhere for Śākalya, not after a
 * long vowel) are forms with no doubling, which the declined option already gives.
 * र is not doubled: the tradition has कर्तरि and अर्क, never *कर्र्तरि (8.4.46 doubles what follows र).
 */
object AnaciCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.47", text = "अनचि च",
    hindiExplanation = "अच् से परे, अच् परे न होने पर यर् को विकल्प से द्वित्व होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840047,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun targets(state: DerivationState): List<LocatedVarna> {
        if (state.terms.any { it.itProcessingPending || it.itDesignations.isNotEmpty() || it.deferredItDesignations.isNotEmpty() }) return emptyList()
        val engine = Ashtadhyayi.pratyaharaEngine
        val sounds = state.terms.flatMap { term -> term.varnas.mapIndexed { index, varna -> LocatedVarna(term.id, index, varna) } }
        val atJoin = state.isJoinOfCompletedPadas()
        return (1..sounds.lastIndex).filter { i ->
            val target = sounds[i].varna
            val next = sounds.getOrNull(i + 1)?.varna
            (!atJoin || state.isJoinSite(listOfNotNull(sounds[i - 1], sounds[i], sounds.getOrNull(i + 1)).map { it.termId to it.index })) &&
                engine.contains(Pratyahara.AC, sounds[i - 1].varna) &&
                engine.contains(Pratyahara.YAR, target) && target != dev.panini.shiksha.Vyanjana.RA &&
                next != null && !engine.contains(Pratyahara.AC, next) && !Varnamala.areSavarna(target, next)
        }.map { sounds[it] }
    }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = targets(context).isNotEmpty()
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val occurrences = targets(state)
        require(occurrences.isNotEmpty())
        // Insert from the right so earlier indices stay valid.
        val doubled = occurrences.sortedWith(compareBy<LocatedVarna>({ state.terms.indexOfFirst { t -> t.id == it.termId } }, { it.index }))
            .asReversed().fold(state) { acc, at -> acc.insertTermVarnas(at.termId, at.index, listOf(at.varna), sutra) }
        return listOf(DerivationChange(doubled, "8.4.47 doubles each yar after a vowel and not before a vowel."),
            DerivationChange(state, "8.4.47 declined.", applied = false))
    }
}
