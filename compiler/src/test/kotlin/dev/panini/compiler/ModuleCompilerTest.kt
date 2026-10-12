package dev.panini.compiler

import dev.panini.execution.PrakriyaParameter
import dev.panini.execution.PrakriyaValueType

import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFails

class ModuleCompilerTest {
    @Test fun `inheritance linking rejects conflicts and cycles across imported metadata`() {
        fun dependency(name: String, child: String, parent: String) = PaniniModuleMetadata(
            moduleName = name, className = name, procedures = emptyList(),
            inheritance = mapOf(child to parent),
        )
        val source = listOf(PaniniModuleSource("main.pvm", ""))
        val first = dependency("first", "गाणित", "गणित")
        val conflict = assertFailsWith<IllegalArgumentException> {
            PaniniModuleDescriptor("conflict", source, listOf(first, dependency("second", "गाणित", "भाषा")))
        }
        assertTrue(conflict.message.orEmpty().contains("Conflicting parent domains"), conflict.message)
        val cycle = assertFailsWith<IllegalArgumentException> {
            PaniniModuleDescriptor("cycle", source, listOf(first, dependency("second", "गणित", "गाणित")))
        }
        assertTrue(cycle.message.orEmpty().contains("Cyclic domain inheritance"), cycle.message)
        PaniniModuleDescriptor("consistent", source, listOf(first, dependency("same", "गाणित", "गणित")))
        val localConflict = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileModule(PaniniModuleDescriptor("local_inheritance_conflict",
                listOf(PaniniModuleSource("main.pvm", "गणित + अण् + सुँ इति अधिकार + सुँ ।")),
                listOf(dependency("imported", "गाणित", "भाषा"))), "LocalInheritanceConflict")
        }
        assertTrue(localConflict.message.orEmpty().contains("Conflicting parent domains"), localConflict.message)
    }

    @Test fun `compiler rejects repeated fields in a written schema`() {
        val source = "मूल्य + सुँ मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"
        val failure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileModule(PaniniModuleDescriptor("duplicate_fields",
                listOf(PaniniModuleSource("main.pvm", source))), "DuplicateFields")
        }
        assertTrue(failure.message.orEmpty().contains("Duplicate fields in Panini schema"), failure.message)
    }

    @Test fun `compiler rejects nondeclarative field clauses without dropping participants`() {
        val source = "मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"
        val assertion = "अवस्था + मतुप् + ङस् मूल्य + सुँ द्वि + सुँ असँ + लट् + तिप् ।"
        for ((invalid, message) in listOf(
            source.replace("लट्", "लृट्") to "present copula",
            source.replace("असँ", "द्वि + अम् असँ") to "extra participants",
            assertion.replace("असँ", "न असँ") to "cannot discard",
            assertion.replace("असँ", "त्रि + अम् असँ") to "cannot discard",
            assertion.replace("लट्", "लृट्") to "present copula",
        )) {
            val failure = assertFailsWith<IllegalArgumentException> {
                BytecodeCompiler.compileModule(PaniniModuleDescriptor("invalid_fields",
                    listOf(PaniniModuleSource("main.pvm", invalid))), "InvalidFields")
            }
            assertTrue(failure.message.orEmpty().contains(message), failure.message)
        }
    }

    @Test fun `metadata preserves contracts before converting declarations to maps`() {
        fun encoded(value: String) = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString(value.toByteArray(Charsets.UTF_8))
        val metadata = PaniniModuleMetadata(moduleName = "records", className = "Records", procedures = emptyList(),
            schemas = mapOf("अवस्था" to listOf("मूल्य", "फल")))
        val source = PaniniModuleMetadataCodec.encode(metadata)
        val schemaLine = source.lineSequence().single { it.startsWith("schema|") }
        assertEquals(metadata, PaniniModuleMetadataCodec.decode(source + schemaLine + "\n"))
        val conflict = "schema|${encoded("अवस्था")}|${encoded("अन्य")}\n"
        assertTrue(assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadataCodec.decode(source + conflict)
        }.message.orEmpty().contains("Conflicting Panini schema"))
        assertTrue(assertFailsWith<IllegalArgumentException> {
            metadata.copy(schemas = mapOf("अवस्था" to listOf("मूल्य", "मूल्य")))
        }.message.orEmpty().contains("Duplicate fields"))
        val duplicateFields = "schema|${encoded("अन्य")}|${encoded("मूल्य")},${encoded("मूल्य")}\n"
        assertTrue(assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadataCodec.decode(source + duplicateFields)
        }.message.orEmpty().contains("Duplicate fields"))
        val parents = "inheritance|${encoded("बाल")}|${encoded("प्रथम")}\n" +
            "inheritance|${encoded("बाल")}|${encoded("द्वितीय")}\n"
        assertTrue(assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadataCodec.decode(source + parents)
        }.message.orEmpty().contains("Conflicting parent domains"))
    }

    @Test fun `imported signatures reject duplicate parameter identities`() {
        val parameter = PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)
        val procedure = PaniniExportedProcedure("परीक्षण", "test", null,
            listOf(parameter), PrakriyaValueType.SANKHYA, null)
        assertTrue(assertFailsWith<IllegalArgumentException> {
            procedure.copy(parameters = listOf(parameter, parameter.copy(type = PrakriyaValueType.SHABDA)))
        }.message.orEmpty().contains("declared more than once"))
        val metadata = PaniniModuleMetadata(moduleName = "parameters", className = "Parameters",
            procedures = listOf(procedure))
        val source = PaniniModuleMetadataCodec.encode(metadata)
        val malformed = source.lineSequence().joinToString("\n") { line ->
            if (!line.startsWith("procedure|")) line else {
                val parts = line.split('|').toMutableList()
                parts[4] = "${parts[4]},${parts[4]}"
                parts.joinToString("|")
            }
        }
        assertTrue(assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadataCodec.decode(malformed)
        }.message.orEmpty().contains("declared more than once"))
    }

    @Test fun `schema linking rejects conflicting fields rather than overwriting a contract`() {
        fun dependency(name: String, fields: List<String>) = PaniniModuleMetadata(
            moduleName = name, className = name, procedures = emptyList(),
            schemas = mapOf("अवस्था" to fields),
        )
        val first = dependency("first", listOf("मूल्य"))
        val conflict = dependency("second", listOf("फल"))
        val failure = assertFailsWith<IllegalArgumentException> {
            PaniniModuleDescriptor("conflicting_schemas", listOf(PaniniModuleSource("main.pvm", "")),
                dependencies = listOf(first, conflict))
        }
        assertTrue(failure.message.orEmpty().contains("Conflicting Panini schema"), failure.message)
        PaniniModuleDescriptor("shared_schema", listOf(PaniniModuleSource("main.pvm", "")),
            dependencies = listOf(first, dependency("same", listOf("मूल्य"))))
        val local = "फल + सुँ प्रयत्न + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"
        val localFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileModule(PaniniModuleDescriptor("local_conflict",
                listOf(PaniniModuleSource("main.pvm", local)), dependencies = listOf(first)), "LocalSchemaConflict")
        }
        assertTrue(localFailure.message.orEmpty().contains("Conflicting Panini schema"), localFailure.message)
    }

    @Test fun `distinct typed overload contracts can be imported from separate libraries`() {
        fun library(type: String, module: String, className: String, body: String) =
            BytecodeCompiler.compileModule(PaniniModuleDescriptor(module, listOf(PaniniModuleSource("library.pvm", """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ $type + सुँ इति मान + सुँ ।
                $body ॥
            """.trimIndent(), false))), className)
        val numeric = library("सङ्ख्या", "numeric_library", "NumericOverloadLibrary",
            "मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप्")
        val text = library("शब्द", "text_library", "TextOverloadLibrary",
            "मान + अम् मुद्र् + णिच् + लोट् + सिप्")
        val application = BytecodeCompiler.compileModule(PaniniModuleDescriptor("overload_application",
            listOf(PaniniModuleSource("main.pvm", "द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")),
            dependencies = listOf(numeric.metadata, text.metadata)), "ImportedOverloadApplication")
        val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
        loader.loadFromBytes(numeric.metadata.className, numeric.bytecode)
        loader.loadFromBytes(text.metadata.className, text.bytecode)
        val generated = loader.loadFromBytes(application.metadata.className, application.bytecode)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(3L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        val ambiguity = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compileModule(PaniniModuleDescriptor("unknown_overload_application",
                listOf(PaniniModuleSource("main.pvm", "सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")),
                dependencies = listOf(numeric.metadata, text.metadata)), "UnknownImportedOverload")
        }
        assertTrue(ambiguity.message.orEmpty().contains("Ambiguous"), ambiguity.message)
    }

    @Test fun `same imported procedure name in separate domains resolves to its qualified library`() {
        fun library(domain: String, module: String, className: String, operands: String) =
            BytecodeCompiler.compileModule(PaniniModuleDescriptor(module, listOf(PaniniModuleSource("library.pvm", """
                $domain + सुँ इति अधिकार + सुँ ।
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                $operands युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(), false))), className)
        val first = library("गणित", "math_domain", "MathDomainLibrary", "एक + अम् द्वि + औट् च")
        val second = library("भाषा", "language_domain", "LanguageDomainLibrary", "त्रि + शस् चतुर् + शस् च")
        for ((index, specification) in listOf("गणित" to 3L, "भाषा" to 7L).withIndex()) {
            val (domain, expected) = specification
            val application = BytecodeCompiler.compileModule(PaniniModuleDescriptor("domain_app$index",
                listOf(PaniniModuleSource("main.pvm", "$domain + ङस् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")),
                dependencies = listOf(first.metadata, second.metadata)), "DomainApplication$index")
            val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            loader.loadFromBytes(first.metadata.className, first.bytecode)
            loader.loadFromBytes(second.metadata.className, second.bytecode)
            val generated = loader.loadFromBytes(application.metadata.className, application.bytecode)
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            assertEquals(expected, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        }
    }

    @Test fun `separately compiled library preserves exception precedence through metadata`() {
        val library = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ इति अपवाद + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            त्रि + शस् चतुर् + शस् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val artifact = BytecodeCompiler.compileModule(PaniniModuleDescriptor("precedence_library",
            listOf(PaniniModuleSource("library.pvm", library, false))), "PrecedenceLibrary")
        val metadata = PaniniModuleMetadataCodec.decode(PaniniModuleMetadataCodec.encode(artifact.metadata))
        assertEquals(artifact.metadata, metadata)
        assertTrue(metadata.procedures.any { it.precedence == dev.panini.vyakaranam.ast.PrakriyaPrecedence.APAVADA })
        val application = BytecodeCompiler.compileModule(PaniniModuleDescriptor("precedence_application",
            listOf(PaniniModuleSource("main.pvm", "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")),
            dependencies = listOf(metadata)), "PrecedenceApplication")
        val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
        loader.loadFromBytes(artifact.metadata.className, artifact.bytecode)
        val generated = loader.loadFromBytes(application.metadata.className, application.bytecode)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(7L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadataCodec.decode("format=1\nmodule=old\nclass=old")
        }
    }

    @Test fun `public wrapper retains library source scope when calling its private helper`() {
        val library = """
            रहस्य + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            रहस्य + टा डुकृञ् + उ + लोट् + सिप् ॥
        """.trimIndent()
        val artifact = BytecodeCompiler.compileModule(PaniniModuleDescriptor("private_helper", listOf(
            PaniniModuleSource("library.pvm", library, false),
            PaniniModuleSource("main.pvm", "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।", true),
        )), "PrivateHelperModule")
        assertEquals(listOf("परीक्षण"), artifact.metadata.procedures.map { it.symbol })
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(artifact.metadata.className, artifact.bytecode)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(3L, (values.getValue("LastResult") as SanskritValue.Sankhya).value)
        assertFails {
            BytecodeCompiler.compileModule(PaniniModuleDescriptor("inaccessible_helper", listOf(
                PaniniModuleSource("library.pvm", library, false),
                PaniniModuleSource("main.pvm", "रहस्य + टा डुकृञ् + उ + लोट् + सिप् ।", true),
            )), "InaccessiblePrivateHelper")
        }
    }

    @Test
    fun `module procedure domains follow preceding adhikara in each source unit`() {
        val source = """
            पूर्व + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + सुँ इति अधिकार + सुँ ।
            मध्य + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            भाषा + सुँ इति अधिकार + सुँ ।
            उत्तर + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + ङस् प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
        """.trimIndent()
        val analyzed = PaniniModuleAnalyzer.analyze(PaniniModuleDescriptor("scopes",
            listOf(PaniniModuleSource("first.pvm", source, false),
                PaniniModuleSource("second.pvm", "स्वतन्त्र + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।", false))))
        assertEquals(mapOf("पूर्व" to null, "मध्य" to "गणित", "उत्तर" to "भाषा",
            "प्रयत्न" to "गणित", "स्वतन्त्र" to null), analyzed.procedures.associate { it.localSymbol to it.domain })
    }

    @Test
    fun `compiled multifile modules match interpreted project results`() {
        listOf(
        "projects/list_operations/samavaya_mukhya.pvm",
        "projects/multifile/mukhya.pvm",
        "projects/paninian_morphology/morph_mukhya.pvm",
        ).forEachIndexed { index, entryPath ->
            val entry = File(entryPath)
            val libraries = entry.parentFile.walkTopDown().filter { file ->
                file.isFile && file.extension == "pvm" && file != entry
            }.sortedBy(File::getName).toList()
            val descriptor = PaniniModuleDescriptor(
                "parity_$index",
                libraries.map { PaniniModuleSource(it.name, it.readText(), false) } +
                    PaniniModuleSource(entry.name, entry.readText(), true),
            )
            val artifact = BytecodeCompiler.compileModule(descriptor, "CompiledParity$index")
            val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
                .loadFromBytes(artifact.metadata.className, artifact.bytecode)
            @Suppress("UNCHECKED_CAST")
            val compiled = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
            val interpreted = PaniniVM().evalProject(entry).last { it is dev.panini.execution.ExecutionResult.Success }
                as dev.panini.execution.ExecutionResult.Success
            assertEquals(requireNotNull(interpreted.typedValue).toDisplayText(), compiled.getValue("LastResult").toDisplayText(), entryPath)
        }
    }

    @Test
    fun `metadata round trips and dependency procedures execute without source`() {
        val library = PaniniModuleDescriptor(
            "ganita_library",
                listOf(PaniniModuleSource("ganita.pvm", File("projects/multifile/ganita.pvm").readText(), false)),
        )
        val libraryArtifact = BytecodeCompiler.compileModule(library, "CompiledGanitaLibrary")
        val metadata = PaniniModuleMetadataCodec.decode(PaniniModuleMetadataCodec.encode(libraryArtifact.metadata))
        assertEquals(libraryArtifact.metadata, metadata)
        assertTrue(metadata.procedures.isNotEmpty())

        val application = PaniniModuleDescriptor(
            "ganita_application",
                listOf(PaniniModuleSource("mukhya.pvm", File("projects/multifile/mukhya.pvm").readText(), true)),
            dependencies = listOf(metadata),
        )
        val applicationArtifact = BytecodeCompiler.compileModule(application, "CompiledGanitaApplication")
        val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
        loader.loadFromBytes(libraryArtifact.metadata.className, libraryArtifact.bytecode)
        val generated = loader.loadFromBytes(applicationArtifact.metadata.className, applicationArtifact.bytecode)
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(15L, (result.getValue("LastResult") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `module discovery honors entries and emits class plus metadata`() {
        val sourceDir = createTempDirectory("panini-module").toFile()
        File(sourceDir, "panini.module").writeText(
            "name=sample\nsources=library.pvm,main.pvm\nentries=main.pvm\n",
        )
        File(sourceDir, "library.pvm").writeText("# library only")
        File(sourceDir, "main.pvm").writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
        val descriptor = PaniniModuleDescriptor.discover(sourceDir)
        assertEquals(listOf(false, true), descriptor.sources.map(PaniniModuleSource::isEntryPoint))
        val outputDir = createTempDirectory("panini-output").toFile()
        val artifact = BytecodeCompiler.compileModuleDirectory(sourceDir, outputDir)
        assertTrue(File(outputDir, "${artifact.metadata.className}.class").isFile)
        assertTrue(File(outputDir, "sample.pvmmeta").isFile)
    }

    @Test
    fun `internal symbols stay private and duplicate declarations fail analysis`() {
        val internalSource = """
            रहस्य + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val internalArtifact = BytecodeCompiler.compileModule(
            PaniniModuleDescriptor("private", listOf(PaniniModuleSource("private.pvm", internalSource, false))),
            "CompiledPrivateModule",
        )
        assertTrue(internalArtifact.metadata.procedures.isEmpty())

        val declaration = """
            योग + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val error = assertFailsWith<IllegalStateException> {
            PaniniModuleAnalyzer.analyze(
                PaniniModuleDescriptor(
                    "duplicate",
                    listOf(
                        PaniniModuleSource("a.pvm", declaration, false),
                        PaniniModuleSource("b.pvm", declaration, false),
                    ),
                ),
            )
        }
        assertTrue(error.message.orEmpty().contains("Duplicate Panini procedure"))
    }

    @Test
    fun `generated execute overload accepts typed initial state`() {
        val source = "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledInitialState")
        val initial = mapOf("LastResult" to SanskritValue.Sankhya(8, "अष्ट"))
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<String, SanskritValue>
        assertNotNull(result["LastResult"])
        assertEquals("अष्ट", result.getValue("LastResult").toDisplayText())
    }

    @Test
    fun `metadata validation rejects incompatible and ambiguous dependencies`() {
        assertFailsWith<IllegalArgumentException> {
            PaniniModuleMetadata(
                formatVersion = PaniniModuleMetadata.CURRENT_FORMAT_VERSION + 1,
                moduleName = "future",
                className = "FutureProgram",
                procedures = emptyList(),
            )
        }
        val procedure = PaniniExportedProcedure("योग", "samjna_yoga", null, emptyList(), null, null)
        val first = PaniniModuleMetadata(moduleName = "first", className = "First", procedures = listOf(procedure))
        val second = PaniniModuleMetadata(moduleName = "second", className = "Second", procedures = listOf(procedure))
        assertFailsWith<IllegalStateException> {
            PaniniModuleDescriptor(
                "ambiguous",
                listOf(PaniniModuleSource("main.pvm", "# empty")),
                dependencies = listOf(first, second),
            )
        }
    }

    @Test
    fun `initial value kinds participate in IR verification`() {
        val program = CompilerProgram(
            className = "TypedInitialProgram",
            entryPoint = listOf(
                CompilerInstruction.Load("आरम्भ"),
                CompilerInstruction.Constant(SanskritValue.Sankhya(2, "द्वि")),
                CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                CompilerInstruction.Store("LastResult"),
            ),
            initialValueKinds = mapOf("आरम्भ" to CompilerValueKind.NUMBER),
        )
        CompilerProgramVerifier.verify(program)
        assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(program.copy(initialValueKinds = mapOf("आरम्भ" to CompilerValueKind.TEXT)))
        }
    }
}
