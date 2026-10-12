package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada4.*
import dev.panini.core.SanadiAffix
import dev.panini.shiksha.*
import kotlin.test.*

class AngaVowelAnnotationTest {
    @Test
    fun `lexical anga substitutions inspect varnas and retain vowel nasality`() {
        for (perfect in listOf(false, true)) {
            val root = DerivationTerm("root", if (perfect) "लँभ्ऽ" else "कँर्ऽ", TermKind.DHATU,
                upadesha = if (perfect) "डुलभँष्" else "कृ")
            val other = if (perfect) DerivationTerm("abhyasa", "ल", TermKind.DHATU)
                else DerivationTerm("suffix", "उ", TermKind.PRATYAYA)
            val original = DerivationState(if (perfect) listOf(other, root) else listOf(root, other),
                context = DerivationalContext(rupa = Rupa(lakara = if (perfect) dev.panini.core.Lakara.LIT else dev.panini.core.Lakara.LOT)),
                appliedSutras = listOf("3.4.87"))
            val rule = if (perfect) AtaEkahalmadhyeAnadesaderLitiSutra else AtaUtSarvadhatukeSutra
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val target = result.terms.single { it.id == root.id }
            assertEquals(if (perfect) Svara.E else Svara.U, target.varnas[1])
            assertTrue(target.phonologicalText.effectiveVarnas[1].nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 3)), target.orthographicSigns)
            assertEquals(root.upadesha, target.upadesha)
            if (perfect) assertEquals(other.copy(surface = "", droppedBySutra = rule.sutra,
                originalSurfaceBeforeDrop = other.surface), result.droppedTerms.single())
            else assertEquals(other, result.terms.last())
        }
    }

    @Test
    fun `vimshati deletion preserves annotated prefix and projects final sign`() {
        val base = DerivationTerm("base", "अँविंशतिऽ", TermKind.PRATIPADIKA)
        val affix = DerivationTerm("affix", "ड", TermKind.PRATYAYA, upadesha = "डट्")
        val original = DerivationState(listOf(base, affix))
        assertTrue(TiVimshaterDitiSutra.matches(original))
        val result = TiVimshaterDitiSutra.apply(original).state
        val target = result.terms.first()
        assertEquals(base.varnas.dropLast(2), target.varnas)
        assertTrue(target.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, target.varnas.size)), target.orthographicSigns)
        assertEquals(base.upadesha, target.upadesha)
        assertEquals(affix, result.terms.last())
        assertEquals(TiVimshaterDitiSutra.sutra, result.substitutions.single().sutra)
    }

    @Test
    fun `anga vowel deletions project signs without erasing surviving nasalization`() {
        val cases = listOf(
            Triple(ShnasorAllopahSutra,
                DerivationTerm("stem", "अँनँद्ऽ", TermKind.DHATU, establishedBySutras = setOf("1.1.47")),
                DerivationTerm("ending", "स्", TermKind.PRATYAYA)),
            Triple(ShnabhyastayorAtahSutra,
                DerivationTerm("stem", "अँश्नाँऽ", TermKind.PRATYAYA, upadesha = "श्ना"),
                DerivationTerm("ending", "इ", TermKind.PRATYAYA)),
            Triple(AllopoAnahSutra,
                DerivationTerm("stem", "अँनन्ऽ", TermKind.PRATIPADIKA),
                DerivationTerm("sup-ta", "इ", TermKind.PRATYAYA)),
            Triple(IHalyaghohSutra,
                DerivationTerm("stem", "अँश्नाँऽ", TermKind.PRATYAYA, upadesha = "श्ना"),
                DerivationTerm("ending", "क", TermKind.PRATYAYA)),
        )
        for ((rule, stem, ending) in cases) {
            val original = DerivationState(listOf(stem, ending),
                samjnas = setOf(SamjnaAssignment(ending.id, Samjna.SARVADHATUKA)))
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val target = result.terms.first()
            val expected = when (rule) {
                ShnasorAllopahSutra -> listOf(Svara.A, Vyanjana.NA, Vyanjana.DA)
                ShnabhyastayorAtahSutra -> listOf(Svara.A, Vyanjana.SHA, Vyanjana.NA)
                AllopoAnahSutra -> listOf(Svara.A, Vyanjana.NA, Vyanjana.NA)
                else -> listOf(Svara.A, Vyanjana.SHA, Vyanjana.NA, Svara.II)
            }
            assertEquals(expected, target.varnas, rule.sutra)
            assertTrue(target.phonologicalText.effectiveVarnas.first().nasalized)
            if (rule == IHalyaghohSutra) assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, expected.size)), target.orthographicSigns)
            assertEquals(stem.id, target.id)
            assertEquals(stem.upadesha, target.upadesha)
            assertEquals(stem.establishedBySutras, target.establishedBySutras)
            assertEquals(ending, result.terms.last())
            assertEquals(original.samjnas, result.samjnas)
            assertEquals(rule.sutra, result.substitutions.single().sutra)
        }
    }

    @Test
    fun `anga lengthening preserves each vowel annotation and orthographic boundary`() {
        val cases = listOf(
            Triple(NamiSutra, "अँइँऽ", DerivationTerm("affix", "नाम्", TermKind.PRATYAYA)),
            Triple(SarvanamasthaneCasambuddhauSutra, "अँन्ऽ", DerivationTerm("sup-su", "स्", TermKind.PRATYAYA, upadesha = "सुँ")),
            Triple(AjjhanagamamSaniSutra, "अँइँऽ", DerivationTerm("san", "स्", TermKind.PRATYAYA, upadesha = SanadiAffix.SAN.upadesha)),
        )
        for ((rule, surface, affix) in cases) {
            val stem = DerivationTerm("stem", surface, if (rule == AjjhanagamamSaniSutra) TermKind.DHATU else TermKind.PRATIPADIKA)
            val prefix = if (rule == AjjhanagamamSaniSutra) listOf(DerivationTerm("abhyasa", "अ", TermKind.DHATU)) else emptyList()
            val original = DerivationState(prefix + listOf(stem, affix), activeAdhikaras = setOf("6.4.1"))
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val target = result.terms.single { it.id == stem.id }
            val index = stem.varnas.indexOfLast { it is Svara }
            assertEquals(if (rule == SarvanamasthaneCasambuddhauSutra) Svara.AA else Svara.II, target.varnas[index])
            assertEquals(stem.phonologicalText.effectiveVarnas.map { it.nasalized }, target.phonologicalText.effectiveVarnas.map { it.nasalized })
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, stem.varnas.size)), target.orthographicSigns)
            assertEquals(stem.id, target.id)
            assertEquals(stem.upadesha, target.upadesha)
            assertEquals(affix, result.terms.last())
            assertEquals(original.terms.take(prefix.size), result.terms.take(prefix.size))
            assertEquals(rule.sutra, result.substitutions.single().sutra)
        }
    }
}
