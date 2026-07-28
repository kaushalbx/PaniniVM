package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class KatapayadiExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_katapayadi_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates Katapayadi option A utterance for Madhava`() {
        val result = vm.eval("कटपय माधव + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // माधव (495) + शत (100) = 595
        assertEquals(595L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates Katapayadi expression in coordination with Khaga`() {
        val result = vm.eval("कटपय खग + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // खग (32) + शत (100) = 132
        assertEquals(132L, sankhya.value)
    }
}
