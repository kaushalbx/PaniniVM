package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada4.NgitiHrasvashCaSutra
import dev.panini.core.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class ShortVowelNadiAlternativesTest {
    @Test fun `short feminine vowels retain both nadi and ghi outcomes`() {
        val engine = DerivationEngine(Ashtadhyayi.executableSutras)
        for ((stem, endings) in listOf(
            "मति" to listOf("मत्यै" to "मतये", "मत्याः" to "मतेः", "मत्याः" to "मतेः", "मत्याम्" to "मतौ"),
            "धेनु" to listOf("धेन्वै" to "धेनवे", "धेन्वाः" to "धेनोः", "धेन्वाः" to "धेनोः", "धेन्वाम्" to "धेनौ"),
        )) {
            for ((index, vibhakti) in listOf(Vibhakti.CHATURTHI, Vibhakti.PANCHAMI, Vibhakti.SASTHI, Vibhakti.SAPTAMI).withIndex()) {
                val initial = SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA, Linga.STRI).initialState()
                val results = engine.deriveAll(initial)
                assertEquals(setOf(endings[index].first, endings[index].second), results.map { it.final.surface }.toSet(), "$stem $vibhakti")
                for (result in results) {
                    val trace = result.applications.map { it.sutra }
                    val nadi = "1.4.6" in trace
                    assertEquals(nadi, "7.3.112" in trace)
                    assertFalse(result.final.samjnas.groupBy { it.targetId }.values.any { assignments ->
                        assignments.any { it.samjna == Samjna.NADI } && assignments.any { it.samjna == Samjna.GHI }
                    })
                    if (nadi && vibhakti == Vibhakti.SAPTAMI) {
                        val substitution = trace.indexOf("7.3.117")
                        val augment = trace.indexOf("7.3.112")
                        val vrddhi = trace.indexOf("6.1.90")
                        assertTrue(substitution >= 0 && augment > substitution && vrddhi > augment)
                        assertTrue(trace.indexOf("6.1.77") > vrddhi)
                        assertFalse("7.3.116" in trace)
                    }
                }
            }
        }
    }

    @Test fun `short vowel designation requires feminine and processed ngit evidence`() {
        val stem = DerivationTerm("stem", "मति", TermKind.PRATIPADIKA)
        val suffix = SupAffix.NGE.term("suffix").copy(surface = "ए", itMarkers = setOf(ItMarker.NGIT),
            itProcessingPhase = ItProcessingPhase.PROCESSED)
        val initial = DerivationState(listOf(stem, suffix),
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI)))
        assertTrue(NgitiHrasvashCaSutra.matches(initial))
        assertFalse(NgitiHrasvashCaSutra.matches(initial.copy(context = DerivationalContext(rupa = Rupa(linga = Linga.PUMS)))))
        assertFalse(NgitiHrasvashCaSutra.matches(initial.replaceTerm(suffix.id, suffix.copy(itMarkers = emptySet()))))
        assertFalse(NgitiHrasvashCaSutra.matches(initial.replaceTerm(suffix.id, suffix.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA))))
        assertFalse(NgitiHrasvashCaSutra.matches(initial.replaceTerm(stem.id, stem.copy(surface = "नदी"))))
    }
}
