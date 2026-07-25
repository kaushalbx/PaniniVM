package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SanskritListActionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_list_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM executes list push action using KshipDhatu`() {
        val result = vm.eval("राम + अम् फल + अम् च क्षिप् + णिच् + लोट् + सिप् ।", sessionKey = "list_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("[राम, फल]", success.value)
    }

    @Test
    fun `PvmFileTest executes list_demo pvm script`() {
        val pvmFile = File("examples/list_demo.pvm")
        val txtFile = File("examples/list_demo.txt")
        val results = vm.evalFile(pvmFile, sessionKey = "file_list_session")
        val expected = txtFile.readLines().map { it.trim() }

        assertEquals(expected.size, results.size)
        results.forEachIndexed { idx, res ->
            val success = assertIs<ExecutionResult.Success>(res)
            assertEquals(expected[idx], success.value)
        }
    }
}
