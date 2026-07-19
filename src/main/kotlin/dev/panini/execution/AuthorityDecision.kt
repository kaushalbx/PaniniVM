package dev.panini.execution

sealed interface AuthorityDecision {
    data object Authorized : AuthorityDecision
    data class NeedsApproval(val effects: Set<ExecutionEffect>) : AuthorityDecision
    data object NeedsAcceptance : AuthorityDecision
    data class Denied(val reason: String) : AuthorityDecision
}
