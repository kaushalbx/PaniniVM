package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class CopularReferencePlanningTest {
    @Test fun `derived comparison references never borrow a base stem binding`() {
        val source = "यदि प्रथम + टाप् + सुँ मान + ङसिँ न्यून + टाप् + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedSubjectDoesNotAliasBase")
        for (derived in listOf<SanskritValue?>(null, SanskritValue.Sankhya(3, "host"))) {
            val initial = buildMap<String, SanskritValue> {
                put("प्रथम", SanskritValue.Sankhya(99, "base host"))
                put("मान", SanskritValue.Sankhya(2, "द्वि"))
                if (derived != null) put("प्रथम + टाप्", derived)
            }
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(if (derived == null) 2L else 1L,
                assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `matching feminine comparison preserves live derived subject identity`() {
        val source = "यदि प्रथम + टाप् + सुँ मान + ङसिँ न्यून + टाप् + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedSubjectCopularIdentity")
        for ((value, expected) in listOf(null to 2L, 1L to 2L, 3L to 1L)) {
            val initial = buildMap<String, SanskritValue> {
                put("मान", SanskritValue.Sankhya(2, "द्वि"))
                if (value != null) put("प्रथम + टाप्", SanskritValue.Sankhya(value, "host"))
            }
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `host payload does not erase established copular gender mismatch`() {
        val source = "यदि मूल्य + सुँ मान + ङसिँ न्यून + टाप् + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("मूल्य" to SanskritValue.Sankhya(1, "एक"),
            "मान" to SanskritValue.Sankhya(2, "द्वि"))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "InvalidCopularGender")
        }
    }

    @Test fun `derived ordering predicate is not silently treated as its base adjective`() {
        for (root in listOf("न्यून", "अधिक")) for (affix in listOf("तरप्", "मतुप्")) {
            val source = "यदि एक + सुँ द्वि + ङसिँ $root + $affix + सुँ असँ + लट् + तिप् तर्हि " +
                "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
                "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val generated = BytecodeCompiler.compileAndLoad(source, "DerivedPredicate")
            val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                generated.getMethod("execute").invoke(null)
            }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
    }

    @Test fun `copular predicate is grammatical identity not a same named host variable`() {
        val source = "यदि एक + सुँ द्वि + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val initial = mapOf("न्यून" to SanskritValue.Shabda("अधिक"))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "CopularPredicateIdentity")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
    }

    @Test fun `copular equality shares numeric meaning across integer and fraction payloads`() {
        val source = "यदि वाम + सुँ दक्षिण + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "MixedNumericEquality")
        val integer = SanskritValue.Sankhya(1, "एक")
        val fraction = SanskritValue.Rational(2, 2, "fraction")
        for ((left, right) in listOf(integer to fraction, fraction to integer,
            SanskritValue.Suchi(listOf(integer)) to SanskritValue.Suchi(listOf(fraction)),
            SanskritValue.Rupa("record", mapOf("field" to integer)) to
                SanskritValue.Rupa("record", mapOf("field" to fraction)))) {
            val initial = mapOf("वाम" to left, "दक्षिण" to right)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `copular ordering compares current rational values exactly`() {
        val source = "यदि वाम + सुँ दक्षिण + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "RationalCopularOrdering")
        for ((left, right, expected) in listOf(
            Triple(SanskritValue.Rational(1, 2, "half"), SanskritValue.Rational(2, 3, "thirds"), 2L),
            Triple(SanskritValue.Rational(Long.MAX_VALUE, 2, "large"), SanskritValue.Rational(Long.MAX_VALUE, 3, "large"), 1L),
            Triple(SanskritValue.Rational(1, -2, "negative"), SanskritValue.Sankhya(1, "एक"), 2L),
        )) {
            val initial = mapOf("वाम" to left, "दक्षिण" to right)
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, values["LastResult"])
        }
    }

    @Test fun `named ordering operands use current scalar values and reject collections`() {
        val source = "यदि वाम + सुँ दक्षिण + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "ScalarCopularOrdering")
        val one = SanskritValue.Sankhya(1L, "एक")
        val two = SanskritValue.Sankhya(2L, "द्वि")
        for ((left, right) in listOf(one to two, two to one, one to one)) {
            val initial = mapOf("वाम" to left, "दक्षिण" to right)
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(if (left.value < right.value) 2L else 1L,
                assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val compiled = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(result.typedValue, compiled["LastResult"])
        }
        val collection = SanskritValue.Suchi(listOf(one))
        val undefined = SanskritValue.Rational(1, 0, "undefined")
        for (initial in listOf(mapOf("वाम" to collection, "दक्षिण" to two),
            mapOf("वाम" to one, "दक्षिण" to collection),
            mapOf("वाम" to undefined, "दक्षिण" to two),
            mapOf("वाम" to one, "दक्षिण" to undefined))) {
            val results = PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial)))
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
                generated.getMethod("execute", Map::class.java).invoke(null, initial)
            }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
    }

    @Test fun `copular equality keeps subject and instrumental reference identities with literal fallbacks`() {
        val source = "यदि वाम + सुँ दक्षिण + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "ExplicitCopularReferences")
        val one = SanskritValue.Sankhya(1L, "एक")
        val two = SanskritValue.Sankhya(2L, "द्वि")
        for ((index, initial) in listOf(emptyMap(), mapOf("वाम" to one, "दक्षिण" to one),
            mapOf("वाम" to one, "दक्षिण" to two)).withIndex()) {
            val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            val value = assertIs<SanskritValue.Sankhya>(result.typedValue)
            assertEquals(if (index == 1) 2L else 1L, value.value)
            val compiled = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
            assertEquals(value, compiled["LastResult"])
        }
    }
}
