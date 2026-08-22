package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.core.SupAffix
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
        return SanskritValue.Shabda(text, samjnas.toSet())
    }

    @JvmStatic
    fun shabdaWithEncodedSamjnas(text: String, samjnas: Array<String>): SanskritValue = SanskritValue.Shabda(
        text,
        samjnas.mapTo(linkedSetOf()) { encoded ->
            if (encoded.startsWith("RUDHI:")) {
                Samjna.Rudhi(encoded.removePrefix("RUDHI:"))
            } else {
                Samjna.valueOf(encoded)
            }
        },
    )

    @JvmStatic
    fun suchi(items: Array<SanskritValue>): SanskritValue = SanskritValue.Suchi(items.toList())

    @JvmStatic
    fun gana(elements: Array<SanskritValue>): SanskritValue = SanskritValue.Gana(elements.toList())

    @JvmStatic
    fun rupa(schema: String, names: Array<String>, values: Array<SanskritValue>): SanskritValue {
        require(names.size == values.size) { "Structured value field names and values must have equal sizes." }
        return SanskritValue.Rupa(schema, names.indices.associate { names[it] to values[it] })
    }

    @JvmStatic
    fun satya(boolean: Boolean, surface: String?): SanskritValue = SanskritValue.Satya(boolean, surface)

    @JvmStatic
    fun range(minimum: SanskritValue.Sankhya, maximum: SanskritValue.Sankhya): SanskritValue =
        SanskritValue.Range(minimum, maximum)

    @JvmStatic
    fun rational(numerator: Long, denominator: Long, word: String): SanskritValue =
        SanskritValue.Rational(numerator, denominator, word)

    @JvmStatic
    fun lopa(): SanskritValue = SanskritValue.Lopa

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
    fun createTypedOperandExpression(
        value: SanskritValue,
        sup: SupAffix,
    ): ExecutionExpression.TypedOperand = ExecutionExpression.TypedOperand(value, sup)

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
                value
            }
            is ExecutionResult.Failure -> {
                error("PaniniVM Execution Error: ${result.error} - ${result.message}")
            }
            else -> error("Execution resulted in unhandled state: $result")
        }
    }
}
