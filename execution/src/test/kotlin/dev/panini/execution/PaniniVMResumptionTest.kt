package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.shiksha.Samjna
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PaniniVMResumptionTest {

    private lateinit var tempDir: File
    private lateinit var vm: PaniniVM

    @BeforeTest
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "paninivm_resumption_test_" + java.util.UUID.randomUUID())
        vm = PaniniVM(storageDir = tempDir)
    }

    @AfterTest
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `PaniniVM yields NeedsApproval when capability is missing and resumes successfully`() {
        var captured: String? = null
        vm.registerExternalCapability(ExecutionEffect.NETWORK) { payload, _ ->
            captured = payload
            "SUCCESS_DISPATCH"
        }

        // Create a restricted scope without NETWORK capability
        val restrictedScope = vm.defaultScope.copy(
            capabilities = setOf(ExecutionEffect.PURE)
        )

        // Evaluate the action requiring NETWORK
        val result = vm.eval("संदेश + अम् प्रेष + णिच् + लोट् + सिप् ।", scope = restrictedScope)

        // Assert that it returned NeedsApproval
        val needsApproval = assertIs<ExecutionResult.NeedsApproval>(result)
        assertEquals("योग-1", needsApproval.invocationId)
        assertTrue(ExecutionEffect.NETWORK in needsApproval.requiredEffects)

        // Now resume with a scope that includes all required capabilities
        val resumeScope = vm.defaultScope
        val resumeResult = vm.resume(needsApproval.continuation, scope = resumeScope)

        // Assert that it completed successfully
        val success = assertIs<ExecutionResult.Success>(resumeResult)
        assertEquals("SUCCESS_DISPATCH", success.value)
        assertEquals("संदेश", captured)
    }

    @Test
    fun `PaniniVM yields NeedsAcceptance for request disposition and resumes successfully`() {
        val yuj = YujirDhatu()
        val invocation = DhatuInvocation(
            id = "योग-1",
            dhatu = yuj,
            selectedOperation = "सङ्ख्यायोजनम्",
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10, "दश"),
                    ExecutionExpression.sankhya(2, "द्वि")
                )
            ),
        )

        val ukti = ExecutableUkti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
            invocations = listOf(invocation),
            prayojana = VakyaPrayojana.PRARTHANA // Resolves to REQUEST_EXECUTION
        )

        // Execute initially with no accepted invocations
        val conversation = SambhashanaContext(speaker = ukti.speaker, listener = ukti.listener)
        val initialScope = vm.defaultScope.copy(
            acceptedInvocations = emptySet()
        )

        val phala = SutraExecutionPipeline.execute(ukti, conversation, initialScope)
        println("ACTUAL PHALA: $phala")
        val response = SanskritPrativacanaRenderer.render(phala)

        // Maps to NeedsAcceptance in eval mapping
        val result = when (val resPhala = response.phala) {
            is Phala.SvikaraApekshita -> ExecutionResult.NeedsAcceptance(
                invocationId = resPhala.invocationId,
                speaker = resPhala.speaker,
                listener = resPhala.listener,
                continuation = requireNotNull(resPhala.pipelineContinuation),
                trace = resPhala.continuation.trace
            )
            is Phala.Asiddha -> resPhala.result
            else -> ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Expected SvikaraApekshita but got $resPhala")
        }

        if (result is ExecutionResult.Failure) {
            println("FAILURE MESSAGE: ${result.message}")
        }
        val needsAcceptance = assertIs<ExecutionResult.NeedsAcceptance>(result, "Result was: $result")
        assertEquals("योग-1", needsAcceptance.invocationId)

        // Now resume with a scope where the invocation is accepted
        val resumeScope = vm.defaultScope.copy(
            acceptedInvocations = setOf("योग-1")
        )
        val resumeResult = vm.resume(needsAcceptance.continuation, scope = resumeScope)

        val success = assertIs<ExecutionResult.Success>(resumeResult)
        assertEquals("द्वादश", success.value)
    }
}
