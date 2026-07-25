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
    fun `PvmUktiSadhaka derives conjugated input sentences for addition pvm file using Paninian derivation engines`() {
        val pvmFile = File("examples/arithmetic/addition.pvm")
        val txtFile = File("examples/arithmetic/addition.txt")

        val derivedText = sadhaka.sadhayaScript(pvmFile.readText())
        txtFile.writeText(derivedText + "\n")

        assertEquals(derivedText.trim(), txtFile.readText().trim())
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
        assertEquals("षट्", line2Result.value)

        val line3Result = assertIs<ExecutionResult.Success>(results[2])
        assertEquals("सप्त", line3Result.value)

        val line4Result = assertIs<ExecutionResult.Success>(results[3])
        assertEquals("रामावतार", line4Result.value)

        val line5Result = assertIs<ExecutionResult.Success>(results[4])
        assertEquals("त्रीणि", line5Result.value)

        val line6Result = assertIs<ExecutionResult.Success>(results[5])
        assertEquals("पञ्च", line6Result.value)
    }

    @Test
    fun `PaniniVM evaluates sandhi pvm file and performs linguistic Sandhi on operands`() {
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
}
