package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmFileTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM
    private val sadhaka = PvmUktiSadhaka()

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_pvm_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PvmUktiSadhaka automatically generates conjugated surface text files for all pvm scripts in examples directory`() {
        val examplesDir = File("examples")
        val pvmFiles = examplesDir.walkTopDown().filter { it.extension == "pvm" }.toList()
        assertTrue(pvmFiles.isNotEmpty(), "PVM example files should exist")

        pvmFiles.forEach { pvmFile ->
            val txtFile = File(pvmFile.parentFile, pvmFile.nameWithoutExtension + ".txt")
            val conjugatedText = sadhaka.sadhayaScript(pvmFile.readText())
            txtFile.writeText(conjugatedText + "\n")
            assertTrue(txtFile.exists(), "Text file for ${pvmFile.name} should exist")
            assertTrue(txtFile.readText().isNotBlank(), "Text file for ${pvmFile.name} should not be blank")
        }
    }

    @Test
    fun `PaniniVM evaluates addition pvm file containing complex arithmetic utterances`() {
        val pvmFile = File("examples/arithmetic/addition.pvm")
        val txtFile = File("examples/arithmetic/addition.txt")

        assertTrue(pvmFile.exists(), "addition.pvm should exist")
        assertTrue(txtFile.exists(), "addition.txt should exist")

        val results = vm.evalFile(pvmFile, sessionKey = "addition_session")

        assertEquals(6, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("त्रीणि", line1Result.value)

        val line2Result = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("त्रीणि", line2Result.value)

        val line3Result = assertIs<ExecutionResult.Success>(results[2])
        assertEquals("सप्त", line3Result.value)

        val line4Result = assertIs<ExecutionResult.Success>(results[3])
        assertEquals("रामावतार", line4Result.value)

        val line5Result = assertIs<ExecutionResult.Success>(results[4])
        assertEquals("द्वे", line5Result.value)

        val line6Result = assertIs<ExecutionResult.Success>(results[5])
        assertEquals("पञ्च", line6Result.value)
    }

    @Test
    fun `PaniniVM evaluates comparison and min pvm files`() {
        val compFile = File("examples/arithmetic/comparison.pvm")
        val compResults = vm.evalFile(compFile, sessionKey = "comp_session")
        assertEquals(1, compResults.size)
        val compSuccess = assertIs<ExecutionResult.Success>(compResults[0])
        assertEquals("सत्यम्", compSuccess.value)

        val minFile = File("examples/arithmetic/min.pvm")
        val minResults = vm.evalFile(minFile, sessionKey = "min_session")
        assertEquals(1, minResults.size)
        val minSuccess = assertIs<ExecutionResult.Success>(minResults[0])
        assertEquals("त्रीणि", minSuccess.value)
    }

    @Test
    fun `PaniniVM evaluates sandhi and subanta pvm files`() {
        val pvmFile = File("examples/linguistic/sandhi.pvm")
        val txtFile = File("examples/linguistic/sandhi.txt")

        assertTrue(pvmFile.exists(), "sandhi.pvm should exist")
        assertTrue(txtFile.exists(), "sandhi.txt should exist")

        val results = vm.evalFile(pvmFile, sessionKey = "sandhi_session")

        assertEquals(3, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("रामावतार", line1Result.value)

        val line2Result = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("देवालय", line2Result.value)

        val line3Result = assertIs<ExecutionResult.Success>(results[2])
        assertEquals("तच्छिव", line3Result.value)

        val subFile = File("examples/linguistic/subanta.pvm")
        val subResults = vm.evalFile(subFile, sessionKey = "sub_session")
        assertEquals(1, subResults.size)
        val subSuccess = assertIs<ExecutionResult.Success>(subResults[0])
        assertEquals("रामः", subSuccess.value)
    }

    @Test
    fun `PaniniVM evaluates conditional pvm file containing control flow utterances`() {
        val pvmFile = File("examples/control_flow/conditional.pvm")
        val txtFile = File("examples/control_flow/conditional.txt")

        assertTrue(pvmFile.exists(), "conditional.pvm should exist")
        assertTrue(txtFile.exists(), "txtFile should exist")

        val results = vm.evalFile(pvmFile, sessionKey = "conditional_session")

        assertEquals(1, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("सप्त", line1Result.value)
    }

    @Test
    fun `PaniniVM evaluates emit_demo pvm file containing io emit utterances`() {
        val pvmFile = File("examples/io/emit_demo.pvm")
        val txtFile = File("examples/io/emit_demo.txt")

        assertTrue(pvmFile.exists(), "emit_demo.pvm should exist")
        assertTrue(txtFile.exists(), "emit_demo.txt should exist")

        val results = vm.evalFile(pvmFile, sessionKey = "emit_session")

        assertEquals(1, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("अर्पितम्: हविस्", line1Result.value)
    }

    @Test
    fun `PaniniVM evaluates resource_demo pvm file containing resource consume and release utterances`() {
        val pvmFile = File("examples/resource/resource_demo.pvm")
        val txtFile = File("examples/resource/resource_demo.txt")

        assertTrue(pvmFile.exists(), "resource_demo.pvm should exist")
        assertTrue(txtFile.exists(), "resource_demo.txt should exist")

        val results = vm.evalFile(pvmFile, sessionKey = "resource_session")

        assertEquals(2, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("भक्षणम् सम्पन्नम्: अन्न", line1Result.value)

        val line2Result = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("पानम् सम्पन्नम्: जल", line2Result.value)
    }

    @Test
    fun `PaniniVM evaluates external_demo pvm file`() {
        val extFile = File("examples/external/external_demo.pvm")
        val extResults = vm.evalFile(extFile, sessionKey = "ext_session")
        assertEquals(1, extResults.size)
        val extSuccess = assertIs<ExecutionResult.Success>(extResults[0])
        assertEquals("Simulated dispatch for effect NETWORK with payload 'वार्ता'", extSuccess.value)
    }
}
