package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BhutasamkhyaExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_bhutasamkhya_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates Bhutasamkhya option A utterance for Netra Veda`() {
        val result = vm.eval("भूतसङ्ख्या नेत्र + वेद + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // नेत्र वेद (42) + शत (100) = 142
        assertEquals(142L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates Bhutasamkhya option A utterance for Agni Bana`() {
        val result = vm.eval("भूतसङ्ख्या अग्नि + बाण + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // अग्नि बाण (53) + शत (100) = 153
        assertEquals(153L, sankhya.value)
    }
}
