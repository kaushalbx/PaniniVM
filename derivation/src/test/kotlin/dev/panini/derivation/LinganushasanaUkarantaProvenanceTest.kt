package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Sources: Liṅgānuśāsana 51 and Amarakośa 3.1.23 (प्रभुः). */
class LinganushasanaUkarantaProvenanceTest {
    @Test fun `prabhu receives masculine gender only through rule 51 and derives exact visarga provenance`() {
        val entry = assertNotNull(PaninianPratipadikaLexicon.findPratipadika("प्रभु"))
        assertEquals(setOf(Linga.PUMS), entry.linga)
        assertEquals(setOf("51"), entry.lingaRuleIds)

        val index = DerivedVisargaLexicon.build(listOf(entry))
        assertTrue(index.failures.isEmpty(), index.failures.joinToString())
        val candidate = assertNotNull(index.candidates("प्रभुः").singleOrNull())
        assertEquals("प्रभु", candidate.request.pratipadika)
        assertEquals(Linga.PUMS, candidate.request.linga)
        assertTrue(candidate.derivation.applications.map { it.sutra }
            .containsAll(listOf("8.2.66", "8.3.15")))
        assertEquals(VisargaSource.ESTABLISHED_RU, candidate.source)

        val engine = SandhiEngine()
        val beforeV = engine.join(candidate.pada,
            SandhiEngine.Pada(DerivationTerm("right-v", "वदति", TermKind.PRATIPADIKA)))
        assertEquals("प्रभुर्वदति", engine.render(beforeV))

        val beforeKhar = engine.join(candidate.pada,
            SandhiEngine.Pada(DerivationTerm("right-k", "तदा", TermKind.PRATIPADIKA)))
        assertEquals("प्रभुस्तदा", engine.render(beforeKhar))
        assertTrue(beforeKhar.applications.any { it.sutra == "8.3.34" })
    }

    @Test fun `rule 51 exceptions and undeclared prefixed spelling are not admitted`() {
        for (stem in listOf("धेनु", "श्मश्रु", "वस्तु")) {
            assertFalse(dev.panini.linganushasanam.adhyaya2.UkarantahSutra.matches(
                dev.panini.linganushasanam.LingaRuleContext(stem)), stem)
        }
        assertFalse(PaninianPratipadikaLexicon.declaredPratipadikas().any { it.text == "अप्रभु" })
    }
}
