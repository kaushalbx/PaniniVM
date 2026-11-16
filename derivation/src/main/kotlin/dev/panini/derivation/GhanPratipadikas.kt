package dev.panini.derivation

import dev.panini.core.KrtAffix
import dev.panini.core.LingaSemanticClass
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanamEngine
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/**
 * Action nouns in घञ् (3.3.18 भावे), each derived forward by the kṛdanta engine from a Dhātupāṭha
 * root (युज् → योग, हृ → हार). The gender comes from Liṅgānuśāsana 36 घञबन्तः, never a fallback.
 * Plain roots only: upasarga compounds (आदेश) would multiply the inventory by twenty.
 */
object GhanPratipadikas {
    fun entries(): List<PratipadikaEntry> {
        val engine = KrdantaEngine()
        val linga = LinganushasanamEngine()
        return DhatuPatha.all.map { it.upadesha }.distinct().mapNotNull { root ->
            (runCatching { engine.deriveSourceStem(root, KrtAffix.GHAN.upadesha) }.getOrNull() as? KrdantaSourceStem.Productive)
                ?.surface
        }.distinct().mapNotNull { stem ->
            val gender = linga.resolveOrNull(LingaRuleContext(stem, pratyaya = KrtAffix.GHAN.upadesha,
                semanticClasses = setOf(LingaSemanticClass.BHAVA))) ?: return@mapNotNull null
            PratipadikaEntry(stem, gender.lingas, lingaRuleIds = setOf(gender.ruleId),
                lingaPratyaya = KrtAffix.GHAN.upadesha, lingaSemanticClasses = setOf(LingaSemanticClass.BHAVA))
        }
    }
}
