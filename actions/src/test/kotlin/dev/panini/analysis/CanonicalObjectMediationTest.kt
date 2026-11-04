package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

/** Supplied readings test object-designation coverage, not lexical sense discovery. */
class CanonicalObjectMediationTest {
    private data class Construction(val rule: String, val root: String,
        val meanings: Set<SemanticRelation>, val prefixes: List<String> = emptyList(), val causative: Boolean = false)

    private val constructions = listOf(
        Construction("1.4.38", "क्रुध्", setOf(SemanticRelation.RECIPIENT, SemanticRelation.ANGER_TARGET), listOf("अभि")),
        Construction("1.4.43", "दिव", setOf(SemanticRelation.INSTRUMENT)),
        Construction("1.4.46", "स्था", setOf(SemanticRelation.LOCATION), listOf("अधि")),
        Construction("1.4.47", "विश्", setOf(SemanticRelation.LOCATION), listOf("अभि", "नि")),
        Construction("1.4.48", "वस्", setOf(SemanticRelation.LOCATION), listOf("उप")),
        Construction("1.4.49", "दा", setOf(SemanticRelation.DESIRED_OBJECT)),
        Construction("1.4.50", "गम्", setOf(SemanticRelation.INDIFFERENT_OBJECT)),
        Construction("1.4.51", "दुह्", setOf(SemanticRelation.SECONDARY_OBJECT)),
        Construction("1.4.52", "गम्", setOf(SemanticRelation.CAUSEE_AGENT), causative = true),
        Construction("1.4.53", "कृ", setOf(SemanticRelation.CAUSEE_AGENT), causative = true),
    )

    private fun context(construction: Construction, passive: Boolean, cases: Set<Vibhakti>, bound: Boolean): KarakaRuleContext {
        val recipient = ParticipantFacts("recipient", AvyayaPada("recipient", "recipient"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.RECIPIENT),
            intendedObject = if (bound) IntendedObject.Written("object") else null)
        val obj = ParticipantFacts("object", AvyayaPada("object", "object"), cases,
            semanticRelations = construction.meanings)
        val verb = TingantaPada("verb", construction.prefixes,
            DhatuPrakriti(construction.root, construction.root,
                if (construction.causative) listOf("णिच्") else emptyList()), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        val voice = if (passive) Prayoga.KARMANI else if (construction.causative) Prayoga.CAUSATIVE else Prayoga.KARTARI
        return KarakaRuleContext(DhatuIdentity(construction.root), recipient, listOf(recipient, obj), voice, verbNode = verb)
    }

    @Test fun `every canonical object designation can supply an explicitly intended mediator`() {
        for (construction in constructions) for (passive in listOf(false, true)) for (bound in listOf(false, true)) {
            val ctx = context(construction, passive, setOf(if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA), bound)
            val objectResult = KarakaRuleEngine.resolve(ctx.copy(participant = ctx.allParticipants[1]))
            assertEquals(Karaka.KARMAN, objectResult.resolved, "${construction.rule} passive=$passive")
            assertEquals(construction.rule, objectResult.evidence.first().sutra)
            val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.KarmanaYamAbhipraitiSampradanamSutra
            assertFalse(rule.matches(ctx), "Unprepared facts cannot substitute for a resolved object.")
            assertTrue(rule.matches(ctx.copy(mediatingObjectResolutions = mapOf("object" to objectResult))))
            val result = KarakaRuleEngine.resolve(ctx)
            assertEquals(listOf("1.4.32", "2.3.13"), result.evidence.map { it.sutra }, "${construction.rule} passive=$passive bound=$bound")
            assertEquals(emptyMap(), result.unresolvedObjectMediators)
        }
    }

    @Test fun `optional special object ambiguity remains an explicit unresolved dependency`() {
        for (construction in constructions.filter { it.rule == "1.4.53" }) {
            for (passive in listOf(false, true)) for (bound in listOf(false, true)) {
                val ctx = context(construction, passive,
                    setOf(if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA, Vibhakti.TRTIYA), bound)
                val obj = KarakaRuleEngine.resolve(ctx.copy(participant = ctx.allParticipants[1]))
                assertNull(obj.resolved, construction.rule)
                assertTrue(Karaka.KARMAN in obj.candidates)
                val recipient = KarakaRuleEngine.resolve(ctx)
                assertNull(recipient.resolved, construction.rule)
                assertEquals(mapOf("object" to obj.candidates), recipient.unresolvedObjectMediators)
            }
        }
    }

    @Test fun `a weaker sesha hypothesis cannot veto a resolved canonical object`() {
        val construction = constructions.single { it.rule == "1.4.49" }
        for (passive in listOf(false, true)) for (bound in listOf(false, true)) {
            val ctx = context(construction, passive, setOf(if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA), bound)
            val obj = ctx.allParticipants[1].copy(
                semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SESHA_RELATION),
                semanticEvidence = setOf(
                    SemanticRelationEvidence(SemanticRelation.DESIRED_OBJECT, SemanticEvidenceSource.CLAUSE_CONTEXT, "Established object."),
                    SemanticRelationEvidence(SemanticRelation.SESHA_RELATION, SemanticEvidenceSource.NOMINAL_CATEGORY, "Hypothesis only.")))
            val result = KarakaRuleEngine.resolve(ctx.copy(allParticipants = listOf(ctx.participant, obj)))
            assertEquals(listOf("1.4.32", "2.3.13"), result.evidence.map { it.sutra })
        }
    }

    @Test fun `a selected nonobject branch cannot mediate recipient intent`() {
        for (construction in constructions.filter { it.rule == "1.4.53" }) {
            for (passive in listOf(false, true)) for (bound in listOf(false, true)) {
                val ctx = context(construction, passive, setOf(Vibhakti.TRTIYA), bound)
                val obj = ctx.allParticipants[1].let {
                    if (construction.rule == "1.4.53") it.copy(semanticRelations = it.semanticRelations + SemanticRelation.INDEPENDENT_AGENT)
                    else it
                }
                val prepared = ctx.copy(allParticipants = listOf(ctx.participant, obj))
                assertEquals(if (construction.rule == "1.4.43") Karaka.KARANA else Karaka.KARTR,
                    KarakaRuleEngine.resolve(prepared.copy(participant = obj)).resolved)
                val recipient = KarakaRuleEngine.resolve(prepared)
                assertNull(recipient.resolved)
                assertEquals(emptyMap(), recipient.unresolvedObjectMediators)
            }
        }
    }
}
