package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada4.ErAnekacoAsamyogapurvasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.SantamahatahSamyogasyaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class AngaRangeAnnotationTest {
    @Test fun `mahat and vidvas transformations retain annotations of unchanged and lengthened vowels`() {
        for ((source, singular, dual) in listOf(
            Triple("मँहँत्ऽ", "मँहाँन्ऽ", "मँहाँन्त्ऽ"),
            Triple("विँद्वँस्ऽ", "विँद्वाँन्ऽ", "विँद्वाँन्स्ऽ"),
        )) {
            for ((upadesha, expected) in listOf("सुँ" to singular, "औ" to dual)) {
                val stem = DerivationTerm("stem", source, TermKind.PRATIPADIKA, createdBySutra = "fixture")
                val affix = DerivationTerm("suffix", if (upadesha == "सुँ") "स्" else "औ", TermKind.PRATYAYA, upadesha = upadesha)
                val before = DerivationState(listOf(stem, affix), activeAdhikaras = setOf("6.4.1"))
                assertTrue(SantamahatahSamyogasyaSutra.matches(before))
                val after = SantamahatahSamyogasyaSutra.apply(before).state
                assertEquals(expected, after.terms.first().surface)
                assertEquals(stem.id, after.terms.first().id)
                assertEquals(stem.upadesha, after.terms.first().upadesha)
                assertEquals(stem.createdBySutra, after.terms.first().createdBySutra)
                assertEquals(stem.varnas.size - 2, after.substitutions.single().sourceVarnaIndex)
                if (upadesha == "सुँ") assertEquals(affix.id, after.droppedTerms.single().id)
                else assertEquals(affix, after.terms.last())
            }
        }
    }

    @Test fun `i yan substitution and affix composition preserve other vowels and signs`() {
        val abhyasa = DerivationTerm("abhyasa", "दि", TermKind.DHATU)
        val root = DerivationTerm("root", "दिँदिऽ", TermKind.DHATU, upadesha = "दि")
        val affix = DerivationTerm("suffix", "अँ", TermKind.PRATYAYA)
        val before = DerivationState(listOf(abhyasa, root, affix))
        assertTrue(ErAnekacoAsamyogapurvasyaSutra.matches(before))
        val after = ErAnekacoAsamyogapurvasyaSutra.apply(before).state
        val changed = after.terms.last()
        assertEquals(listOf(Vyanjana.DA, Svara.I, Vyanjana.DA, Vyanjana.YA, Svara.A), changed.varnas)
        assertEquals(listOf(false, true, false, false, true), changed.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertTrue(changed.orthographicSigns.contains(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 4)))
        assertEquals(root.id, changed.id)
        assertEquals(root.upadesha, changed.upadesha)
        assertEquals(abhyasa, after.terms.first())
        assertEquals(3, after.substitutions.single().sourceVarnaIndex)
        assertEquals(affix.id, after.droppedTerms.single().id)
        assertFalse(ErAnekacoAsamyogapurvasyaSutra.matches(after))
    }
}
