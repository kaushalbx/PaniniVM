package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionError
import dev.panini.execution.SanskritValue
import dev.panini.core.Karaka
import java.util.LinkedHashMap

/** Mutable execution context shared by all methods in one generated program invocation. */
class CompiledProgramRuntime private constructor(
    private val maxConditionIterations: Long?,
) {
    constructor() : this(null)
    constructor(maxConditionIterations: Long) : this(maxConditionIterations.also {
        require(it > 0L) { "The compiled condition-loop budget must be positive." }
    } as Long?)
    constructor(initialValues: Map<String, SanskritValue>) : this(null) {
        values.putAll(initialValues)
    }

    private val values = LinkedHashMap<String, SanskritValue>()
    private val parameterFrames = ArrayDeque<ParameterFrame>()
    private var conditionIterations = 0L
    private var breakRequested = false
    private var reportedCondition: Boolean? = null

    fun isBreakRequested(): Boolean = breakRequested

    fun consumeBreak(): Boolean = breakRequested.also { breakRequested = false }

    fun requestBreak(): SanskritValue = SanskritValue.Shabda("विजयः").also {
        breakRequested = true
        values["LastResult"] = it
    }

    fun clearReportedCondition() {
        reportedCondition = null
    }

    fun requireReportedCondition(): Boolean = reportedCondition
        ?: throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE,
            "A compiled फल-controlled loop body must produce a truth value.",
        )

    fun enterConditionIteration() {
        val limit = maxConditionIterations
        if (limit != null && conditionIterations >= limit) {
            throw CompiledExecutionLimitExceededException(limit)
        }
        conditionIterations++
    }

    fun enterFrame(names: Array<String>, argumentValues: Array<SanskritValue>) {
        require(names.size == argumentValues.size) {
            "Compiled saṃjñā argument values must match its parameter count."
        }
        val parameterValues = names.indices.associate { index ->
            names[index] to argumentValues[index]
        }
        parameterFrames.addLast(ParameterFrame(parameterValues))
    }

    fun resolveArgument(name: String, fallback: SanskritValue?): SanskritValue = runtimeValue(name)
        ?: runCatching {
            val evaluated = dev.panini.sankhya.SankhyaEvaluator().evaluateStems(listOf(name))
            val word = dev.panini.sankhya.SankhyaGenerator().cardinal(evaluated.value).final.surface
            SanskritValue.Sankhya(evaluated.value, word)
        }.getOrNull()
        ?: fallback
        ?: SanskritValue.of(name)

    fun exitFrame() {
        check(parameterFrames.isNotEmpty()) { "No compiled saṃjñā parameter frame is active." }
        parameterFrames.removeLast()
    }

    fun executeDirectValue(
        dhatuUpadesha: String,
        operationName: String,
        requiredSanadi: String,
        bindings: Map<Karaka, ExecutionExpression>,
    ): SanskritValue {
        val runtimeBindings = bindings.mapValues { (_, expression) ->
            expression.resolveCompiledReferences()
        }
        val value = PaniniRuntime.execute(
            dhatuUpadesha,
            operationName,
            requiredSanadi,
            runtimeBindings,
            values,
        )
        return value
    }

    private fun ExecutionExpression.resolveCompiledReferences(): ExecutionExpression = when (this) {
        is ExecutionExpression.Pada -> runtimeValue(prakriti)?.let { resolved ->
            copy(samjnas = resolved.samjnas, value = resolved)
        } ?: this
        is ExecutionExpression.Coordination -> copy(
            members = members.map { it.resolveCompiledReferences() },
        )
        is ExecutionExpression.Reference -> runtimeValue(name)?.let { resolved ->
            ExecutionExpression.Pada(name, resolved.samjnas, resolved)
        } ?: this
        is ExecutionExpression.TypedOperand -> this
    }

    private fun runtimeValue(name: String): SanskritValue? =
        parameterFrames.reversed().firstNotNullOfOrNull { it.parameterValues[name] }
            ?: values[name]
            ?: if (name == "फल") values["LastResult"] else null

    fun snapshot(): Map<String, SanskritValue> = LinkedHashMap(values)

    internal fun resolveValue(name: String): SanskritValue? = runtimeValue(name)

    fun loadValue(name: String): SanskritValue = runtimeValue(name)
        ?: error("No compiled value is bound to '$name'.")

    fun storeValue(name: String, value: SanskritValue) {
        values[name] = value
        if (name == "LastResult" && value is SanskritValue.Satya) {
            reportedCondition = value.boolean
        }
    }

    private data class ParameterFrame(
        val parameterValues: Map<String, SanskritValue>,
    )
}

class CompiledExecutionLimitExceededException(limit: Long) : IllegalStateException(
    "Compiled condition-controlled execution exhausted its host budget of $limit iterations.",
)
