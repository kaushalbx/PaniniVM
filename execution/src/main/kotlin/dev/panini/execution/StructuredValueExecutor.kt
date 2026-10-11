package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Sequence

/** Owns structured-value attribute lookup, conditional lowering, and attribute pipelines. */
internal class StructuredValueExecutor {
    data class ResolvedInvocation(
        val invocation: Invocation,
        val environment: ValueEnvironment,
    )

    fun resolveInvocation(
        invocation: Invocation,
        structStore: Map<String, TaddhitaStruct>,
        knownOperationStems: Set<String> = emptySet(),
    ): ResolvedInvocation? {
        val operands = linkedMapOf<String, SanskritValue>()
        val resolved = replaceAttributeReferences(invocation, structStore, operands, knownOperationStems) ?: return null
        return ResolvedInvocation(resolved, ValueEnvironment(operands))
    }

    fun resolve(
        access: TaddhitaAttributeAccess,
        structStore: Map<String, TaddhitaStruct>,
    ): ExecutionResult {
        val chain = access.chain
        var currentObject: TaddhitaStruct? = structStore[chain[0]]
        var resolvedValue: SanskritValue? = null
        var failedStep: String? = null
        for (index in 1 until chain.size) {
            val key = chain[index]
            if (currentObject == null) {
                failedStep = chain[index - 1]
                break
            }
            val typedAttribute = currentObject.typedAttributes[key]
            val attribute = currentObject.attributes[key]
            if (typedAttribute != null || attribute != null) {
                if (index == chain.lastIndex) {
                    resolvedValue = typedAttribute ?: SanskritValue.of(requireNotNull(attribute))
                } else {
                    currentObject = attribute?.let(structStore::get)
                }
            } else if (index == chain.lastIndex) {
                resolvedValue = SanskritValue.Lopa
            } else {
                failedStep = key
                break
            }
        }
        if (resolvedValue == null) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "षष्ठी-असंगतिः: Attribute '$failedStep' not found in nested genitive chain $chain",
            )
        }
        return ExecutionResult.Success(
            operation = "taddhita.nested_query",
            value = resolvedValue.toDisplayText(),
            typedValue = resolvedValue,
        )
    }

    fun executePipeline(
        pipeline: PvmSentenceSemantics.AttributePipeline,
        scope: ExecutionScope,
        structStore: Map<String, TaddhitaStruct>,
        executeTarget: (Invocation, ExecutionScope, SanskritValue) -> List<ExecutionResult>,
        onResult: ((ExecutionResult) -> Unit)?,
    ): List<ExecutionResult> {
        val source = resolve(pipeline.access, structStore)
        if (source !is ExecutionResult.Success) return listOf(source)
        var pipedValue = source.typedValue ?: return listOf(source)
        val results = mutableListOf<ExecutionResult>(source)
        for (target in pipeline.targets) {
            val targetScope = scope.copy(
                environment = scope.environment.mergedWith(
                    ValueEnvironment(mapOf(PIPE_OPERAND to pipedValue)),
                ),
            )
            val targetResults = executeTarget(target, targetScope, pipedValue).map { result ->
                if (result is ExecutionResult.Success && result.outputKind == OutputKind.CONSOLE) {
                    result.copy(typedValue = pipedValue)
                } else result
            }
            results += targetResults
            targetResults.forEach { onResult?.invoke(it) }
            if (targetResults.any { it !is ExecutionResult.Success }) break
            pipedValue = targetResults.filterIsInstance<ExecutionResult.Success>()
                .lastOrNull()?.typedValue ?: pipedValue
        }
        return results
    }


    private fun replaceAttributeReferences(
        invocation: Invocation,
        structStore: Map<String, TaddhitaStruct>,
        operands: MutableMap<String, SanskritValue>,
        knownOperationStems: Set<String> = emptySet(),
    ): Invocation? {
        val projection = AttributeOperandProjection.project(invocation, "__pvm_attribute_${operands.size}_", knownOperationStems)
        projection.operands.forEach { operand ->
            val resolved = resolve(operand.access, structStore) as? ExecutionResult.Success ?: return null
            operands[operand.name] = requireNotNull(resolved.typedValue)
        }
        return projection.invocation
    }


}
