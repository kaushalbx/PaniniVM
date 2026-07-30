package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MultiTurnResultChainingTest {
    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_multiturn_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `evaluates multi-turn calculation with purvaphala reference`() {
        val res1 = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_1")
        assertIs<ExecutionResult.Success>(res1)
        assertEquals("द्वादश", res1.value)

        val res2 = vm.eval("युज् + ल्युट् + ङस् फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_1")
        val success2 = assertIs<ExecutionResult.Success>(res2, res2.toString())
        assertEquals("पञ्चदश", success2.value)
        assertEquals(15L, assertIs<SanskritValue.Sankhya>(success2.typedValue).value)
    }

    @Test
    fun `evaluates multi-turn calculation using phala reference`() {
        val res1 = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_phala")
        assertIs<ExecutionResult.Success>(res1)
        assertEquals("द्वादश", res1.value)

        val res2 = vm.eval("फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_phala")
        val success2 = assertIs<ExecutionResult.Success>(res2, res2.toString())
        assertEquals("पञ्चदश", success2.value)
        assertEquals(15L, assertIs<SanskritValue.Sankhya>(success2.typedValue).value)
    }

    @Test
    fun `evaluates multi-turn calculation with ordinal result history lookups`() {
        val res1 = vm.eval("द्वि + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_ordinal")
        assertIs<ExecutionResult.Success>(res1)
        assertEquals("पञ्च", res1.value) // 5

        val res2 = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_ordinal")
        assertIs<ExecutionResult.Success>(res2)
        assertEquals("द्वादश", res2.value) // 12

        val res3 = vm.eval("प्रथमफल + अम् द्वितीयफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "sess_ordinal")
        val success3 = assertIs<ExecutionResult.Success>(res3, res3.toString())
        assertEquals("सप्तदश", success3.value) // 5 + 12 = 17
        assertEquals(17L, assertIs<SanskritValue.Sankhya>(success3.typedValue).value)
    }
}
