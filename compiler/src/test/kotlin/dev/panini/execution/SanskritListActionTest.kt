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
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_list_test_" + java.util.UUID.randomUUID())
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
        val pvmFile = File("examples/collections/list_demo.pvm")
        val results = vm.evalFile(pvmFile, sessionKey = "file_list_session")
        assertEquals(2, results.size)

        val res1 = assertIs<ExecutionResult.Success>(results[0])
        assertEquals("[राम, फल]", res1.value)

        val res2 = assertIs<ExecutionResult.Success>(results[1])
        assertEquals("नयनम् सिद्धम्: राम", res2.value)
    }
}
