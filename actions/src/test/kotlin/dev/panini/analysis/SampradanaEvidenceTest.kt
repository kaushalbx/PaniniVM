package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.*

class SampradanaEvidenceTest {
    @Test fun `written binding checks its particular object and never falls back to another object`() {
        val recipient = ParticipantFacts("recipient", AvyayaPada("रामाय", "रामाय"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.RECIPIENT))
        val valid = ParticipantFacts("object", AvyayaPada("अन्नम्", "अन्नम्"), setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        val nonObject = valid.copy(id = "other", semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT))
        fun resolve(binding: IntendedObject, cases: Set<Vibhakti> = setOf(Vibhakti.CHATURTHI),
            others: List<ParticipantFacts> = listOf(valid, nonObject)): KarakaResolution {
            val p = recipient.copy(intendedObject = binding, possibleVibhaktis = cases)
            return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p) + others, Prayoga.KARTARI))
        }
        assertEquals(listOf("1.4.32", "2.3.13"), resolve(IntendedObject.Written("object")).evidence.map { it.sutra })
        for (id in listOf("other", "missing", "recipient")) assertEquals(null, resolve(IntendedObject.Written(id)).resolved)
        assertEquals(null, resolve(IntendedObject.Written("object"), others = listOf(valid, valid.copy())).resolved)
        assertEquals(null, resolve(IntendedObject.Written("object"), others = listOf(valid.copy(
            semanticRelations = valid.semanticRelations + SemanticRelation.SESHA_RELATION))).resolved)
        val badCase = resolve(IntendedObject.Written("object"), setOf(Vibhakti.SASTHI))
        assertEquals(Karaka.SAMPRADANA, badCase.resolved)
        assertEquals(null, badCase.resolvedVibhakti)
        assertEquals(listOf("1.4.32"), badCase.evidence.map { it.sutra })
        assertEquals(listOf("1.4.32", "2.3.13"), resolve(IntendedObject.Implied,
            others = listOf(nonObject)).evidence.map { it.sutra })
    }

    private fun resolve(source: SemanticEvidenceSource, others: List<ParticipantFacts> = emptyList(),
        binding: IntendedObject? = null): KarakaResolution {
        val recipient = ParticipantFacts("recipient", AvyayaPada("रामाय", "रामाय"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.RECIPIENT),
            semanticEvidence = setOf(SemanticRelationEvidence(SemanticRelation.RECIPIENT, source, "test premise")),
            intendedObject = binding)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), recipient,
            listOf(recipient) + others, Prayoga.KARTARI))
    }

    @Test fun `dative and human category alone cannot establish 1 4 32 even in a one participant clause`() {
        for (source in listOf(SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY, SemanticEvidenceSource.NOMINAL_CATEGORY)) {
            assertEquals(null, resolve(source).resolved)
            val obj = ParticipantFacts("object", AvyayaPada("कटम्", "कटम्"), setOf(Vibhakti.DVITIYA),
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
            assertEquals(null, resolve(source, listOf(obj)).resolved)
        }
    }

    @Test fun `independent recipient evidence supports implied or semantically established object`() {
        for (source in listOf(SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.CLAUSE_CONTEXT,
            SemanticEvidenceSource.DHATU_VALENCY)) {
            assertEquals(null, resolve(source).resolved)
            assertEquals(listOf("1.4.32", "2.3.13"), resolve(source, binding = IntendedObject.Implied).evidence.map { it.sutra })
            val bare = ParticipantFacts("other", AvyayaPada("other", "other"), setOf(Vibhakti.DVITIYA))
            assertEquals(null, resolve(source, listOf(bare)).resolved)
            val obj = bare.copy(semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
            assertEquals(listOf("1.4.32", "2.3.13"), resolve(source, listOf(obj)).evidence.map { it.sutra })
        }
    }

    @Test fun `root spelling cannot veto independently established ordinary recipient intent`() {
        for (root in listOf("क्रुध्", "रुच्", "स्पृह्", "धृ", "स्था", "श्लाघ्", "ह्नु", "शप्", "प्रतिश्रु", "अनुगृ")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val recipient = ParticipantFacts("recipient", AvyayaPada("रामाय", "रामाय"),
                    setOf(Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.RECIPIENT),
                    intendedObject = IntendedObject.Implied)
                val context = KarakaRuleContext(DhatuIdentity(root), recipient, listOf(recipient), voice)
                val result = KarakaRuleEngine.resolve(context)
                assertEquals(listOf("1.4.32", "2.3.13"), result.evidence.map { it.sutra }, "$root $voice")
                assertEquals(null, KarakaRuleEngine.resolve(context.copy(
                    participant = recipient.copy(intendedObject = null),
                    allParticipants = listOf(recipient.copy(intendedObject = null)))).resolved, "$root $voice")
            }
        }
    }
}
