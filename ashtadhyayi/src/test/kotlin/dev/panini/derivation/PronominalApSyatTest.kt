package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.SarvanamnahSyadHrasvasCaSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class PronominalApSyatTest {
    @Test fun `pronominal ap ngit forms use syat not yat`() {
        for ((stem, lexical) in listOf("सर्वा" to "सर्व", "विश्वा" to "विश्व", "अन्या" to "अन्य")) {
            for ((vibhakti, ending) in listOf(Vibhakti.CHATURTHI to "स्यै", Vibhakti.PANCHAMI to "स्याः",
                Vibhakti.SASTHI to "स्याः", Vibhakti.SAPTAMI to "स्याम्")) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA,
                    Linga.STRI, stemFormation = NominalStemFormation.AP, pratipadikaUpadesha = lexical))
                assertEquals(lexical + ending, result.final.surface, "$stem $vibhakti")
                val trace = result.applications.map { it.sutra }
                assertEquals(1, trace.count { it == "7.3.114" })
                assertFalse("7.3.113" in trace)
                val introduced = trace.indexOf("7.3.114")
                assertTrue(trace.drop(introduced + 1).containsAll(listOf("1.3.3", "1.1.46", "1.3.9")))
                assertTrue(trace.drop(introduced + 1).contains(if (vibhakti == Vibhakti.CHATURTHI) "6.1.88" else "6.1.101"))
                if (vibhakti == Vibhakti.SAPTAMI) assertTrue(trace.indexOf("7.3.116") < introduced)
                if (vibhakti in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI))
                    assertTrue(trace.drop(introduced + 1).containsAll(listOf("8.2.66", "8.3.15")))
                assertEquals(lexical, result.initial.terms.first().upadesha)
                val augment = result.final.droppedTerms.single { it.createdBySutra == "7.3.114" }
                assertEquals("स्याट्", augment.upadesha)
                assertEquals("1.1.46", augment.droppedBySutra)
                result.final.requireCompleteItProcessing()
            }
        }
    }

    @Test fun `producer requires both ap and sarvanama evidence and introduces only raw syat`() {
        val stem = DerivationTerm("stem", "सर्वाँऽ", TermKind.PRATIPADIKA, upadesha = "सर्व",
            establishedBySutras = setOf("4.1.4"))
        val suffix = SupAffix.NGE.term("suffix").copy(surface = "ए", itMarkers = setOf(ItMarker.NGIT),
            itProcessingPhase = ItProcessingPhase.PROCESSED)
        val original = DerivationState(listOf(stem, suffix), activeAdhikaras = setOf("6.4.1"),
            samjnas = setOf(SamjnaAssignment(stem.id, Samjna.SARVANAMA)), stage = DerivationStage.IT_PROCESSED)
        assertTrue(SarvanamnahSyadHrasvasCaSutra.matches(original))
        val after = SarvanamnahSyadHrasvasCaSutra.apply(original).state
        assertEquals("सर्वँऽ", after.terms.first().surface)
        assertEquals(stem.id, after.terms.first().id)
        assertEquals(stem.upadesha, after.terms.first().upadesha)
        assertEquals(suffix, after.terms.last())
        val augment = after.terms[1]
        assertEquals(listOf(Vyanjana.SA, Vyanjana.YA, Svara.AA, Vyanjana.TTA), augment.varnas)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
        assertEquals(suffix.id, augment.augmentTargetId)
        assertFalse(SarvanamnahSyadHrasvasCaSutra.matches(after))
        assertFalse(SarvanamnahSyadHrasvasCaSutra.matches(original.copy(samjnas = emptySet())))
        assertFalse(SarvanamnahSyadHrasvasCaSutra.matches(original.replaceTerm(stem.id, stem.copy(establishedBySutras = emptySet()))))
        assertFalse(SarvanamnahSyadHrasvasCaSutra.matches(original.replaceTerm(suffix.id, SupAffix.TA.term("suffix"))))
    }

    @Test fun `shortened ap does not reopen original a stem case replacements`() {
        val stem = DerivationTerm("stem", "सर्व", TermKind.PRATIPADIKA,
            establishedBySutras = setOf("4.1.4"))
        for ((affix, rule) in listOf(
            SupAffix.NGE to dev.panini.ashtadhyayi.adhyaya7.pada1.SarvanamnasSmaiSutra,
            SupAffix.NGASI to dev.panini.ashtadhyayi.adhyaya7.pada1.NasinyohSmatsminauSutra,
            SupAffix.NGI to dev.panini.ashtadhyayi.adhyaya7.pada1.NasinyohSmatsminauSutra,
        )) {
            val state = DerivationState(listOf(stem, affix.term()), activeAdhikaras = setOf("6.4.1"),
                samjnas = setOf(SamjnaAssignment(stem.id, Samjna.SARVANAMA)))
            assertFalse(rule.matches(state), affix.name)
            assertTrue(rule.matches(state.replaceTerm(stem.id, stem.copy(establishedBySutras = emptySet()))), affix.name)
        }
    }
}
