package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Source: https://sanskritdocuments.org/doc_z_misc_major_works/lingAnushAsanam.html (145, 183). */
class LinganushasanaCommonGenderProvenanceTest {
    @Test fun `common gender stems retain linga rule and forward nominal traces`() {
        val index = DerivedVisargaLexicon.build(PaninianPratipadikaLexicon.declaredPratipadikas())
        assertTrue(index.failures.isEmpty(), index.failures.joinToString())
        for ((stem, rule, masculine, neuter) in listOf(
            listOf("मूल", "145", "मूलः", "मूलम्"),
            listOf("बाल", "145", "बालः", "बालम्"),
            listOf("शूल", "145", "शूलः", "शूलम्"),
            listOf("दण्ड", "183", "दण्डः", "दण्डम्"),
            listOf("शव", "183", "शवः", "शवम्"),
        )) {
            val entry = assertNotNull(PaninianPratipadikaLexicon.findPratipadika(stem))
            assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), entry.linga, stem)
            assertTrue(rule in entry.lingaRuleIds, stem)
            val candidate = assertNotNull(index.candidates(masculine).singleOrNull(), masculine)
            assertEquals(rule, candidate.entry.lingaRuleIds.single())
            assertTrue(candidate.derivation.applications.map { it.sutra }
                .containsAll(listOf("8.2.66", "8.3.15")))
            assertTrue(index.nominalCandidates(neuter).any { it.request.linga == Linga.NAPUMSAKA }, neuter)
        }
        assertFalse(index.candidates("अबालः").isNotEmpty())
        assertFalse(index.candidates("अदण्डः").isNotEmpty())
    }
}
