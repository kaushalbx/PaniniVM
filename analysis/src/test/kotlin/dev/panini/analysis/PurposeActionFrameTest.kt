package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.*
import kotlin.test.*

class PurposeActionFrameTest {
    private fun root(text: String, karmatva: Karmatva = Karmatva.SAKARMAKA) = Dhatu("purpose.$text", 1,
        text, text, "test", "test", "test", if (text == "आस्") DhatuGana.ADADI else DhatuGana.BHVADI,
        PadaType.PARASMAIPADA, karmatva = karmatva)
    private val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
        listOf("एध", "अन्न", "राम").map { PratipadikaEntry(it, setOf(Linga.PUMS)) },
        listOf(root("नी"), root("हृ"), root("आस्", Karmatva.AKARMAKA)))))
    private fun noun(stem: String, sup: String) = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(sup, sup))
    private fun binding(written: Boolean): PurposeActionGovernor = if (!written)
        PurposeActionGovernor.Implied("gather", DhatuPrakriti("हृ", "हृ"), listOf("आङ्")) else
        PurposeActionGovernor.Written("gather", AvyayaPada("आहर्तुम्", "आहर्तुम्",
            AvyayaKridantaDerivation(listOf("आङ्"), DhatuPrakriti("हृ", "हृ"), "तुमुँन्")))
    private fun frame(governor: PurposeActionGovernor, sup: String, passive: Boolean,
        objectMeaning: Boolean = true): KriyaFrame {
        val verb = TingantaPada("finite action", emptyList(), DhatuPrakriti("नी", "नी"), Lakara.LAT,
            TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
            vikarana = if (passive) Vikarana.YAK else null)
        val padas = listOf(noun("एध", sup), noun("अन्न", if (passive) "सुँ" else "अम्"),
            noun("राम", if (passive) "टा" else "सुँ")) +
            listOfNotNull((governor as? PurposeActionGovernor.Written)?.pada) + verb
        val facts = mutableMapOf(1 to setOf(SemanticRelation.DESIRED_OBJECT),
            2 to setOf(SemanticRelation.INDEPENDENT_AGENT))
        if (objectMeaning) facts[0] = setOf(SemanticRelation.DESIRED_OBJECT)
        return analyzer.analyze(AkhyataVakya("separate purpose action", padas, verb),
            semanticContext = VakyaSemanticContext(facts, purposeGovernors = mapOf(0 to governor)))
    }

    @Test fun `omitted and written purpose objects have distinct canonical case chains in both outer voices`() {
        for (passive in listOf(false, true)) for (written in listOf(false, true)) {
            val result = frame(binding(written), if (written) "शस्" else "भ्यस्", passive)
            val purpose = result.purposeKarakas.single()
            assertEquals(0, purpose.dependent)
            assertEquals(listOf("1.4.49", if (written) "2.3.2" else "2.3.14"), purpose.relation.evidence.map { it.sutra })
            assertEquals(if (written) Vibhakti.DVITIYA else Vibhakti.CHATURTHI, purpose.relation.resolvedVibhakti)
            assertNotEquals(result.id, purpose.relation.kriyaId)
            assertEquals(2, result.relations.size)
            assertTrue(result.relations.none { it.participant === purpose.relation.participant })
            assertTrue(result.diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        }
    }

    @Test fun `wrong cases do not borrow finite voice or ordinary accusative fallback`() {
        for (passive in listOf(false, true)) for ((written, sup) in listOf(false to "अम्", true to "ङे", false to "जस्")) {
            val result = frame(binding(written), sup, passive)
            val relation = result.purposeKarakas.single().relation
            assertEquals(listOf("1.4.49"), relation.evidence.map { it.sutra })
            assertNull(relation.resolvedVibhakti)
            assertTrue(result.diagnostics.any { it.nominalIndex == 0 && it.code == FrameDiagnosticCode.UNVALIDATED_VIBHAKTI })
            assertTrue(result.diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        }
    }

    @Test fun `case alone and an intransitive selected root do not establish purpose karman`() {
        assertTrue(frame(binding(false), "ङे", false, objectMeaning = false).purposeKarakas.single().relation.evidence.isEmpty())
        val intransitive = PurposeActionGovernor.Implied("sit", DhatuPrakriti("आस्", "आस्"))
        assertTrue(frame(intransitive, "ङे", false).purposeKarakas.single().relation.evidence.isEmpty())
    }

    @Test fun `invalid purpose identities occurrences roots and morphology are rejected`() {
        val written = binding(true) as PurposeActionGovernor.Written
        VakyaSemanticContext(purposeGovernors = mapOf(0 to written)).validate(1, listOf(written.pada))
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(purposeGovernors = mapOf(0 to written.copy(pada = written.pada.copy())))
                .validate(1, listOf(written.pada))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(purposeGovernors = mapOf(0 to written, 1 to written.copy(id = "another")))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(purposeGovernors = mapOf(0 to written, 1 to binding(false)))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(purposeGovernors = mapOf(1 to written)).validate(1, listOf(written.pada))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(purposeGovernors = mapOf(0 to written), avyayaGovernors = mapOf(0 to written.pada))
        }
        assertFailsWith<IllegalArgumentException> {
            PurposeActionGovernor.Written("bad", written.pada.copy(derivation = null))
        }
        assertFailsWith<IllegalArgumentException> {
            frame(PurposeActionGovernor.Implied("unknown", DhatuPrakriti("अज्ञात", "अज्ञात")), "ङे", false)
        }
        assertFailsWith<IllegalArgumentException> {
            frame(PurposeActionGovernor.Implied("bad-prefix", DhatuPrakriti("हृ", "हृ"), listOf("अज्ञात")), "ङे", false)
        }
    }
}
