package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaBhinnaExecutionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_bhinna_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates Saardha Dvi + Sata expression`() {
        val result = vm.eval("सार्ध + द्वि + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // 2 (integer part of सार्ध द्वि) + शत (100) = 102
        assertEquals(102L, sankhya.value)
    }

    @Test
    fun `PaniniVM evaluates Sapaada Tri + Sata expression`() {
        val result = vm.eval("सपाद + त्रि + अम् शत + अम् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val sankhya = assertIs<SanskritValue.Sankhya>(success.typedValue)
        // 3 (integer part of सपाद त्रि) + शत (100) = 103
        assertEquals(103L, sankhya.value)
    }
}
