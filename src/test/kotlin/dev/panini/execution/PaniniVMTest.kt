package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PaniniVMTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_test_api_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM evaluates addition utterance`() {
        val result = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("द्वादश", success.value)
    }

    @Test
    fun `parsed vi upasarga selects yuj subtraction`() {
        val result = vm.eval("त्रि + अम् एक + औट् च वि + युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("द्वे", success.value)
    }

    @Test
    fun `PaniniVM evaluates multi-clause chain and persists session`() {
        val res1 = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "session_math")
        assertIs<ExecutionResult.Success>(res1)

        val res2 = vm.eval("पूर्वफल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् ।", sessionKey = "session_math")
        val success2 = assertIs<ExecutionResult.Success>(res2, res2.toString())
        assertEquals("चतुर्विंशतिः", success2.value)

        val loaded = vm.loadSession("session_math")
        assertNotNull(loaded)
        assertEquals(2, loaded.turnNumber)
        assertEquals(2, loaded.resultHistory.size)
        assertEquals("चतुर्विंशतिः", loaded.resultHistory.last().value)
    }

    @Test
    fun `PaniniVM evaluates 3-clause chained utterance`() {
        val result = vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । फल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् । फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("नव", success.value)
    }

    @Test
    fun `PaniniVM evaluates external process dispatch`() {
        var captured: String? = null
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { payload, _ ->
            captured = payload
            "SUCCESS_DISPATCH"
        }

        val result = vm.eval("संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("SUCCESS_DISPATCH", success.value)
        assertEquals("संदेश", captured)
    }

    @Test
    fun `PaniniVM instances isolate external capability handlers`() {
        val first = PaniniVM(File(tempDir, "first"))
        val second = PaniniVM(File(tempDir, "second"))
        first.registerExternalCapability(ExecutionEffect.NETWORK) { _, _ -> "FIRST" }
        second.registerExternalCapability(ExecutionEffect.NETWORK) { _, _ -> "SECOND" }
        val utterance = "संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।"

        assertEquals("FIRST", assertIs<ExecutionResult.Success>(first.eval(utterance)).value)
        assertEquals("SECOND", assertIs<ExecutionResult.Success>(second.eval(utterance)).value)
    }
}
