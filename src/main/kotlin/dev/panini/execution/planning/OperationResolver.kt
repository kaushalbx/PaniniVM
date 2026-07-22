package dev.panini.execution

import dev.panini.core.Karaka

object OperationResolver {
    fun resolve(
        invocation: DhatuInvocation,
        variables: Map<String, SanskritValue>,
    ): OperationResolution {
        return resolveInvocation(invocation, variables)
    }

    private fun resolveInvocation(
        invocation: DhatuInvocation,
        variables: Map<String, SanskritValue>,
    ): OperationResolution {
        val dhatu = invocation.dhatu
        val operations = dhatu.operations
        if (operations.isEmpty()) {
            return OperationResolution.Invalid(
                ExecutionError.DHATU_NOT_EXECUTABLE,
                "Dhātu ${dhatu.upadesha} has no executable operations.",
            )
        }

        val named = invocation.selectedOperation?.let { selected ->
            operations.filter { it.name == selected }
        } ?: operations.filter { it.trigger.matches(invocation.grammaticalFeatures) }
        if (named.isEmpty()) {
            val message = invocation.selectedOperation?.let {
                "Dhātu ${dhatu.upadesha} has no operation '$it'."
            } ?: "No operation of dhātu ${dhatu.upadesha} matches the parsed grammatical features."
            return OperationResolution.Invalid(
                ExecutionError.OPERATION_NOT_FOUND,
                message,
            )
        }

        val evaluations = named.map { operation ->
            val context = contextFor(invocation, operation, variables)
            Triple(operation, context, evaluate(operation.signature, context))
        }
        val compatible = evaluations.filter { it.third == SignatureEvaluation.Compatible }
        if (compatible.isEmpty()) {
            val missing = evaluations.flatMap { (_, _, result) ->
                (result as? SignatureEvaluation.Missing)?.karakas.orEmpty()
            }.toSet()
            if (missing.isNotEmpty()) {
                return OperationResolution.MissingInput(
                    missing,
                    "Required kārakas are missing for dhātu ${dhatu.upadesha}: $missing",
                )
            }
            val reason = evaluations.firstNotNullOfOrNull { (_, _, result) ->
                (result as? SignatureEvaluation.Incompatible)?.reason
            } ?: "No operation signature accepts this invocation."
            return OperationResolution.Invalid(ExecutionError.INVALID_VALUE, reason)
        }

        val maximal = compatible.filter { candidate ->
            compatible.none { other -> other !== candidate && moreSpecific(other.first.signature, candidate.first.signature) }
        }
        val selected = if (maximal.size == 1) {
            maximal.single()
        } else {
            return OperationResolution.Ambiguous(
                maximal.map { it.first.name },
                "More than one incomparable operation signature matches dhātu ${dhatu.upadesha}.",
            )
        }
        val operation = selected.first
        val context = selected.second

        return OperationResolution.Resolved(
            ResolvedOperation(
                invocation,
                operation,
                context,
                invocation.karakaTrace + "Resolved ${dhatu.upadesha} to operation ${operation.name}.",
            )
        )
    }

    private fun contextFor(
        invocation: DhatuInvocation,
        operation: DhatuOperation,
        variables: Map<String, SanskritValue>,
    ): ExecutionContext {
        val acceptedKarakas = operation.signature.requirements.mapTo(mutableSetOf()) { it.karaka } +
            operation.signature.optionalKarakas
        val bindings = invocation.bindings.toMutableMap()
        invocation.ambiguousBindings.forEach { ambiguous ->
            val matches = ambiguous.candidates intersect acceptedKarakas
            if (matches.size == 1) {
                val karaka = matches.single()
                bindings[karaka] = bindings[karaka]?.let { existing ->
                    ExecutionExpression.Coordination(listOf(existing, ambiguous.expression))
                } ?: ambiguous.expression
            }
        }
        return invocation.executionContext(variables).copy(bindings = bindings)
    }

    private fun moreSpecific(left: OperationSignature, right: OperationSignature): Boolean {
        val rightByKaraka = right.requirements.associateBy { it.karaka }
        var strictlyStronger = false
        for (leftRequirement in left.requirements) {
            val rightRequirement = rightByKaraka[leftRequirement.karaka]
            if (rightRequirement == null) {
                strictlyStronger = true
                continue
            }
            if (leftRequirement.minimumMembers < rightRequirement.minimumMembers) return false
            val leftMax = leftRequirement.maximumMembers ?: Int.MAX_VALUE
            val rightMax = rightRequirement.maximumMembers ?: Int.MAX_VALUE
            if (leftMax > rightMax) return false
            if (rightRequirement.shape != null && leftRequirement.shape != rightRequirement.shape) return false
            if (!leftRequirement.memberSamjnas.containsAll(rightRequirement.memberSamjnas)) return false
            if (leftRequirement != rightRequirement) strictlyStronger = true
        }
        if (right.requirements.any { requirement -> left.requirements.none { it.karaka == requirement.karaka } }) return false
        return strictlyStronger
    }

    private fun evaluate(signature: OperationSignature, context: ExecutionContext): SignatureEvaluation {
        val missing = signature.requirements.map { it.karaka }.filterNot { it in context.bindings }
        if (missing.isNotEmpty()) return SignatureEvaluation.Missing(missing.toSet())

        signature.requirements.forEach { requirement ->
            val expression = requireNotNull(context.bindings[requirement.karaka])
            val shape = when (expression) {
                is ExecutionExpression.Pada -> ExpressionShape.LITERAL
                is ExecutionExpression.Coordination -> ExpressionShape.COORDINATION
                is ExecutionExpression.Reference -> ExpressionShape.REFERENCE
            }
            if (requirement.shape != null && requirement.shape != shape) {
                return SignatureEvaluation.Incompatible("${requirement.karaka} requires ${requirement.shape}, but received $shape.")
            }
            val typedValues = context.resolveValues(expression)
            if (typedValues.isEmpty()) {
                return SignatureEvaluation.Incompatible("${requirement.karaka} contains an unresolved reference.")
            }
            if (typedValues.size < requirement.minimumMembers) {
                return SignatureEvaluation.Incompatible("${requirement.karaka} requires at least ${requirement.minimumMembers} members.")
            }
            if (requirement.maximumMembers != null && typedValues.size > requirement.maximumMembers) {
                return SignatureEvaluation.Incompatible("${requirement.karaka} accepts at most ${requirement.maximumMembers} members.")
            }
            if (typedValues.any { !it.samjnas.containsAll(requirement.memberSamjnas) }) {
                return SignatureEvaluation.Incompatible("Every ${requirement.karaka} member requires saṃjñās ${requirement.memberSamjnas}.")
            }
        }
        return SignatureEvaluation.Compatible
    }

    private sealed interface SignatureEvaluation {
        data object Compatible : SignatureEvaluation
        data class Missing(val karakas: Set<Karaka>) : SignatureEvaluation
        data class Incompatible(val reason: String) : SignatureEvaluation
    }
}
