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
                "अक्रियाधातुः: ${dhatu.upadesha} इत्यस्य निष्पादनीया क्रिया नास्ति।",
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
            val isAllMissing = evaluations.all { it.third is SignatureEvaluation.Missing }
            if (isAllMissing) {
                val missing = evaluations.flatMap { (_, _, result) ->
                    (result as SignatureEvaluation.Missing).karakas
                }.toSet()
                return OperationResolution.MissingInput(
                    missing,
                "कारकाभावः: ${dhatu.upadesha} इति धातुः " +
                    "${missing.joinToString { it.sanskritName }} कारकम् अपेक्षते।",
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
                is ExecutionExpression.TypedOperand -> ExpressionShape.LITERAL
            }
            val expectedShape = requirement.shape
            if (expectedShape != null && expectedShape != shape) {
                return SignatureEvaluation.Incompatible(
                    "कारकरूपदोषः: ${requirement.karaka.sanskritName} कारके " +
                        "${expectedShape.sanskritName} अपेक्षितम्, ${shape.sanskritName} प्राप्तम्।",
                )
            }
            val typedValues = context.resolveValues(expression)
            if (typedValues.isEmpty()) {
                return SignatureEvaluation.Incompatible(
                    "कारकनिर्देशदोषः: ${requirement.karaka.sanskritName} कारकस्य निर्देशः न सिद्धः।",
                )
            }
            if (typedValues.size < requirement.minimumMembers) {
                return SignatureEvaluation.Incompatible(
                    "कारकसङ्ख्यादोषः: ${requirement.karaka.sanskritName} कारके न्यूनातिन्यूनं " +
                        "${requirement.minimumMembers} पदानि अपेक्षितानि।",
                )
            }
            val maxMembers = requirement.maximumMembers
            if (maxMembers != null && typedValues.size > maxMembers) {
                return SignatureEvaluation.Incompatible(
                    "कारकसङ्ख्यादोषः: ${requirement.karaka.sanskritName} कारके अधिकात्यधिकं " +
                        "$maxMembers पदानि स्वीक्रियन्ते।",
                )
            }
            if (typedValues.any { !it.samjnas.containsAll(requirement.memberSamjnas) }) {
                return SignatureEvaluation.Incompatible(
                    "कारकसंज्ञादोषः: ${requirement.karaka.sanskritName} कारकस्य प्रत्येकं पदं " +
                        "${requirement.memberSamjnas} संज्ञाः अपेक्षते।",
                )
            }
        }
        return SignatureEvaluation.Compatible
    }

    private sealed interface SignatureEvaluation {
        data object Compatible : SignatureEvaluation
        data class Missing(val karakas: Set<Karaka>) : SignatureEvaluation
        data class Incompatible(val reason: String) : SignatureEvaluation
    }

    private val ExpressionShape.sanskritName: String
        get() = when (this) {
            ExpressionShape.LITERAL -> "एकपदम्"
            ExpressionShape.COORDINATION -> "समुच्चितपदम्"
            ExpressionShape.REFERENCE -> "निर्दिष्टपदम्"
        }
}

fun DhatuInvocation.executionContext(
    variables: Map<String, SanskritValue>,
    stateStore: dev.panini.execution.persistence.StateStore? = null,
    externalDispatcher: dev.panini.execution.external.ExternalCapabilityDispatcher? = null,
): ExecutionContext = ExecutionContext(
    bindings = bindings,
    selectedOperation = selectedOperation,
    variables = variables,
    metadata = metadata,
    stateStore = stateStore,
    externalDispatcher = externalDispatcher,
)
