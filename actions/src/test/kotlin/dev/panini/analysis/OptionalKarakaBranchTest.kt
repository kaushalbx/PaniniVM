package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class OptionalKarakaBranchTest {
    @Test fun `established hiring cannot replace the inherited selected means prerequisite`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI, Prayoga.BHAVE)) {
            for (source in SemanticEvidenceSource.entries) {
                val p = ParticipantFacts("payment", AvyayaPada("payment", "payment"), setOf(Vibhakti.CHATURTHI),
                    semanticRelations = setOf(SemanticRelation.INSTRUMENT, SemanticRelation.FIXED_TERM_HIRING_PAYMENT),
                    semanticEvidence = setOf(
                        SemanticRelationEvidence(SemanticRelation.FIXED_TERM_HIRING_PAYMENT,
                            SemanticEvidenceSource.CLAUSE_CONTEXT, "Fixed-term wage, not outright purchase."),
                        SemanticRelationEvidence(SemanticRelation.INSTRUMENT, source, "Proposed selected means.")))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("क्री"), p, listOf(p), voice))
                val established = source in setOf(SemanticEvidenceSource.CLAUSE_CONTEXT,
                    SemanticEvidenceSource.LEXICAL_ENTRY)
                assertEquals(established, result.evidence.any { it.sutra == "1.4.44" }, "$voice $source")
                if (!established) {
                    assertNull(result.primaryOptionalDecision, "$voice $source")
                    assertTrue(result.evidence.none { it.sutra == "2.3.13" }, "$voice $source")
                }
            }
        }
    }

    @Test fun `fixed term hiring is an established construction meaning rather than a root prefix test`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.ParikrayaneSampradanamAnyatarasyamSutra
        for (root in listOf("क्री", "परिक्री", "ग्रह्")) {
            for (source in SemanticEvidenceSource.entries) {
                val p = ParticipantFacts("payment", AvyayaPada("payment", "payment"), setOf(Vibhakti.CHATURTHI),
                    semanticRelations = setOf(SemanticRelation.INSTRUMENT, SemanticRelation.FIXED_TERM_HIRING_PAYMENT),
                    semanticEvidence = setOf(SemanticRelationEvidence(SemanticRelation.FIXED_TERM_HIRING_PAYMENT,
                        source, "Fixed-term hiring premise.")))
                val context = KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI)
                val established = source in setOf(SemanticEvidenceSource.CLAUSE_CONTEXT,
                    SemanticEvidenceSource.LEXICAL_ENTRY)
                assertEquals(established, rule.matches(context), "$root $source")
                if (established) {
                    val result = KarakaRuleEngine.resolve(context)
                    assertEquals(listOf("1.4.44", "2.3.13"), result.evidence.map { it.sutra })
                    assertEquals(Karaka.SAMPRADANA, result.resolved)
                }
            }
        }
        val ordinary = ParticipantFacts("payment", AvyayaPada("payment", "payment"), setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT))
        assertFalse(rule.matches(KarakaRuleContext(DhatuIdentity("परिक्री"), ordinary, listOf(ordinary), Prayoga.KARTARI)))
    }

    private data class Construction(val root: String, val rule: String, val applied: Karaka, val underlying: Karaka,
        val underlyingRule: String, val meanings: Set<SemanticRelation>, val causative: Boolean = false)
    private val constructions = listOf(
        Construction("परिक्री", "1.4.44", Karaka.SAMPRADANA, Karaka.KARANA, "1.4.42",
            setOf(SemanticRelation.INSTRUMENT, SemanticRelation.FIXED_TERM_HIRING_PAYMENT)),
        Construction("कृ", "1.4.53", Karaka.KARMAN, Karaka.KARTR, "1.4.54", setOf(SemanticRelation.CAUSEE_AGENT), true),
    )

    private fun resolve(c: Construction, passive: Boolean, cases: Set<Vibhakti>): KarakaResolution {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), cases, semanticRelations = c.meanings)
        val verb = TingantaPada("verb", emptyList(), DhatuPrakriti(c.root, c.root,
            if (c.causative) listOf("णिच्") else emptyList()), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(c.root), p, listOf(p),
            if (passive) Prayoga.KARMANI else if (c.causative) Prayoga.CAUSATIVE else Prayoga.KARTARI, verbNode = verb))
    }

    @Test fun `both directions preserve canonical proof and the separate optional decision`() {
        for (c in constructions) for (passive in listOf(false, true)) {
            val appliedCase = if (c.applied == Karaka.SAMPRADANA) Vibhakti.CHATURTHI
                else if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA
            for (apply in listOf(false, true)) {
                val result = resolve(c, passive, setOf(if (apply) appliedCase else Vibhakti.TRTIYA))
                assertEquals(if (apply) c.applied else c.underlying, result.resolved, "${c.rule} $passive $apply")
                assertEquals(if (apply) appliedCase else Vibhakti.TRTIYA, result.resolvedVibhakti)
                assertEquals(if (apply) c.rule else c.underlyingRule, result.evidence.first().sutra)
                assertTrue(result.evidence.all { it.applied })
                assertEquals(apply, result.primaryOptionalDecision?.applied)
                assertEquals(c.rule, result.primaryOptionalDecision?.sutra)
                val alternative = result.alternatives.single()
                assertEquals(if (apply) c.underlying else c.applied, alternative.karaka)
                assertEquals(if (apply) c.underlyingRule else c.rule, alternative.evidence.first().sutra)
                assertTrue(alternative.evidence.all { it.applied })
                assertEquals(!apply, alternative.optionalDecision?.applied)
                assertFalse(alternative.matchesObservedMorphology)
            }
        }
    }

    @Test fun `both or neither compatible cases preserve both optional designations`() {
        for (c in constructions) for (passive in listOf(false, true)) {
            val appliedCase = if (c.applied == Karaka.SAMPRADANA) Vibhakti.CHATURTHI
                else if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA
            for (cases in listOf(setOf(appliedCase, Vibhakti.TRTIYA), setOf(Vibhakti.SASTHI))) {
                val result = resolve(c, passive, cases)
                assertNull(result.resolved, "${c.rule} $passive $cases")
                assertNull(result.resolvedVibhakti)
                assertEquals(setOf(c.applied, c.underlying), result.candidates)
                assertEquals(c.underlyingRule, result.alternatives.single().evidence.first().sutra)
            }
        }
    }

    @Test fun `optional causee needs an established original agent and records only inherited terms`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.HrKrorAnyatarasyamSutra
        assertEquals(setOf("1.4.23"), rule.adhikara)
        assertEquals(setOf("1.4.49", "1.4.52"), rule.anuvrtti)
        for (source in listOf(SemanticEvidenceSource.NOMINAL_CATEGORY, SemanticEvidenceSource.VOICE_CONSTRUCTION,
            SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY, SemanticEvidenceSource.CLAUSE_CONTEXT)) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
                semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT), semanticEvidence = setOf(
                    SemanticRelationEvidence(SemanticRelation.CAUSEE_AGENT, source, "Original agent premise.")))
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("कृ", "कृ", listOf("णिच्")),
                Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val context = KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p), Prayoga.CAUSATIVE, verbNode = verb)
            assertEquals(source == SemanticEvidenceSource.CLAUSE_CONTEXT, rule.matches(context))
        }
    }
}
