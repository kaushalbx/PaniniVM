package dev.panini.dhatupatha

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NewDhatusTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_new_dhatus_test_" + System.currentTimeMillis())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `SthaDhatu executes state wait action`() {
        val result = vm.eval("गृह + अम् स्था + णिच् + लोट् + सिप् ।", sessionKey = "stha_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("स्थितिः संजाता: गृह", success.value)
    }

    @Test
    fun `NiDhatu executes list move action`() {
        val result = vm.eval("राम + अम् नी + णिच् + लोट् + सिप् ।", sessionKey = "ni_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("नयनम् सिद्धम्: राम", success.value)
    }

    @Test
    fun `PaaDhatu executes resource release action`() {
        val result = vm.eval("जल + अम् पा + णिच् + लोट् + सिप् ।", sessionKey = "paa_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("पानम् सम्पन्नम्: जल", success.value)
    }

    @Test
    fun `BhuDhatu executes state instantiate action`() {
        val result = vm.eval("राम + अम् भू + णिच् + लोट् + सिप् ।", sessionKey = "bhu_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("सत्ता संजाता: राम", success.value)
    }

    @Test
    fun `EdhDhatu executes scale action`() {
        val result = vm.eval("पञ्च + अम् एध् + णिच् + लोट् + सिप् ।", sessionKey = "edh_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("दश", success.value)
    }

    @Test
    fun `AdDhatu executes consume action`() {
        val result = vm.eval("अन्न + अम् अद् + णिच् + लोट् + सिप् ।", sessionKey = "ad_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("भक्षणम् सम्पन्नम्: अन्न", success.value)
    }

    @Test
    fun `HuDhatu executes emit action`() {
        val result = vm.eval("हविस् + अम् हु + णिच् + लोट् + सिप् ।", sessionKey = "hu_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("अर्पितम्: हविस्", success.value)
    }

    @Test
    fun `DivDhatu executes random choice action`() {
        val result = vm.eval("अक्ष + अम् दिव् + णिच् + लोट् + सिप् ।", sessionKey = "div_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("अक्ष", success.value)
    }

    @Test
    fun `SuDhatu executes summarize action`() {
        val result = vm.eval("वाक्य + अम् सु + णिच् + लोट् + सिप् ।", sessionKey = "su_session")
        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("वाक्य", success.value)
    }
}
