package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaMathExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_math_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates segmented Dvi Gunita Sata + Sata expression`() {
        val result = vm.eval("द्वि + गुणित + अम् शत + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // (2 * 100) + 100 = 300
        assertEquals(300L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates segmented Varga Krita Pancha + Sata expression`() {
        val result = vm.eval("वर्ग + कृत + अम् पञ्च + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // (5^2) + 100 = 125
        assertEquals(125L, sankhya.value)
    }
}
