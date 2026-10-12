package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class OrdinalSelectorIdentityTest {
    @Test fun `ordinal named whole is not an additional ordinal object modifier`() {
        val initial = mapOf("प्रथम" to SanskritValue.Suchi(listOf(
            SanskritValue.Sankhya(2, "द्वि"), SanskritValue.Sankhya(7, "सप्तन्"))))
        for ((index, phrase) in listOf("प्रथम + ङस् द्वितीय + अम् मूल्य + अम्",
            "मूल्य + अम् द्वितीय + अम् प्रथम + ङस्",
            "प्रथम + ङसिँ द्वितीय + अम् मूल्य + अम्").withIndex()) {
            val source = "$phrase ग्रहँ + श्ना + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(7L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalNamedObjectWhole$index")
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `genitive whole named final is not a member selector`() {
        val initial = mapOf("अन्तिम" to SanskritValue.Suchi(listOf(
            SanskritValue.Sankhya(2, "द्वि"), SanskritValue.Sankhya(7, "सप्तन्"))))
        for ((index, phrase) in listOf("अन्तिम + ङस् प्रथमा + अम्", "अन्तिम + ङस् अन्तिम + अम्",
            "प्रथमा + अम् अन्तिम + ङस्", "अन्तिम + अम् अन्तिम + ङस्").withIndex()) {
            val source = "$phrase उद् + हृ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(if (index % 2 == 0) 2L else 7L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "FinalNamedWhole$index")
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `derived collection whole cannot borrow base binding or accept invalid current value`() {
        val source = "सङ्ग्रह + मतुप् + ङस् प्रथमा + अम् उद् + हृ + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "InvalidDerivedGenitiveWhole")
        for (whole in listOf<SanskritValue?>(null, SanskritValue.Sankhya(1, "एक"),
            SanskritValue.Shabda("text"), SanskritValue.Suchi(emptyList()))) {
            val initial = buildMap<String, SanskritValue> {
                put("सङ्ग्रह", SanskritValue.Suchi(listOf(SanskritValue.Sankhya(99, "base"))))
                if (whole != null) put("सङ्ग्रह + मतुप्", whole)
            }
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                generated.getMethod("execute", Map::class.java).invoke(null, initial)
            }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
    }
    @Test fun `genitive extraction preserves derived whole identity in either word order`() {
        for ((index, phrase) in listOf("सङ्ग्रह + मतुप् + ङस् प्रथमा + अम्",
            "प्रथमा + अम् सङ्ग्रह + मतुप् + ङस्").withIndex()) {
            val source = "$phrase उद् + हृ + लोट् + सिप् ।"
            val generated = BytecodeCompiler.compileAndLoad(source, "DerivedGenitiveWhole$index")
            for (number in listOf(1L, 7L)) {
                val initial = mapOf("सङ्ग्रह + मतुप्" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(number, "host"))),
                    "सङ्ग्रह" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(99, "base"))))
                val results = PaniniVM().evalScript(source,
                    scope = ExecutionScope(environment = ValueEnvironment(initial)))
                val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
                assertEquals(number, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
                val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
                assertEquals(result.typedValue, values["LastResult"])
            }
        }
    }
    @Test fun `genitive whole makes ordinal a selector despite a same named host binding`() {
        val one = SanskritValue.Sankhya(1L, "एक")
        val two = SanskritValue.Sankhya(2L, "द्वि")
        val initial = mapOf("सूची" to SanskritValue.Suchi(listOf(one, two)),
            "प्रथमा" to SanskritValue.Sankhya(9L, "नव"))
        for ((index, phrase) in listOf("सूची + ङस् प्रथमा + अम्",
            "प्रथमा + अम् सूची + ङस्").withIndex()) {
            val source = "$phrase उद् + हृ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(1L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "OrdinalSelectorIdentity$index")
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }
}
