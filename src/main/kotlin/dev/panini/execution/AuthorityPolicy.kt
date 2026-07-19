package dev.panini.execution

object AuthorityPolicy {
    fun authorize(plan: ExecutionPlan, scope: ExecutionScope): AuthorityDecision {
        when (plan.disposition) {
            ExecutionDisposition.EXECUTE -> if (plan.speaker !in scope.authorizedSpeakers) {
                return AuthorityDecision.Denied(
                    "Speaker ${plan.speaker} is not authorized to command listener ${plan.listener}.",
                )
            }
            ExecutionDisposition.REQUEST_EXECUTION -> if (plan.invocationId !in scope.acceptedInvocations) {
                return AuthorityDecision.NeedsAcceptance
            }
            else -> Unit
        }
        val missing = plan.requiredEffects - scope.capabilities
        return if (missing.isEmpty()) AuthorityDecision.Authorized else AuthorityDecision.NeedsApproval(missing)
    }
}
