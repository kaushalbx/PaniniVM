package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PvmFileTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

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
    fun `PaniniVM evaluates addition pvm file containing complex arithmetic utterances`() {
        val pvmFile = File("src/test/kotlin/dev/panini/parser/addition.pvm")
        val results = vm.evalFile(pvmFile, sessionKey = "addition_session")

        assertEquals(5, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("षट्", line1Result.value)

        val line2Result = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("पञ्च", line2Result.value)

        val line3Result = assertIs<ExecutionResult.Success>(results[2])
        assertEquals("षड्त्रिंशत्", line3Result.value)

        val line4Result = assertIs<ExecutionResult.Success>(results[3])
        assertEquals("द्वात्रिंशत्", line4Result.value)

        val line5Result = assertIs<ExecutionResult.Success>(results[4])
        assertEquals("चत्वारि", line5Result.value)
    }
}
