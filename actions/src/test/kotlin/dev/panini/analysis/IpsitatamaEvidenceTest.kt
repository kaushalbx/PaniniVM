package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertEquals

class IpsitatamaEvidenceTest {
    @Test fun `weak competing object tags cannot veto an established object reading`() {
        for ((meaning, rule) in listOf(SemanticRelation.DESIRED_OBJECT to "1.4.49",
            SemanticRelation.INDIFFERENT_OBJECT to "1.4.50")) {
            for (weak in listOf(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.CAUSEE_AGENT)) {
                val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
                    semanticRelations = setOf(meaning, weak), semanticEvidence = setOf(
                        SemanticRelationEvidence(meaning, SemanticEvidenceSource.CLAUSE_CONTEXT, "Selected object reading."),
                        SemanticRelationEvidence(weak, SemanticEvidenceSource.VOICE_CONSTRUCTION, "Weak competing hypothesis.")))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दुह्"), p, listOf(p), Prayoga.KARTARI))
                assertEquals(listOf(rule, "2.3.2"), result.evidence.map { it.sutra }, "$meaning $weak")
            }
        }
    }

    @Test fun `equally established primary and secondary readings preserve distinct same-role proofs`() {
        for ((meaning, rule) in listOf(SemanticRelation.DESIRED_OBJECT to "1.4.49",
            SemanticRelation.INDIFFERENT_OBJECT to "1.4.50")) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
                semanticRelations = setOf(meaning, SemanticRelation.SECONDARY_OBJECT))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दुह्"), p, listOf(p), Prayoga.KARTARI))
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(setOf(listOf(rule, "2.3.2"), listOf("1.4.51", "2.3.2")),
                (listOf(result.evidence) + result.alternatives.map { it.evidence }).map { proofs -> proofs.map { it.sutra } }.toSet())
        }
    }

    @Test fun `object meaning cannot be established by case category voice or root valency alone`() {
        for ((meaning, rule, root) in listOf(
            Triple(SemanticRelation.DESIRED_OBJECT, "1.4.49", "भुज्"),
            Triple(SemanticRelation.INDIFFERENT_OBJECT, "1.4.50", "भुज्"),
            Triple(SemanticRelation.SECONDARY_OBJECT, "1.4.51", "दुह्"))) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) for (source in SemanticEvidenceSource.entries) {
                val case = if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA
                val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case),
                    semanticRelations = setOf(meaning), semanticEvidence = setOf(
                        SemanticRelationEvidence(meaning, source, "Proposed object reading.")))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), voice))
                val established = source in setOf(SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.CLAUSE_CONTEXT)
                assertEquals(established, result.evidence.any { it.sutra == rule }, "$rule $voice $source")
            }
        }
    }

    @Test fun `anipsita rule inherits karman but seeing alone does not establish indifference`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.TathayuktamCanipsitamSutra
        assertEquals(setOf("1.4.23"), rule.adhikara)
        assertEquals(setOf("1.4.49"), rule.anuvrtti)
        assertEquals(false, SemanticRelation.INDIFFERENT_OBJECT in requireNotNull(DhatuKarakaProfiles.forSurface("पश्यति")).relations)
    }
    @Test fun `two compatible proofs of one role do not prevent case from rejecting a different role`() {
        for ((case, role) in listOf(Vibhakti.DVITIYA to Karaka.KARMAN, Vibhakti.PANCHAMI to Karaka.APADANA,
            Vibhakti.SASTHI to null)) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case), semanticRelations = setOf(
                SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDIFFERENT_OBJECT,
                SemanticRelation.SOURCE, SemanticRelation.SEPARATION_REFERENCE))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), Prayoga.KARTARI))
            assertEquals(role, result.resolved)
            if (role == null) assertEquals(setOf(Karaka.KARMAN, Karaka.APADANA), result.candidates)
        }
    }

    @Test fun `anipsita hypothesis alone cannot prove the special meaning`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.INDIFFERENT_OBJECT), semanticEvidence = setOf(
                SemanticRelationEvidence(SemanticRelation.INDIFFERENT_OBJECT, SemanticEvidenceSource.NOMINAL_CATEGORY, "Hypothesis.")))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(null, result.resolved)
        assertEquals(emptyList(), result.evidence)
    }
    @Test fun `anipsita meaning outranks weak desired defaults and retains independent designation with wrong case`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (case in listOf(if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA, Vibhakti.SASTHI)) {
                val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case),
                    semanticRelations = setOf(SemanticRelation.INDIFFERENT_OBJECT, SemanticRelation.DESIRED_OBJECT),
                    semanticEvidence = setOf(
                        SemanticRelationEvidence(SemanticRelation.INDIFFERENT_OBJECT, SemanticEvidenceSource.CLAUSE_CONTEXT, "Undesired in this reading."),
                        SemanticRelationEvidence(SemanticRelation.DESIRED_OBJECT, SemanticEvidenceSource.DHATU_VALENCY, "General object position.")))
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), voice))
                assertEquals(Karaka.KARMAN, result.resolved)
                assertEquals("1.4.50", result.evidence.first().sutra)
                assertEquals(case.takeUnless { it == Vibhakti.SASTHI }, result.resolvedVibhakti)
            }
        }
    }

    @Test fun `equally supported desired and undesired readings retain both proofs of the same object role`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val case = if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case),
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDIFFERENT_OBJECT))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p), voice))
            assertEquals(Karaka.KARMAN, result.resolved)
            assertEquals(case, result.resolvedVibhakti)
            assertEquals("1.4.49", result.evidence.first().sutra)
            assertEquals("1.4.50", result.alternatives.single().evidence.first().sutra)
            assertEquals(Karaka.KARMAN, result.alternatives.single().karaka)
        }
    }
    @Test fun `an object candidate needs action-relative desired or anipsita meaning`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            val case = if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA
            fun resolve(meaning: Set<SemanticRelation>): KarakaResolution {
                val participant = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case), semanticRelations = meaning)
                return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), participant,
                    listOf(participant), voice, candidates = setOf(Karaka.KARMAN)))
            }
            val caseSutra = if (voice == Prayoga.KARTARI) "2.3.2" else "2.3.46"
            assertEquals(listOf("1.4.49", caseSutra), resolve(setOf(SemanticRelation.DESIRED_OBJECT)).evidence.map { it.sutra })
            assertEquals(listOf("1.4.50", caseSutra), resolve(setOf(SemanticRelation.INDIFFERENT_OBJECT)).evidence.map { it.sutra })
            assertEquals(null, resolve(emptySet()).resolved)
            assertEquals(emptyList(), resolve(emptySet()).evidence)
        }
    }
}
