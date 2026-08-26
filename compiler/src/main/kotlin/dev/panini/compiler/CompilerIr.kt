package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.SanskritValue
import dev.panini.execution.bindingName

/** Backend-neutral instructions produced after grammatical planning. */
internal sealed interface CompilerInstruction {
    data class Constant(val value: SanskritValue) : CompilerInstruction

    data class Load(val name: String) : CompilerInstruction

    data class Store(val name: String) : CompilerInstruction

    data object LoadLastResult : CompilerInstruction

    data class Call(
        val dhatuUpadesha: String,
        val operationName: String,
        val requiredSanadi: String,
        val bindings: Map<Karaka, ExecutionExpression>,
        val destination: String? = null,
        val resultMode: CallResultMode = CallResultMode.VALUE,
    ) : CompilerInstruction

    data class ProcedureCall(
        val methodName: String,
        val parameterNames: List<String>,
        val arguments: List<String>,
        val argumentValues: List<SanskritValue?>,
    ) : CompilerInstruction

    data object Compare : CompilerInstruction

    data class Branch(val target: String, val whenTrue: Boolean = false) : CompilerInstruction

    data class Jump(val target: String) : CompilerInstruction

    data class Label(val name: String) : CompilerInstruction

    data class InitializeCounter(val name: String) : CompilerInstruction

    data class TestCounter(val name: String, val limit: Long) : CompilerInstruction

    data class IncrementCounter(val name: String) : CompilerInstruction

    data object ConsumeBreak : CompilerInstruction

    data object EnterConditionIteration : CompilerInstruction

    data class PublishLoopOutcome(val outcome: String, val counter: String) : CompilerInstruction

    data class InitializeLoopCondition(val name: String) : CompilerInstruction

    data class TestLoopCondition(val name: String, val negated: Boolean) : CompilerInstruction

    data object ClearReportedCondition : CompilerInstruction

    data class CaptureReportedCondition(val name: String) : CompilerInstruction

    data object RequestBreak : CompilerInstruction

    data object Return : CompilerInstruction

    data object ReturnIfBreak : CompilerInstruction
}

internal enum class CallResultMode {
    VALUE,
    BOOLEAN,
    LOOP_TARGET,
}

/** Converts resolved grammatical leaves into a stable compiler representation. */
internal object CompilerIrLowering {
    /** Lowers condition-controlled loops, including bounds and reported-result conditions. */
    fun lowerWhile(
        condition: CompilerInstruction.Call?,
        body: List<CompilerInstruction>,
        maximumIterations: Long? = null,
        exhausted: List<CompilerInstruction> = emptyList(),
        resultTarget: List<CompilerInstruction> = emptyList(),
        usesReportedCondition: Boolean = false,
        negatedReportedCondition: Boolean = false,
        namePrefix: String = "while",
    ): List<CompilerInstruction> {
        require(usesReportedCondition || condition?.resultMode == CallResultMode.BOOLEAN) {
            "While condition must produce a boolean result"
        }
        require(maximumIterations == null || maximumIterations >= 0) {
            "Maximum iterations must not be negative: $maximumIterations"
        }
        val counter = "${namePrefix}_counter"
        val reportedCondition = "${namePrefix}_reported_condition"
        val conditionLabel = "${namePrefix}_condition"
        val victory = "${namePrefix}_victory"
        val exhaustedLabel = "${namePrefix}_exhausted"
        val target = "${namePrefix}_target"
        return buildList {
            add(CompilerInstruction.InitializeCounter(counter))
            if (usesReportedCondition) {
                add(CompilerInstruction.InitializeLoopCondition(reportedCondition))
            }
            add(CompilerInstruction.Label(conditionLabel))
            if (usesReportedCondition) {
                add(CompilerInstruction.TestLoopCondition(reportedCondition, negatedReportedCondition))
            } else {
                add(requireNotNull(condition))
            }
            add(CompilerInstruction.Branch(victory))
            if (maximumIterations != null) {
                add(CompilerInstruction.TestCounter(counter, maximumIterations))
                add(CompilerInstruction.Branch(exhaustedLabel))
            }
            add(CompilerInstruction.EnterConditionIteration)
            if (usesReportedCondition) add(CompilerInstruction.ClearReportedCondition)
            addAll(body)
            add(CompilerInstruction.IncrementCounter(counter))
            if (usesReportedCondition) {
                add(CompilerInstruction.CaptureReportedCondition(reportedCondition))
            }
            add(CompilerInstruction.ConsumeBreak)
            add(CompilerInstruction.Branch(victory, whenTrue = true))
            add(CompilerInstruction.Jump(conditionLabel))
            add(CompilerInstruction.Label(victory))
            add(CompilerInstruction.PublishLoopOutcome("विजय", counter))
            add(CompilerInstruction.Jump(target))
            if (maximumIterations != null) {
                add(CompilerInstruction.Label(exhaustedLabel))
                addAll(exhausted)
                add(CompilerInstruction.PublishLoopOutcome("समाप्ति", counter))
            }
            add(CompilerInstruction.Label(target))
            addAll(resultTarget)
        }.also(CompilerIrVerifier::verify)
    }

    /** Lowers bounded repetition; TestCounter and ConsumeBreak each produce a boolean. */
    fun lowerRepeat(
        count: Int,
        body: List<CompilerInstruction>,
        namePrefix: String = "repeat",
    ): List<CompilerInstruction> {
        require(count >= 0) { "Repetition count must not be negative: $count" }
        val counter = "${namePrefix}_counter"
        val start = "${namePrefix}_start"
        val exit = "${namePrefix}_exit"
        return buildList {
            add(CompilerInstruction.InitializeCounter(counter))
            add(CompilerInstruction.Label(start))
            add(CompilerInstruction.TestCounter(counter, count.toLong()))
            add(CompilerInstruction.Branch(exit))
            addAll(body)
            add(CompilerInstruction.ConsumeBreak)
            add(CompilerInstruction.Branch(exit, whenTrue = true))
            add(CompilerInstruction.IncrementCounter(counter))
            add(CompilerInstruction.Jump(start))
            add(CompilerInstruction.Label(exit))
        }.also(CompilerIrVerifier::verify)
    }

    /**
     * Lowers a conditional into linear IR. [CompilerInstruction.Branch] jumps
     * when the boolean value produced by [condition] is false.
     */
    fun lowerConditional(
        condition: CompilerInstruction.Call,
        consequent: List<CompilerInstruction>,
        alternate: List<CompilerInstruction> = emptyList(),
        labelPrefix: String = "conditional",
    ): List<CompilerInstruction> {
        require(condition.resultMode == CallResultMode.BOOLEAN) {
            "Conditional condition must produce a boolean result"
        }
        val alternateLabel = "${labelPrefix}_alternate"
        val endLabel = "${labelPrefix}_end"
        return buildList {
            add(condition)
            add(CompilerInstruction.Branch(alternateLabel))
            addAll(consequent)
            add(CompilerInstruction.Jump(endLabel))
            add(CompilerInstruction.Label(alternateLabel))
            addAll(alternate)
            add(CompilerInstruction.Label(endLabel))
        }.also(CompilerIrVerifier::verify)
    }

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

/** Checks structural invariants required by every compiler backend. */
internal object CompilerIrVerifier {
    fun verify(instructions: List<CompilerInstruction>) {
        val labels = instructions.filterIsInstance<CompilerInstruction.Label>()
        val duplicate = labels.groupingBy { it.name }.eachCount().entries.firstOrNull { it.value > 1 }
        require(duplicate == null) { "Duplicate IR label: ${duplicate?.key}" }

        val labelNames = labels.mapTo(mutableSetOf()) { it.name }
        instructions.forEach { instruction ->
            val target = when (instruction) {
                is CompilerInstruction.Branch -> instruction.target
                is CompilerInstruction.Jump -> instruction.target
                else -> null
            }
            require(target == null || target in labelNames) { "Unknown IR label: $target" }
        }

        val counters = instructions.filterIsInstance<CompilerInstruction.InitializeCounter>()
        val duplicateCounter = counters.groupingBy { it.name }.eachCount().entries
            .firstOrNull { it.value > 1 }
        require(duplicateCounter == null) { "Duplicate IR counter: ${duplicateCounter?.key}" }
        val counterNames = counters.mapTo(mutableSetOf()) { it.name }
        instructions.forEach { instruction ->
            val counter = when (instruction) {
                is CompilerInstruction.TestCounter -> instruction.name
                is CompilerInstruction.IncrementCounter -> instruction.name
                is CompilerInstruction.PublishLoopOutcome -> instruction.counter
                else -> null
            }
            require(counter == null || counter in counterNames) { "Unknown IR counter: $counter" }
        }

        val conditions = instructions.filterIsInstance<CompilerInstruction.InitializeLoopCondition>()
        val duplicateCondition = conditions.groupingBy { it.name }.eachCount().entries
            .firstOrNull { it.value > 1 }
        require(duplicateCondition == null) {
            "Duplicate IR loop condition: ${duplicateCondition?.key}"
        }
        val conditionNames = conditions.mapTo(mutableSetOf()) { it.name }
        instructions.forEach { instruction ->
            val condition = when (instruction) {
                is CompilerInstruction.TestLoopCondition -> instruction.name
                is CompilerInstruction.CaptureReportedCondition -> instruction.name
                else -> null
            }
            require(condition == null || condition in conditionNames) {
                "Unknown IR loop condition: $condition"
            }
        }
        verifyValueStack(instructions)
    }

    private fun verifyValueStack(instructions: List<CompilerInstruction>) {
        if (instructions.isEmpty()) return
        val labelIndices = instructions.mapIndexedNotNull { index, instruction ->
            (instruction as? CompilerInstruction.Label)?.name?.let { it to index }
        }.toMap()
        val depths = mutableMapOf(0 to 0)
        val pending = ArrayDeque<Int>().apply { add(0) }
        while (pending.isNotEmpty()) {
            val index = pending.removeFirst()
            if (index !in instructions.indices) continue
            val instruction = instructions[index]
            val before = requireNotNull(depths[index])
            val effect = when (instruction) {
                is CompilerInstruction.Constant,
                is CompilerInstruction.Load,
                CompilerInstruction.LoadLastResult,
                is CompilerInstruction.TestCounter,
                CompilerInstruction.ConsumeBreak,
                is CompilerInstruction.TestLoopCondition,
                -> 1
                is CompilerInstruction.Store,
                is CompilerInstruction.Branch,
                -> -1
                is CompilerInstruction.Call -> if (instruction.resultMode == CallResultMode.BOOLEAN) 1 else 0
                else -> 0
            }
            val after = before + effect
            require(after >= 0) { "IR value stack underflow at instruction $index: $instruction" }
            val successors = when (instruction) {
                is CompilerInstruction.Jump -> listOf(requireNotNull(labelIndices[instruction.target]))
                is CompilerInstruction.Branch -> listOfNotNull(
                    requireNotNull(labelIndices[instruction.target]),
                    (index + 1).takeIf { it < instructions.size },
                )
                CompilerInstruction.Return -> emptyList()
                else -> listOfNotNull((index + 1).takeIf { it < instructions.size })
            }
            successors.forEach { successor ->
                val previous = depths.putIfAbsent(successor, after)
                require(previous == null || previous == after) {
                    "IR value stack depth mismatch at instruction $successor: $previous versus $after"
                }
                if (previous == null) pending.add(successor)
            }
            if (successors.isEmpty()) {
                require(after == 0) { "IR leaves $after value(s) on the stack at instruction $index" }
            }
        }
    }
}
