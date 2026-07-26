package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AryabhatiyaExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_aryabhatiya_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates Aryabhatiya option A utterance for Gi`() {
        val result = vm.eval("आर्यभटीय गि + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // गि (300) + शत (100) = 400
        assertEquals(400L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates Aryabhatiya option A utterance for Khyu`() {
        val result = vm.eval("आर्यभटीय ख्यु + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // ख्यु (320,000) + शत (100) = 320,100
        assertEquals(320100L, sankhya.value)
    }
}
