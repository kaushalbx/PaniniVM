package dev.panini.execution

import dev.panini.dhatupatha.bhvadi.SmrDhatu
import dev.panini.dhatupatha.curadi.PreshDhatu
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.FileStateStore
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class PersistenceAndExternalCapabilityTest {

    private lateinit var tempDir: File
    private lateinit var store: FileStateStore

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_test_store_" + System.currentTimeMillis())
        store = FileStateStore(tempDir)
        SmritiSaveAction.globalStore = store
        SmritiLoadAction.globalStore = store
        ExternalCapabilityDispatcher.clear()
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
        SmritiSaveAction.globalStore = null
        SmritiLoadAction.globalStore = null
        ExternalCapabilityDispatcher.clear()
    }

    @Test
    fun `FileStateStore persists and restores context state`() {
        val initialContext = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            mentionedEntities = mapOf("मूल्यम्" to "दश"),
            previousResults = linkedMapOf("योग-१" to "दश"),
        )

        store.save("session_1", initialContext)
        val loaded = store.load("session_1")

        assertNotNull(loaded)
        assertEquals("प्रयोक्ता", loaded.speaker)
        assertEquals("दश", loaded.mentionedEntities["मूल्यम्"])
        assertEquals("दश", loaded.previousResults["योग-१"])
    }

    @Test
    fun `SmritiSaveAction saves context under session key`() {
        val smr = SmrDhatu()
        val op = smr.operations.first { it.id == SmritiSaveAction.ID }
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Literal("परीक्षण-सत्रम्", setOf(ExecutionSamjna.SHABDA))),
            variables = mapOf("फलं" to SanskritValue.Sankhya(5L, "पञ्च")),
        )

        val result = assertIs<ExecutionResult.Success>(SmritiSaveAction.execute(context, op))
        assertEquals("परीक्षण-सत्रम्", result.value)

        val loaded = store.load("परीक्षण-सत्रम्")
        assertNotNull(loaded)
        assertEquals("पञ्च", loaded.mentionedEntities["फलं"])
    }

    @Test
    fun `PreshDhatu dispatches payload to registered external handler`() {
        var capturedPayload: String? = null
        var capturedEffect: ExecutionEffect? = null

        ExternalCapabilityDispatcher.register(ExecutionEffect.NETWORK) { payload, effect ->
            capturedPayload = payload
            capturedEffect = effect
            "Response from server: OK"
        }

        val presh = PreshDhatu()
        val op = presh.operations.first()
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Literal("संदेशम्_प्रेषय", setOf(ExecutionSamjna.SHABDA))),
        )

        val result = assertIs<ExecutionResult.Success>(BahyaSendAction.execute(context, op))
        assertEquals("Response from server: OK", result.value)
        assertEquals("संदेशम्_प्रेषय", capturedPayload)
        assertEquals(ExecutionEffect.NETWORK, capturedEffect)
    }

    @Test
    fun `OperationResolver resolves SmritiSaveAction invocation`() {
        val smr = SmrDhatu()
        val invocation = DhatuInvocation(
            id = "inv-1",
            dhatu = smr,
            selectedOperation = "स्मृतिरक्षणम्",
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Literal("सत्रम्", setOf(ExecutionSamjna.SHABDA))),
        )

        val resolution = OperationResolver.resolve(invocation, variables = emptyMap<String, SanskritValue>())
        assertIs<OperationResolution.Resolved>(resolution)
    }
}
