package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class MemoryOrderQualifierTest {
    @Test
    fun `single named result relation selects first history across word orders`() {
        for (phrase in listOf(
            "प्रथम + अम् युज् + ल्युट् + ङस् फल + अम्",
            "युज् + ल्युट् + ङस् फल + अम् प्रथम + अम्",
            "फल + अम् युज् + ल्युट् + ङस् प्रथम + अम्",
        )) {
            val results = dev.panini.execution.PaniniVM().evalScript(
                "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
                    "त्रि + अम् चतुर् + अम् च युज् + लोट् + सिप् ।\n" +
                    "$phrase मुद्र् + णिच् + लोट् + सिप् ।",
            )
            assertTrue(results.all { it is dev.panini.execution.ExecutionResult.Success }, "$phrase: $results")
            assertEquals("त्रीणि", kotlin.test.assertIs<dev.panini.execution.ExecutionResult.Success>(results.last()).value)
        }
    }

    @Test
    fun `typed ordinal normalization retains gender for agreement`() {
        val target = subanta("फल", "अम्")
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for (form in listOf("प्रथमा", "प्रथम + टाप्", "प्रथम")) {
            val original = parser.parse("$form + अम् ।").grammaticalVakyas().single().padas.single() as SubantaPada
            val normalized = NumeralAstNormalizer.normalize(original)
            kotlin.test.assertIs<dev.panini.vyakaranam.ast.SankhyaPratipadika>(normalized.pratipadika)
            val order = MemoryOrderQualifier(pada = normalized, ordinalNumber = 1L)
            assertEquals(form == "प्रथम", order.agreesWith(target), form)
        }
    }

    @Test
    fun `executed result history rejects feminine ordering modifier`() {
        val results = dev.panini.execution.PaniniVM().evalScript(
            "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् प्रथमा + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        kotlin.test.assertIs<dev.panini.execution.ExecutionResult.Success>(results.first())
        kotlin.test.assertIs<dev.panini.execution.ExecutionResult.Failure>(results.last())
    }

    @Test
    fun `feminine ordinal cannot agree with neuter result noun`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse(
            "युज् + ल्युट् + ङस् प्रथमा + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        val padas = (parsed.body as dev.panini.vyakaranam.ast.Invocation).vakya.padas
        val reference = NamedActionResultReferenceResolver.resolve(padas).single()
        assertFalse(reference.orderingAgrees)
        assertTrue(reference.hasOrderingQualifier)
    }

    @kotlin.test.Test
    fun `derived ordinal result reference cannot silently select latest history`() {
        val results = dev.panini.execution.PaniniVM().evalScript(
            "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् प्रथम + तरप् + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        kotlin.test.assertIs<dev.panini.execution.ExecutionResult.Success>(results.first())
        kotlin.test.assertIs<dev.panini.execution.ExecutionResult.Failure>(results.last())
    }

    @Test
    fun `karaka history analysis exposes canonical action role and ordering without memory`() {
        val invocation = dev.panini.vyakaranam.parser.PaniniParser().parse(
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body as dev.panini.vyakaranam.ast.Invocation
        val padas = invocation.vakya.padas
        val reference = KarakaReferenceResolver.references(padas).single()
        assertEquals(dev.panini.core.Karaka.KARMAN, reference.karaka)
        assertEquals(1L, reference.ordinalFromOldest)
        assertTrue(reference.orderingValid)
        assertTrue(!reference.dhatuUpadesha.isNullOrBlank())
        assertTrue(reference.referent === padas[2])
        assertTrue(reference.qualifier === padas[1])
        assertEquals(3, KarakaReferenceResolver.protectedPadas(padas).size)
    }
    @Test
    fun `nonpositive typed ordinals never reach positive index memory APIs`() {
        val target = subanta("फल", "अम्")
        for (value in listOf(0L, -1L, Long.MIN_VALUE)) {
            val pada = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
                sourceText = "invalid typed ordinal", stems = emptyList(), value = value,
                sup = SupPratyaya("अम्", "अम्"),
            )
            val qualifier = MemoryOrderQualifierResolver.before(target, listOf(pada, target))
            assertTrue(qualifier.isExplicit)
            assertFalse(qualifier.agreesWith(target))
            assertEquals(null, qualifier.select(listOf("latest")))
            assertEquals(null, qualifier.select(dev.panini.execution.memory.KriyaMemory(), "root"))
        }
    }

    @Test
    fun `unresolved typed ordinal cannot mean latest result`() {
        val target = subanta("फल", "अम्")
        val ordinal = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
            sourceText = "unresolved ordinal", stems = emptyList(),
            sup = SupPratyaya("अम्", "अम्"),
        )
        val qualifier = MemoryOrderQualifierResolver.before(target, listOf(ordinal, target))
        assertTrue(qualifier.isExplicit)
        assertTrue(qualifier.unresolvedOrdinal)
        assertFalse(qualifier.agreesWith(target))
        assertEquals(null, qualifier.select(listOf("latest")))
        assertEquals(null, qualifier.select(dev.panini.execution.memory.KriyaMemory(), "root"))
    }

    @Test
    fun `unresolved ordinal remains a modifier of a named result`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse(
            "युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        val invocation = parsed.body as dev.panini.vyakaranam.ast.Invocation
        val padas = invocation.vakya.padas.toMutableList()
        padas[1] = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
            sourceText = "unresolved ordinal", stems = emptyList(), sup = SupPratyaya("अम्", "अम्"),
        )
        val reference = NamedActionResultReferenceResolver.resolve(padas).single()
        assertTrue(reference.hasOrderingQualifier)
        assertFalse(reference.orderingAgrees)
        val operands = NamedActionResultReferenceResolver.operandPadas(padas)
        assertEquals(listOf(padas[2], padas[3]), operands)
    }

    @Test
    fun `recognizes typed purva qualifier before target`() {
        val target = subanta("फल", "अम्")
        assertTrue(MemoryOrderQualifierResolver.before(target, listOf(subanta("पूर्व", "अम्"), target)).previous)
        assertFalse(MemoryOrderQualifierResolver.before(target, listOf(subanta("उत्तर", "अम्"), target)).previous)
    }

    @Test
    fun `equal looking results have occurrence specific ordering`() {
        val first = subanta("फल", "अम्")
        val second = subanta("फल", "अम्")
        assertEquals(first, second)
        val padas = listOf(first, subanta("पूर्व", "अम्"), second)
        assertFalse(MemoryOrderQualifierResolver.before(first, padas).previous)
        assertTrue(MemoryOrderQualifierResolver.before(second, padas).previous)
    }

    @Test
    fun `ordering qualifier agrees in case and number with its referent`() {
        val target = subanta("फल", "अम्")
        assertTrue(MemoryOrderQualifierResolver.before(target, listOf(subanta("पूर्व", "अम्"), target)).agreesWith(target))
        assertFalse(MemoryOrderQualifierResolver.before(target, listOf(subanta("पूर्व", "सुँ"), target)).agreesWith(target))
        assertFalse(MemoryOrderQualifierResolver.before(target, listOf(subanta("पूर्व", "शस्"), target)).agreesWith(target))
    }

    @Test
    fun `lexical third ordinal uses its typed identity`() {
        val target = subanta("फल", "अम्")
        val qualifier = MemoryOrderQualifierResolver.before(target, listOf(subanta("तृतीय", "अम्"), target))
        assertEquals(3L, qualifier.ordinalNumber)
        assertTrue(qualifier.agreesWith(target))
    }

    @Test
    fun `large ordinal never wraps into a small valid position`() {
        val values = listOf("first", "second")
        assertEquals(null, MemoryOrderQualifier(ordinalNumber = 4_294_967_297L).select(values))
        assertEquals(null, MemoryOrderQualifier(ordinalNumber = Long.MAX_VALUE).select(values))
        assertEquals(null, dev.panini.execution.memory.KriyaMemory().ordinalKriya(Long.MAX_VALUE))
    }

    @Test
    fun `ordinal extraction preserves an already typed semantic value`() {
        val pada = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
            sourceText = "typed ordinal", stems = emptyList(), value = 4_294_967_297L,
            sup = SupPratyaya("अम्", "अम्"),
        )
        assertEquals(pada.value, NumeralPadaBinder.extractOrdinalValue(pada))
        val target = subanta("फल", "अम्")
        assertEquals(pada.value, MemoryOrderQualifierResolver.before(target, listOf(pada, target)).ordinalNumber)
    }

    private fun subanta(stem: String, sup: String) = SubantaPada(
        sourceText = "$stem + $sup",
        pratipadika = MulaPratipadika(stem, stem),
        sup = SupPratyaya(sup, sup),
    )
}
