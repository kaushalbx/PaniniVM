package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya7.pada3.VibhasaDvitiyatritiyabhyamSutra
import dev.panini.core.*
import kotlin.test.*

class OrdinalApSyatAlternativesTest {
    @Test fun `ordinal identity does not replace ap or ngit evidence`() {
        val stem = DerivationTerm("stem", "द्वितीया", TermKind.PRATIPADIKA, upadesha = "द्वितीय",
            establishedBySutras = setOf("4.1.4"))
        val suffix = SupAffix.NGE.term("suffix").copy(surface = "ए", itMarkers = setOf(ItMarker.NGIT),
            itProcessingPhase = ItProcessingPhase.PROCESSED)
        val state = DerivationState(listOf(stem, suffix), activeAdhikaras = setOf("6.4.1"))
        assertTrue(VibhasaDvitiyatritiyabhyamSutra.matches(state))
        assertFalse(VibhasaDvitiyatritiyabhyamSutra.matches(state.replaceTerm(stem.id, stem.copy(establishedBySutras = emptySet()))))
        assertFalse(VibhasaDvitiyatritiyabhyamSutra.matches(state.replaceTerm(stem.id, stem.copy(upadesha = "प्रथम"))))
        assertFalse(VibhasaDvitiyatritiyabhyamSutra.matches(state.replaceTerm(suffix.id, SupAffix.TA.term("suffix"))))
        assertFalse(VibhasaDvitiyatritiyabhyamSutra.matches(state.replaceTerm(suffix.id, suffix.copy(itMarkers = emptySet()))))
    }

    @Test fun `ordinal ngit derives both authentic branches independently`() {
        val engine = DerivationEngine(Ashtadhyayi.executableSutras)
        for ((stem, base) in listOf("द्वितीया" to "द्वितीय", "तृतीया" to "तृतीय")) {
            for ((vibhakti, endings) in listOf(
                Vibhakti.CHATURTHI to ("स्यै" to "ायै"),
                Vibhakti.PANCHAMI to ("स्याः" to "ायाः"),
                Vibhakti.SASTHI to ("स्याः" to "ायाः"),
                Vibhakti.SAPTAMI to ("स्याम्" to "ायाम्"),
            )) {
                val initial = SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA, Linga.STRI,
                    stemFormation = NominalStemFormation.AP, pratipadikaUpadesha = base).initialState()
                val results = engine.deriveAll(initial)
                assertEquals(setOf(base + endings.first, base + endings.second), results.map { it.final.surface }.toSet())
                for (result in results) {
                    assertEquals(DerivationStage.FINAL, result.final.stage)
                    val trace = result.applications.map { it.sutra }
                    assertEquals(1, trace.count { it in setOf("7.3.113", "7.3.115") })
                    assertFalse("7.3.114" in trace)
                    val producer = trace.first { it in setOf("7.3.113", "7.3.115") }
                    assertTrue(trace.drop(trace.indexOf(producer) + 1).containsAll(listOf("1.3.3", "1.1.46", "1.3.9")))
                    result.final.requireCompleteItProcessing()
                }
                val skipped = engine.derive(initial, DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
                assertEquals(base + endings.second, skipped.final.surface)
                assertFalse(skipped.applications.any { it.sutra == "7.3.115" })
            }
        }
    }
}
