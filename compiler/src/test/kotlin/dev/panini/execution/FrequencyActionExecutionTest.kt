package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FrequencyActionExecutionTest {
    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_freq_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `evaluates frequency action with dvikrtvah 2 times`() {
        var callCount = 0
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { _, _ ->
            callCount++
            "CALL_$callCount"
        }

        val result = vm.eval("द्वि + कृत्वः संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("CALL_2", success.value)
        assertEquals(2, callCount)
    }

    @Test
    fun `evaluates frequency action with segmented dvi + suc 2 times`() {
        var callCount = 0
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { _, _ ->
            callCount++
            "DISPATCH_$callCount"
        }

        val result = vm.eval("द्वि + सुच् संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("DISPATCH_2", success.value)
        assertEquals(2, callCount)
    }

    @Test
    fun `evaluates frequency action with pancakrtvah 5 times`() {
        var callCount = 0
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { _, _ ->
            callCount++
            "COUNT_$callCount"
        }

        val result = vm.eval("पञ्च + कृत्वः संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("COUNT_5", success.value)
        assertEquals(5, callCount)
    }
}
