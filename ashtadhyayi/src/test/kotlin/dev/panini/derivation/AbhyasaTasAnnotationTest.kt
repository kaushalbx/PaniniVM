package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada4.*
import dev.panini.core.Lakara
import dev.panini.shiksha.*
import kotlin.test.*

class AbhyasaTasAnnotationTest {
    @Test
    fun `haladi sesha deletes scattered consonants and preserves surviving annotated vowels`() {
        for ((surface, expected) in listOf(
            "अँक्ऽइँत्" to listOf(Svara.A, Svara.I),
            "कँत्ऽइँप्" to listOf(Vyanjana.KA, Svara.A, Svara.I),
        )) {
            val abhyasa = DerivationTerm("abhyasa", surface, TermKind.DHATU)
            val original = DerivationState(listOf(abhyasa), samjnas = setOf(SamjnaAssignment(abhyasa.id, Samjna.ABHYASA)))
            assertTrue(HaladisSeshahSutra.matches(original))
            val result = HaladisSeshahSutra.apply(original).state
            val target = result.terms.single()
            assertEquals(expected, target.varnas)
            assertTrue(target.phonologicalText.effectiveVarnas.filter { it.varna is Svara }.all { it.nasalized })
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, expected.size - 1)), target.orthographicSigns)
            assertEquals(abhyasa.upadesha, target.upadesha)
            assertEquals(HaladisSeshahSutra.sutra, result.substitutions.single().sutra)
        }
    }

    @Test
    fun `initial and guna substitutions preserve abhyasa annotations`() {
        for ((rule, surface, expected) in listOf(
            Triple(AtaAdesSutra, "अँऽ", listOf(Svara.AA)),
            Triple(KuhohCuhSutra, "कँऽ", listOf(Vyanjana.CA, Svara.A)),
            Triple(GunoYangiSutra, "ऋँऽ", listOf(Svara.A, Vyanjana.RA)),
        )) {
            val abhyasa = DerivationTerm("abhyasa", surface, TermKind.DHATU)
            val original = DerivationState(listOf(abhyasa),
                samjnas = setOf(SamjnaAssignment(abhyasa.id, Samjna.ABHYASA), SamjnaAssignment(abhyasa.id, Samjna.YANG)),
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)))
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val target = result.terms.single()
            assertEquals(expected, target.varnas)
            assertTrue(target.phonologicalText.effectiveVarnas.single { it.varna is Svara }.nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, expected.size)), target.orthographicSigns)
            assertEquals(abhyasa.upadesha, target.upadesha)
            assertEquals(rule.sutra, result.substitutions.single().sutra)
        }
    }

    @Test
    fun `grouped abhyasa vowel mutations preserve each occurrence and one trace`() {
        for (rule in listOf(HrasvahSutra, UratSutra, BhrnamItSutra)) {
            // Deliberately multi-vowel fixtures verify the existing grouped transformation.
            val surface = if (rule == HrasvahSutra) "आँईँऽ" else "ऋँॠँऽ"
            val abhyasa = DerivationTerm("abhyasa", surface, TermKind.DHATU)
            val root = DerivationTerm("root", "भृ", TermKind.DHATU, upadesha = "भृञ्")
            val original = DerivationState(listOf(abhyasa, root),
                samjnas = setOf(SamjnaAssignment(abhyasa.id, Samjna.ABHYASA)))
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val target = result.terms.first()
            val expected = when (rule) {
                HrasvahSutra -> listOf(Svara.A, Svara.I)
                UratSutra -> listOf(Svara.A, Svara.A)
                else -> listOf(Svara.I, Svara.I)
            }
            assertEquals(expected, target.varnas)
            assertTrue(target.phonologicalText.effectiveVarnas.all { it.nasalized })
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), target.orthographicSigns)
            assertEquals(abhyasa.upadesha, target.upadesha)
            assertEquals(original.samjnas, result.samjnas)
            assertEquals(root, result.terms.last())
            assertEquals(rule.sutra, result.substitutions.single().sutra)
        }
    }

    @Test
    fun `bhu abhyasa vowel substitution retains nasality and avagraha`() {
        val abhyasa = DerivationTerm("abhyasa", "भुँऽ", TermKind.DHATU)
        val root = DerivationTerm("root", "भू", TermKind.DHATU)
        val original = DerivationState(listOf(abhyasa, root),
            samjnas = setOf(SamjnaAssignment(abhyasa.id, Samjna.ABHYASA)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)))
        assertTrue(BhavaterAhSutra.matches(original))
        val result = BhavaterAhSutra.apply(original).state
        val target = result.terms.first()
        assertEquals(listOf(Vyanjana.BHA, Svara.A), target.varnas)
        assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), target.orthographicSigns)
        assertEquals(abhyasa.upadesha, target.upadesha)
        assertEquals(root, result.terms.last())
        assertEquals(original.samjnas, result.samjnas)
        assertEquals(BhavaterAhSutra.sutra, result.substitutions.single().sutra)
    }

    @Test
    fun `tas operations preserve annotations and their existing trace count`() {
        for (ha in listOf(false, true)) {
            val tasi = DerivationTerm("tasi", "ताँस्ऽ", TermKind.PRATYAYA, upadesha = "तासि")
            val ending = DerivationTerm("ending", if (ha) "एँऽ" else "स्", TermKind.PRATYAYA)
            val original = DerivationState(listOf(tasi, ending), context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUT)))
            val rule = if (ha) HaEtiSutra else TasastyorLopahSutra
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val target = result.terms.first()
            assertEquals(listOf(Vyanjana.TA, Svara.AA), target.varnas)
            assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), target.orthographicSigns)
            assertEquals(tasi.upadesha, target.upadesha)
            if (ha) {
                val last = result.terms.last()
                assertEquals(listOf(Vyanjana.HA, Svara.E), last.varnas)
                assertTrue(last.phonologicalText.effectiveVarnas.last().nasalized)
                assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), last.orthographicSigns)
                assertEquals(ending.upadesha, last.upadesha)
            } else assertEquals(ending, result.terms.last())
            assertEquals(if (ha) 2 else 1, result.substitutions.size)
            assertTrue(result.substitutions.all { it.sutra == rule.sutra })
        }
    }
}
