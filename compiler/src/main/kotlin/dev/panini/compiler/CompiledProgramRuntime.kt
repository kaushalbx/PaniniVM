package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import java.util.LinkedHashMap
import java.util.UUID

/** Mutable execution context shared by all methods in one generated program invocation. */
class CompiledProgramRuntime {
    private val vm = PaniniVM()
    private val sessionKey = "compiled-${UUID.randomUUID()}"
    private val values = LinkedHashMap<String, SanskritValue>()

    fun evaluate(source: String): SanskritValue {
        val result = vm.eval(source, sessionKey = sessionKey)
        val success = result as? ExecutionResult.Success
            ?: error("Compiled PaniniVM operation failed: $result")
        val value = success.typedValue ?: SanskritValue.of(success.value)
        values["LastResult"] = value
        return value
    }

    fun evaluateBoolean(source: String): Boolean {
        val result = vm.eval(source, sessionKey = sessionKey)
        val success = result as? ExecutionResult.Success
            ?: error("Compiled PaniniVM condition failed: $result")
        val condition = success.conditionValue ?: (success.typedValue as? SanskritValue.Satya)?.boolean
        return condition ?: error("Compiled PaniniVM condition did not produce सत्य/असत्य: $source")
    }

    fun snapshot(): Map<String, SanskritValue> = LinkedHashMap(values)
}
