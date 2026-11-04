package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class SecondaryObjectActionIdentityTest {
    @Test fun `causatives retain established secondary objects without promoting unannotated hypotheses`() {
        for (passive in listOf(false, true)) {
            val participant = ParticipantFacts("cow", AvyayaPada("cow", "cow"),
                setOf(if (passive) Vibhakti.PRATHAMA else Vibhakti.DVITIYA),
                semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT))
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("दुह्", "दुह्", listOf("णिच्")),
                Lakara.LAT, TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
                vikarana = if (passive) Vikarana.YAK else null)
            val context = KarakaRuleContext(DhatuIdentity("दुह्"), participant, listOf(participant),
                if (passive) Prayoga.KARMANI else Prayoga.CAUSATIVE, verbNode = verb)
            assertEquals(listOf("1.4.51", if (passive) "2.3.46" else "2.3.2"),
                KarakaRuleEngine.resolve(context).evidence.map { it.sutra })
            val uncertain = participant.copy(semanticRelations = setOf(SemanticRelation.AMBIGUOUS_DVIKARMAKA_OBJECT))
            assertFalse(dev.panini.ashtadhyayi.adhyaya1.pada4.AkathitamCaSutra.matches(context.copy(
                participant = uncertain, allParticipants = listOf(uncertain), candidates = setOf(Karaka.KARMAN))))
        }
    }

    private fun competing(cases: Set<Vibhakti>, objectSource: SemanticEvidenceSource,
        instrumentSource: SemanticEvidenceSource): KarakaResolution {
        val participant = ParticipantFacts("participant", AvyayaPada("participant", "participant"), cases,
            semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.INSTRUMENT),
            semanticEvidence = setOf(
                SemanticRelationEvidence(SemanticRelation.SECONDARY_OBJECT, objectSource, "Secondary-object reading."),
                SemanticRelationEvidence(SemanticRelation.INSTRUMENT, instrumentSource, "Instrument reading.")))
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दुह्"), participant,
            listOf(participant), Prayoga.KARTARI))
    }

    @Test fun `equal secondary object and instrument readings retain canonical proofs and case ambiguity`() {
        for (cases in listOf(setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA), setOf(Vibhakti.SASTHI))) {
            val result = competing(cases, SemanticEvidenceSource.CLAUSE_CONTEXT, SemanticEvidenceSource.CLAUSE_CONTEXT)
            assertNull(result.resolved)
            assertNull(result.resolvedVibhakti)
            assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.candidates)
            assertTrue((result.evidence + result.alternatives.flatMap { it.evidence }).map { it.sutra }
                .containsAll(listOf("1.4.51", "1.4.42")))
        }
        val instrumental = competing(setOf(Vibhakti.TRTIYA), SemanticEvidenceSource.CLAUSE_CONTEXT,
            SemanticEvidenceSource.CLAUSE_CONTEXT)
        assertEquals(Karaka.KARANA, instrumental.resolved)
        assertEquals(listOf("1.4.42", "2.3.18"), instrumental.evidence.map { it.sutra })
        assertTrue(instrumental.alternatives.any { it.karaka == Karaka.KARMAN && it.evidence.first().sutra == "1.4.51" })
    }

    @Test fun `stronger secondary object or instrument reading controls designation independently of ending`() {
        val objectReading = competing(setOf(Vibhakti.DVITIYA), SemanticEvidenceSource.CLAUSE_CONTEXT,
            SemanticEvidenceSource.LEXICAL_ENTRY)
        assertEquals(listOf("1.4.51", "2.3.2"), objectReading.evidence.map { it.sutra })
        val instrumentReading = competing(setOf(Vibhakti.TRTIYA), SemanticEvidenceSource.LEXICAL_ENTRY,
            SemanticEvidenceSource.CLAUSE_CONTEXT)
        assertEquals(listOf("1.4.42", "2.3.18"), instrumentReading.evidence.map { it.sutra })
    }

    @Test fun `established secondary object designation survives an incompatible ending`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) for (source in SemanticEvidenceSource.entries) {
            val p = ParticipantFacts("secondary", AvyayaPada("secondary", "secondary"), setOf(Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT),
                semanticEvidence = setOf(SemanticRelationEvidence(SemanticRelation.SECONDARY_OBJECT, source,
                    "Secondary object reading independent of the ending.")))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दुह्"), p, listOf(p), voice))
            val established = source in setOf(SemanticEvidenceSource.CLAUSE_CONTEXT,
                SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.DHATU_VALENCY)
            assertEquals(if (established) Karaka.KARMAN else null, result.resolved, "$voice $source")
            assertEquals(if (established) listOf("1.4.51") else emptyList(), result.evidence.map { it.sutra }, "$voice $source")
            assertNull(result.resolvedVibhakti, "$voice $source")
        }
    }

    @Test fun `secondary object rule uses nominal action identity and rejects derived stem compatibility forms`() {
        val participant = ParticipantFacts("secondary", AvyayaPada("secondary", "secondary"), setOf(Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT))
        val outer = TingantaPada("outer", emptyList(), DhatuPrakriti("पठ्", "पठ्"), Lakara.LAT,
            TingPratyaya("तिप्", "तिप्"))
        val nominal = KridantaPratipadika("nominal", emptyList(), DhatuPrakriti("दुह्", "दुह्"), "तुमुन्")
        val context = KarakaRuleContext(DhatuIdentity("पठ्"), participant, listOf(participant), Prayoga.KARTARI,
            candidates = setOf(Karaka.KARMAN), verbNode = outer, governingKrt = nominal)
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.AkathitamCaSutra
        assertTrue(rule.matches(context))
        assertFalse(rule.matches(context.copy(dhatu = DhatuIdentity("दुह्"),
            verbNode = outer.copy(dhatu = DhatuPrakriti("दुह्", "दुह्")),
            governingKrt = nominal.copy(dhatu = DhatuPrakriti("पठ्", "पठ्")))))
        for (stem in listOf("दोह्", "रोध्", "चे", "जे", "मोष्", "ने", "हार", "कर्ष", "वाह")) {
            assertFalse(rule.matches(context.copy(dhatu = DhatuIdentity(stem), verbNode = null, governingKrt = null)), stem)
        }
    }
}
