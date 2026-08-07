package dev.panini.execution

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SamjnaKriyaMultiFileTest {

    @Test
    fun `test samjna kriya parsing and execution across multi-file project`() {
        val vm = PaniniVM()
        val entryFile = File("examples/multifile/mukhya.pvm")
        
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
        val samjna = parsed.first() as PvmScriptStatement.SamjnaDefinition
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
        val samjna = parsed.first() as PvmScriptStatement.SamjnaDefinition
        assertEquals("युज् + ल्युट् + सुँ", samjna.nameSegmented)
        assertEquals(2, samjna.body.size)
        assertEquals("एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।", samjna.body[0].text)
        assertEquals("युज् + घञ् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥", samjna.body[1].text)
    }

    @Test
    fun `test samjna registry stem extraction and apavada overrides`() {
        val registry = SamjnaKriyaRegistry()

        val utsargaKriya = SamjnaKriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = SamjnaKriyaRegistry.stripSupSuffix("युज् + ल्युट् + सुँ"),
            body = listOf(PvmScriptStatement.Sentence("युज् + णिच् + लोट् + सिप् ॥")),
            sourceFile = "ganita.pvm",
            isApavada = false,
        )
        registry.register(utsargaKriya)

        assertEquals("युज् + ल्युट्", utsargaKriya.nameStem)
        assertEquals("ganita.pvm", registry.resolve("युज् + ल्युट्")?.sourceFile)

        // Apavāda (entry-point override)
        val apavadaKriya = SamjnaKriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = SamjnaKriyaRegistry.stripSupSuffix("युज् + ल्युट् + सुँ"),
            body = listOf(PvmScriptStatement.Sentence("एक + अम् युज् + णिच् + लोट् + सिप् ॥")),
            sourceFile = "mukhya.pvm",
            isApavada = true,
        )
        registry.register(apavadaKriya)

        assertEquals("mukhya.pvm", registry.resolve("युज् + ल्युट्")?.sourceFile, "Apavāda override must replace Ut-sarga definition.")
    }

    @Test
    fun `test samjna invocation detection`() {
        val registry = SamjnaKriyaRegistry()
        registry.register(
            SamjnaKriya(
                nameSegmented = "युज् + ल्युट् + सुँ",
                nameStem = "युज् + ल्युट्",
                body = listOf(PvmScriptStatement.Sentence("युज् + णिच् + लोट् + सिप् ॥")),
            ),
        )

        val invocation = registry.detectInvocation("एक + अम् द्वि + अम् च युज् + ल्युट् + टा कृ + लोट् + सिप् ।")
        assertNotNull(invocation, "Instrumental case with कृ must be detected as saṃjñā invocation.")
        assertEquals("एक + अम् द्वि + अम् च", invocation.karmaText)
        assertEquals("युज् + ल्युट् + सुँ", invocation.kriya.nameSegmented)
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
        val samjnaDef = parsed.first() as PvmScriptStatement.SamjnaDefinition
        val kriya = SamjnaKriya(
            nameSegmented = samjnaDef.nameSegmented,
            nameStem = SamjnaKriyaRegistry.stripSupSuffix(samjnaDef.nameSegmented),
            body = samjnaDef.body,
        )

        assertEquals(1, kriya.nishedhaGuards.size, "Should identify 1 prohibition rule (निषेध-सूत्र).")
        assertEquals("न द्वितीय + अम् शून्य + अम् ।", kriya.nishedhaGuards.first().text)
        assertEquals(1, kriya.vidhiSentences.size, "Should identify 1 mandate rule (विधि-सूत्र).")
    }

    @Test
    fun `test nishedha sutra prohibition guard enforcement on zero operand`() {
        val vm = PaniniVM()
        val registry = SamjnaKriyaRegistry()
        val script = """
            विभाज् + ल्युट् + सुँ ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.SamjnaDefinition
        registry.register(
            SamjnaKriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = SamjnaKriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        // Test with zero operand (should trigger Niṣedha prohibition)
        val invocationText = "दस + अम् शून्य + अम् च विभाज् + ल्युट् + टा कृ + लोट् + सिप् ।"
        val invocation = registry.detectInvocation(invocationText)
        assertNotNull(invocation)

        val results = vm.evalScript(invocationText, samjnaRegistry = registry)
        val failure = results.filterIsInstance<ExecutionResult.Failure>().firstOrNull()
        assertNotNull(failure, "Evaluation with zero argument must trigger Niṣedha failure.")
        assertTrue(failure.message.contains("निषेध-प्रतिषेधः"), "Failure message must reference Niṣedha prohibition.")
    }

    @Test
    fun `test samavaya list batch fold addition in samjna`() {
        val vm = PaniniVM()
        val registry = SamjnaKriyaRegistry()
        val script = """
            समवाय + ल्युट् + सुँ ।
            समवाय + अम् युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.SamjnaDefinition
        registry.register(
            SamjnaKriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = SamjnaKriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        val invocationText = "एक + अम् द्वि + अम् त्रि + अम् चतुर् + अम् पञ्च + अम् च समवाय + ल्युट् + टा कृ + लोट् + सिप् ।"
        val results = vm.evalScript(invocationText, samjnaRegistry = registry)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty())
        assertEquals("पञ्चदश", successful.last().value, "Sum of 1 + 2 + 3 + 4 + 5 in saṃjñā list fold should be पञ्चदश (15).")
    }

    @Test
    fun `test samavaya project multi-file execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("examples/list_operations/samavaya_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "List operations project execution should succeed.")
        assertEquals("पञ्चदश", successful.last().value, "Sum of 1..5 in samavaya project should be पञ्चदश (15).")
    }

    @Test
    fun `test samjna execution scope isolation child environment`() {
        val vm = PaniniVM()
        val registry = SamjnaKriyaRegistry()
        val script = """
            गुणप्रक्रिया + ल्युट् + सुँ ।
            द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script).first() as PvmScriptStatement.SamjnaDefinition
        registry.register(
            SamjnaKriya(
                nameSegmented = parsed.nameSegmented,
                nameStem = SamjnaKriyaRegistry.stripSupSuffix(parsed.nameSegmented),
                body = parsed.body,
            ),
        )

        val callerScope = ExecutionScope(environment = ValueEnvironment(mapOf("मुख्यस्थ" to dev.panini.execution.SanskritValue.of("सौम्य"))))
        val results = vm.evalScript("गुणप्रक्रिया + ल्युट् + टा कृ + लोट् + सिप् ।", scope = callerScope, samjnaRegistry = registry)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty())
        assertEquals("पञ्च", successful.last().value)
        assertEquals("सौम्य", callerScope.environment.values["मुख्यस्थ"]?.toDisplayText(), "Parent scope environment variables must remain unpolluted.")
    }

    @Test
    fun `test scope isolation project multi-file execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("examples/scope_isolation/isolation_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Scope isolation project execution should succeed.")
        assertEquals("पञ्चत्रिंशत्", successful.last().value, "Result of (3 + 4) * 5 should be पञ्चत्रिंशत् (35).")
    }

    @Test
    fun `test antaranga internal samjna parsing and visibility`() {
        val script = """
            अन्तरङ्गा द्विगुणन + ल्युट् + सुँ ।
            प्रथम + अम् द्वि + अम् च गण + णिच् + लोट् + सिप् ॥

            जटिलगणित + ल्युट् + सुँ ।
            प्रथम + अम् द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ॥
        """.trimIndent()

        val parsed = PvmScript.parse(script)
        assertEquals(2, parsed.size)

        val internalDef = parsed[0] as PvmScriptStatement.SamjnaDefinition
        assertTrue(internalDef.isInternal, "Header with अन्तरङ्गा prefix must set isInternal = true.")
        assertEquals("द्विगुणन + ल्युट् + सुँ", internalDef.nameSegmented)

        val publicDef = parsed[1] as PvmScriptStatement.SamjnaDefinition
        assertTrue(!publicDef.isInternal, "Standard saṃjñā header must set isInternal = false.")
    }

    @Test
    fun `test antaranga internal samjna project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("examples/private_scope/private_mukhya.pvm")

        val results = vm.evalProject(entryFile)
        val successful = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successful.isNotEmpty(), "Private scope project execution should succeed.")
        assertEquals("दश", successful.last().value, "Result of (3 * 2) + 4 via private helper should be दश (10).")
    }

    @Test
    fun `test adhikara domain genitive project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("examples/adhikara_domain/mukhya.pvm")

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

        val ktaDef = parsed.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().firstOrNull()
        assertNotNull(ktaDef)
        val kriya = SamjnaKriya(
            nameSegmented = ktaDef.nameSegmented,
            nameStem = SamjnaKriyaRegistry.stripSupSuffix(ktaDef.nameSegmented),
            body = ktaDef.body,
        )
        assertTrue(kriya.isMemoized, "Saṃjñā with क्त pratyaya must have isMemoized = true.")
    }

    @Test
    fun `test paninian morphology project execution from disk`() {
        val vm = PaniniVM()
        val entryFile = File("examples/paninian_morphology/morph_mukhya.pvm")

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
}
