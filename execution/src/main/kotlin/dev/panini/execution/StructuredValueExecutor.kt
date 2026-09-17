package dev.panini.execution

import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya

/** Owns structured-value attribute lookup, conditional lowering, and attribute pipelines. */
internal class StructuredValueExecutor {
    data class ResolvedInvocation(
        val invocation: Invocation,
        val environment: ValueEnvironment,
    )
    data class AttributePipeline(
        val access: TaddhitaAttributeAccess,
        val targets: List<Invocation>,
    )

    fun containsAttributeCondition(conditional: Conditional): Boolean =
        ((conditional.condition as? Invocation)?.vakya
            ?.let(TaddhitaStructEngine::detectAttributeReference) != null) ||
            (conditional.alternate as? Conditional)?.let(::containsAttributeCondition) == true

    fun executeConditional(
        conditional: Conditional,
        structStore: Map<String, TaddhitaStruct>,
        evaluate: (Conditional, ValueEnvironment) -> ExecutionResult,
    ): ExecutionResult {
        val operands = linkedMapOf<String, SanskritValue>()
        val resolved = resolveAttributeConditions(conditional, structStore, operands)
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "A structured attribute used by the condition could not be resolved.",
            )
        return evaluate(resolved, ValueEnvironment(operands))
    }

    fun resolveInvocation(
        invocation: Invocation,
        structStore: Map<String, TaddhitaStruct>,
    ): ResolvedInvocation? {
        val operands = linkedMapOf<String, SanskritValue>()
        val resolved = replaceAttributeReferences(invocation, structStore, operands) ?: return null
        return ResolvedInvocation(resolved, ValueEnvironment(operands))
    }

    fun resolve(
        access: TaddhitaAttributeAccess,
        structStore: Map<String, TaddhitaStruct>,
        inflectResult: Boolean = false,
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
        if (inflectResult) resolvedValue = inflectAttributeValue(resolvedValue, access.resultAffix)
        return ExecutionResult.Success(
            operation = "taddhita.nested_query",
            value = resolvedValue.toDisplayText(),
            typedValue = resolvedValue,
        )
    }

    fun detectPipeline(program: ProgramNode?): AttributePipeline? {
        val sequence = program as? Sequence ?: return null
        if (sequence.statements.size < 2 || sequence.connectors.any { it != "ततः" }) return null
        val source = sequence.statements.first() as? Invocation ?: return null
        val targets = sequence.statements.drop(1).map { it as? Invocation ?: return null }
        val access = TaddhitaStructEngine.detectAttributeAccess(source.vakya) ?: return null
        return AttributePipeline(access, targets)
    }

    fun executePipeline(
        pipeline: AttributePipeline,
        scope: ExecutionScope,
        structStore: Map<String, TaddhitaStruct>,
        executeTarget: (Invocation, ExecutionScope, SanskritValue) -> List<ExecutionResult>,
        onResult: ((ExecutionResult) -> Unit)?,
    ): List<ExecutionResult> {
        val source = resolve(pipeline.access, structStore, inflectResult = true)
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

    private fun resolveAttributeConditions(
        conditional: Conditional,
        structStore: Map<String, TaddhitaStruct>,
        operands: MutableMap<String, SanskritValue>,
    ): Conditional? {
        val invocation = conditional.condition as? Invocation ?: return null
        val condition = replaceAttributeReferences(invocation, structStore, operands) ?: return null
        val alternate = conditional.alternate?.let {
            if (it is Conditional) resolveAttributeConditions(it, structStore, operands) ?: return null else it
        }
        return conditional.copy(condition = condition, alternate = alternate)
    }

    private fun replaceAttributeReferences(
        invocation: Invocation,
        structStore: Map<String, TaddhitaStruct>,
        operands: MutableMap<String, SanskritValue>,
    ): Invocation? {
        var current = invocation
        while (true) {
            val reference = TaddhitaStructEngine.detectAttributeReference(current.vakya) ?: return current
            val resolved = resolve(reference.access, structStore) as? ExecutionResult.Success ?: return null
            val name = typedOperandName(operands.size)
            operands[name] = requireNotNull(resolved.typedValue)
            val sup = reference.access.resultAffix.upadesha
            val replacement = SubantaPada(
                sourceText = "$name+$sup",
                pratipadika = MulaPratipadika(name, name),
                sup = SupPratyaya(sup, sup),
            )
            val padas = current.vakya.padas.toMutableList().apply {
                subList(reference.padaRange.first, reference.padaRange.last + 1).clear()
                add(reference.padaRange.first, replacement)
            }
            val vakya = current.vakya as? AkhyataVakya ?: return null
            current = current.copy(vakya = vakya.copy(padas = padas))
        }
    }

    private fun inflectAttributeValue(value: SanskritValue, affix: dev.panini.core.SupAffix): SanskritValue =
        when (value) {
            is SanskritValue.Sankhya -> value.copy(
                word = dev.panini.sankhya.SankhyaGenerator().decline(
                    value.value,
                    affix.vibhakti,
                    affix.vacana,
                ),
            )
            is SanskritValue.Shabda -> value.copy(text = deriveSubantaSurface(value.text, affix))
            is SanskritValue.Satya -> value.copy(
                surface = deriveSubantaSurface(if (value.boolean) "सत्य" else "असत्य", affix),
            )
            else -> value
        }

    private fun deriveSubantaSurface(stem: String, affix: dev.panini.core.SupAffix): String = runCatching {
        dev.panini.derivation.SubantaEngine().derive(
            dev.panini.derivation.SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana),
        ).final.surface
    }.getOrDefault(stem)

    private fun typedOperandName(index: Int): String =
        "विशेषणफल" + dev.panini.sankhya.SankhyaGenerator().cardinal(index.toLong() + 1L).final.surface

    private companion object {
        const val PIPE_OPERAND = "विशेषणफल"
    }
}
