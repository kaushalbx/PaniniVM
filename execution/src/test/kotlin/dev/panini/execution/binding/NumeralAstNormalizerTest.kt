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
    fun `cardinal derivation is not an unchanged numeric literal`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse("एक + मतुप् + अम् ।")
        val pada = parsed.grammaticalVakyas().single().padas.single() as SubantaPada
        kotlin.test.assertNull(NumeralAstNormalizer.resolve(pada.pratipadika))
        kotlin.test.assertNull(NumeralPadaBinder.resolveSemanticValue(pada))
        assertEquals(pada, NumeralAstNormalizer.normalize(pada))
        val vikaras = (pada.pratipadika as dev.panini.vyakaranam.ast.MulaPratipadika).vikaras
        val typed = SankhyaPratipadika("एक + मतुप्", SanskritValue.Sankhya(1L, "एक"), vikaras)
        kotlin.test.assertNull(NumeralAstNormalizer.resolve(typed))
    }

    @Test
    fun `derived ordinal does not inherit an unmodified numeric rank`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse("प्रथम + तरप् + अम् ।")
        val pada = parsed.grammaticalVakyas().single().padas.single() as SubantaPada
        kotlin.test.assertNull(dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada))
        kotlin.test.assertNull(NumeralPadaBinder.extractOrdinalValue(pada))
        kotlin.test.assertNull(NumeralAstNormalizer.resolve(pada.pratipadika))
        assertEquals(pada, NumeralAstNormalizer.normalize(pada))
        val target = SubantaPada("फल + अम्", MulaPratipadika("फल", "फल"), SupPratyaya("अम्", "अम्"))
        val order = MemoryOrderQualifierResolver.before(target, listOf(pada, target))
        kotlin.test.assertTrue(order.isExplicit)
        kotlin.test.assertFalse(order.agreesWith(target))
        kotlin.test.assertNull(order.select(listOf(10, 20)))
    }

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
