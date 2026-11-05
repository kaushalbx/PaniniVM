package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class AgentDesignationEvidenceTest {
    @Test fun `weak prompter hypothesis cannot turn an independent causal participant into the expressed causer`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.PROMPTER_CAUSE),
            semanticEvidence = setOf(
                SemanticRelationEvidence(SemanticRelation.INDEPENDENT_AGENT, SemanticEvidenceSource.CLAUSE_CONTEXT, "Selected agent."),
                SemanticRelationEvidence(SemanticRelation.PROMPTER_CAUSE, SemanticEvidenceSource.VOICE_CONSTRUCTION, "Causer hypothesis.")))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("पच्"), p, listOf(p), Prayoga.CAUSATIVE))
        assertEquals(listOf("1.4.54", "2.3.18"), result.evidence.map { it.sutra })
    }

    @Test fun `special action senses require independent evidence before causee promotion`() {
        for ((root, sense) in listOf("वह्" to SemanticRelation.VAH_NIYANTR_KARTRKA,
            "भक्ष्" to SemanticRelation.BHAKS_INJURIOUS_SENSE)) for (source in SemanticEvidenceSource.entries) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT, sense), semanticEvidence = setOf(
                    SemanticRelationEvidence(sense, source, "Proposed action sense.")))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.CAUSATIVE))
            val established = source in setOf(SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.CLAUSE_CONTEXT)
            assertEquals(established, result.evidence.any { it.sutra == "1.4.52" }, "$root $source")
        }
    }

    @Test fun `typed finite morphology cannot be replaced by a causative label`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
        for ((prefixes, affixes) in listOf(emptyList<String>() to emptyList(), listOf("unknown") to listOf("णिच्"),
            emptyList<String>() to listOf("णिच्", "unknown"))) {
            val verb = TingantaPada("verb", prefixes, DhatuPrakriti("गम्", "गम्", affixes), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), p, listOf(p), Prayoga.CAUSATIVE, verbNode = verb))
            assertTrue(result.evidence.none { it.sutra == "1.4.52" }, "$prefixes $affixes")
        }
    }

    @Test fun `original agent independent agent and causer require established meanings`() {
        for ((root, meaning, rule) in listOf(
            Triple("गम्", SemanticRelation.CAUSEE_AGENT, "1.4.52"),
            Triple("कृ", SemanticRelation.CAUSEE_AGENT, "1.4.53"),
            Triple("पच्", SemanticRelation.INDEPENDENT_AGENT, "1.4.54"),
            Triple("गम्", SemanticRelation.PROMPTER_CAUSE, "1.4.55"))) {
            for (passive in listOf(false, true)) for (source in SemanticEvidenceSource.entries) {
                val causative = rule != "1.4.54"
                val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(
                    if (meaning == SemanticRelation.CAUSEE_AGENT) { if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA }
                    else if (passive) Vibhakti.TRTIYA else Vibhakti.PRATHAMA),
                    semanticRelations = setOf(meaning), semanticEvidence = setOf(SemanticRelationEvidence(meaning, source, "Proposed agent reading.")))
                val verb = TingantaPada("verb", emptyList(), DhatuPrakriti(root, root,
                    if (causative) listOf("णिच्") else emptyList()), Lakara.LAT,
                    TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
                    vikarana = if (passive) Vikarana.YAK else null)
                val voice = if (passive) Prayoga.KARMANI else if (causative) Prayoga.CAUSATIVE else Prayoga.KARTARI
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice, verbNode = verb))
                val established = source in setOf(SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.CLAUSE_CONTEXT)
                assertEquals(established, result.evidence.any { it.sutra == rule }, "$rule passive=$passive $source")
            }
        }
    }

    @Test fun `original agent designation survives an incompatible ending`() {
        for (passive in listOf(false, true)) for (case in listOf(Vibhakti.DVITIYA, Vibhakti.PRATHAMA, Vibhakti.TRTIYA)) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case), semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("गम्", "गम्", listOf("णिच्")), Lakara.LAT,
                TingPratyaya("त", "त"), vikarana = if (passive) Vikarana.YAK else null)
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("गम्"), p, listOf(p),
                if (passive) Prayoga.KARMANI else Prayoga.CAUSATIVE, verbNode = verb))
            val valid = case == if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(listOf("1.4.52") + if (valid) listOf(if (passive) "2.3.46" else "2.3.2") else emptyList(),
                result.evidence.map { it.sutra })
        }
    }
}
