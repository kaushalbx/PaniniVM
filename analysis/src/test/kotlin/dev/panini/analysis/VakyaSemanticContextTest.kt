package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import kotlin.test.*

class VakyaSemanticContextTest {
    @Test fun `unvalidated case diagnostic belongs to one occurrence rather than identical source text`() {
        val first = nominal("देवदत्त", "ङस्")
        val second = first.copy()
        val givingVerb = verb.copy(sourceText = "ददाति", dhatu = DhatuPrakriti("दा", "दा"))
        val clause = AkhyataVakya("case diagnostic", listOf(first, second, nominal("अन्न", "अम्"), givingVerb), givingVerb)
        val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(
            mapOf(1 to setOf(SemanticRelation.SESHA_RELATION)), intendedObjects = mapOf(0 to 2)))
        assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMPRADANA), frame.relations[0].resolution)
        assertNull(frame.relations[0].resolvedVibhakti)
        val diagnostic = frame.diagnostics.single { it.code == FrameDiagnosticCode.UNVALIDATED_VIBHAKTI }
        assertEquals(0, diagnostic.nominalIndex)
        assertEquals(FrameDiagnosticSeverity.WARNING, diagnostic.severity)
        assertFalse(FrameDiagnosticPolicy.blocksExecution(diagnostic))
        assertEquals(listOf("2.3.50"), frame.relations[1].evidence.map { it.sutra })
        assertTrue(frame.diagnostics.none { it.nominalIndex == 1 && it.code == FrameDiagnosticCode.UNVALIDATED_VIBHAKTI })
        assertTrue(frame.diagnostics.none { it.nominalIndex == 0 && it.code == FrameDiagnosticCode.UNASSIGNED_PARTICIPANT })
        val unknown = analyzer.analyze(AkhyataVakya("unknown role", listOf(first, verb), verb))
        assertTrue(unknown.diagnostics.any { it.nominalIndex == 0 && it.code == FrameDiagnosticCode.UNASSIGNED_PARTICIPANT })
        assertTrue(unknown.diagnostics.none { it.code == FrameDiagnosticCode.UNVALIDATED_VIBHAKTI })
    }

    @Test fun `object link supplies recipient intent while selected object must actually qualify`() {
        val recipient = nominal("देवदत्त", "ङे")
        val obj = nominal("अन्न", "अम्")
        val givingVerb = verb.copy(sourceText = "ददाति", dhatu = DhatuPrakriti("दा", "दा"))
        val clause = AkhyataVakya("mediation", listOf(recipient, obj, givingVerb), givingVerb)
        // The specified object must retain an object reading, rather than
        // being independently construed as a śeṣa relation.
        val valid = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(
            mapOf(1 to setOf(SemanticRelation.DESIRED_OBJECT)), intendedObjects = mapOf(0 to 1)))
        assertEquals(listOf("1.4.32", "2.3.13"), valid.relations[0].evidence.map { it.sutra })
        assertTrue(valid.relations[0].evidence.first().semanticEvidence.any { it.detail.contains("Written(participantId=p_1)") })
        val invalid = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(
            mapOf(1 to setOf(SemanticRelation.SESHA_RELATION)), intendedObjects = mapOf(0 to 1)))
        assertTrue(invalid.relations[0].evidence.none { it.sutra == "1.4.32" })
        val impliedClause = clause.copy(padas = listOf(recipient, nominal("देवदत्त", "सुँ"), givingVerb))
        val implied = analyzer.analyze(impliedClause, semanticContext = VakyaSemanticContext(intendedObjects = mapOf(0 to null)))
        assertEquals(listOf("1.4.32", "2.3.13"), implied.relations[0].evidence.map { it.sutra })
        assertFailsWith<IllegalArgumentException> { VakyaSemanticContext(intendedObjects = mapOf(0 to 0)) }
        assertFailsWith<IllegalArgumentException> {
            analyzer.analyze(clause, semanticContext = VakyaSemanticContext(intendedObjects = mapOf(0 to 2)))
        }
    }

    private val premises = setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.SESHA_RELATION)
    private fun nominal(stem: String, sup: String) = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(sup, sup))
    private val verb = TingantaPada("अस्ति", emptyList(), DhatuPrakriti("अस्", "अस्"), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
    private val lexical = InMemoryVyakaranamLexicon(listOf(
        PratipadikaEntry("तुल्य", setOf(Linga.PUMS)), PratipadikaEntry("देवदत्त", setOf(Linga.PUMS)),
        PratipadikaEntry("अन्न", setOf(Linga.NAPUMSAKA))),
        listOf(Dhatu(id = "as.context.test", krama = 1, upadesha = "अस्", sourceSurface = "अस्",
            artha = "भुवि", arthaHindi = "होना", arthaEnglish = "be", gana = DhatuGana.ADADI,
            pada = PadaType.PARASMAIPADA, karmatva = Karmatva.AKARMAKA),
            Dhatu(id = "da.context.test", krama = 2, upadesha = "दा", sourceSurface = "दा",
                artha = "दाने", arthaHindi = "देना", arthaEnglish = "give", gana = DhatuGana.JUHOTYADI,
                pada = PadaType.PARASMAIPADA, karmatva = Karmatva.SAKARMAKA)))
    private val analyzer = VakyaAnalyzer(PadaAnalyzer(lexical))

    @Test fun `identical words retain occurrence local facts without lexical or clause leakage`() {
        val first = nominal("देवदत्त", "ङस्")
        val second = first.copy()
        val clause = AkhyataVakya("repeated", listOf(nominal("तुल्य", "सुँ"), first, second, verb), verb)
        val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(mapOf(1 to premises)))
        assertEquals(listOf("2.3.72"), frame.relations[1].evidence.map { it.sutra })
        assertTrue(frame.relations[2].evidence.isEmpty())
        assertEquals(listOf(1), frame.nominalCaseLinks!!.established.map { it.dependent })
        assertTrue(frame.relations[1].evidence.single().semanticEvidence.any { it.source == SemanticEvidenceSource.CLAUSE_CONTEXT })
        assertTrue(frame.relations[2].evidence.none { proof -> proof.semanticEvidence.any { it.source == SemanticEvidenceSource.CLAUSE_CONTEXT } })
        assertTrue(lexical.findPratipadika("देवदत्त")!!.semanticRelations.isEmpty())
        val next = analyzer.analyze(clause)
        assertTrue(next.nominalCaseLinks!!.established.isEmpty())
        assertTrue(next.relations[1].evidence.isEmpty())
    }

    @Test fun `nominal clauses also preserve occurrence local premises and reject invalid indices`() {
        val clause = NamaVakya("nominal", listOf(nominal("तुल्य", "सुँ"), nominal("देवदत्त", "ङस्")))
        val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(mapOf(1 to premises)))
        assertEquals(listOf(1), frame.nominalCaseLinks!!.established.map { it.dependent })
        assertFailsWith<IllegalArgumentException> { VakyaSemanticContext(mapOf(-1 to premises)) }
        assertFailsWith<IllegalArgumentException> {
            analyzer.analyze(clause, semanticContext = VakyaSemanticContext(mapOf(2 to premises)))
        }
        val finite = AkhyataVakya("finite", clause.padas + verb, verb)
        assertFailsWith<IllegalArgumentException> {
            analyzer.analyze(finite, semanticContext = VakyaSemanticContext(mapOf(2 to premises)))
        }
    }
}
