package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PvmFileTest {

   /* private lateinit var tempDir: File
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
    fun `PaniniVM evaluates addition pvm file containing segmented Sanskrit utterances`() {
        val pvmFile = File("src/test/kotlin/dev/panini/parser/addition.pvm")
        val results = vm.evalFile(pvmFile, sessionKey = "addition_session")

        assertEquals(2, results.size)

        val line1Result = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("षट्", line1Result.value)

        val line2Result = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("पञ्च", line2Result.value)
    }*/
}
