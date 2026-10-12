package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ProcedureResultSchemaTest {
    @Test fun `early procedure break cannot bypass declared schema`() {
        val source = """
            अवस्था + सुँ मान + सुँ च फलरूप + ङस् क्षेत्र + जस् असँ + लट् + झि ।
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फलरूप + सुँ इति परिणाम + सुँ ।
            वि + स्था + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "EarlySchemaContract")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    private val fields = listOf("अवस्था", "मान")

    private fun execute(result: SanskritValue): Map<String, SanskritValue> {
        val program = CompilerProgram("SchemaContract", listOf(
            CompilerInstruction.EnterFrame(emptyList()),
            CompilerInstruction.InvokeProcedure("procedure", 0, CompilerValueKind.RECORD),
            CompilerInstruction.ExitFrame,
        ), listOf(CompilerProcedure("procedure", listOf(
            CompilerInstruction.Constant(result),
            CompilerInstruction.Store("LastResult"),
            CompilerInstruction.CheckProcedureResultSchema("फलरूप", fields),
            CompilerInstruction.Return,
        ), returnKind = CompilerValueKind.RECORD)))
        CompilerProgramVerifier.verify(program)
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(program.className, CompilerProgramJvmEmitter.emit(program))
        @Suppress("UNCHECKED_CAST")
        return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }

    @Test fun `schema identity and exact field set are required`() {
        val values = fields.associateWith { SanskritValue.Shabda("value") }
        val valid = SanskritValue.Rupa("फलरूप", values)
        assertEquals(valid, execute(valid)["LastResult"])
        for (invalid in listOf(
            SanskritValue.Rupa("अन्य", values),
            SanskritValue.Rupa("फलरूप", values - "मान"),
            SanskritValue.Rupa("फलरूप", values + ("अन्य" to SanskritValue.Shabda("extra"))),
            SanskritValue.Shabda("not a record"),
        )) {
            val failure = assertFailsWith<InvocationTargetException> { execute(invalid) }
            assertIs<CompiledPaniniExecutionException>(failure.cause)
        }
        // Schema field order is not a record-identity condition.
        execute(SanskritValue.Rupa("फलरूप", values.entries.reversed().associate { it.toPair() }))
    }

    @Test fun `frontend lowers declared schema and rejects scalar return in both backends`() {
        val source = """
            अवस्था + सुँ मान + सुँ च फलरूप + ङस् क्षेत्र + जस् असँ + लट् + झि ।
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फलरूप + सुँ इति परिणाम + सुँ ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val ir = CompilerFrontend.lower(source, "SourceSchemaContract")
        assertEquals(CompilerInstruction.CheckProcedureResultSchema("फलरूप", fields),
            ir.procedures.single().instructions.filterIsInstance<CompilerInstruction.CheckProcedureResultSchema>().single())
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "SourceSchemaContract")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    @Test fun `local result contract can use an imported schema`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फलरूप + सुँ इति परिणाम + सुँ ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val imported = PaniniModuleMetadata(moduleName = "schemas", className = "Schemas",
            procedures = emptyList(), schemas = mapOf("फलरूप" to fields))
        val ir = CompilerFrontend.lowerModule(PaniniModuleDescriptor("consumer",
            listOf(PaniniModuleSource("consumer.pvm", source, false)), dependencies = listOf(imported)),
            "ImportedSchemaConsumer")
        assertEquals(CompilerInstruction.CheckProcedureResultSchema("फलरूप", fields),
            ir.procedures.single().instructions.filterIsInstance<CompilerInstruction.CheckProcedureResultSchema>().single())
        assertFailsWith<IllegalArgumentException> {
            CompilerFrontend.lower(source, "MissingSchema")
        }
    }

    @Test fun `derived result noun retains schema identity through compiler lowering`() {
        val source = """
            अवस्था + सुँ मान + सुँ च शब्द + त्व + ङस् क्षेत्र + जस् असँ + लट् + झि ।
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            शब्द + त्व + सुँ इति परिणाम + सुँ ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val ir = CompilerFrontend.lower(source, "DerivedResultSchema")
        val procedure = ir.procedures.single()
        assertEquals(CompilerValueKind.RECORD, procedure.returnKind)
        assertEquals(CompilerInstruction.CheckProcedureResultSchema("शब्द + त्व", fields),
            procedure.instructions.filterIsInstance<CompilerInstruction.CheckProcedureResultSchema>().single())
        val artifact = BytecodeCompiler.compileModule(PaniniModuleDescriptor("derived",
            listOf(PaniniModuleSource("derived.pvm", source, false))), "DerivedSchemaLibrary")
        val metadata = PaniniModuleMetadataCodec.decode(PaniniModuleMetadataCodec.encode(artifact.metadata))
        assertEquals("शब्द + त्व", metadata.procedures.single().resultSchema)
        assertEquals(fields, metadata.schemas["शब्द + त्व"])
        assertNull(metadata.schemas["शब्द"])
    }
}
