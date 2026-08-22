package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.bindingName

/** Backend-neutral instructions produced after grammatical planning. */
internal sealed interface CompilerInstruction {
    data class Load(val name: String) : CompilerInstruction

    data class Store(val name: String) : CompilerInstruction

    data class Call(
        val dhatuUpadesha: String,
        val operationName: String,
        val requiredSanadi: String,
        val bindings: Map<Karaka, ExecutionExpression>,
        val destination: String? = null,
        val resultMode: CallResultMode = CallResultMode.VALUE,
    ) : CompilerInstruction

    data object Compare : CompilerInstruction

    data class Branch(val target: String) : CompilerInstruction

    data class Jump(val target: String) : CompilerInstruction

    data class Label(val name: String) : CompilerInstruction

    data object RequestBreak : CompilerInstruction

    data object Return : CompilerInstruction
}

internal enum class CallResultMode {
    VALUE,
    BOOLEAN,
    LOOP_TARGET,
}

/** Converts resolved grammatical leaves into a stable compiler representation. */
internal object CompilerIrLowering {
    fun lowerLeaf(
        plan: ExecutionPlan,
        resultMode: CallResultMode = CallResultMode.VALUE,
    ): CompilerInstruction {
        if (plan.resolved.operation.name == "विजयः") {
            return CompilerInstruction.RequestBreak
        }
        val bindingKaraka = plan.resolved.operation.resultBindingKaraka
        val destination = if (resultMode == CallResultMode.VALUE && bindingKaraka != null) {
            plan.resolved.context.bindings[bindingKaraka]?.bindingName()
                ?: plan.resolved.invocation.bindings[bindingKaraka]?.bindingName()
        } else {
            null
        }
        return CompilerInstruction.Call(
            dhatuUpadesha = plan.resolved.invocation.dhatu.upadesha,
            operationName = plan.resolved.operation.name,
            requiredSanadi = plan.resolved.operation.trigger.requiredSanadi.sorted().joinToString(","),
            bindings = plan.resolved.context.bindings,
            destination = destination,
            resultMode = resultMode,
        )
    }
}
