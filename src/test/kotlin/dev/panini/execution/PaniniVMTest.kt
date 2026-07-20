package dev.panini.execution

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PaniniVMTest {

   /* private lateinit var tempDir: File
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
        val result = vm.eval("दश द्वि च योजय।")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("द्वादश", success.value)
    }

    @Test
    fun `PaniniVM evaluates multi-clause chain and persists session`() {
        val res1 = vm.eval("दश द्वि च योजय।", sessionKey = "session_math")
        assertIs<ExecutionResult.Success>(res1)

        val res2 = vm.eval("पूर्वफलं द्वि च गुणय।", sessionKey = "session_math")
        val success2 = assertIs<ExecutionResult.Success>(res2)
        assertEquals("चतुर्विंशतिः", success2.value)

        val loaded = vm.loadSession("session_math")
        assertNotNull(loaded)
        assertEquals("चतुर्विंशतिः", loaded.mentionedEntities["योग-१"])
    }

    @Test
    fun `PaniniVM evaluates external process dispatch`() {
        var captured: String? = null
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { payload, _ ->
            captured = payload
            "SUCCESS_DISPATCH"
        }

        val result = vm.eval("संदेशम् प्रेषय।")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("SUCCESS_DISPATCH", success.value)
        assertEquals("संदेशम्", captured)
    }*/
}
