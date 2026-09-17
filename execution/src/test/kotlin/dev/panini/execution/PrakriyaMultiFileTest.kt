package dev.panini.execution

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PrakriyaMultiFileTest {

    @Test
    fun `named operations can recursively dispatch a two-counter machine`() {
        val script = """
            वृध् + ल्युट् + सुँ ।
            योग + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् योग + ङे ।
            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ॥

            हृ + ल्युट् + सुँ ।
            सङ्ख्या + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् सङ्ख्या + ङे ।
            एक + अम् अवस्था + ङे दा + लोट् + सिप् ॥

            स्था + ल्युट् + सुँ ।
            शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ॥

            चर् + ल्युट् + सुँ ।
            यदि सङ्ख्या + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा स्था + ल्युट् + टा कृ + लोट् + सिप् ॥

            दिश् + ल्युट् + सुँ ।
            यदि अवस्था + अम् एक + अम् च विद् + लोट् + सिप् तर्हि चर् + ल्युट् + टा कृ + लोट् + सिप् अन्यथा वृध् + ल्युट् + टा कृ + लोट् + सिप् ॥

            द्वि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            त्रि + अम् सङ्ख्या + ङे दा + लोट् + सिप् ।
            शून्य + अम् योग + ङे दा + लोट् + सिप् ।
            यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् न तावत् दिश् + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् योग + अम् ।
        """.trimIndent()

        val results = PaniniVM().evalScript(script, sessionKey = "two-counter-dispatch")
        val invalid = results.filterNot { it is ExecutionResult.Success }

        assertTrue(invalid.isEmpty(), results.toString())
        assertEquals(
            "त्रीणि",
            results.filterIsInstance<ExecutionResult.Success>().last().value,
            results.toString(),
        )
    }

    @Test
    fun `frequency qualifier repeats a samjna kriya body`() {
        val script = """
            प्रयत्न + ल्युट् + सुँ ।
            आवृत्ति + अम् मुद्र् + णिच् + लोट् + सिप् ॥

            पञ्च + कृत्वसुच् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val successes = PaniniVM().evalScript(script).filterIsInstance<ExecutionResult.Success>()

        assertEquals(5, successes.count { it.value == "आवृत्ति" })
    }

    @Test
    fun `vi stha terminates the nearest samjna repetition`() {
        val script = """
            प्रयत्न + ल्युट् + सुँ ।
            वि + स्था + लोट् + सिप् ॥

            पञ्च + कृत्वसुच् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = PaniniVM().evalScript(script)
        val breaks = results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.controlSignal == ExecutionControlSignal.BREAK_LOOP }

        assertEquals(1, breaks.size, results.toString())
    }

    @Test
    fun `test samjna kriya parsing and execution across multi-file project`() {
        val vm = PaniniVM()
        val entryFile = File("projects/multifile/mukhya.pvm")
        
        val results = vm.evalProject(entryFile)
        
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Project execution should yield successful results.")
        val values = successful.map { it.value }.filter { it.isNotBlank() }
        assertTrue(values.contains("विंशतिः"), "Calculation of (2 + 3) * 4 should produce विंशतिः (20).")
        assertTrue(values.contains("पञ्चदश"), "Batch list sum of (1 + 2 + 3 + 4 + 5) via समवाययोजनम् should produce पञ्चदश (15).")
        assertEquals("पञ्चदश", values.last(), "Final printed result of multi-file project should be पञ्चदश (15).")
    }

    @Test
    fun `test pure paninian samjna header parsing with double danda on last sentence`() {
        val script = """
            युज् + ल्युट् + सुँ ।
            युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        assertEquals(1, parsed.size)
        val samjna = parsed.first() as PvmScriptStatement.PrakriyaDefinition
        assertEquals("युज् + ल्युट् + सुँ", samjna.nameSegmented)
        assertEquals(1, samjna.body.size)
        assertEquals("युज् + णिच् + लोट् + सिप् ॥", samjna.body.first().text)
    }

    @Test
    fun `test multi-sentence samjna definition block parsing`() {
        val script = """
            युज् + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
            युज् + घञ् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        assertEquals(1, parsed.size)
        val samjna = parsed.first() as PvmScriptStatement.PrakriyaDefinition
        assertEquals("युज् + ल्युट् + सुँ", samjna.nameSegmented)
        assertEquals(2, samjna.body.size)
        assertEquals("एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।", samjna.body[0].text)
        assertEquals("युज् + घञ् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥", samjna.body[1].text)
    }

    @Test
    fun `test samjna registry stem extraction and apavada overrides`() {
        val registry = PrakriyaRegistry()

        val utsargaKriya = Prakriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = PrakriyaRegistry.stripSupSuffix("युज् + ल्युट् + सुँ"),
            body = listOf(PvmScriptStatement.Sentence("युज् + णिच् + लोट् + सिप् ॥")),
            sourceFile = "ganita.pvm",
            precedence = PrakriyaPrecedence.DEFAULT,
        )
        registry.register(utsargaKriya)

        assertEquals("युज् + ल्युट्", utsargaKriya.nameStem)
        assertEquals("ganita.pvm", registry.resolve("युज् + ल्युट्")?.sourceFile)

        // Apavāda (entry-point override)
        val apavadaKriya = Prakriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = PrakriyaRegistry.stripSupSuffix("युज् + ल्युट् + सुँ"),
            body = listOf(PvmScriptStatement.Sentence("एक + अम् युज् + णिच् + लोट् + सिप् ॥")),
            sourceFile = "mukhya.pvm",
            precedence = PrakriyaPrecedence.APAVADA,
        )
        registry.register(apavadaKriya)

        assertEquals("mukhya.pvm", registry.resolve("युज् + ल्युट्")?.sourceFile, "Apavāda override must replace Ut-sarga definition.")
    }

    @Test
    fun `test samjna invocation detection`() {
        val registry = PrakriyaRegistry()
        registry.register(
            Prakriya(
                nameSegmented = "युज् + ल्युट् + सुँ",
                nameStem = "युज् + ल्युट्",
                body = listOf(PvmScriptStatement.Sentence("युज् + णिच् + लोट् + सिप् ॥")),
            ),
        )

        val invocation = registry.detectInvocation(
            PaniniParser().parse("एक + अम् द्वि + अम् च युज् + ल्युट् + टा कृ + लोट् + सिप् ।"),
        )
        assertNotNull(invocation, "Instrumental case with कृ must be detected as saṃjñā invocation.")
        assertEquals("एक + अम् द्वि + अम् च", invocation.karmaText)
        assertEquals("युज् + ल्युट् + सुँ", invocation.kriya.nameSegmented)
        assertEquals(listOf("एक", "द्वि"), invocation.arguments.map { it.term })
        assertTrue(invocation.arguments.all { it.pada != null })
        assertTrue(invocation.arguments.all { it.origin == PrakriyaArgumentOrigin.WRITTEN })
    }

    @Test
    fun `AST invocation detection preserves the semantic pipe operand position`() {
        val registry = PrakriyaRegistry()
        registry.register(
            Prakriya(
                nameSegmented = "युज् + ल्युट् + सुँ",
                nameStem = "युज् + ल्युट्",
                body = listOf(PvmScriptStatement.Sentence("युज् + णिच् + लोट् + सिप् ॥")),
            ),
        )
        val ukti = PaniniParser().parse("द्वि + अम् युज् + ल्युट् + टा कृ + लोट् + सिप् ।")

        val preDetected = registry.detectInvocation(ukti, injectedKarman = "विशेषणफल" to null)
        assertNotNull(preDetected)
        assertEquals(listOf(null, null), preDetected.argumentValues)

        val operand = SanskritValue.Sankhya(3, "त्रि")
        val detected = registry.detectInvocation(ukti, injectedKarman = "विशेषणफल" to operand)
        assertNotNull(detected)
        assertEquals("विशेषणफल + अम् द्वि + अम्", detected.karmaText)
        assertEquals(listOf(operand, null), detected.argumentValues)
        assertEquals(listOf(PrakriyaArgumentOrigin.PIPE, PrakriyaArgumentOrigin.WRITTEN), detected.arguments.map { it.origin })
        assertEquals(operand, detected.arguments.first().value)
        assertEquals("द्वि", detected.arguments.last().term)
    }

    @Test
    fun `AST invocation matcher extracts operation domain and karma roles`() {
        val source = "पञ्च + अम् गणित + ङस् युज् + ल्युट् + टा कृ + लोट् + सिप् ।"
        val shape = PrakriyaInvocationMatcher.match(PaniniParser().parse(source), setOf("युज् + ल्युट्"))

        assertNotNull(shape)
        assertEquals("युज् + ल्युट्", shape.operationStem)
        assertEquals("गणित", shape.domainStem)
        assertEquals("पञ्च + अम्", shape.karmaText)

        val taddhita = PrakriyaInvocationMatcher.match(
            PaniniParser().parse("पञ्च + अम् गुण + वत् + ङस् वर्द्धन + ल्युट् + टा कृ + लोट् + सिप् ।"),
            setOf("वर्द्धन + ल्युट्"),
        )
        assertNotNull(taddhita)
        assertEquals("गुण", taddhita.domainStem)
    }

    @Test
    fun `test nishedha sutra parsing and guard separation`() {
        val script = """
            विभाज् + ल्युट् + सुँ ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        assertEquals(1, parsed.size)
        val samjnaDef = parsed.first() as PvmScriptStatement.PrakriyaDefinition
        val kriya = Prakriya(
            nameSegmented = samjnaDef.nameSegmented,
            nameStem = PrakriyaRegistry.stripSupSuffix(samjnaDef.nameSegmented),
            body = samjnaDef.body,
        )

        assertEquals(1, kriya.nishedhaGuards.size, "Should identify 1 prohibition rule (निषेध-सूत्र).")
        assertEquals("न द्वितीय + अम् शून्य + अम् ।", kriya.nishedhaGuards.first().text)
        assertNotNull(kriya.nishedhaGuards.first().ukti, "Niṣedha classification must come from a parsed utterance.")
        assertEquals(1, kriya.vidhiSentences.size, "Should identify 1 mandate rule (विधि-सूत्र).")
    }

    @Test
    fun `test nishedha sutra prohibition guard enforcement on zero operand`() {
        val vm = PaniniVM()
        val registry = PrakriyaRegistry()
        val script = """
            विभाज् + ल्युट् + सुँ ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.PrakriyaDefinition
        registry.register(
            Prakriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = PrakriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        // Test with zero operand (should trigger Niṣedha prohibition)
        val invocationText = "दस + अम् शून्य + अम् च विभाज् + ल्युट् + टा कृ + लोट् + सिप् ।"
        val invocation = registry.detectInvocation(PaniniParser().parse(invocationText))
        assertNotNull(invocation)

        val results = vm.evalScript(invocationText, prakriyaRegistry = registry)
        val failure = results.filterIsInstance<ExecutionResult.Failure>().firstOrNull()
        assertNotNull(failure, "Evaluation with zero argument must trigger Niṣedha failure.")
        assertTrue(failure.message.contains("निषेध-प्रतिषेधः"), "Failure message must reference Niṣedha prohibition.")
    }

    @Test
    fun `test samavaya list batch fold addition in samjna`() {
        val vm = PaniniVM()
        val registry = PrakriyaRegistry()
        val script = """
            समवाय + ल्युट् + सुँ ।
            समवाय + अम् युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.PrakriyaDefinition
        registry.register(
            Prakriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = PrakriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        val invocationText = "एक + अम् द्वि + अम् त्रि + अम् चतुर् + अम् पञ्च + अम् च समवाय + ल्युट् + टा कृ + लोट् + सिप् ।"
        val results = vm.evalScript(invocationText, prakriyaRegistry = registry)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty())
        assertEquals("पञ्चदश", successful.last().value, "Sum of 1 + 2 + 3 + 4 + 5 in saṃjñā list fold should be पञ्चदश (15).")
    }

    @Test
    fun `test samavaya project multi-file execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("projects/list_operations/samavaya_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "List operations project execution should succeed.")
        assertEquals("पञ्चदश", successful.last().value, "Sum of 1..5 in samavaya project should be पञ्चदश (15).")
    }

    @Test
    fun `test samjna execution scope isolation child environment`() {
        val vm = PaniniVM()
        val registry = PrakriyaRegistry()
        val script = """
            गुणप्रक्रिया + ल्युट् + सुँ ।
            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.PrakriyaDefinition
        registry.register(
            Prakriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = PrakriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        val callerScope = ExecutionScope(environment = ValueEnvironment(mapOf("मुख्यस्थ" to dev.panini.execution.SanskritValue.of("सौम्य"))))
        val results = vm.evalScript("गुणप्रक्रिया + ल्युट् + टा कृ + लोट् + सिप् ।", scope = callerScope, prakriyaRegistry = registry)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty())
        assertEquals("पञ्च", successful.last().value)
        assertEquals("सौम्य", callerScope.environment.values["मुख्यस्थ"]?.toDisplayText(), "Parent scope environment variables must remain unpolluted.")
    }

    @Test
    fun `test scope isolation project multi-file execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("projects/scope_isolation/isolation_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Scope isolation project execution should succeed.")
        assertEquals("पञ्चत्रिंशत्", successful.last().value, "Result of (3 + 4) * 5 should be पञ्चत्रिंशत् (35).")
    }

    @Test
    fun `test antaranga internal prakriya parsing visibility and precedence`() {
        val header = "द्विगुणन + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।"
        val headerQualifiers = PrakriyaDefinitionMarkerParser.qualifiers(header)
        assertTrue(
            PrakriyaDefinitionQualifier.ANTARANGA in headerQualifiers?.qualifiers.orEmpty(),
            headerQualifiers.toString(),
        )
        val script = """
            $header
            प्रथम + अम् द्वि + अम् च गण + णिच् + लोट् + सिप् ॥

            जटिलगणित + ल्युट् + सुँ ।
            प्रथम + अम् द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        assertEquals(2, parsed.size)

        val internalDef = parsed[0] as PvmScriptStatement.PrakriyaDefinition
        assertTrue(internalDef.isInternal, "An अन्तरङ्गा प्रक्रिया declaration must set internal visibility.")
        assertTrue(internalDef.isAntaranga, "An अन्तरङ्गा प्रक्रिया must carry antaranga precedence.")
        assertEquals("द्विगुणन + ल्युट् + सुँ", internalDef.nameSegmented)

        val publicDef = parsed[1] as PvmScriptStatement.PrakriyaDefinition
        assertTrue(!publicDef.isInternal, "A standard prakriyā declaration must set isInternal = false.")

        assertTrue(
            PvmScript.parse("अन्तरङ्गा द्विगुणन + ल्युट् + सुँ ।").none {
                it is PvmScriptStatement.PrakriyaDefinition
            },
            "A bare अन्तरङ्गा prefix is not a grammatical prakriyā declaration.",
        )
    }

    @Test
    fun `internal prakriya is visible only to its source file`() {
        val registry = PrakriyaRegistry()
        registry.register(
            Prakriya(
                nameSegmented = "द्विगुणन + ल्युट् + सुँ",
                nameStem = "द्विगुणन + ल्युट्",
                body = listOf(PvmScriptStatement.Sentence("द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥")),
                sourceFile = "library.pvm",
                visibility = PrakriyaVisibility.INTERNAL,
            ),
        )
        val invocation = "द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ।"

        val ukti = PaniniParser().parse(invocation)
        assertNotNull(registry.detectInvocation(ukti, callerSourceFile = "library.pvm"))
        assertEquals(null, registry.detectInvocation(ukti, callerSourceFile = "main.pvm"))
        assertEquals(null, registry.detectInvocation(ukti))
    }

    @Test
    fun `test antaranga internal samjna project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("projects/private_scope/private_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Private scope project execution should succeed.")
        assertEquals("दश", successful.last().value, "Result of (3 * 2) + 4 via private helper should be दश (10). Results: $results")
    }

    @Test
    fun `test adhikara domain genitive project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("projects/adhikara_domain/mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Adhikāra domain project execution should succeed.")
        assertEquals("पञ्च", successful.last().value, "Result of 2 + 3 in गणित domain should be पञ्च (5).")
    }

    @Test
    fun `test adhikara sutra parsing with segmented sup suffix`() {
        val script = """
            गणित + सुँ इति अधिकार + सुँ ।

            योजन + ल्युट् + सुँ ।
            प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        val adhikara = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
        assertNotNull(adhikara, "Adhikāra sūtra with अधिकार + सुँ must be parsed.")
        assertEquals("गणित + सुँ", adhikara.domainSegmented)
    }

    @Test
    fun `test krt pratyaya memoization classification and morphological adhikara`() {
        val script = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            सिद्ध + क्त + सुँ ।
            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        val adhikara = parsed.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().firstOrNull()
        assertNotNull(adhikara, "Morphological Adhikāra header with अधि + कृ + घञ् + सुँ must be parsed.")
        assertEquals("गणित + सुँ", adhikara.domainSegmented)

        val ktaDef = parsed.filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().firstOrNull()
        assertNotNull(ktaDef)
        val kriya = Prakriya(
            nameSegmented = ktaDef.nameSegmented,
            nameStem = PrakriyaRegistry.stripSupSuffix(ktaDef.nameSegmented),
            body = ktaDef.body,
        )
        assertTrue(kriya.isMemoized, "Saṃjñā with क्त pratyaya must have isMemoized = true.")
    }

    @Test
    fun `test paninian morphology project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("projects/paninian_morphology/morph_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Paninian morphology project execution should succeed.")
        assertEquals("नव", successful.last().value, "Printed result of (4 + 5) via morph procedure should be नव (9).")
    }

    @Test
    fun `test taddhita struct matup creation and genitive attribute access`() {
        val vm = PaniniVM()
        val script = """
            दश + अम् मूल्य + अम् पञ्च + अम् परिमाण + अम् गुण + वत् + सुँ ।
            गुण + वत् + ङस् मूल्य + अम् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Taddhita struct evaluation should succeed.")
        assertEquals("दश", successful.last().value, "Accessing गुणवतः मूल्यम् should return दश (10).")
    }

    @Test
    fun `test taddhita struct member method definition and tritiya invocation`() {
        val vm = PaniniVM()
        val script = """
            गुण + वत् + ङस् वर्द्धन + ल्युट् + सुँ ।
            प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥

            पञ्च + अम् द्वि + अम् गुण + वत् + ङस् वर्द्धन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Taddhita struct method invocation should succeed.")
        assertEquals("सप्त", successful.last().value, "Calling गुणवतः वर्द्धनेन on 5 (पञ्च) and 2 (द्वि) should return 7 (सप्त).")
    }

    @Test
    fun `test taddhita subclass inheritance apatyadhikara and inherited method invocation`() {
        val vm = PaniniVM()
        val entryFile = File("projects/taddhita_inheritance/inheritance_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.any { it.value == "सप्त" }, "Calling inherited method वर्द्धनेन via child struct गाणितवत् should return 7 (सप्त). Results: $results")
    }
}
