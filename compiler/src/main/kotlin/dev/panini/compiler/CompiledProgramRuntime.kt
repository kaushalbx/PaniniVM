package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionControlSignal
import dev.panini.execution.NamedSamjnaParameterResolver
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import java.util.LinkedHashMap
import java.util.UUID

/** Mutable execution context shared by all methods in one generated program invocation. */
class CompiledProgramRuntime private constructor(
    private val maxConditionIterations: Long?,
) {
    constructor() : this(null)
    constructor(maxConditionIterations: Long) : this(maxConditionIterations.also {
        require(it > 0L) { "The compiled condition-loop budget must be positive." }
    } as Long?)

    private val vm = PaniniVM()
    private val sessionKey = "compiled-${UUID.randomUUID()}"
    private val values = LinkedHashMap<String, SanskritValue>()
    private val parameterFrames = ArrayDeque<Map<String, String>>()
    private var conditionIterations = 0L
    private var breakRequested = false

    fun isBreakRequested(): Boolean = breakRequested

    fun consumeBreak(): Boolean = breakRequested.also { breakRequested = false }

    fun enterConditionIteration() {
        val limit = maxConditionIterations
        if (limit != null && conditionIterations >= limit) {
            throw CompiledExecutionLimitExceededException(limit)
        }
        conditionIterations++
    }

    fun enterFrame(names: Array<String>, arguments: Array<String>) {
        require(names.size == arguments.size) {
            "Compiled saṃjñā expected ${names.size} arguments, but received ${arguments.size}."
        }
        parameterFrames.addLast(names.zip(arguments).toMap())
    }

    fun exitFrame() {
        check(parameterFrames.isNotEmpty()) { "No compiled saṃjñā parameter frame is active." }
        parameterFrames.removeLast()
    }

    fun evaluate(source: String): SanskritValue {
        val result = vm.eval(interpolate(source), sessionKey = sessionKey)
        val success = result as? ExecutionResult.Success
            ?: error("Compiled PaniniVM operation failed: $result")
        if (success.controlSignal == ExecutionControlSignal.BREAK_LOOP) breakRequested = true
        val value = success.typedValue ?: SanskritValue.of(success.value)
        values["LastResult"] = value
        return value
    }

    fun evaluateBoolean(source: String): Boolean {
        val result = vm.eval(interpolate(source), sessionKey = sessionKey)
        val success = result as? ExecutionResult.Success
            ?: error("Compiled PaniniVM condition failed: $result")
        val condition = success.conditionValue ?: (success.typedValue as? SanskritValue.Satya)?.boolean
        return condition ?: error("Compiled PaniniVM condition did not produce सत्य/असत्य: $source")
    }

    fun snapshot(): Map<String, SanskritValue> = LinkedHashMap(values)

    private fun interpolate(source: String): String = parameterFrames.reversed().fold(source) { text, frame ->
        frame.entries.fold(text) { current, (name, argument) ->
            NamedSamjnaParameterResolver.replace(current, name, argument)
        }
    }
}

class CompiledExecutionLimitExceededException(limit: Long) : IllegalStateException(
    "Compiled condition-controlled execution exhausted its host budget of $limit iterations.",
)
