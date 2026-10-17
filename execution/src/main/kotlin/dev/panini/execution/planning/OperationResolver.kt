package dev.panini.execution

import dev.panini.core.Karaka

object OperationResolver {
    private const val MAX_ROLE_ASSIGNMENTS = 4096
    fun resolve(
        invocation: DhatuInvocation,
        variables: Map<String, SanskritValue>,
    ): OperationResolution {
        return resolveInvocation(invocation, variables, emptyMap())
    }

    fun resolve(invocation: DhatuInvocation, environment: ValueEnvironment): OperationResolution =
        resolveInvocation(invocation, environment.values, environment.constraints)

    private fun resolveInvocation(
        invocation: DhatuInvocation,
        variables: Map<String, SanskritValue>,
        constraints: Map<String, SemanticValueConstraint>,
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

        val evaluations = mutableListOf<Triple<DhatuOperation, ExecutionContext, SignatureEvaluation>>()
        var examined = 0
        for (operation in named) {
            val accepted = operation.signature.requirements.mapTo(mutableSetOf()) { it.karaka } +
                operation.signature.optionalKarakas
            if (invocation.ambiguousBindings.any { it.candidates.intersect(accepted).isEmpty() }) {
                evaluations += Triple(operation, invocation.executionContext(variables),
                    SignatureEvaluation.Incompatible(
                        "An ambiguous participant has no grammatical role accepted by operation ${operation.name}.",
                    ))
                continue
            }
            var compatibleAssignments = 0
            for (context in contextsFor(invocation, operation, variables)) {
                if (++examined > MAX_ROLE_ASSIGNMENTS) return OperationResolution.Invalid(
                    ExecutionError.INVALID_VALUE,
                    "Grammatical role analysis exceeded its assignment budget; no assignment was selected.",
                )
                val evaluation = evaluate(operation, context, constraints)
                evaluations += Triple(operation, context, evaluation)
                // Two valid assignments already prove ambiguity for this signature.
                if (evaluation == SignatureEvaluation.Compatible && ++compatibleAssignments == 2) break
            }
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
                maximal.map { it.first.name }.distinct(),
                "More than one operation or grammatical role assignment matches dhātu ${dhatu.upadesha}.",
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

    private fun contextsFor(
        invocation: DhatuInvocation,
        operation: DhatuOperation,
        variables: Map<String, SanskritValue>,
    ): Sequence<ExecutionContext> {
        val acceptedKarakas = operation.signature.requirements.mapTo(mutableSetOf()) { it.karaka } +
            operation.signature.optionalKarakas
        fun assignments(index: Int, bindings: Map<Karaka, ExecutionExpression>): Sequence<ExecutionContext> = sequence {
            if (index == invocation.ambiguousBindings.size) {
                yield(invocation.executionContext(variables).copy(bindings = bindings))
                return@sequence
            }
            val ambiguous = invocation.ambiguousBindings[index]
            val matches = ambiguous.candidates intersect acceptedKarakas
            for (karaka in matches) {
                val assigned = (ambiguous.expression as? ExecutionExpression.TypedOperand)?.let { operand ->
                    ambiguous.supSlots[karaka]?.let { operand.copy(sup = it) }
                } ?: ambiguous.expression
                val expression = bindings[karaka]?.let { existing ->
                    ExecutionExpression.Coordination(listOf(existing, assigned))
                } ?: assigned
                yieldAll(assignments(index + 1, bindings + (karaka to expression)))
            }
        }
        return assignments(0, invocation.bindings)
    }

    private fun moreSpecific(left: OperationSignature, right: OperationSignature): Boolean {
        val rightByKaraka = right.requirements.associateBy { it.karaka }
        var strictlyStronger = false
        if (right.copularPredicates.isNotEmpty()) {
            if (left.copularPredicates.isEmpty() || !right.copularPredicates.containsAll(left.copularPredicates)) return false
        }
        if (left.copularPredicates.isNotEmpty() && left.copularPredicates != right.copularPredicates) strictlyStronger = true
        if (!left.forbiddenKarakas.containsAll(right.forbiddenKarakas)) return false
        if (left.forbiddenKarakas != right.forbiddenKarakas) strictlyStronger = true
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
            if (rightRequirement.valueType != null && leftRequirement.valueType != rightRequirement.valueType) return false
            if (leftRequirement != rightRequirement) strictlyStronger = true
        }
        if (right.requirements.any { requirement -> left.requirements.none { it.karaka == requirement.karaka } }) return false
        return strictlyStronger
    }

    private fun evaluate(
        operation: DhatuOperation,
        context: ExecutionContext,
        constraints: Map<String, SemanticValueConstraint>,
    ): SignatureEvaluation {
        val signature = operation.signature
        val predicate = CopularPredicate.from(context.bindings[Karaka.KARMAN])
        if (predicate != null && signature.copularPredicates.isNotEmpty() && predicate !in signature.copularPredicates) {
            return SignatureEvaluation.Incompatible("This operation does not accept the written copular predicate.")
        }
        if (signature.forbiddenKarakas.any { it in context.bindings }) {
            return SignatureEvaluation.Incompatible("This operation does not accept the supplied grammatical roles.")
        }
        val memberSum = dev.panini.execution.NaturalOperationResolver.resolve(operation, context)
            as? dev.panini.execution.NaturalOperation.CollectionSummation
        val missing = signature.requirements.map { it.karaka }.filterNot { it in context.bindings }
        if (missing.isNotEmpty()) return SignatureEvaluation.Missing(missing.toSet())

        signature.requirements.forEach { requirement ->
            if (memberSum != null && requirement.karaka == dev.panini.core.Karaka.KARMAN) {
                // The plural object denotes members through its genitive whole,
                // not a written coordination of unrelated scalar operands.
                return@forEach
            }
            val expression = requireNotNull(context.bindings[requirement.karaka])
            val shape = when (expression) {
                is ExecutionExpression.Pada -> ExpressionShape.LITERAL
                is ExecutionExpression.Coordination -> ExpressionShape.COORDINATION
                is ExecutionExpression.Reference -> ExpressionShape.REFERENCE
                is ExecutionExpression.TypedOperand ->
                    if (expression.referenceName != null) ExpressionShape.REFERENCE else ExpressionShape.LITERAL
            }
            val expectedShape = requirement.shape
            if (expectedShape != null && expectedShape != shape) {
                return SignatureEvaluation.Incompatible(
                    "कारकरूपदोषः: ${requirement.karaka.sanskritName} कारके " +
                        "${expectedShape.sanskritName} अपेक्षितम्, ${shape.sanskritName} प्राप्तम्।",
                )
            }
            val facts = expression.valueFacts(context, constraints)
            if (facts == null || facts.isEmpty()) {
                return SignatureEvaluation.Incompatible(
                    "कारकनिर्देशदोषः: ${requirement.karaka.sanskritName} कारकस्य निर्देशः न सिद्धः।",
                )
            }
            if (facts.size < requirement.minimumMembers) {
                return SignatureEvaluation.Incompatible(
                    "कारकसङ्ख्यादोषः: ${requirement.karaka.sanskritName} कारके न्यूनातिन्यूनं " +
                        "${requirement.minimumMembers} पदानि अपेक्षितानि।",
                )
            }
            val maxMembers = requirement.maximumMembers
            if (maxMembers != null && facts.size > maxMembers) {
                return SignatureEvaluation.Incompatible(
                    "कारकसङ्ख्यादोषः: ${requirement.karaka.sanskritName} कारके अधिकात्यधिकं " +
                        "$maxMembers पदानि स्वीक्रियन्ते।",
                )
            }
            if (facts.any { it.samjnas.isNotEmpty() && !it.samjnas.containsAll(requirement.memberSamjnas) }) {
                return SignatureEvaluation.Incompatible(
                    "कारकसंज्ञादोषः: ${requirement.karaka.sanskritName} कारकस्य प्रत्येकं पदं " +
                        "${requirement.memberSamjnas} संज्ञाः अपेक्षते।",
                )
            }
            if (requirement.valueType?.let { expected -> facts.any { !it.accepts(expected) } } == true) {
                return SignatureEvaluation.Incompatible(
                    "कारकमूल्यदोषः: ${requirement.karaka.sanskritName} कारके ${requirement.valueType} मूल्यम् अपेक्षितम्।",
                )
            }
        }
        return SignatureEvaluation.Compatible
    }

    private data class ValueFact(
        val value: SanskritValue? = null,
        val constraint: SemanticValueConstraint? = null,
    ) {
        val samjnas get() = value?.samjnas ?: constraint?.samjnas.orEmpty()

        fun accepts(expected: OperationValueType): Boolean = value?.let(expected::accepts)
            ?: when (val actual = constraint?.valueType) {
                null -> true
                OperationValueType.INTEGER -> expected == OperationValueType.INTEGER || expected == OperationValueType.NUMBER
                else -> actual == expected
            }
    }

    private fun ExecutionExpression.valueFacts(
        context: ExecutionContext,
        constraints: Map<String, SemanticValueConstraint>,
    ): List<ValueFact>? = when (this) {
        is ExecutionExpression.Pada -> context.resolveValues(this).map { ValueFact(value = it) }
        is ExecutionExpression.TypedOperand -> context.resolveValues(this).map { ValueFact(value = it) }
        is ExecutionExpression.Reference -> context.variables[name]?.let { listOf(ValueFact(value = it)) }
            ?: constraints[name]?.let { listOf(ValueFact(constraint = it)) }
        is ExecutionExpression.Coordination -> buildList {
            members.forEach { member -> addAll(member.valueFacts(context, constraints) ?: return null) }
        }
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
