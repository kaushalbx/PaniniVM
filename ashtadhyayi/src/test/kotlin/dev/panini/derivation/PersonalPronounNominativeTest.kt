package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.NgePrathamayorAmSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.JasahShiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.*
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class PersonalPronounNominativeTest {
    private fun state(stem: String = "युष्मद्", sup: SupAffix = SupAffix.SU) = DerivationState(listOf(
        DerivationTerm("stem", stem, TermKind.PRATIPADIKA), sup.term("suffix")),
        activeAdhikaras = setOf("6.4.1", "7.2.91"), stage = DerivationStage.IT_PROCESSED)

    @Test fun `maparyanta substitution retains ad until independent ti lopa`() {
        val before = state()
        val substituted = TvahauSauSutra.apply(before).state
        assertEquals(listOf(Vyanjana.TA, Vyanjana.VA, Svara.A, Svara.A, Vyanjana.DA), substituted.terms.first().varnas)
        assertEquals(before.terms.last(), substituted.terms.last())
        val lopa = SheseLopahSutra.apply(substituted).state
        assertEquals(listOf(Vyanjana.TA, Vyanjana.VA, Svara.A), lopa.terms.first().varnas)
        assertFalse(SheseLopahSutra.matches(lopa))
        assertFalse(TvahauSauSutra.matches(substituted))
    }

    @Test fun `complete am preserves original jas and cannot be reselected as jas`() {
        val before = state(sup = SupAffix.JAS).withSamjnas(setOf(SamjnaAssignment("stem", Samjna.SARVANAMA)))
        val prefix = YuyavayauJasiSutra.apply(before).state
        val lopa = SheseLopahSutra.apply(prefix).state
        val after = NgePrathamayorAmSutra.apply(lopa).state
        assertEquals(listOf(Svara.A, Vyanjana.MA), after.terms.last().varnas)
        assertEquals(SupAffix.JAS, after.terms.last().sourceSupAffix)
        assertFalse(JasahShiSutra.matches(after))
        assertFalse(NgePrathamayorAmSutra.matches(after))
    }

    @Test fun `nominatives are independent of requested gender and record actual rules`() {
        val engine = SubantaEngine()
        for (linga in Linga.entries) {
            for ((stem, vacana, expected, prefixRule) in listOf(
                Case("युष्मद्", Vacana.EKAVACANA, "त्वम्", "7.2.94"),
                Case("अस्मद्", Vacana.EKAVACANA, "अहम्", "7.2.94"),
                Case("युष्मद्", Vacana.BAHUVACANA, "यूयम्", "7.2.93"),
                Case("अस्मद्", Vacana.BAHUVACANA, "वयम्", "7.2.93"),
            )) {
                val result = engine.derive(SubantaDerivationRequest(stem, Vibhakti.PRATHAMA, vacana, linga))
                assertEquals(expected, result.final.surface, "$stem $vacana $linga: ${result.applications.map { it.sutra }}")
                val trace = result.applications.map { it.sutra }
                assertTrue(trace.containsAll(listOf("7.2.91", prefixRule, "7.2.90", "7.1.28", "6.1.107")))
                assertFalse("7.2.86" in trace)
                assertFalse("4.1.4" in trace)
                assertEquals(if (vacana == Vacana.EKAVACANA) SupAffix.SU else SupAffix.JAS,
                    result.final.droppedTerms.single { it.sourceSupAffix != null }.sourceSupAffix)
            }
        }
    }

    @Test fun `scope and residual case exclusions are grammatical not spelling shortcuts`() {
        assertFalse(TvahauSauSutra.matches(state().copy(activeAdhikaras = setOf("6.4.1"))))
        assertFalse(TvahauSauSutra.matches(state("अन्य")))
        assertFalse(YuyavayauJasiSutra.matches(state(sup = SupAffix.SAS)))
        for (sup in listOf(SupAffix.AU, SupAffix.AM, SupAffix.TA, SupAffix.NGI)) {
            assertFalse(SheseLopahSutra.matches(state(sup = sup)))
        }
        val initial = state("अतियुष्मद्").replaceTerm("stem", DerivationTerm("stem", "अतियुष्मद्", TermKind.PRATIPADIKA,
            compoundHeadUpadesha = "युष्मद्"))
        val after = TvahauSauSutra.apply(initial).state
        val prefixSize = requireNotNull(initial.terms.first().personalPronounMaparyantaRange()).first
        assertEquals(initial.terms.first().varnas.take(prefixSize), after.terms.first().varnas.take(prefixSize))
        assertEquals("अतित्वअद्", after.terms.first().surface)
    }

    private data class Case(val stem: String, val vacana: Vacana, val expected: String, val prefixRule: String)
}
