package dev.panini.execution

import dev.panini.execution.persistence.StateStore
import dev.panini.execution.external.ExternalCapabilityDispatcher

enum class ExecutionEffect {
    PURE, READ_MEMORY, WRITE_MEMORY, READ_RESOURCE, WRITE_RESOURCE,
    DELETE_RESOURCE, NETWORK, SEND_MESSAGE, EXECUTE_PROCESS,
}

/** Capabilities and trusted identities supplied by the host. */
data class ExecutionScope(
    val capabilities: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val environment: ValueEnvironment = ValueEnvironment(),
    val stateStore: StateStore? = null,
    val externalDispatcher: ExternalCapabilityDispatcher? = null,
    val authorizedSpeakers: Set<String> = emptySet(),
    val acceptedInvocations: Set<String> = emptySet(),
)

sealed interface AuthorityDecision {
    data object Authorized : AuthorityDecision
    data class NeedsApproval(val effects: Set<ExecutionEffect>) : AuthorityDecision
    data object NeedsAcceptance : AuthorityDecision
    data class Denied(val reason: String) : AuthorityDecision
}

object AuthorityPolicy {
    fun authorize(plan: ExecutionPlan, scope: ExecutionScope): AuthorityDecision {
        when (plan.disposition) {
            ExecutionDisposition.EXECUTE ->
                if (scope.authorizedSpeakers.isNotEmpty() && plan.speaker !in scope.authorizedSpeakers) {
                    return AuthorityDecision.Denied(
                        "Speaker ${plan.speaker} is not authorized to command listener ${plan.listener}.",
                    )
                }
            ExecutionDisposition.REQUEST_EXECUTION ->
                if (plan.invocationId !in scope.acceptedInvocations) return AuthorityDecision.NeedsAcceptance
            else -> Unit
        }
        val missing = plan.requiredEffects - scope.capabilities
        return if (missing.isEmpty()) AuthorityDecision.Authorized else AuthorityDecision.NeedsApproval(missing)
    }
}
