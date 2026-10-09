package dev.panini.compiler

import dev.panini.execution.ListMemberType
import dev.panini.execution.SanskritValue
import dev.panini.execution.sutra.ProgramSutraArthaCodec
import kotlin.test.*

class TypedListTest {
    @Test
    fun `natural number member sum requires a list whole`() {
        val number = SanskritValue.Sankhya(1, "एक")
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(
            CompilerValueOperations.listNumberMemberSum(SanskritValue.Suchi(listOf(number)))).value)
        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            assertFailsWith<CompiledPaniniExecutionException> {
                CompilerValueOperations.listNumberMemberSum(SanskritValue.Gana(listOf(number)))
            }.error)
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(
            CompilerValueOperations.listSum(SanskritValue.Gana(listOf(number)))).value)
    }

    @Test
    fun `sum checks empty declared type nesting and overflow`() {
        assertEquals(0L, assertIs<SanskritValue.Sankhya>(CompilerValueOperations.listSum(
            SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER))).value)
        for (value in listOf(
            SanskritValue.Suchi(emptyList(), ListMemberType.TEXT),
            SanskritValue.Suchi(listOf(SanskritValue.Suchi(emptyList()))),
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(Long.MAX_VALUE, "maximum"), SanskritValue.Sankhya(1, "एक"))),
        )) assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            assertFailsWith<CompiledPaniniExecutionException> { CompilerValueOperations.listSum(value) }.error)
    }

    private val numeric = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")), ListMemberType.NUMBER)

    @Test
    fun `list transformations retain and enforce member type`() {
        val two = SanskritValue.Sankhya(2, "द्वि")
        for (result in listOf(
            CompilerValueOperations.listReverse(numeric),
            CompilerValueOperations.listFlatten(numeric),
            CompilerValueOperations.listAppend(numeric, two),
            CompilerValueOperations.listConcat(numeric, SanskritValue.Suchi(listOf(two))),
            CompilerValueOperations.listSlice(numeric, two, two),
        )) assertEquals(ListMemberType.NUMBER, assertIs<SanskritValue.Suchi>(result).memberType)
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.listAppend(numeric, SanskritValue.Shabda("राम"))
        }
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.listConcat(numeric, SanskritValue.Suchi(listOf(SanskritValue.Shabda("राम"))))
        }
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.listConcat(SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER),
                SanskritValue.Suchi(emptyList(), ListMemberType.TEXT))
        }
    }

    @Test
    fun `semantic codec and action history retain member type`() {
        assertEquals(numeric, ProgramSutraArthaCodec.decodeValue(ProgramSutraArthaCodec.encodeValue(numeric)))
        val runtime = CompiledProgramRuntime()
        runtime.recordActionResult("ग्रह्", numeric)
        assertEquals(numeric, runtime.loadActionResult("ग्रह्", 1))
    }

    @Test
    fun `typed construction rejects nonnumbers instead of trusting tags`() {
        assertFailsWith<CompiledPaniniExecutionException> {
            PaniniRuntime.typedSuchi(arrayOf(SanskritValue.Shabda("एक")), "NUMBER")
        }
    }
}
