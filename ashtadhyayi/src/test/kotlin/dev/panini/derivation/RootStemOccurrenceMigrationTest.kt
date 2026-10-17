package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya3.pada1.BhrshadibhyoBhuvyacverLopashCaHalahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.PaghraSthaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class RootStemOccurrenceMigrationTest {
    @Test fun `stha allomorph preserves source annotation and root identity`() {
        val root = DerivationTerm("root", "स्थाँऽ", TermKind.DHATU, upadesha = "ष्ठा",
            grammaticalAccents = listOf(GrammaticalAccent(2, AccentType.UDATTA, "source-accent")))
        val affix = DerivationTerm("suffix", "अ", TermKind.PRATYAYA, upadesha = "शप्")
        val original = DerivationState(listOf(root, affix))
        assertTrue(PaghraSthaSutra.matches(original))
        val result = PaghraSthaSutra.apply(original).state
        val target = result.terms.first()
        assertEquals(listOf(Vyanjana.TA, Svara.I, Vyanjana.SSA, Vyanjana.TTHA, Svara.A), target.varnas)
        assertTrue(target.phonologicalText.effectiveVarnas[1].nasalized)
        assertFalse(target.phonologicalText.effectiveVarnas[4].nasalized)
        assertEquals(listOf(GrammaticalAccent(1, AccentType.UDATTA, "source-accent")), target.grammaticalAccents)
        assertTrue(target.orthographicSigns.any { it.sign == OrthographicSign.AVAGRAHA && it.afterVarnaCount == 5 })
        assertEquals(root.upadesha, target.upadesha)
        assertEquals(root.id, target.id)
        assertEquals(affix, result.terms.last())
        assertEquals(0, result.substitutions.single().sourceVarnaIndex)
        assertEquals(root.surface, result.substitutions.single().originalSurface)
        assertFalse(PaghraSthaSutra.matches(result))
    }

    @Test fun `stha selection retains original identity and adjacent suffix restrictions`() {
        val root = DerivationTerm("root", "स्था", TermKind.DHATU, upadesha = "ष्ठा")
        val suffix = DerivationTerm("suffix", "अ", TermKind.PRATYAYA, upadesha = "शप्")
        assertFalse(PaghraSthaSutra.matches(DerivationState(listOf(root.copy(upadesha = "अन्य"), suffix))))
        assertFalse(PaghraSthaSutra.matches(DerivationState(listOf(root, suffix.copy(upadesha = "णिच्")))))
        assertFalse(PaghraSthaSutra.matches(DerivationState(listOf(root.copy(surface = "तिष्ठ"), suffix))))
    }

    @Test fun `bhrshadi consonant deletion records exact occurrence and leaves raw suffix pending`() {
        val stem = DerivationTerm("stem", "सुमनस्", TermKind.PRATIPADIKA,
            orthographicSigns = listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 7)))
        val original = DerivationState(listOf(stem), context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA))
        assertTrue(BhrshadibhyoBhuvyacverLopashCaHalahSutra.matches(original))
        val result = BhrshadibhyoBhuvyacverLopashCaHalahSutra.apply(original).state
        assertEquals(stem.varnas.dropLast(1), result.terms.first().varnas)
        assertEquals(stem.upadesha, result.terms.first().upadesha)
        assertEquals(stem.varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
        assertEquals("क्यङ्", result.terms.last().upadesha)
        assertTrue(result.terms.last().itProcessingPending)
        assertFalse(BhrshadibhyoBhuvyacverLopashCaHalahSutra.matches(result))
    }
}
