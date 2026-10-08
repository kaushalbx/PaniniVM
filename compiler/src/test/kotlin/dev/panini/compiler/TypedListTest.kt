package dev.panini.compiler

import dev.panini.execution.ListMemberType
import dev.panini.execution.SanskritValue
import dev.panini.execution.sutra.ProgramSutraArthaCodec
import kotlin.test.*

class TypedListTest {
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
