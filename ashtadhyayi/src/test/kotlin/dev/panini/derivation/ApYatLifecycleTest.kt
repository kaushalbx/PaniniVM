package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.YadapahSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class ApYatLifecycleTest {
    private fun state(): DerivationState {
        val stem = DerivationTerm("stem", "माला", TermKind.PRATIPADIKA,
            establishedBySutras = setOf("4.1.4"))
        val suffix = SupAffix.NGE.term("suffix").copy(surface = "ए",
            itMarkers = setOf(ItMarker.NGIT), itProcessingPhase = ItProcessingPhase.PROCESSED)
        return DerivationState(listOf(stem, suffix), activeAdhikaras = setOf("6.4.1"),
            stage = DerivationStage.IT_PROCESSED)
    }

    @Test fun `producer adds raw augment without changing stem or ending`() {
        val original = state()
        assertTrue(YadapahSutra.matches(original))
        val result = YadapahSutra.apply(original).state
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals(original.terms.last(), result.terms.last())
        val augment = result.terms[1]
        assertEquals(listOf(Vyanjana.YA, Svara.AA, Vyanjana.TTA), augment.varnas)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
        assertEquals("suffix", augment.augmentTargetId)
        assertTrue(augment.itDesignations.isEmpty())
        assertTrue(result.substitutions.isEmpty())
        assertFalse(YadapahSutra.matches(result))
        assertFalse(YadapahSutra.matches(original.replaceTerm("stem",
            original.terms.first().copy(establishedBySutras = emptySet()))))
        assertFalse(YadapahSutra.matches(original.replaceTerm("suffix", SupAffix.TA.term("suffix"))))
    }

    @Test fun `ngit forms retain separate placement it and coalescence traces`() {
        for ((vibhakti, expected) in listOf(Vibhakti.CHATURTHI to "रमायै",
            Vibhakti.PANCHAMI to "रमायाः", Vibhakti.SASTHI to "रमायाः", Vibhakti.SAPTAMI to "रमायाम्")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("रमा", vibhakti,
                Vacana.EKAVACANA, Linga.STRI, stemFormation = NominalStemFormation.AP))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            val introduced = trace.indexOf("7.3.113")
            assertEquals(1, trace.count { it == "7.3.113" })
            assertTrue(introduced >= 0)
            val subsequent = trace.drop(introduced + 1)
            assertTrue(subsequent.containsAll(listOf("1.3.3", "1.1.46", "1.3.9")))
            assertTrue(subsequent.contains(if (vibhakti == Vibhakti.CHATURTHI) "6.1.88" else "6.1.101"))
            if (vibhakti == Vibhakti.SAPTAMI) assertTrue(trace.indexOf("7.3.116") < introduced)
            if (vibhakti in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI))
                assertTrue(subsequent.containsAll(listOf("8.2.66", "8.3.15")))
            val augment = result.final.droppedTerms.single { it.createdBySutra == "7.3.113" }
            assertEquals("याट्", augment.upadesha)
            assertEquals("1.1.46", augment.droppedBySutra)
            assertTrue(result.final.allEffectiveTerms.any { term ->
                term.itMarkerProvenance.any { it.designatedText == "ट्" && it.designationSutra == "1.3.3" }
            })
            result.final.requireCompleteItProcessing()
        }
    }
}
