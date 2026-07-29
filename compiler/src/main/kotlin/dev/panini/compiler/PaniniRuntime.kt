package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.shiksha.Samjna
import dev.panini.dhatupatha.DhatuPatha

object PaniniRuntime {
    @JvmStatic
    fun sankhya(value: Long, word: String): SanskritValue {
        return SanskritValue.Sankhya(value, word)
    }

    @JvmStatic
    fun shabda(text: String): SanskritValue {
        return SanskritValue.of(text)
    }

    @JvmStatic
    fun shabdaWithSamjnas(text: String, samjnas: Array<Samjna>): SanskritValue {
        return SanskritValue.of(text, samjnas.toSet())
    }

    @JvmStatic
    fun createPadaExpression(prakriti: String, value: SanskritValue?): ExecutionExpression.Pada {
        return ExecutionExpression.Pada(prakriti, value?.samjnas ?: emptySet(), value)
    }

    @JvmStatic
    fun createCoordinationExpression(members: Array<ExecutionExpression>): ExecutionExpression.Coordination {
        return ExecutionExpression.Coordination(members.toList())
    }

    @JvmStatic
    fun createReferenceExpression(name: String): ExecutionExpression.Reference {
        return ExecutionExpression.Reference(name)
    }

    @JvmStatic
    fun resolveOperation(
        dhatuUpadesha: String,
        operationName: String,
        requiredSanadi: String,
    ): DhatuOperation {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val sanadi = requiredSanadi.split(',').filter(String::isNotEmpty).toSet()
        if (sanadi.isEmpty()) return DhatuPatha.resolveOperation(dhatuUpadesha, operationName)
        return DhatuPatha.all.asSequence()
            .filter { it.upadesha == dhatuUpadesha }
            .flatMap { it.operations.asSequence() }
            .first { it.name == operationName && it.trigger.requiredSanadi == sanadi }
    }

    @JvmStatic
    val defaultDispatcher = dev.panini.execution.external.ExternalCapabilityDispatcher().apply {
        register(dev.panini.execution.ExecutionEffect.NETWORK) { payload, _ ->
            "Simulated dispatch for effect NETWORK with payload '$payload'"
        }
        register(dev.panini.execution.ExecutionEffect.SEND_MESSAGE) { payload, _ ->
            "Simulated dispatch for effect SEND_MESSAGE with payload '$payload'"
        }
        register(dev.panini.execution.ExecutionEffect.EXECUTE_PROCESS) { payload, _ ->
            "Simulated dispatch for effect EXECUTE_PROCESS with payload '$payload'"
        }
    }

    @JvmStatic
    fun execute(
        dhatuUpadesha: String,
        operationName: String,
        requiredSanadi: String,
        bindings: Map<Karaka, ExecutionExpression>,
        variables: Map<String, SanskritValue>
    ): SanskritValue {
        dev.panini.sankhya.SankhyaCountingFormRenderer.init()
        val operation = resolveOperation(dhatuUpadesha, operationName, requiredSanadi)
        val action = operation.action
        val context = ExecutionContext(
            bindings = bindings,
            variables = variables,
            externalDispatcher = defaultDispatcher
        )
        return when (val result = action.execute(context, operation)) {
            is ExecutionResult.Success -> {
                val value = result.typedValue ?: SanskritValue.of(result.value, operation.resultSamjnas)
                println("✓ Result: ${value.toDisplayText()}")
                value
            }
            is ExecutionResult.Failure -> {
                error("PaniniVM Execution Error: ${result.error} - ${result.message}")
            }
            else -> error("Execution resulted in unhandled state: $result")
        }
    }
}
