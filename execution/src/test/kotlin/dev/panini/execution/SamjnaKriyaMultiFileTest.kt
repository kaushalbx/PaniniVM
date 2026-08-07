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
}
