package dev.panini.execution

import dev.panini.execution.sutra.SanskritGranthaSourceCompilation
import dev.panini.execution.sutra.SanskritGranthaSourceCompiler
import dev.panini.sutra.runtime.GranthaId
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PaniniVMTest {
    @Test
    fun `segmented da sentence binds a local name for a later sentence`() {
        val vm = PaniniVM()
        val results = vm.evalScript(
            """
            एक + अम् आरम्भ + ङे दा + लोट् + सिप् ।
            आरम्भ + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
            sessionKey = "named_local",
        )

        assertEquals(2, results.size)
        assertEquals("एक", assertIs<ExecutionResult.Success>(results[0]).value)
        val sum = assertIs<SanskritValue.Sankhya>(
            assertIs<ExecutionResult.Success>(results[1]).typedValue,
        )
        assertEquals(3L, sum.value)
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(
            vm.loadSession("named_local")?.previousTypedResults?.get("आरम्भ"),
        ).value)
    }


    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_test_api_" + java.util.UUID.randomUUID())
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
    fun `PaniniVM evaluates its canonical sutra grantha source`() {
        val compilation = assertIs<SanskritGranthaSourceCompilation.Success>(
            SanskritGranthaSourceCompiler.compile(
                "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
                GranthaId("api-addition"),
            ),
        )

        val success = assertIs<ExecutionResult.Success>(
            vm.evalGrantha(compilation.source, sourceName = "api-addition.sutra"),
        )

        assertEquals("द्वादश", success.value)
        assertEquals("panini.grantha", success.operation)
        assertEquals(12L, assertIs<SanskritValue.Sankhya>(success.typedValue).value)
    }

    @Test
    fun `PaniniVM evaluates a canonical sutra grantha file`() {
        val compilation = assertIs<SanskritGranthaSourceCompilation.Success>(
            SanskritGranthaSourceCompiler.compile(
                "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
                GranthaId("file-addition"),
            ),
        )
        val sourceFile = File(tempDir, "file-addition.sutra").apply {
            parentFile.mkdirs()
            writeText(compilation.source)
        }

        val success = assertIs<ExecutionResult.Success>(vm.evalGranthaFile(sourceFile))

        assertEquals("त्रीणि", success.value)
    }

    @Test
    fun `PaniniVM reports invalid sutra source as an execution failure`() {
        val failure = assertIs<ExecutionResult.Failure>(
            vm.evalGrantha("{not canonical sutra source"),
        )

        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue(failure.message.isNotBlank())
    }

    @Test
    fun `parsed vi upasarga selects yuj subtraction`() {
        val result = vm.eval("त्रि + अम् एक + औट् च वि + युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("द्वे", success.value)
    }

    @Test
    fun `parsed nyunataya selects minimum operation`() {
        val result = vm.eval("त्रि + अम् एक + औट् च न्यूनतया विद् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        val number = assertIs<SanskritValue.Sankhya>(success.typedValue)
        assertEquals(1, number.value)
        assertTrue(success.trace.any { "सङ्ख्यान्यूनत्वम्" in it })
    }

    @Test
    fun `parsed punah selects memory load after save`() {
        assertIs<ExecutionResult.Success>(vm.eval("सत्र + अम् स्मृ + लोट् + सिप् ।"))

        val result = vm.eval("पुनः सत्र + अम् स्मृ + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(result, result.toString())
        assertTrue(success.trace.any { "स्मृतिपुनर्प्राप्तिः" in it })
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

    @Test
    fun `VM eval evaluates utterance and ignores hash comments in script`() {
        val res = VM.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
        val success = assertIs<ExecutionResult.Success>(res)
        assertEquals("द्वादश", success.value)

        val scriptResults = VM.evalScript(
            """
            # First comment
            दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            # Second comment
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            """.trimIndent()
        )
        assertEquals(2, scriptResults.size)
        assertEquals("द्वादश", assertIs<ExecutionResult.Success>(scriptResults[0]).value)
        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(scriptResults[1]).value)
    }
}
