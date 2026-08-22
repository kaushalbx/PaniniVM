package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionControlSignal
import dev.panini.execution.NamedSamjnaParameterResolver
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import dev.panini.sankhya.PrimitiveSankhya
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
    private var reportedCondition: Boolean? = null

    fun isBreakRequested(): Boolean = breakRequested

    fun consumeBreak(): Boolean = breakRequested.also { breakRequested = false }

    fun clearReportedCondition() {
        reportedCondition = null
    }

    fun requireReportedCondition(): Boolean = reportedCondition
        ?: error("A compiled फल-controlled loop body must produce a truth value.")

    fun publishLoopOutcome(outcome: String, iterations: Long) {
        val outcomeValue = SanskritValue.Shabda(outcome)
        val countWord = dev.panini.sankhya.SankhyaGenerator().cardinal(iterations).final.surface
        val countValue = SanskritValue.Sankhya(iterations, countWord)
        val structured = SanskritValue.Rupa(
            schema = "परिणाम",
            fields = mapOf("अवस्था" to outcomeValue, "प्रयत्नसङ्ख्या" to countValue),
        )
        values["परिणाम"] = structured
        values["प्रयत्नसङ्ख्या"] = countValue
        values["LastResult"] = structured
    }

    fun evaluateLoopTarget(source: String): SanskritValue {
        val outcome = (values["परिणाम"] as? SanskritValue.Rupa)
            ?.fields?.get("अवस्था")?.toDisplayText()
            ?: error("No compiled loop outcome is available for its result target.")
        evaluate("$outcome + अम् $source")
        val structured = values.getValue("परिणाम")
        values["LastResult"] = structured
        return structured
    }

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
        parameterFrames.addLast(names.zip(arguments.map(::resolveFrameArgument)).toMap())
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
        success.conditionValue?.let { reportedCondition = it }
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

    private fun resolveFrameArgument(argument: String): String {
        val stem = argument.substringBefore('+').trim()
        if (stem != "फल") return argument
        val result = values["LastResult"]
            ?: error("A compiled saṃjñā received फल before any operation produced a result.")
        val sourceText = when (result) {
            is SanskritValue.Sankhya -> PrimitiveSankhya.fromValue(result.value)?.pratipadika
                ?: result.toDisplayText()
            else -> result.toDisplayText()
        }
        return argument.replaceFirst(stem, sourceText)
    }

    private fun interpolate(source: String): String = parameterFrames.reversed().fold(source) { text, frame ->
        frame.entries.fold(text) { current, (name, argument) ->
            NamedSamjnaParameterResolver.replace(current, name, argument)
        }
    }
}

class CompiledExecutionLimitExceededException(limit: Long) : IllegalStateException(
    "Compiled condition-controlled execution exhausted its host budget of $limit iterations.",
)
