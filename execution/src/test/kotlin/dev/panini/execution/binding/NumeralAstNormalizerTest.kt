package dev.panini.execution.binding

import dev.panini.execution.SanskritValue
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NumeralAstNormalizerTest {
    @Test
    fun `generic numeral stem gains a first class Sanskrit value`() {
        val source = SubantaPada(
            sourceText = "द्वि + औट्",
            pratipadika = MulaPratipadika(sourceText = "द्वि", text = "द्वि"),
            sup = SupPratyaya(sourceText = "औट्", text = "औट्"),
        )

        val normalized = NumeralAstNormalizer.normalize(source)
        val numeral = assertIs<SankhyaPratipadika>(normalized.pratipadika)

        assertEquals(SanskritValue.Sankhya(2, "द्वि"), numeral.semanticValue)
        assertEquals("द्वि + औट्", normalized.sourceText)
        assertEquals("औट्", normalized.sup.text)
    }

    @Test
    fun `ordinary pratipadika remains unchanged`() {
        val source = SubantaPada(
            sourceText = "राम + सुँ",
            pratipadika = MulaPratipadika(sourceText = "राम", text = "राम"),
            sup = SupPratyaya(sourceText = "सुँ", text = "सुँ"),
        )

        assertEquals(source, NumeralAstNormalizer.normalize(source))
    }
}
