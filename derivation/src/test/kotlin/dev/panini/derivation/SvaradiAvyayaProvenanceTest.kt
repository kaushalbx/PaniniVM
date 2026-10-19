package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.ganapatha.SvaradiGana
import dev.panini.ashtadhyayi.adhyaya6.pada4.SarvanamasthaneCasambuddhauSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Sources: https://cdn.jsdelivr.net/gh/ashtadhyayi-com/data@4a63049/ganapath/data.txt,
 * https://www.ashtadhyayi.com/sutraani/plsk102, and
 * https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.4.11.htm.
 */
class SvaradiAvyayaProvenanceTest {
    private val engine = SubantaEngine()

    @Test
    fun `s final svaradi members derive indeclinable visarga padas`() {
        for ((upadesha, surface) in listOf(
            "उच्चैस्" to "उच्चैः",
            "नीचैस्" to "नीचैः",
            "शनैस्" to "शनैः",
            "भूयस्" to "भूयः",
            "ह्यस्" to "ह्यः",
            "श्वस्" to "श्वः",
            "अधस्" to "अधः",
            "तिरस्" to "तिरः",
            "मिथस्" to "मिथः",
            "प्रायस्" to "प्रायः",
            "मुहुस्" to "मुहुः",
            "नमस्" to "नमः",
            "अम्भस्" to "अम्भः",
        )) {
            assertTrue(SvaradiGana.contains(upadesha))
            val result = engine.derive(SubantaDerivationRequest(
                upadesha,
                Vibhakti.PRATHAMA,
                Vacana.EKAVACANA,
                Linga.PUMS,
                pratipadikaUpadesha = upadesha,
            ))
            assertEquals(surface, result.final.surface)
            assertTrue(result.applications.map { it.sutra }.containsAll(listOf("1.1.37", "2.4.82", "8.2.66", "8.3.15")),
                result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
        }
    }

    @Test
    fun `s final svaradi provenance licenses both va shari branches`() {
        val index = DerivedVisargaLexicon.build(emptyList(),
            ganaAvyayaUpadeshas = DeclaredSvaradiAvyayas.upadeshas)
        val candidate = index.avyayaCandidates("नमः").single()
        val engine = SandhiEngine()
        val outcomes = engine.joinAll(candidate.pada,
            SandhiEngine.Pada(DerivationTerm("right", "शिवाय", TermKind.PRATIPADIKA)))
        assertEquals(setOf("नमःशिवाय", "नमश्शिवाय"), outcomes.map(engine::render).toSet())
        assertTrue(outcomes.any { result -> result.events.filterIsInstance<DerivationEvent.BranchCreated>()
            .any { it.sutra == "8.3.36" } })
        assertTrue(candidate.derivation.applications.map { it.sutra }
            .containsAll(listOf("1.1.37", "2.4.82", "8.2.66", "8.3.15")))
    }

    @Test
    fun `r final svaradi members retain pada identity through sup luk and take visarga at pause`() {
        for ((upadesha, surface) in listOf(
            "स्वर्" to "स्वः",
            "अन्तर्" to "अन्तः",
            "प्रातर्" to "प्रातः",
            "पुनर्" to "पुनः",
            "भूर्" to "भूः",
            "भुवर्" to "भुवः",
        )) {
            assertTrue(SvaradiGana.contains(upadesha))
            val result = engine.derive(SubantaDerivationRequest(
                upadesha,
                Vibhakti.PRATHAMA,
                Vacana.EKAVACANA,
                Linga.PUMS,
                pratipadikaUpadesha = upadesha,
            ))
            assertEquals(surface, result.final.surface, result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
            assertTrue(result.applications.map { it.sutra }.containsAll(listOf("1.1.37", "2.4.82", "8.3.15")),
                result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
            assertTrue(result.final.samjnas.any { it.targetId == "pratipadika" && it.samjna == dev.panini.shiksha.Samjna.PADA })
            assertFalse(result.applications.any { it.sutra == "6.4.11" })
        }
        assertFalse(SvaradiGana.contains("अपुनर्"))
    }

    @Test
    fun `declared svaradi candidates retain gana and derivation provenance through sandhi`() {
        val index = DerivedVisargaLexicon.build(emptyList(),
            ganaAvyayaUpadeshas = DeclaredSvaradiAvyayas.upadeshas)
        assertTrue(index.failures.isEmpty(), index.failures.joinToString())
        for ((surface, right, expected) in listOf(
            Triple("उच्चैः", "उदात्तः", "उच्चैरुदात्तः"),
            Triple("शनैः", "आर्या", "शनैरार्या"),
            Triple("भूयः", "भवति", "भूयोभवति"),
            Triple("पुनः", "भवति", "पुनर्भवति"),
            Triple("प्रातः", "एव", "प्रातरेव"),
        )) {
            val candidate = index.avyayaCandidates(surface).single()
            assertEquals(DeclaredSvaradiAvyayas.source, candidate.source)
            val trace = candidate.derivation.applications.map { it.sutra }
            assertTrue(trace.containsAll(listOf("1.1.37", "2.4.82", "8.3.15")))
            if (candidate.upadesha.endsWith("स्")) assertTrue("8.2.66" in trace) else assertFalse("8.2.66" in trace)
            val result = SandhiEngine().join(candidate.pada,
                SandhiEngine.Pada(DerivationTerm("right", right, TermKind.PRATIPADIKA)))
            assertEquals(expected, SandhiEngine().render(result))
        }
    }

    @Test
    fun `7 1 94 anang retains it provenance through the complete pitr derivation`() {
        val result = engine.derive(SubantaDerivationRequest("पितृ", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        val integrated = result.applications.single { it.sutra == "1.1.53" }.after
        assertTrue(SarvanamasthaneCasambuddhauSutra.matches(integrated), integrated.toString())
        assertEquals("पिता", result.final.surface)
        assertTrue(result.applications.map { it.sutra }.containsAll(listOf("7.1.94", "1.3.3", "1.3.9", "1.1.53", "6.4.8", "8.2.7")))
        val carrier = result.final.droppedTerms.single { it.upadesha == "अनङ्" }
        assertTrue(carrier.itMarkerProvenance.any { it.designationSutra == "1.3.3" })
        assertEquals("pratipadika", carrier.mergedIntoTermId)
    }

    @Test
    fun `6 4 11 is limited to its enumerated stems`() {
        val svasarau = engine.derive(SubantaDerivationRequest("स्वसृ", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.STRI))
        assertEquals("स्वसारौ", svasarau.final.surface)
        assertTrue(svasarau.applications.any { it.sutra == "6.4.11" })

        val pitarau = engine.derive(SubantaDerivationRequest("पितृ", Vibhakti.PRATHAMA, Vacana.DVIVACANA, Linga.PUMS))
        assertEquals("पितरौ", pitarau.final.surface)
        assertFalse(pitarau.applications.any { it.sutra == "6.4.11" })
    }
}
