package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

/** Numeric member meaning is independent of the host payload's representation. */
class NumberMemberEligibilityTest {
    @Test fun `fraction member sum flows through typed procedure and later copular equality`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् योग + ङे दा + लोट् + सिप् ॥
            सूची + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप्
                ततः परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            यदि योग + सुँ अपेक्षित + टा सम + सुँ असँ + लट् + तिप् तर्हि
                एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा
                शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val initial = mapOf("सूची" to SanskritValue.Suchi(listOf(
            SanskritValue.Rational(1, 2, "अर्ध"), SanskritValue.Sankhya(1, "एक")), ListMemberType.NUMBER),
            "अपेक्षित" to SanskritValue.Rational(6, 4, "equivalent"))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "RationalMemberProcedurePipeline")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
        assertTrue(SanskritValue.Rational(3, 2, "fraction").semanticallyEquals(values["योग"] as SanskritValue))
    }

    @Test fun `member summation agrees on integer lists and rejects unsupported numeric shapes`() {
        val source = "सूची + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "NumberMemberEligibility")
        val one = SanskritValue.Sankhya(1, "एक")
        val valid = mapOf("सूची" to SanskritValue.Suchi(listOf(one, one), ListMemberType.NUMBER))
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(valid))).last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        assertEquals(result.typedValue,
            (generated.getMethod("execute", Map::class.java).invoke(null, valid) as Map<*, *>)["LastResult"])
        for ((members, expected) in listOf(
            listOf<SanskritValue>(SanskritValue.Rational(1, 2, "अर्ध"), one) to SanskritValue.Rational(3, 2, "3/2"),
            listOf<SanskritValue>(SanskritValue.Rational(1, 2, "अर्ध"), SanskritValue.Rational(1, 2, "अर्ध")) to one,
        )) {
            val initial = mapOf("सूची" to SanskritValue.Suchi(members, ListMemberType.NUMBER))
            val success = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertTrue(expected.semanticallyEquals(success.typedValue!!))
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(success.typedValue, values["LastResult"])
        }
        for (whole in listOf(
            SanskritValue.Suchi(listOf(SanskritValue.Rational(1, 0, "undefined"))),
            SanskritValue.Suchi(listOf(SanskritValue.Suchi(listOf(one)))),
            SanskritValue.Suchi(listOf(SanskritValue.Shabda("एक")), ListMemberType.TEXT),
        )) {
            val initial = mapOf("सूची" to whole)
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val failure = assertFailsWith<InvocationTargetException> {
                generated.getMethod("execute", Map::class.java).invoke(null, initial)
            }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
    }
}
