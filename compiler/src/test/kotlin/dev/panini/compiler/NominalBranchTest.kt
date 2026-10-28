package dev.panini.compiler

import dev.panini.execution.*
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class NominalBranchTest {
    @Test fun `bare primitive numeral branches infer grammatical nominative endings`() {
        val source = "$condition द्वि अन्यथा त्रि ।"
        val conditional = assertIs<Conditional>(PaniniParser().parse(source).body)
        val first = assertIs<SubantaPada>(assertIs<Invocation>(conditional.consequent).implicitValuePada)
        val second = assertIs<SubantaPada>(assertIs<Invocation>(conditional.alternate).implicitValuePada)
        assertEquals("औ", first.sup.text)
        assertEquals("जस्", second.sup.text)
        val rendered = PvmUktiSadhaka().sadhayaLine(source)
        assertFalse("+" in rendered, rendered)
    }

    @Test fun `explicit wrong cardinal number is rejected by both value paths`() {
        for (nominal in listOf("द्वि + सुँ", "त्रि + औ")) {
            val source = "$condition $nominal अन्यथा असत्य + सुँ ।"
            assertFailsWith<IllegalArgumentException> { PaniniVM().eval(source) }
            assertFailsWith<IllegalArgumentException> { compiled(source, "InvalidCardinalNominal") }
        }
    }
    @Test fun `inflected numeral branches retain scalar numbers regardless of vacana`() {
        for ((nominal, expected) in listOf("द्वि + औ" to 2L, "त्रि + जस्" to 3L)) {
            val source = "$condition $nominal अन्यथा असत्य + सुँ ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().eval(source))
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(
                compiled(source, "NumeralNominalBranch$expected")["LastResult"]).value)
        }
    }
    @Test fun `oblique single nominal is not silently made a nominative result`() {
        for (ending in listOf("अम्", "टा", "ङे", "ङस्", "ङि")) {
            assertFailsWith<IllegalArgumentException> {
                PaniniParser().parse("$condition विजय + $ending अन्यथा पराजय ।")
            }
        }
    }

    @Test fun `derived nominal result never aliases an underived base binding`() {
        val source = "नव + अम् विजय + ङे दा + लोट् + सिप् ।\n$condition विजय + मतुप् + सुँ अन्यथा पराजय ।"
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val value = assertIs<ExecutionResult.Success>(results.last()).typedValue
        assertIs<SanskritValue.Shabda>(value)
        assertEquals(value, compiled(source, "DerivedNominalBranch")["LastResult"])
    }
    private val condition = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि"

    private fun compiled(source: String, name: String): Map<String, SanskritValue> {
        val type = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        return type.getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }

    @Test fun `nominal branch retains its grammatical sentence without a synthetic verb`() {
        val node = assertIs<Conditional>(PaniniParser().parse("$condition विजय अन्यथा पराजय ।").body)
        val branch = assertIs<Invocation>(node.consequent)
        assertIs<NamaVakya>(branch.vakya)
        assertTrue(branch.vakya.padas.none { it is TingantaPada })
        assertFalse("दा" in branch.sourceText)
        val ir = CompilerFrontend.lower("$condition विजय अन्यथा पराजय ।", "NominalHistoryIr")
        assertTrue(ir.entryPoint.filterIsInstance<CompilerInstruction.RecordActionFrame>().isEmpty())
    }

    @Test fun `nominal branch resolves a typed named list without invoking dana`() {
        val source = "एक + सुँ द्वि + औ च + इति सूची + सुँ असँ + लट् + तिप् ।\n$condition सूची + सुँ अन्यथा असत्य + सुँ ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val list = assertIs<SanskritValue.Suchi>(result.typedValue)
        assertEquals(2, list.items.size)
        assertEquals("pvm.nominal-result", result.operation)
        assertEquals(list, compiled(source, "NamedNominalBranch")["LastResult"])
    }

    @Test fun `direct utterance nominal branch pipes its typed result`() {
        val source = "$condition सत्य + सुँ अन्यथा असत्य + सुँ ततः मुद्र् + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().eval(source))
        assertEquals("सत्यम्", result.value)
        assertEquals(OutputKind.CONSOLE, result.outputKind)
        assertEquals(result.value, compiled(source, "PipedNominalBranch").getValue("LastResult").toDisplayText())
    }

    @Test fun `nominal procedure branch retains a typed argument reference`() {
        val source = """
            चयन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि मान + सुँ अन्यथा असत्य + सुँ ॥
            एक + सुँ द्वि + औ च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + अम् चयन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val list = assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.last()).typedValue)
        assertEquals(list, compiled(source, "ProcedureNominalBranch")["LastResult"])
    }
}
