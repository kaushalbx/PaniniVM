package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NamaVakyaExecutionTest {
    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_namavakya_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `evaluates nominal sentence using implicit copula`() {
        // Turn 1: Seed a Suchi containing 2
        val res1 = vm.eval(
            "एक + अम् उपान्तिम + ङे दा + लोट् + सिप् ततः एक + अम् अन्तिम + ङे दा + लोट् + सिप् ततः क्षिप् + णिच् + लोट् + सिप् उपान्तिम + अम् अन्तिम + अम् च ।",
            sessionKey = "sess_namavakya"
        )
        assertIs<ExecutionResult.Success>(res1)

        // Turn 2: Nominal sentence checking if 1 exists in the result list
        val res2 = vm.eval(
            "एक + सुँ क्षिप् + घञ् + ङस् फल + अम् ।",
            sessionKey = "sess_namavakya"
        )
        val success2 = assertIs<ExecutionResult.Success>(res2, res2.toString())
        assertEquals("सत्यम्", success2.value)
        assertEquals(true, assertIs<SanskritValue.Satya>(success2.typedValue).boolean)
    }
}
