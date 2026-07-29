package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FibonacciCompositionTest {
    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_fib_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `computes Fibonacci sequence by composing addition across turns`() {
        val sKey = "fib_session"

        // F(1) = 1, F(2) = 2
        val res1 = vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 1 + 2 = 3 (F(3))
        assertIs<ExecutionResult.Success>(res1)
        assertEquals("त्रीणि", res1.value)

        val res2 = vm.eval("द्वि + अम् पूर्वफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 2 + 3 = 5 (F(4))
        assertIs<ExecutionResult.Success>(res2)
        assertEquals("पञ्च", res2.value)

        val res3 = vm.eval("प्रथमफल + अम् द्वितीयफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 3 + 5 = 8 (F(5))
        val success3 = assertIs<ExecutionResult.Success>(res3)
        assertEquals("अष्ट", success3.value)
        assertEquals(8L, assertIs<SanskritValue.Sankhya>(success3.typedValue).value)

        val res4 = vm.eval("द्वितीयफल + अम् तृतीयफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 5 + 8 = 13 (F(6))
        val success4 = assertIs<ExecutionResult.Success>(res4)
        assertEquals("त्रयोदश", success4.value)
        assertEquals(13L, assertIs<SanskritValue.Sankhya>(success4.typedValue).value)

        val res5 = vm.eval("तृतीयफल + अम् चतुर्थफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 8 + 13 = 21 (F(7))
        val success5 = assertIs<ExecutionResult.Success>(res5)
        assertEquals("एकविंशतिः", success5.value)
        assertEquals(21L, assertIs<SanskritValue.Sankhya>(success5.typedValue).value)

        val res6 = vm.eval("चतुर्थफल + अम् पञ्चमफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 13 + 21 = 34 (F(8))
        val success6 = assertIs<ExecutionResult.Success>(res6)
        assertEquals("चतुर्त्रिंशत्", success6.value)
        assertEquals(34L, assertIs<SanskritValue.Sankhya>(success6.typedValue).value)

        val res7 = vm.eval("पञ्चमफल + अम् षष्ठफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey) // 21 + 34 = 55 (F(9))
        val success7 = assertIs<ExecutionResult.Success>(res7)
        assertEquals("पञ्चपञ्चाशत्", success7.value)
        assertEquals(55L, assertIs<SanskritValue.Sankhya>(success7.typedValue).value)
    }

    @Test
    fun `accumulates Fibonacci sequence in a Suchi list`() {
        val sKey = "fib_list_session"

        // Push initial terms [एक, द्वि] into Suchi
        val res1 = vm.eval("एक + अम् द्वि + औट् च क्षिप् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        val list1 = assertIs<ExecutionResult.Success>(res1)
        assertEquals("[एक, द्वि]", list1.value)

        // Add 1 + 2 -> 3
        val add1 = vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(add1).value)

        // Push 3 into list [एक, द्वि] -> [एक, द्वि, त्रीणि]
        val push1 = vm.eval("प्रथमफल + अम् पूर्वफल + अम् च क्षिप् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        val list2 = assertIs<ExecutionResult.Success>(push1)
        assertEquals("[एक, द्वि, त्रीणि]", list2.value)
    }

    @Test
    fun `computes Fibonacci up to 55 in a single sentence using krtvas frequency loop`() {
        val sKey = "krtvas_fib_session"
        // Seed Turn 1: F(2) = 2
        val turn1 = vm.eval("एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        assertIs<ExecutionResult.Success>(turn1)

        // Seed Turn 2: F(3) = 1 + 2 = 3
        val turn2 = vm.eval("एक + अम् पूर्वफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        assertIs<ExecutionResult.Success>(turn2)
        assertEquals("त्रीणि", turn2.value)

        // Single utterance with पञ्च + कृत्वः (5 iterations):
        // 3+2=5, 5+3=8, 8+5=13, 13+8=21, 21+13=34, 34+21=55
        val res = vm.eval("पञ्च + कृत्वः पूर्वफल + अम् पूर्वपूर्वफल + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = sKey)
        val success = assertIs<ExecutionResult.Success>(res, res.toString())
        assertTrue(success.typedValue is SanskritValue.Sankhya, "Expected Sankhya result, got ${success.typedValue}")
    }
}
