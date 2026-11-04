package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import kotlin.test.*

class NominalCaseFrameTest {
    private val comparison = setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.SESHA_RELATION)
    private val blessing = setOf(SemanticRelation.BENEDICTION_WELLBEING, SemanticRelation.SESHA_RELATION)
    private fun nominal(stem: String, sup: String) = SubantaPada("$stem + $sup", MulaPratipadika(stem, stem), SupPratyaya(sup, sup))
    private val verb = TingantaPada("अस् + लट् + तिप्", emptyList(), DhatuPrakriti("अस्", "अस्"),
        Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
    private fun analyzer(premises: Set<SemanticRelation>) = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
        listOf(PratipadikaEntry("देवदत्त", setOf(Linga.PUMS), semanticRelations = premises),
            PratipadikaEntry("नर", setOf(Linga.PUMS)), PratipadikaEntry("तुल्य", setOf(Linga.PUMS)),
            PratipadikaEntry("हित", setOf(Linga.NAPUMSAKA))),
        listOf(Dhatu(id = "as.case.test", krama = 1, upadesha = "अस्", sourceSurface = "अस्",
            artha = "भुवि", arthaHindi = "होना", arthaEnglish = "be", gana = DhatuGana.ADADI,
            pada = PadaType.PARASMAIPADA, karmatva = Karmatva.AKARMAKA)))))

    @Test fun `sentence discovers unique comparison governor across order and intervening words`() {
        val dependent = nominal("देवदत्त", "ङस्")
        val head = nominal("तुल्य", "सुँ")
        val subject = nominal("नर", "सुँ")
        val now = AvyayaPada("इदानीम्", "इदानीम्")
        for (words in listOf(listOf(dependent, subject, now, head, verb), listOf(head, verb, now, subject, dependent))) {
            val frame = analyzer(comparison).analyze(AkhyataVakya("comparison", words, verb))
            val links = requireNotNull(frame.nominalCaseLinks)
            assertEquals(1, links.established.size)
            val link = links.established.single()
            assertEquals(dependent, frame.nominalRelations!!.nominalPadas[link.dependent].pada)
            assertEquals(head, frame.nominalRelations!!.nominalPadas[link.governor].pada)
            val relation = frame.relations.single { it.participant.pada === dependent }
            assertEquals(FrameKarakaResolution.Resolved(Karaka.SAMBANDHA), relation.resolution)
            assertEquals(listOf("2.3.72"), relation.evidence.map { it.sutra })
            assertEquals(Vibhakti.SASTHI, relation.resolvedVibhakti)
            assertEquals(Vibhakti.TRTIYA, relation.alternatives.single().vibhakti)
            assertTrue(relation.evidence.single().semanticEvidence.any { it.source == SemanticEvidenceSource.GOVERNING_CONSTRUCTION })
        }
    }

    @Test fun `sentence discovers benediction link with established meaning and preserves alternative`() {
        val dependent = nominal("देवदत्त", "ङे")
        val head = nominal("हित", "सुँ")
        val frame = analyzer(blessing).analyze(AkhyataVakya("benediction", listOf(head, dependent, verb), verb))
        assertEquals("2.3.73", frame.nominalCaseLinks!!.established.single().sutra)
        val relation = frame.relations.single { it.participant.pada === dependent }
        assertEquals(listOf("2.3.73"), relation.evidence.map { it.sutra })
        assertEquals(Vibhakti.CHATURTHI, relation.resolvedVibhakti)
        assertEquals(Vibhakti.SASTHI, relation.alternatives.single().vibhakti)
    }

    @Test fun `identical competing governors remain separate occurrences without selecting nearest`() {
        val dependent = nominal("देवदत्त", "ङस्")
        val first = nominal("तुल्य", "सुँ")
        val second = first.copy()
        val frame = analyzer(comparison).analyze(AkhyataVakya("ambiguous", listOf(first, dependent, second, verb), verb))
        assertEquals(setOf(0, 2), frame.nominalCaseLinks!!.candidates.map { it.governor }.toSet())
        assertTrue(frame.nominalCaseLinks!!.established.isEmpty())
        assertTrue(frame.relations.single { it.participant.pada === dependent }.evidence.none { it.sutra == "2.3.72" })
    }

    @Test fun `unique word alone leaves missing premises explicit in finite and nominal clauses`() {
        val dependent = nominal("देवदत्त", "ङस्")
        val head = nominal("तुल्य", "सुँ")
        val local = analyzer(emptySet())
        val finite = local.analyze(AkhyataVakya("unknown reading", listOf(dependent, head, verb), verb))
        val nominal = local.analyze(NamaVakya("unknown reading", listOf(head, dependent)))
        for (frame in listOf(finite, nominal)) {
            assertEquals(comparison, frame.nominalCaseLinks!!.candidates.single().missingSemanticPremises)
            assertTrue(frame.nominalCaseLinks!!.established.isEmpty())
        }
        assertTrue(finite.relations.single { it.participant.pada === dependent }.evidence.none { it.sutra == "2.3.72" })
    }
}
