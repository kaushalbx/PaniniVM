package dev.panini.ashtadhyayi.adhyaya2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.analysis.SamasaMorphologicalFeature
import dev.panini.ashtadhyayi.adhyaya2.pada2.KartariChaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.NityamKridajivikayohSutra
import dev.panini.core.KrtAffix
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KridaJivikaSemanticTest {
    @Test
    fun `explicit livelihood is the scoped exception to the agentive prohibition`() {
        val c = context("दन्त", "लेखक", SamasaSemanticRelation.LIVELIHOOD)
        val agentive = c.copy(padas = listOf(c.purvaPada, c.uttaraPada.copy(
            morphologicalFeatures = setOf(SamasaMorphologicalFeature.KRIT_DERIVED))),
            semanticRelations = c.semanticRelations + SamasaSemanticRelation.AGENT_RELATION)
        assertFalse(KartariChaSutra.matches(agentive))
        assertTrue(KartariChaSutra.matches(agentive.copy(semanticRelations = setOf(SamasaSemanticRelation.AGENT_RELATION))))
    }

    private fun context(first: String, second: String, meaning: SamasaSemanticRelation) = SamasaRuleContext(
        listOf(SamasaPada(first, Vibhakti.SASTHI), SamasaPada(second, krtAffix = KrtAffix.NVUL)),
        SamasaType.TATPURUSA, semanticRelations = setOf(meaning))

    @Test
    fun `explicit livelihood and play meanings do not depend on five spelling endings`() {
        for (c in listOf(
            context("दन्त", "लेखक", SamasaSemanticRelation.LIVELIHOOD),
            context("उद्दालकपुष्प", "भञ्जिका", SamasaSemanticRelation.SPORT_OR_PLAY),
            context("वस्त्र", "सीवक", SamasaSemanticRelation.LIVELIHOOD),
        )) assertTrue(NityamKridajivikayohSutra.matches(c))
    }

    @Test
    fun `spelling alone and an unrelated semantic relation cannot license the rule`() {
        val c = context("दन्त", "लेखक", SamasaSemanticRelation.LIVELIHOOD)
        assertFalse(NityamKridajivikayohSutra.matches(c.copy(semanticRelations = emptySet())))
        assertFalse(NityamKridajivikayohSutra.matches(c.copy(semanticRelations = setOf(SamasaSemanticRelation.PROPER_NAME))))
        assertFalse(NityamKridajivikayohSutra.matches(c.copy(padas = listOf(c.purvaPada, SamasaPada("लेखक")))))
    }

    @Test
    fun `case compound type and affix identity remain independently required`() {
        val c = context("दन्त", "लेखक", SamasaSemanticRelation.LIVELIHOOD)
        for (case in Vibhakti.entries.filter { it != Vibhakti.SASTHI }) {
            assertFalse(NityamKridajivikayohSutra.matches(c.copy(padas = listOf(c.purvaPada.copy(vibhakti = case), c.uttaraPada))))
        }
        assertFalse(NityamKridajivikayohSutra.matches(c.copy(samasaType = SamasaType.KARMADHARAYA)))
        assertFalse(NityamKridajivikayohSutra.matches(c.copy(padas = listOf(c.purvaPada, c.uttaraPada.copy(krtAffix = KrtAffix.TRC)))))
    }
}
