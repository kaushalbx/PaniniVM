package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya5.pada3.PancamyasTasilSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.KimSarvanamaBahubhyoAdvyadibhyahSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.IdamaIshSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.KuTihohSutra
import dev.panini.core.TaddhitaAffix
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Sources: Kāśikā on 5.3.1, 5.3.2, 5.3.7, 5.3.10, 7.2.102 and 7.2.104.
 * https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/5/5.3.7.htm
 */
class PragdisiyaTaddhitaDerivationTest {
    private val engine = DerivationEngine(Ashtadhyayi.executableSutras)

    @Test
    fun `tasil derivations retain vibhakti identity and canonical traces`() {
        for ((base, expected, baseRule) in listOf(
            Triple("तद्", "ततः", "7.2.102"),
            Triple("यद्", "यतः", "7.2.102"),
            Triple("किम्", "कुतः", "7.2.104"),
            Triple("इदम्", "इतः", "5.3.3"),
        )) {
            val outcomes = engine.deriveAll(TaddhitaDerivationRequest(base, DerivationalMeaning.APADANA).initialState())
            val evidence = outcomes.joinToString(" | ") {
                "${it.final.surface}:${it.applications.joinToString(" ") { app -> app.sutra }}"
            }
            val derived = outcomes.singleOrNull { it.final.surface == expected }
            assertTrue(derived != null, "$base expected $expected; outcomes=$evidence")
            val trace = derived.applications.map { it.sutra }
            assertTrue("5.3.1" in trace)
            assertTrue("5.3.2" in trace)
            assertTrue("5.3.7" in trace)
            assertTrue("1.1.38" in trace)
            assertTrue(baseRule in trace)
            if (base == "इदम्") assertTrue("1.1.55" in trace)
            assertTrue("1.3.3" in trace && "1.3.9" in trace)
            assertTrue("8.2.66" in trace && "8.3.15" in trace)
            val carriers = derived.final.terms + derived.final.droppedTerms
            assertTrue(carriers.any { it.sourceTaddhitaAffix == TaddhitaAffix.TASIL },
                carriers.joinToString { "${it.id}:${it.upadesha}:${it.sourceTaddhitaAffix}" })
        }
    }

    @Test
    fun `inherited va preserves the undeclined optional branch`() {
        val outcomes = engine.deriveAll(TaddhitaDerivationRequest("तद्", DerivationalMeaning.APADANA).initialState())
        val evidence = outcomes.joinToString(" | ") {
            "${it.final.surface}:${it.applications.joinToString(" ") { app -> app.sutra }}"
        }
        assertTrue(outcomes.any { it.applications.any { app -> app.sutra == "5.3.7" } }, evidence)
        assertTrue(outcomes.any { it.applications.none { app -> app.sutra == "5.3.7" } })
    }

    @Test
    fun `tasil rejects an ineligible ordinary noun and wrong semantic request`() {
        val noun = TaddhitaDerivationRequest("वृक्ष", DerivationalMeaning.APADANA).initialState()
            .copy(activeAdhikaras = setOf("4.1.76", "5.3.1"))
        assertFalse(PancamyasTasilSutra.matches(noun))
        val wrongMeaning = TaddhitaDerivationRequest("तद्", DerivationalMeaning.BHAVA).initialState()
            .copy(activeAdhikaras = setOf("4.1.76", "5.3.1", "5.3.2"))
        assertFalse(PancamyasTasilSutra.matches(wrongMeaning))
    }

    @Test
    fun `five three two uses conditioned Sarvadi membership and the dvyadi exclusion`() {
        fun state(base: String, uses: Set<LexicalUse> = emptySet()): DerivationState {
            val initial = TaddhitaDerivationRequest(base, DerivationalMeaning.APADANA).initialState()
            return initial.replaceTerm("pratipadika", initial.terms.single().copy(lexicalUses = uses))
                .copy(activeAdhikaras = setOf("5.3.1"))
        }

        assertTrue(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("तद्")))
        assertTrue(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("पूर्व", setOf(LexicalUse.RELATIVE_POSITION))))
        assertFalse(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("पूर्व")))
        assertFalse(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("द्वि")))
        assertFalse(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("युष्मद्")))
        assertTrue(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("किम्")))
        assertTrue(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("बहु")))
        assertFalse(KimSarvanamaBahubhyoAdvyadibhyahSutra.matches(state("वृक्ष")))
    }

    @Test
    fun `idam ish retains raw shit provenance and rejects an ineligible base`() {
        val outcomes = engine.deriveAll(TaddhitaDerivationRequest("इदम्", DerivationalMeaning.APADANA).initialState())
        val derived = assertNotNull(outcomes.singleOrNull { it.final.surface == "इतः" })
        val carrier = assertNotNull(derived.final.droppedTerms.singleOrNull { it.upadesha == "इश्" })
        assertEquals("pratipadika", carrier.mergedIntoTermId)
        assertTrue(carrier.itMarkerProvenance.any {
            it.marker == dev.panini.core.ItMarker.SH && it.designationSutra == "1.3.3"
        })
        val origin = derived.final.terms.single { it.id == "pratipadika" }.adeshaOrigins.single {
            it.prescribingSutra == "5.3.3"
        }
        assertEquals("1.1.55", origin.targetingSutra)
        assertEquals(listOf(Svara.I), derived.final.terms.first().varnas)

        val tad = TaddhitaDerivationRequest("तद्", DerivationalMeaning.APADANA).initialState()
        assertFalse(IdamaIshSutra.matches(tad.copy(activeAdhikaras = setOf("5.3.1", "5.3.2"))))
    }

    @Test
    fun `ku substitution changes exact canonical varnas and preserves identities`() {
        val initial = TaddhitaDerivationRequest("किम्", DerivationalMeaning.APADANA).initialState()
        val introduced = PancamyasTasilSutra.apply(initial.copy(
            activeAdhikaras = setOf("4.1.76", "5.3.1", "5.3.2"),
        )).state
        assertTrue(KuTihohSutra.matches(introduced))
        val changed = KuTihohSutra.apply(introduced).state
        assertEquals(listOf(Vyanjana.KA, Svara.U), changed.terms.first().varnas)
        assertEquals("किम्", changed.terms.first().upadesha)
        assertEquals(TaddhitaAffix.TASIL, changed.terms.last().sourceTaddhitaAffix)
    }
}
