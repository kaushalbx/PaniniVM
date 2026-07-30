package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaGeoExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_geo_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `executes Jya utterance in PaniniVM`() {
        val result = vm.eval("ज्या + नवति + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // sin(90) + 100 = 1 + 100 = 101
        assertEquals(101L, sankhya.value)
    }

    @Test
    fun `executes Kotijya utterance in PaniniVM`() {
        val result = vm.eval("कोटि + ज्या + शून्य + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // cos(0) + 100 = 1 + 100 = 101
        assertEquals(101L, sankhya.value)
    }

    @Test
    fun `executes Karna utterance in PaniniVM`() {
        val result = vm.eval("कर्ण + त्रि + चतुर् + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // sqrt(3^2 + 4^2) + 100 = 5 + 100 = 105
        assertEquals(105L, sankhya.value)
    }
}
