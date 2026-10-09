package dev.panini.compiler

import dev.panini.execution.SanskritValue
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.IFEQ
import org.objectweb.asm.Opcodes.ALOAD
import java.io.File
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class StructuredBytecodeCompilerTest {
    @Test
    fun `unsupported prohibition is rejected rather than silently ignored`() {
        val source = "वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "न शून्य + अम् राम + अम् शून्य + अम् ।\n" +
            "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "वाचन + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            (PaniniVM().evalScript(source).last() as ExecutionResult.Failure).error)
        assertTrue(assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(source, "RejectedUnsupportedProhibition")
        }.message.orEmpty().contains("Unsupported procedure prohibition"))
    }

    @Test
    fun `assignment and lookup keep derived referents separate from base names`() {
        for ((index, suffix) in listOf("मतुप्", "तरप्", "टाप्").withIndex()) {
            val source = "नवन् + शस् मान + ङे दा + लोट् + सिप् ।\n" +
                "एक + अम् मान + $suffix + ङे दा + लोट् + सिप् ।\n" +
                "द्वि + औट् मान + $suffix + ङे दा + लोट् + सिप् ।\n" +
                "मान + $suffix + अम् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "मान + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals("द्वि", (results[3] as ExecutionResult.Success).value)
            assertEquals("नवन्", (results.last() as ExecutionResult.Success).value)
            val compiled = compileAndInspect(source, "CompiledDerivedReferent$index")
            assertEquals(9L, (compiled.values.getValue("मान") as SanskritValue.Sankhya).value)
            assertEquals(2L, (compiled.values.getValue("मान + $suffix") as SanskritValue.Sankhya).value)
            assertEquals((results.last() as ExecutionResult.Success).value,
                compiled.values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `nested procedure prohibition compares resolved parameter values`() {
        for ((index, second) in listOf("एक", "द्वि").withIndex()) {
            val source = "वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "पूर्व + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "उत्तर + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "न पूर्व + अम् उत्तर + अम् ।\n" +
                "पूर्व + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "प्रेषण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "आदि + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "अन्त + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "आदि + अम् अन्त + अम् च वाचन + टा डुकृञ् + उ + लोट् + सिप् ॥\n" +
                "एक + अम् निवेश + ङे दा + लोट् + सिप् ।\n" +
                "$second + अम् मान + ङे दा + लोट् + सिप् ।\n" +
                "निवेश + अम् मान + अम् च प्रेषण + टा डुकृञ् + उ + लोट् + सिप् ।"
            val result = PaniniVM().evalScript(source).last()
            if (index == 0) {
                assertEquals(dev.panini.execution.ExecutionError.ACTION_FAILED, (result as ExecutionResult.Failure).error)
                val failure = assertFailsWith<InvocationTargetException> {
                    compileAndInspect(source, "CompiledNestedProhibition$index")
                }
                assertEquals(dev.panini.execution.ExecutionError.ACTION_FAILED,
                    (failure.cause as CompiledPaniniExecutionException).error)
            } else assertEquals((result as ExecutionResult.Success).value,
                compileAndInspect(source, "CompiledNestedProhibition$index").values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `numeric prohibition checks named runtime values before procedure body`() {
        for ((index, number) in listOf("शून्य", "एक").withIndex()) {
            val source = "वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "न मान + अम् शून्य + अम् ।\n" +
                "मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "$number + अम् निवेश + ङे दा + लोट् + सिप् ।\n" +
                "निवेश + अम् वाचन + टा डुकृञ् + उ + लोट् + सिप् ।"
            val interpreted = PaniniVM().evalScript(source).last()
            if (index == 0) {
                assertEquals(dev.panini.execution.ExecutionError.ACTION_FAILED,
                    (interpreted as ExecutionResult.Failure).error)
                val failure = assertFailsWith<InvocationTargetException> {
                    compileAndInspect(source, "CompiledDynamicProhibition$index")
                }
                assertEquals(dev.panini.execution.ExecutionError.ACTION_FAILED,
                    (failure.cause as CompiledPaniniExecutionException).error)
            } else {
                assertEquals((interpreted as ExecutionResult.Success).value,
                    compileAndInspect(source, "CompiledDynamicProhibition$index")
                        .values.getValue("LastResult").toDisplayText())
            }
        }
    }

    @Test
    fun `procedure prior action member role and same named display argument have parity`() {
        for ((index, specification) in listOf(
            "अन्तिम" to "मान + ङस् अन्तिम + अम् उद् + हृ + ल्यप्",
            "सङ्ख्या" to "मान + ङस् सङ्ख्या + शस् युज् + णिच् + क्त्वा",
        ).withIndex()) {
            val (name, prior) = specification
            val source = "वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "मान + सुँ सूची + सुँ इति मान + सुँ ।\n" +
                "$name + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "$prior $name + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + अम् नवन् + शस् च वाचन + टा डुकृञ् + उ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals("नवन्", (results.last() as ExecutionResult.Success).value)
            assertEquals("नवन्", compileAndInspect(source, "CompiledMemberRoleParameter$index")
                .values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `single scalar cannot masquerade as a declared list parameter`() {
        val source = "योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सूची + सुँ इति मान + सुँ ।\n" +
            "मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् योजन + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertTrue(PaniniVM().evalScript(source).last() is ExecutionResult.Failure)
        assertFailsWith<InvocationTargetException> {
            compileAndInspect(source, "CompiledInvalidScalarListParameter")
        }
    }

    @Test
    fun `typed procedure list parameter uses natural member sum rather than reserved slot`() {
        val source = "योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सूची + सुँ इति मान + सुँ ।\n" +
            "सङ्ख्या + सुँ इति परिणाम + सुँ ।\n" +
            "मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ॥\n" +
            "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + अम् योजन + टा डुकृञ् + उ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(6L, (compileAndInspect(source, "CompiledListParameterMemberSum")
            .values.getValue("LastResult") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `nonfinite member sum prints through shared typed collection lowering`() {
        val source = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् सङ्ख्या + शस् युज् + णिच् + क्त्वा मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals((results.last() as ExecutionResult.Success).value,
            compileAndInspect(source, "CompiledNonfiniteMemberSum").values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `genitive list number members sum through typed collection IR`() {
        for ((index, phrase) in listOf("सूची + ङस् सङ्ख्या + शस्", "सङ्ख्या + शस् सूची + ङस्").withIndex()) {
            val source = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "$phrase युज् + णिच् + लोट् + सिप् ।"
            assertEquals(6L, (compileAndInspect(source, "CompiledMemberSum$index")
                .values.getValue("LastResult") as SanskritValue.Sankhya).value)
        }
    }

    @Test
    fun `compiled derived truth subject cannot read plain base state`() {
        val source = "सत्य + अम् विजय + ङे दा + लोट् + सिप् ।\n" +
            "यदि विजय + मतुप् + सुँ भू + लट् + तिप् तर्हि " +
            "एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
            "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।"
        val failure = assertFailsWith<InvocationTargetException> {
            compileAndInspect(source, "CompiledDistinctDerivedTruthSubject")
        }
        assertTrue(failure.cause?.message.orEmpty().contains("विजय"), failure.cause.toString())
    }

    @Test
    fun `derived truth branch remains a nominal rather than a boolean`() {
        for ((index, affix) in listOf("मतुप्", "तरप्").withIndex()) {
            val source = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
                "सत्य + $affix + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
                "असत्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val compiled = compileAndInspect(source, "CompiledDerivedTruth$index").values.getValue("LastResult")
            assertTrue(compiled is SanskritValue.Shabda, compiled.toString())
            assertEquals((results.last() as ExecutionResult.Success).value, compiled.toDisplayText())
        }
    }

    @Test
    fun `named list example with nonfinite extraction has backend parity`() {
        val source = File("examples/collections/named_list_declaration.pvm").readText()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals((results.last() as ExecutionResult.Success).value,
            compileAndInspect(source, "CompiledNamedListExample").values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `compiled nonfinite extraction preserves selector morphology`() {
        for (selector in listOf("अन्तिम + मतुप् + अम्", "अन्तिम + तरप् + अम्", "अन्तिम + शस्")) {
            assertFailsWith<IllegalArgumentException>(selector) {
                CompilerFrontend.lower(
                    "सूची + ङस् $selector उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।",
                    "InvalidNonfiniteFinalMember")
            }
        }
        val source = "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् अन्तिम + अम् उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("द्वि", compileAndInspect(source, "CompiledNonfiniteFinalMember")
            .values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `compiler preserves final member selector morphology`() {
        for (selector in listOf("अन्तिम + मतुप् + अम्", "अन्तिम + तरप् + अम्",
            "अन्तिम + टाप् + अम्", "अन्तिम + शस्", "अन्तिम + अम् अन्तिम + अम्")) {
            assertFailsWith<IllegalArgumentException>(selector) {
                CompilerFrontend.lower("सूची + ङस् $selector उद् + हृ + लोट् + सिप् ।",
                    "InvalidFinalMemberMorphology")
            }
        }
        for ((index, phrase) in listOf("सूची + ङस् अन्तिम + अम्", "अन्तिम + अम् सूची + ङस्").withIndex()) {
            val source = "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "$phrase उद् + हृ + लोट् + सिप् ।"
            assertEquals(2L, (compileAndInspect(source, "CompiledFinalMemberOrder$index")
                .values.getValue("LastResult") as SanskritValue.Sankhya).value)
        }
    }

    @Test
    fun `compiler rejects derived purva rather than selecting previous history`() {
        for (affix in listOf("तरप्", "मतुप्")) {
            val source = "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
                "त्रि + अम् चतुर् + अम् च युज् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् पूर्व + $affix + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertFailsWith<IllegalArgumentException>(affix) {
                CompilerFrontend.lower(source, "InvalidDerivedPreviousSelector")
            }
        }
    }

    @Test
    fun `compiler cannot erase affixes from an ordinal value object`() {
        for (affix in listOf("मतुप्", "तरप्", "टाप्")) {
            val source = "एक + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् प्रथम + अम् मूल्य + $affix + अम् ग्रहँ + श्ना + लोट् + सिप् ।"
            assertFailsWith<IllegalArgumentException>(affix) {
                CompilerFrontend.lower(source, "InvalidDerivedOrdinalObject")
            }
        }
    }

    @Test
    fun `nama procedure declaration executes like iti in both backends`() {
        for ((index, marker) in listOf("नाम", "इति").withIndex()) {
            val source = "गणन + सुँ $marker प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "नवन् + शस् गणन + टा डुकृञ् + उ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            val compiled = compileAndInspect(source, "CompiledNamaProcedure$index")
            assertEquals((results.last() as ExecutionResult.Success).value,
                compiled.values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `compiler cannot reinterpret derived list nouns as plain declarations`() {
        for (source in listOf(
            "एक + ङस् सूची + मतुप् + सुँ असँ + लट् + तिप् ।",
            "एक + ङस् सङ्ख्या + मतुप् + आम् सूची + सुँ असँ + लट् + तिप् ।",
            "राम + ङस् शब्द + मतुप् + आम् सूची + सुँ असँ + लट् + तिप् ।",
        )) assertFailsWith<IllegalArgumentException>(source) {
            CompilerFrontend.lower(source, "InvalidDerivedListDeclaration")
        }
    }

    @Test
    fun `compiled procedure rejects competing history selectors before rebinding`() {
        val source = "वाचन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् वाचन + टा कृ + लोट् + सिप् ।"
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "InvalidProcedureHistorySelectors")
        }
    }

    @Test
    fun `procedure bodies preserve reordered history selectors across backends`() {
        for ((index, phrase) in listOf(
            "प्रथम + अम् युज् + ल्युट् + ङस् फल + अम्",
            "युज् + ल्युट् + ङस् फल + अम् प्रथम + अम्",
            "फल + अम् युज् + ल्युट् + ङस् प्रथम + अम्",
        ).withIndex()) {
            val source = "वाचन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
                "$phrase मुद्र् + णिच् + लोट् + सिप् ॥\n" +
                "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
                "नवन् + शस् वाचन + टा कृ + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals("त्रीणि", (results.last() as ExecutionResult.Success).value)
            assertEquals("त्रीणि", compileAndInspect(source, "CompiledProcedureHistoryOrder$index")
                .values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `compiled single result relation selects first history without adjacency`() {
        for ((index, phrase) in listOf(
            "प्रथम + अम् युज् + ल्युट् + ङस् फल + अम्",
            "युज् + ल्युट् + ङस् फल + अम् प्रथम + अम्",
            "फल + अम् युज् + ल्युट् + ङस् प्रथम + अम्",
        ).withIndex()) {
            val source = "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
                "त्रि + अम् चतुर् + अम् च युज् + लोट् + सिप् ।\n" +
                "$phrase मुद्र् + णिच् + लोट् + सिप् ।"
            val result = compileAndInspect(source, "CompiledHistoryWordOrder$index")
            assertEquals("त्रीणि", result.values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `compiled history rejects feminine modifier of neuter result`() {
        val source = "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् प्रथमा + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "InvalidHistoryGender")
        }
    }

    @Test
    fun `compiled history reference rejects unresolved derived ordinal`() {
        val source = "एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + तरप् + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "InvalidDerivedHistoryOrdinal")
        }
    }

    @Test
    fun `compiler rejects feminine ordinal attached to neuter value object`() {
        for (ordinal in listOf("प्रथमा", "द्वितीया", "तृतीया", "प्रथम + टाप्")) {
            val source = "एक + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् $ordinal + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।"
            assertFailsWith<IllegalArgumentException>(ordinal) {
                CompilerFrontend.lower(source, "InvalidOrdinalGender")
            }
        }
    }

    @Test
    fun `compiled ordinal object relation does not depend on adjacency`() {
        for ((index, phrase) in listOf(
            "मूल्य + अम् सूची + ङस् द्वितीय + अम्",
            "द्वि + तीय + अम् सूची + ङस् मूल्य + अम्",
        ).withIndex()) {
            val source = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "$phrase ग्रहँ + श्ना + लोट् + सिप् ।"
            val result = compileAndInspect(source, "CompiledOrdinalWordOrder$index")
            assertEquals(2L, (result.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        }
    }

    @Test
    fun `compiled nama declaration and bare list reference follow the requested discourse`() {
        val source = "एक + ङस् द्वि + ओस् त्रि + आम् च क्रम + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "क्रम + ङस् प्रथम + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।"
        val result = compileAndInspect(source, "CompiledNamaListDiscourse")
        assertEquals("त्रि", result.values.getValue("LastResult").toDisplayText())
        assertEquals(result.values.getValue("क्रम"), result.values.getValue("सूची"))
        assertEquals(listOf(1L, 2L, 3L), (result.values.getValue("क्रम") as SanskritValue.Suchi).items
            .map { (it as SanskritValue.Sankhya).value })
    }
    @Test
    fun `compiled nominal iti naming retains list identity across cases`() {
        val source = "क्रम + सुँ इति एक + ङस् द्वि + ओस् च सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "क्रम + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।"
        val result = compileAndInspect(source, "CompiledNamedNaturalList")
        assertEquals(2L, (result.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(dev.panini.execution.ListMemberType.NUMBER,
            (result.values.getValue("क्रम") as SanskritValue.Suchi).memberType)
        assertEquals(result.values.getValue("क्रम"), result.values.getValue("सूची"))
    }
    @Test
    fun `migrated natural list examples retain typed values and backend parity`() {
        for ((index, name) in listOf("membership", "ordinal_index").withIndex()) {
            val source = File("examples/collections/$name.pvm").readText()
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, "$name: $interpreted")
            val compiled = compileAndInspect(source, "CompiledNaturalListExample$index")
            assertEquals((interpreted.last() as ExecutionResult.Success).value,
                compiled.values.getValue("LastResult").toDisplayText(), name)
            val list = compiled.values.getValue("सूची") as SanskritValue.Suchi
            assertEquals(dev.panini.execution.ListMemberType.NUMBER, list.memberType, name)
            val expected = if (name == "membership") listOf(1L, 3L) else listOf(1L, 2L, 3L)
            assertEquals(expected, list.items.map { (it as SanskritValue.Sankhya).value }, name)
            val expectedOutput = if (name == "membership") "असत्यम्" else list.items[1].toDisplayText()
            assertEquals(expectedOutput, compiled.values.getValue("LastResult").toDisplayText(), name)
        }
    }
    @Test
    fun `compiled word qualifier creates a typed word list`() {
        val source = "राम + ङस् सीता + ङस् च शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।"
        val value = compileAndInspect(source, "CompiledWordList").values.getValue("सूची") as SanskritValue.Suchi
        assertEquals(dev.panini.execution.ListMemberType.TEXT, value.memberType)
        assertEquals(listOf("राम", "सीता"), value.items.map { (it as SanskritValue.Shabda).text })
    }
    @Test
    fun `compiled genitive number qualifier constrains list members`() {
        val source = "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च " +
            "सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।"
        val value = compileAndInspect(source, "CompiledTypedNaturalList").values.getValue("सूची") as SanskritValue.Suchi
        assertEquals(dev.panini.execution.ListMemberType.NUMBER, value.memberType)
        assertEquals(listOf(1L, 2L, 3L, 4L), value.items.map { (it as SanskritValue.Sankhya).value })
    }
    @Test
    fun `compiled genitive declaration creates an ordered list`() {
        val source = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।"
        val value = compileAndInspect(source, "CompiledNaturalList").values.getValue("सूची") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L), value.items.map { (it as SanskritValue.Sankhya).value })
    }
    @Test
    fun `skipped compiled history display does not evaluate missing participants`() {
        val source = "यदि एक + सुँ द्वि + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
            "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् " +
            "अन्यथा नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("नवन्", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("नवन्", compileAndInspect(source, "CompiledSkippedParticipantHistory")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `compiled procedure body selects participant history instead of argument one`() {
        val source = "वाचन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् वाचन + टा कृ + लोट् + सिप् ।"
        assertEquals("एक द्वि", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("एक द्वि", compileAndInspect(source, "CompiledParticipantHistoryProcedure")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `compiled history display validates ordering modifier agreement`() {
        for (sup in listOf("सुँ", "शस्")) {
            assertFailsWith<IllegalArgumentException> {
                CompilerFrontend.lower("युज् + ल्युट् + ङस् पूर्व + $sup कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                    "InvalidKarakaDisplayAgreement")
            }
        }
    }
    @Test
    fun `mixed history display retains coordinated ordinary operands`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् दशन् + शस् च युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val interpreted = (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value
        assertEquals("नवन् दशन् एक द्वि", interpreted)
        assertEquals(interpreted, compileAndInspect(source, "CompiledCoordinatedHistoryDisplay")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `compiled history display rejects unavailable action or participant relation`() {
        for (query in listOf(
            "युज् + ल्युट् + ङस् पूर्व + अम् कर्मन् + अम्",
            "युज् + ल्युट् + ङस् तृतीय + अम् कर्मन् + अम्",
            "युज् + ल्युट् + ङस् करण + अम्",
        )) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                query + " मुद्र् + णिच् + लोट् + सिप् ।"
            assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
                (PaniniVM().evalScript(source).last() as ExecutionResult.Failure).error)
            val failure = assertFailsWith<InvocationTargetException> {
                compileAndInspect(source, "CompiledMissingParticipantDisplay")
            }
            assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
                (failure.cause as CompiledPaniniExecutionException).error)
        }
    }
    @Test
    fun `compiled mixed ordinary and history display retains written operand order`() {
        val setup = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n"
        for ((operands, expected) in listOf(
            "नवन् + शस् युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम्" to "नवन् एक द्वि",
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् नवन् + शस्" to "एक द्वि नवन्",
        )) {
            val source = setup + operands + " मुद्र् + णिच् + लोट् + सिप् ।"
            assertEquals(expected, (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
            assertEquals(expected, compileAndInspect(source, "CompiledMixedKarakaDisplay")
                .values.getValue("LastResult").toDisplayText())
        }
    }
    @Test
    fun `compiled display keeps distinct ordered karaka occurrences`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् " +
            "युज् + ल्युट् + ङस् द्वि + तीय + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("एक द्वि द्वि त्रि", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("एक द्वि द्वि त्रि", compileAndInspect(source, "CompiledOrderedKarakaDisplay")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `karaka history display compiles with participant semantics`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("एक द्वि", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("एक द्वि", compileAndInspect(source, "CompiledKarakaHistory").values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `procedure body preserves history ordinal rather than rebinding it as argument one`() {
        val source = "वाचन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् वाचन + टा कृ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals("त्रीणि", (results.last() as ExecutionResult.Success).value)
        assertEquals("त्रीणि", compileAndInspect(source, "CompiledHistoryInsideProcedure")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `lexical third result selects the third completed matching action`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "त्रि + शस् चतुर् + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् तृतीय + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("सप्त", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("सप्त", compileAndInspect(source, "CompiledLexicalThirdHistory")
            .values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `compiler rejects result ordering with mismatched case or number`() {
        for (sup in listOf("सुँ", "शस्")) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् पूर्व + $sup फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidResultAgreement") }
        }
    }
    @Test
    fun `two procedure operands retain distinct named result occurrences`() {
        val source = "संयोजन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "सङ्ख्या + सुँ इति परिणाम + सुँ ।\n" +
            "वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "वाम + ङि युज् + ल्युट् + ङस् पूर्व + अम् फल + अम् " +
            "दक्षिण + ङि युज् + ल्युट् + ङस् फल + अम् संयोजन + टा कृ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals(8L, ((results.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
        assertEquals(8L, (compileAndInspect(source, "CompiledTwoHistoryArguments")
            .values.getValue("LastResult") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `procedure arguments select named action history in both backends`() {
        val definition = "वर्धन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "सङ्ख्या + सुँ इति परिणाम + सुँ ।\n" +
            "मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n"
        for ((index, fixture) in listOf("" to 6L, "पूर्व + अम् " to 4L, "प्रथम + अम् " to 4L).withIndex()) {
            val (qualifier, expected) = fixture
            for ((namedIndex, slot) in listOf("", "मान + ङि ").withIndex()) {
                val source = definition +
                    "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                    "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
                    "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                    "${slot}युज् + ल्युट् + ङस् ${qualifier}फल + अम् वर्धन + टा कृ + लोट् + सिप् ।"
                val results = PaniniVM().evalScript(source)
                assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
                assertEquals(expected, ((results.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
                assertEquals(expected, (compileAndInspect(source, "CompiledHistoryArgument${index}_$namedIndex")
                    .values.getValue("LastResult") as SanskritValue.Sankhya).value)
            }
        }
    }

    @Test
    fun `missing named procedure argument fails before executing its body`() {
        val source = "वर्धन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् फल + अम् वर्धन + टा कृ + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.last() is ExecutionResult.Failure, results.toString())
        val failure = assertFailsWith<InvocationTargetException> {
            compileAndInspect(source, "CompiledMissingHistoryArgument")
        }
        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            (failure.cause as CompiledPaniniExecutionException).error)
    }

    @Test
    fun `missing named history fails at execution in both backends`() {
        for ((index, qualifier) in listOf("", "पूर्व + अम् ", "द्वि + तीय + अम् ").withIndex()) {
            val source = "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् ${qualifier}फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertTrue(PaniniVM().evalScript(source).last() is ExecutionResult.Failure)
            val failure = assertFailsWith<InvocationTargetException> {
                compileAndInspect(source, "CompiledMissingHistory$index")
            }
            assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
                (failure.cause as CompiledPaniniExecutionException).error)
        }
    }

    @Test
    fun `named history loads select the latest matching completed action`() {
        val prefix = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n"
        val source = prefix + "युज् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("पञ्च", (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
        assertEquals("पञ्च", compileAndInspect(source, "CompiledLatestHistory").values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `previous named result ignores intervening actions in both backends`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् पूर्व + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val vm = PaniniVM()
        val results = vm.evalScript(source)
        assertEquals("त्रीणि", (results.last() as ExecutionResult.Success).value, results.toString())
        assertEquals("त्रीणि", compileAndInspect(source, "CompiledPreviousHistory").values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `condition can compare a previous named result`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "यदि युज् + ल्युट् + ङस् पूर्व + सुँ फल + सुँ त्रि + भिस् सम + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        val interpreted = (results.last() as ExecutionResult.Success).value
        assertEquals("एक", interpreted, results.toString())
        assertEquals(interpreted, compileAndInspect(source, "CompiledPreviousHistoryCondition")
            .values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `loop reevaluates named action history rather than body truth`() {
        for ((index, qualifier) in listOf("", "पूर्व + सुँ ").withIndex()) {
            val initial = if (qualifier.isEmpty()) "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n"
                else "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                    "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n"
            val source = initial +
                "पञ्च + कृत्वसुच् यावत् युज् + ल्युट् + ङस् ${qualifier}फल + सुँ त्रि + भिस् सम + सुँ असँ + लट् + तिप् तावत् " +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + क्त्वा नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
            val completion = results.filterIsInstance<ExecutionResult.Success>().last { it.operation == "pvm.while" }
            val expectedIterations = if (qualifier.isEmpty()) 1L else 2L
            assertEquals(expectedIterations, completion.iterationCount)
            val compiled = compileAndInspect(source, "CompiledNamedLoop$index")
            val outcome = compiled.values.getValue("परिणाम") as SanskritValue.Rupa
            assertEquals(expectedIterations, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        }
    }

    @Test
    fun `ordinal named results preserve chronological order in both backends`() {
        for ((index, fixture) in listOf("प्रथम + अम्" to "त्रीणि", "द्वि + तीय + अम्" to "पञ्च").withIndex()) {
            val (qualifier, expected) = fixture
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
                "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् $qualifier फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertEquals(expected, (PaniniVM().evalScript(source).last() as ExecutionResult.Success).value)
            assertEquals(expected, compileAndInspect(source, "CompiledOrdinalHistory$index")
                .values.getValue("LastResult").toDisplayText())
        }
    }

    @Test
    fun `ordinal references work in conditions and bounded loops`() {
        val prefix = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n"
        val condition = "युज् + ल्युट् + ङस् प्रथम + सुँ फल + सुँ त्रि + भिस् सम + सुँ असँ + लट् + तिप्"
        val branch = prefix + "यदि $condition तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् " +
            "अन्यथा द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("एक", (PaniniVM().evalScript(branch).last() as ExecutionResult.Success).value)
        assertEquals("एक", compileAndInspect(branch, "CompiledOrdinalCondition").values.getValue("LastResult").toDisplayText())
        val loop = prefix + "द्वि + कृत्वसुच् यावत् $condition तावत् नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(loop)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        val completion = results.filterIsInstance<ExecutionResult.Success>().last { it.operation == "pvm.while" }
        assertEquals(2L, completion.iterationCount)
        val outcome = compileAndInspect(loop, "CompiledOrdinalLoop").values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals(2L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `dice named choice result retains its number after printing`() {
        val source = File("examples/algorithms/dice_opposite_face.pvm").readText()
        val program = CompilerFrontend.lower(source, "DiceNamedHistory")
        assertTrue(program.entryPoint.any { it is CompilerInstruction.LoadActionResult && it.dhatuUpadesha == "चिञ्" })
        compileAndInspect(source, "CompiledDiceNamedHistory")
    }
    @Test
    fun `historical action results never silently alias the latest print result`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)
        assertEquals("त्रीणि", (results.last() as ExecutionResult.Success).value)
        val compiled = compileAndInspect(source, "CompiledHistoricalActionResult")
        assertEquals("त्रीणि", compiled.values.getValue("LastResult").toDisplayText())
    }
    @Test
    fun `objectless prior action display resolves context in both backends`() {
        for ((index, target) in listOf("" to "त्रीणि", "नवन् + शस् " to "नवन्").withIndex()) {
            val (objectSource, expected) = target
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
                objectSource + "मुद्र् + णिच् + लोट् + सिप् ।"
            val interpreted = PaniniVM().evalScript(source)
            assertEquals(expected,
                (interpreted.last() as ExecutionResult.Success).value)
            val compiled = compileAndInspect(source, "CompiledEllipticalPriorDisplay$index")
            assertEquals(expected, compiled.values.getValue("LastResult").toDisplayText())
        }
    }
    @Test
    fun `conditional prior actions execute only the selected branch in both backends`() {
        for ((index, condition) in listOf("एक" to 3L, "द्वि" to 7L).withIndex()) {
            val (operand, expected) = condition
            val source = "यदि एक + सुँ $operand + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
                "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
                "फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् अन्यथा " +
                "त्रि + शस् चतुर् + शस् च युज् + णिच् + क्त्वा " +
                "फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।"
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
            assertEquals(expected, ((interpreted.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
            val compiled = compileAndInspect(source, "CompiledConditionalPrior$index")
            assertEquals(expected, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
            assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        }
        val unselectedFailure = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
            "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
            "फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् अन्यथा " +
            "एक + अम् द्वि + औट् च वि + युज् + णिच् + ल्यप् " +
            "फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।"
        val interpreted = PaniniVM().evalScript(unselectedFailure)
        assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
        val compiled = compileAndInspect(unselectedFailure, "CompiledUnselectedPriorFailure")
        assertEquals(3L, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `failed compiled prior action stops before the reusable main command`() {
        val source = """
            प्रदर्शन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च वि + युज् + णिच् + ल्यप्
            प्रदर्शन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val failure = assertFailsWith<InvocationTargetException> {
            compileAndInspect(source, "CompiledFailedPriorProcedure")
        }
        assertTrue(failure.cause?.message.orEmpty().contains("-1"), failure.cause.toString())
    }

    @Test
    fun `prior action executes before a compiled reusable procedure call`() {
        val source = """
            प्रदर्शन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा
            प्रदर्शन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val compiled = compileAndInspect(source, "CompiledPriorProcedure")
        assertEquals("त्रीणि", compiled.values.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `multiple prior actions preserve explicit result flow in compiler IR`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
            "फल + अम् त्रि + शस् च युज् + णिच् + क्त्वा " +
            "फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।"
        val compiled = compileAndInspect(source, "CompiledMultiplePriorActions")
        assertEquals(6L, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `prior action morphology shares primitive lowering with tatah`() {
        for ((index, item) in listOf("युज् + णिच् + क्त्वा" to 3L,
            "वि + युज् + णिच् + ल्यप्" to 1L,
            "युज् + णिच् + लोट् + सिप् ततः" to 3L).withIndex()) {
            val (addition, expected) = item
            val source = "द्वि + औट् एक + अम् च $addition फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् " +
                "ततः फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val compiled = compileAndInspect(source, "CompiledPriorAction$index")
            assertEquals(expected, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
            assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        }
    }

    @Test
    fun `una and adhika numeral constructions preserve values in both backends`() {
        for ((construction, expected) in listOf(
            "एक + ऊन + विंशति" to 19L,
            "द्वि + विंशति + अधिक + शत" to 122L,
        )) {
            val source = "$construction + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।"
            val compiled = compileAndInspect(source, "CompiledConstructedNumeral$expected")
            assertEquals(expected, (compiled.values.getValue("परिणाम") as SanskritValue.Sankhya).value)
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
            assertEquals(expected, ((interpreted.last() as ExecutionResult.Success).typedValue as SanskritValue.Sankhya).value)
            assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        }
    }
    @Test
    fun `natural membership uses locative collection in compiled and interpreted execution`() {
        val source = """
            एक + अम् त्रि + शस् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            यदि एक + सुँ सूची + ङि असँ + लट् + तिप् तर्हि सत्य + सुँ अन्यथा असत्य + सुँ
                ततः फल + अम् उपस्थित + ङि स्था + णिच् + लोट् + सिप् ।
            यदि शून्य + सुँ सूची + ङि असँ + लट् + तिप् तर्हि सत्य + सुँ अन्यथा असत्य + सुँ ।
        """.trimIndent()
        val compiled = compileAndInspect(source, "CompiledNaturalMembership")
        assertEquals(SanskritValue.Satya(true), compiled.values.getValue("उपस्थित"))
        assertEquals(SanskritValue.Satya(false), compiled.values.getValue("LastResult"))
        val interpreted = PaniniVM().evalScript(source)
        assertTrue(interpreted.none { it is ExecutionResult.Failure }, interpreted.toString())
        assertEquals(SanskritValue.Satya(false), (interpreted.last() as ExecutionResult.Success).typedValue)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `locative ordinal lowers to a direct one based collection index`() {
        val source = """
            एक + अम् द्वि + अम् त्रि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            सूची + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()
        val compiled = compileAndInspect(source, "CompiledOrdinalCollectionIndex")
        assertEquals(2L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `bare numeric conditional values remain typed compiler constants`() {
        val source =
            "यदि एक + अम् एक + अम् च विद् + लोट् + सिप् " +
                "तर्हि द्वि अन्यथा त्रि ।"

        val compiled = compileAndInspect(source, "CompiledBareNumericConditional")

        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural indexed retrieval lowers source and position to index IR`() {
        val source = """
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रम + ङि नि + क्षिप् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रमाङ्क + ङि स्था + णिच् + लोट् + सिप् ।
            क्रम + ङसिँ क्रमाङ्क + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalIndexedRetrieval")

        assertEquals(2L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `existential clause branches directly on a named truth state`() {
        val source = """
            सत्य + अम् अवस्था + ङि स्था + णिच् + लोट् + सिप् ।
            यदि अवस्था + सुँ असँ + लट् + तिप्
                तर्हि सत्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा असत्य + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledExistentialTruthClause")

        assertEquals(true, (compiled.values.getValue("परिणाम") as SanskritValue.Satya).boolean)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural locative insertion appends through explicit compiler IR`() {
        val source = """
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            द्वि + अम् क्रम + ङि नि + क्षिप् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् नवीनक्रम + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalLocativeInsertion")
        val result = requireNotNull(compiled.values["नवीनक्रम"]) { compiled.values.toString() } as SanskritValue.Suchi

        assertEquals(listOf(1L, 2L), result.items.map { (it as SanskritValue.Sankhya).value })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `natural range choice excludes values in ablative collection`() {
        val source = """
            एक + ङसिँ द्वि + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् क्षिप् + घञ् + ङस् फल + अम् क्रम + ङे ।
            क्रम + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप् ततः चयन + ङे दा + लोट् + सिप् ।
        """.trimIndent()

        val compiled = compileAndInspect(source, "CompiledNaturalExcludedChoice")

        assertEquals(2L, (compiled.values.getValue("चयन") as SanskritValue.Sankhya).value)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `compiled execution preserves interpreter failure details`() {
        val source = collectionProgram("सूची + अम् चतुर् + टा स्था + लोट् + सिप्")
        val interpreted = PaniniVM().evalScript(source).filterIsInstance<ExecutionResult.Failure>().last()
        val bytes = BytecodeCompiler.compile(source, "CompiledInvalidListIndex")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? = object : MethodVisitor(ASM9) {
                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                            name?.let(runtimeCalls::add)
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledInvalidListIndex", bytes)

        val invocation = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        val compiled = invocation.targetException as CompiledPaniniExecutionException

        assertTrue("storeValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue(runtimeCalls.none { it.startsWith("evaluate") }, runtimeCalls.toString())
        assertEquals(interpreted.error, compiled.error)
        assertEquals(interpreted.message, compiled.message)
        assertEquals(interpreted.trace, compiled.trace)
    }

    @Test
    fun `state backed for each accumulation executes directly`() {
        val source = collectionProgram("सूची + अम् यु + टा दशन् + ङे अनु + वृत् + लोट् + सिप्")
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListForEach")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(16L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(3, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `higher order list operations execute directly`() {
        val cases = listOf(
            CollectionResultCase(
                name = "Map",
                operation = "सूची + अम् एधँ + टा सम् + यु + लोट् + सिप्",
                expected = SanskritValue.Suchi(
                    listOf(
                        SanskritValue.Sankhya(2L, "द्वे"),
                        SanskritValue.Sankhya(4L, "चत्वारि"),
                        SanskritValue.Sankhya(6L, "षट्"),
                    ),
                ),
            ),
            CollectionResultCase(
                name = "Filter",
                operation = "सूची + अम् यु + टा वि + वृज् + लोट् + सिप्",
                expected = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(2L, "द्वि"))),
            ),
            CollectionResultCase(
                name = "Fold",
                operation = "सूची + अम् यु + टा शून्य + ङे सम् + क्षिप् + लोट् + सिप्",
                expected = SanskritValue.Sankhya(6L, "षट्"),
            ),
        )

        cases.forEach { case ->
            val source = collectionProgram(case.operation)
            val interpretedResults = PaniniVM().evalScript(source)
            assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
            val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
            val compiled = compileAndInspect(source, "CompiledDirectList${case.name}")

            assertEquals(interpreted, compiled.values.getValue("LastResult"), case.name)
            assertEquals(case.expected, compiled.values.getValue("LastResult"), case.name)
            assertTrue("evaluate" !in compiled.runtimeCalls, "$case: ${compiled.runtimeCalls}")
        }
    }

    @Test
    fun `nested stored lists flatten directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च प्रथमा + ङे दा + लोट् + सिप् ।
            त्रि + अम् चतुर् + अम् च द्वितीया + ङे दा + लोट् + सिप् ।
            प्रथमा + अम् द्वितीया + अम् च संयुक्ता + ङे दा + लोट् + सिप् ।
            संयुक्ता + अम् तन् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListFlatten")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        val nested = compiled.values.getValue("संयुक्ता") as SanskritValue.Suchi
        assertTrue(nested.items.all { it is SanskritValue.Suchi }, nested.toString())
        val flattened = compiled.values.getValue("LastResult") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L, 4L), flattened.items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(7, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list transformations execute directly`() {
        val cases = listOf(
            CollectionCase(
                name = "Reverse",
                operation = "सूची + अम् प्रति + वृत् + लोट् + सिप्",
                expected = listOf(3L, 2L, 1L),
            ),
            CollectionCase(
                name = "Append",
                operation = "सूची + अम् चतुर् + अम् च क्षिप् + लोट् + सिप्",
                expected = listOf(1L, 2L, 3L, 4L),
            ),
            CollectionCase(
                name = "Slice",
                operation = "सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप्",
                expected = listOf(2L, 3L),
            ),
        )

        cases.forEach { case ->
            val source = collectionProgram(case.operation)
            val interpretedResults = PaniniVM().evalScript(source)
            assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
            val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
            val compiled = compileAndInspect(source, "CompiledDirectList${case.name}")

            assertEquals(interpreted, compiled.values.getValue("LastResult"), case.name)
            val result = compiled.values.getValue("LastResult") as SanskritValue.Suchi
            assertEquals(case.expected, result.items.map { (it as SanskritValue.Sankhya).value }, case.name)
            assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" }, case.name)
            assertTrue("evaluate" !in compiled.runtimeCalls, "$case: ${compiled.runtimeCalls}")
        }
    }

    @Test
    fun `state backed list pop executes directly`() {
        val source = collectionProgram("सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप्")
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListPop")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(listOf(1L, 2L, 3L), (compiled.values.getValue("सूची") as SanskritValue.Suchi).items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `two stored lists concatenate directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् पूर्वसूची + ङि स्था + णिच् + लोट् + सिप् ।
            त्रि + अम् चतुर् + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् उत्तरसूची + ङि स्था + णिच् + लोट् + सिप् ।
            पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListConcat")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        val result = compiled.values.getValue("LastResult") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L, 4L), result.items.map { (it as SanskritValue.Sankhya).value })
        assertTrue("executeDirectValue" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `constructed list and length execute directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् त्रि + अम् च सूची + ङे दा + लोट् + सिप् ।
            सूची + अम् गण् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListLength")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(3L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        val list = compiled.values.getValue("सूची") as SanskritValue.Suchi
        assertEquals(listOf(1L, 2L, 3L), list.items.map { (it as SanskritValue.Sankhya).value })
        assertEquals(3, compiled.runtimeCalls.count { it == "storeValue" })
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list indexing executes directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            दशन् + अम् विंशति + अम् त्रिंशत् + अम् च सूची + ङे दा + लोट् + सिप् ।
            सूची + अम् द्वि + टा स्था + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListIndex")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(20L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(1, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list indexing accepts a numeric index stored under a symbolic name`() {
        val source = """
            दशन् + अम् विंशति + अम् त्रिंशत् + अम् च सूची + ङे दा + लोट् + सिप् ।
            द्वि + अम् क्रमाङ्क + ङे दा + लोट् + सिप् ।
            सूची + अम् क्रमाङ्क + टा स्था + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectNamedListIndex")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(20L, (compiled.values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2, compiled.runtimeCalls.count { it == "executeDirectValue" })
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `state backed list containment executes directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् त्रि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            यदि द्वि + सुँ सूची + ङि असँ + लट् + तिप्
                तर्हि सत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा असत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledDirectListContains")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(true, (compiled.values.getValue("LastResult") as SanskritValue.Satya).boolean)
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `implicit tatah print consumes the direct numeric result`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectTatahPrint")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectTatahPrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals("पञ्च", values.getValue("LastResult").toDisplayText())
        assertEquals(0, runtimeCalls.count { it == "executeDirectValue" }, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
    }

    @Test
    fun `print leaf loads previously compiled state directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            सप्त + अम् अवस्था + ङे दा + लोट् + सिप् ।
            अवस्था + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStatePrint")
        val runtimeCalls = mutableListOf<String>()
        var referencesState = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitLdcInsn(value: Any?) {
                            if (value == "अवस्था") referencesState = true
                        }

                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStatePrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(7L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertEquals("सप्त", values.getValue("LastResult").toDisplayText())
        assertTrue("storeValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue("executeDirectValue" in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
        assertTrue(referencesState)
    }

    @Test
    fun `simple print leaf executes directly without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            पञ्च + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectPrint")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectPrint", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertTrue("executeDirectValue" !in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
    }

    @Test
    fun `tatah numeric pipeline stores its result without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectTatahPipeline")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectTatahPipeline", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertEquals(5L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `explicit phala stores the previous direct result without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।
            फल + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectPhalaStore")
        val executeCalls = mutableListOf<String>()
        var referencesLastResult = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitLdcInsn(value: Any?) {
                            if (value == "LastResult") referencesLastResult = true
                        }

                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectPhalaStore", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertEquals(5L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateAndStore" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `fixed repetition loads compiled state directly on every iteration`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् अवस्था + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStateRepeat")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStateRepeat", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(3L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "storeValue" } == 3, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 1, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `simple loop result target executes without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ततः परिणाम + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectLoopTarget")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectLoopTarget", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa

        assertEquals(outcome, values.getValue("LastResult"))
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue("executeDirectLoopTarget" !in executeCalls, executeCalls.toString())
        assertTrue("executeDirectValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluateLoopTarget" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded direct state loop lowers its exhaustion leaf directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            एक + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectExhaustion")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectExhaustion", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(1L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "storeValue" } == 11, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 2, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `bounded loop reads a negated named truth state directly`() {
        val source = """
            शून्य + अम् एक + अम् च विद् + लोट् + सिप् ।
            फल + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + सुँ न भू + लट् + तिप् तावत् एक + अम् मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val compiled = compileAndInspect(source, "CompiledNegatedNamedTruthLoop")

        assertEquals(interpreted, compiled.values.getValue("LastResult"))
        assertEquals(false, (compiled.values.getValue("अवस्था") as SanskritValue.Satya).boolean)
        val outcome = compiled.values.getValue("LastResult") as SanskritValue.Rupa
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(3L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue("evaluateBoolean" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
        assertTrue("evaluate" !in compiled.runtimeCalls, compiled.runtimeCalls.toString())
    }

    @Test
    fun `bounded state loop loads and stores directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectStateLoop")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectStateLoop", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(0L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(1L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
        assertTrue(executeCalls.count { it == "storeValue" } == 10, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `assigned state feeds a directly compiled final conditional`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            यदि अवस्था + अम् एक + अम् च विद् + लोट् + सिप् तर्हि द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् अन्यथा शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledStateConditional")
        val executeCalls = mutableListOf<String>()
        var createsReference = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                            if (owner == "dev/panini/compiler/PaniniRuntime" &&
                                name == "createReferenceExpression"
                            ) {
                                createsReference = true
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledStateConditional", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(5L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertEquals(2L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue(!createsReference)
    }

    @Test
    fun `straight line assignment is loaded directly by a later numeric leaf`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            अवस्था + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledDirectLoad")
        val executeCalls = mutableListOf<String>()
        var createsReference = false
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                            if (owner == "dev/panini/compiler/PaniniRuntime" &&
                                name == "createReferenceExpression"
                            ) {
                                createsReference = true
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledDirectLoad", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(3L, (values.getValue("अवस्था") as SanskritValue.Sankhya).value)
        assertEquals(5L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue(executeCalls.count { it == "executeDirectValue" } == 1, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateAndStore" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `terminal literal assignment stores compiled state directly`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledTerminalStore")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledTerminalStore", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals(interpreted, values.getValue("अवस्था"))
        assertTrue("storeValue" in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `literal numeric condition branches without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् तर्हि द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् अन्यथा शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledLiteralCondition")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledLiteralCondition", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(5L, (compiled as SanskritValue.Sankhya).value)
        assertTrue("executeDirectBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluateBoolean" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `pure numeric leaves execute directly without the interpreter bridge`() {
        val source = """
            परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            दशन् + शस् त्रि + शस् च वि + युज् + णिच् + लोट् + सिप् ।
            त्रि + शस् द्वि + औट् च गण् + णिच् + लोट् + सिप् ।
            दशन् + शस् द्वि + औट् च हृ + लोट् + सिप् ।
            दशन् + शस् त्रि + शस् च शिष् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(interpretedResults.none { it is ExecutionResult.Failure }, interpretedResults.toString())
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledNumericLeaves")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledNumericLeaves", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(1L, (compiled as SanskritValue.Sankhya).value)
        assertEquals(0, executeCalls.count { it == "executeDirectValue" }, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `compiled parameter frames preserve structured values`() {
        val runtime = CompiledProgramRuntime()
        val structuredOutcome = SanskritValue.Rupa(
            "परिणाम",
            mapOf(
                "अवस्था" to SanskritValue.Shabda("विजय"),
                "प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(3L, "त्रीणि"),
            ),
        )
        runtime.storeValue("LastResult", structuredOutcome)
        runtime.enterFrame(arrayOf("मान"), arrayOf(structuredOutcome))

        val returned = runtime.resolveValue("मान")

        runtime.exitFrame()
        val structured = returned as SanskritValue.Rupa
        assertEquals("परिणाम", structured.schema)
        assertEquals("विजय", structured.fields.getValue("अवस्था").toDisplayText())
        assertEquals(3L, (structured.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `compiled runtime exposes only the unified action value entry point`() {
        val methods = CompiledProgramRuntime::class.java.declaredMethods.map { it.name }.toSet()

        assertTrue("executeDirectValue" in methods)
        assertTrue("executeDirect" !in methods)
        assertTrue("executeDirectBoolean" !in methods)
        assertTrue("executeDirectStore" !in methods)
        assertTrue("executeDirectLoopTarget" !in methods)
        assertTrue("publishLoopOutcome" !in methods)
    }

    @Test
    fun `large grammatical loop bounds use explicit numeric value comparison`() {
        val source = """
            शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
            कोटि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledLargeLoopBound")
        val instructions = mutableListOf<Int>()
        val valueCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitInsn(opcode: Int) {
                        instructions += opcode
                    }

                    override fun visitVarInsn(opcode: Int, varIndex: Int) {
                        instructions += opcode
                    }

                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompilerValueOperations") {
                            name?.let(valueCalls::add)
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledLargeLoopBound", bytes)
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = result.getValue("परिणाम") as SanskritValue.Rupa

        assertTrue(ALOAD in instructions)
        assertTrue("lessThan" in valueCalls, valueCalls.toString())
        assertEquals(0L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `nested compiled loops preserve interpreter state parity`() {
        val source = """
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अन्तरावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अन्तरावस्था + ङे ॥

            क्षि + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            बाह्यावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् बाह्यावस्था + ङे ॥

            अन्तरचक्र + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वि + अम् अन्तरावस्था + ङे दा + लोट् + सिप् ।
            यावत् अन्तरावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् ॥

            बाह्यचक्र + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अन्तरचक्र + टा कृ + लोट् + सिप् ।
            क्षि + ल्युट् + टा कृ + लोट् + सिप् ॥

            द्वि + अम् बाह्यावस्था + ङे दा + लोट् + सिप् ।
            यावत् बाह्यावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् बाह्यचक्र + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् बाह्यावस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledNestedLoops")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `recursive generated samjna calls unwind and resume top-level execution`() {
        val source = """
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ॥

            गण् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            यदि अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा वि + स्था + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् अवस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledRecursiveCountdown")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `compiled named calls enforce signatures and prohibitions`() {
        val wrongType = """
            गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            राम + अम् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val prohibited = """
            विभाज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भज् + णिच् + लोट् + सिप् ॥
            दश + अम् शून्य + अम् च विभाज् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val typeFailure = assertFailsWith<InvocationTargetException> {
            compileAndInspect(wrongType, "RejectedCompiledType")
        }
        val prohibitionFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(prohibited, "RejectedCompiledProhibition")
        }

        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            (typeFailure.cause as CompiledPaniniExecutionException).error)
        assertTrue(prohibitionFailure.message.orEmpty().contains("निषेध-प्रतिषेध"))
    }

    @Test
    fun `phala controlled loops have interpreter compiler parity`() {
        fun execute(source: String, className: String): Pair<SanskritValue, SanskritValue> {
            val interpreted = PaniniVM().evalScript(source)
                .filterIsInstance<ExecutionResult.Success>().last().typedValue!!
            val generated = BytecodeCompiler.compileAndLoad(source, className)
            @Suppress("UNCHECKED_CAST")
            val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
                .getValue("LastResult")
            return interpreted to compiled
        }

        val exhausted = """
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val victory = """
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val exhaustedResults = execute(exhausted, "CompiledPhalaExhaustion")
        val victoryResults = execute(victory, "CompiledPhalaVictory")
        assertEquals(exhaustedResults.first, exhaustedResults.second)
        assertEquals(victoryResults.first, victoryResults.second)
        assertEquals(
            "समाप्ति",
            (exhaustedResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
        assertEquals(
            "विजय",
            (victoryResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
    }

    @Test
    fun `bounded compiled loop publishes and pipes its exhaustion outcome`() {
        val source = """
            हृ + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            द्वि + कृत्वसुच् यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledLoopOutcome")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(2L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `break signal exits the nearest compiled repetition`() {
        val source = """
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥

            पञ्च + कृत्वसुच् प्रयत्न + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>()
            .single { it.controlSignal != null }
            .typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledBreakRepetition")
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitMethodInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?,
                        isInterface: Boolean,
                    ) {
                        if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                            name?.let(runtimeCalls::add)
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledBreakRepetition", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertTrue("requestBreak" in runtimeCalls, runtimeCalls.toString())
        assertTrue("evaluate" !in runtimeCalls, runtimeCalls.toString())
    }

    @Test
    fun `compiled non-halting loop obeys an explicit host budget`() {
        val source = """
            एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
            यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledBudgetedLoop")

        val thrown = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute", java.lang.Long.TYPE).invoke(null, 3L)
        }

        val limit = thrown.targetException as CompiledExecutionLimitExceededException
        assertTrue(limit.message.orEmpty().contains("3 iterations"))
    }

    @Test
    fun `parameterized named operation has interpreter compiler parity`() {
        val source = """
            व्यवकलन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङस् द्वि + अम् वाम + ङस् पञ्च + अम् व्यवकलन + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledParameterizedSamjna")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(3L, (compiled as SanskritValue.Sankhya).value)
    }

    @Test
    fun `pipeline result enters a typed generated samjna operation`() {
        val source = """
            वर्धन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ततः वर्धन + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(
            interpretedResults.none { it is ExecutionResult.Failure },
            interpretedResults.joinToString(),
        )
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val bytes = BytecodeCompiler.compile(source, "CompiledPipedSamjna")
        val executeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(executeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledPipedSamjna", bytes)
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(4L, (compiled as SanskritValue.Sankhya).value)
        assertTrue("executeDirectValue" !in executeCalls, executeCalls.toString())
        assertTrue("evaluate" !in executeCalls, executeCalls.toString())
    }

    @Test
    fun `two-counter proof compiles to JVM branches and executes`() {
        val source = File("examples/control_flow/two_counter_machine.pvm").readText()
        val bytes = BytecodeCompiler.compile(source, "CompiledTwoCounterMachine")
        val jumps = mutableListOf<Int>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitJumpInsn(opcode: Int, label: Label?) {
                        jumps += opcode
                    }
                }
            },
            0,
        )

        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledTwoCounterMachine", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertTrue(IFEQ in jumps, "Conditional and loop exits must use JVM conditional branches.")
        assertTrue(GOTO in jumps, "The unbounded loop must contain a JVM backward branch.")
        assertEquals("त्रीणि", values.getValue("LastResult").toDisplayText())
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(7L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    private fun compileAndInspect(source: String, className: String): CompiledExecution {
        val bytes = BytecodeCompiler.compile(source, className)
        val runtimeCalls = mutableListOf<String>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor? {
                    if (name != "execute" || descriptor != "()Ljava/util/Map;") return null
                    return object : MethodVisitor(ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int,
                            owner: String?,
                            name: String?,
                            descriptor: String?,
                            isInterface: Boolean,
                        ) {
                            if (owner == "dev/panini/compiler/CompiledProgramRuntime") {
                                name?.let(runtimeCalls::add)
                            }
                        }
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(className, bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        return CompiledExecution(values, runtimeCalls)
    }

    private data class CompiledExecution(
        val values: Map<String, SanskritValue>,
        val runtimeCalls: List<String>,
    )

    private fun collectionProgram(operation: String): String = """
        परिचय + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
        एक + अम् मुद्र् + लोट् + सिप् ॥

        एक + अम् द्वि + अम् त्रि + अम् च सूची + ङे दा + लोट् + सिप् ।
        $operation ।
    """.trimIndent()

    private data class CollectionCase(
        val name: String,
        val operation: String,
        val expected: List<Long>,
    )

    private data class CollectionResultCase(
        val name: String,
        val operation: String,
        val expected: SanskritValue,
    )
}
