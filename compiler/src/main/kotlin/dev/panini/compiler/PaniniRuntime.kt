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
    fun resolveOperation(dhatuUpadesha: String, operationName: String): DhatuOperation {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        val normalized = dhatuUpadesha.trimEnd('्', 'ँ')
        val dhatu = DhatuPatha.all.firstOrNull { 
            (it.upadesha == dhatuUpadesha || 
             it.id == dhatuUpadesha ||
             it.upadesha.trimEnd('्', 'ँ') == normalized ||
             it.id.trimEnd('्', 'ँ') == normalized) &&
            it.operations.any { op -> op.name == operationName }
        } ?: DhatuPatha.all.firstOrNull { 
            it.upadesha == dhatuUpadesha || 
            it.id == dhatuUpadesha ||
            it.upadesha.trimEnd('्', 'ँ') == normalized ||
            it.id.trimEnd('्', 'ँ') == normalized
        } ?: error("Dhātu not found in registry: $dhatuUpadesha")
        return dhatu.operations.firstOrNull { it.name == operationName }
            ?: error("Operation '$operationName' not found for dhātu $dhatuUpadesha")
    }

    @JvmStatic
    fun execute(
        dhatuUpadesha: String,
        operationName: String,
        bindings: Map<Karaka, ExecutionExpression>,
        variables: Map<String, SanskritValue>
    ): SanskritValue {
        dev.panini.sankhya.SankhyaCountingFormRenderer.init()
        val operation = resolveOperation(dhatuUpadesha, operationName)
        val action = operation.action
        val context = ExecutionContext(
            bindings = bindings,
            variables = variables
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
