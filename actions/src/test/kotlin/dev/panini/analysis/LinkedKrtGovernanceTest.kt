package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinkedKrtGovernanceTest {
    private val actionNoun = KridantaPratipadika(sourceText = "करणम्", upasargas = emptyList(),
        dhatu = DhatuPrakriti("कृ", "कृ"), krtPratyaya = "ल्युट्")

    private fun resolve(role: SemanticRelation, voice: Prayoga = Prayoga.KARTARI,
        expressed: Set<Karaka>? = emptySet(), governor: KridantaPratipadika? = actionNoun,
        extras: Set<SemanticRelation> = emptySet(), cases: Set<Vibhakti> = setOf(Vibhakti.SASTHI),
        eligibility: KrtGenitiveEligibility? = null, governorId: String? = "action-1"): KarakaResolution {
        val participant = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), cases,
            semanticRelations = setOf(role) + extras)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), participant,
            listOf(participant), voice, governingKrt = governor, governingKrtExpressedRoles = expressed,
            governingKrtOccurrenceId = governorId, governingKrtGenitiveEligibility = eligibility))
    }

    @Test fun `linked action noun establishes agent or object genitive independently of finite voice`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI, Prayoga.BHAVE)) {
            for ((premise, role, sutra) in listOf(
                Triple(SemanticRelation.INDEPENDENT_AGENT, Karaka.KARTR, "1.4.54"),
                Triple(SemanticRelation.DESIRED_OBJECT, Karaka.KARMAN, "1.4.49"))) {
                for (extras in listOf(emptySet(), setOf(SemanticRelation.SESHA_RELATION))) {
                    val result = resolve(premise, voice, extras = extras)
                    assertEquals(role, result.resolved)
                    assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                    assertEquals(listOf(sutra, "2.3.65"), result.evidence.map { it.sutra })
                }
            }
        }
    }

    @Test fun `unknown reading does not grant genitive and expressed agent is not unexpressed`() {
        assertEquals(null, resolve(SemanticRelation.INDEPENDENT_AGENT, expressed = null).resolvedVibhakti)
        val agentNoun = actionNoun.copy(krtPratyaya = "तृच्", sourceText = "कर्ता")
        assertEquals(null, resolve(SemanticRelation.INDEPENDENT_AGENT,
            expressed = setOf(Karaka.KARTR), governor = agentNoun).resolvedVibhakti)
        assertEquals(listOf("1.4.49", "2.3.65"), resolve(SemanticRelation.DESIRED_OBJECT,
            expressed = setOf(Karaka.KARTR), governor = agentNoun).evidence.map { it.sutra })
    }

    @Test fun `unlinked unknown and taddhita wrapped stems do not establish direct krt governance`() {
        val wrapped = actionNoun.copy(vikaras = listOf(TaddhitaVikara("इनि", "इनि")))
        for (governor in listOf(null, wrapped, actionNoun.copy(krtPratyaya = "अज्ञात"))) {
            val result = resolve(SemanticRelation.DESIRED_OBJECT, governor = governor)
            assertTrue(result.evidence.none { it.sutra == "2.3.65" })
            assertEquals(null, result.resolvedVibhakti)
        }
        assertTrue(resolve(SemanticRelation.INSTRUMENT).evidence.none { it.sutra == "2.3.65" })
    }

    @Test fun `dual eligibility preserves object proof and blocks agent genitive`() {
        val eligibility = KrtGenitiveEligibility("action-1", setOf(Karaka.KARTR, Karaka.KARMAN))
        val result = resolve(SemanticRelation.DESIRED_OBJECT,
            eligibility = eligibility)
        assertEquals(Karaka.KARMAN, result.resolved)
        assertEquals(listOf("1.4.49", "2.3.66"), result.evidence.map { it.sutra })
        assertEquals(null, resolve(SemanticRelation.INDEPENDENT_AGENT, eligibility = eligibility).resolvedVibhakti)
        val agent = resolve(SemanticRelation.INDEPENDENT_AGENT, eligibility = eligibility, cases = setOf(Vibhakti.TRTIYA))
        assertEquals(listOf("1.4.54", "2.3.18"), agent.evidence.map { it.sutra })
        assertTrue(result.evidence.first().semanticEvidence.any {
            it.relation == SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT && it.detail.contains("action-1")
        })
    }

    @Test fun `another occurrence cannot supply dual eligibility even with an identical governor`() {
        val eligibility = KrtGenitiveEligibility("action-2", setOf(Karaka.KARTR, Karaka.KARMAN))
        val mismatched = resolve(SemanticRelation.DESIRED_OBJECT, eligibility = eligibility)
        assertEquals(null, mismatched.resolvedVibhakti)
        assertTrue(mismatched.evidence.none { it.sutra == "2.3.66" })
        assertEquals(listOf("1.4.49", "2.3.66"), resolve(SemanticRelation.DESIRED_OBJECT,
            eligibility = eligibility, governorId = "action-2").evidence.map { it.sutra })
        assertEquals(null, resolve(SemanticRelation.DESIRED_OBJECT, eligibility = eligibility,
            governorId = null).resolvedVibhakti)
    }

    @Test fun `separate single role eligibilities do not create a combined restriction`() {
        val agent = resolve(SemanticRelation.INDEPENDENT_AGENT,
            eligibility = KrtGenitiveEligibility("agent-governor", setOf(Karaka.KARTR)), governorId = "agent-governor")
        val obj = resolve(SemanticRelation.DESIRED_OBJECT,
            eligibility = KrtGenitiveEligibility("object-governor", setOf(Karaka.KARMAN)), governorId = "object-governor")
        assertEquals(listOf("1.4.54", "2.3.65"), agent.evidence.map { it.sutra })
        assertEquals(listOf("1.4.49", "2.3.65"), obj.evidence.map { it.sutra })
    }

    @Test fun `unbound markers and expressed role contradictions fail closed`() {
        for (premise in listOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.DESIRED_OBJECT)) {
            val marker = if (premise == SemanticRelation.INDEPENDENT_AGENT)
                SemanticRelation.KRT_DUAL_ELIGIBLE_AGENT else SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT
            val result = resolve(premise, extras = setOf(marker))
            assertEquals(null, result.resolvedVibhakti)
            assertTrue(result.evidence.none { it.sutra == "2.3.65" || it.sutra == "2.3.66" })
        }
        assertEquals(null, resolve(SemanticRelation.DESIRED_OBJECT, expressed = setOf(Karaka.KARTR),
            eligibility = KrtGenitiveEligibility("action-1", setOf(Karaka.KARTR, Karaka.KARMAN))).resolvedVibhakti)
        assertEquals(null, resolve(SemanticRelation.DESIRED_OBJECT,
            eligibility = KrtGenitiveEligibility("action-1", setOf(Karaka.KARTR))).resolvedVibhakti)
    }
}
