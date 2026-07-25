package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class StandardLibraryIoTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_io_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM executes print action using DrshDhatu`() {
        val result = vm.eval("राम + अम् प्र + दृश् + णिच् + लोट् + सिप् ।", sessionKey = "print_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("राम", success.value)
    }

    @Test
    fun `PaniniVM executes read action using GrahDhatu`() {
        val result = vm.eval("निवेश + अम् ग्रह् + णिच् + लोट् + सिप् ।", sessionKey = "read_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("स्वीकृतम्", success.value)
    }
}
