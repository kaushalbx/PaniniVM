package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.SanskritValue
import dev.panini.execution.bindingName

/** A complete backend-neutral compilation unit. */
internal data class CompilerProgram(
    val className: String,
    val entryPoint: List<CompilerInstruction>,
    val procedures: List<CompilerProcedure> = emptyList(),
)

internal data class CompilerProcedure(
    val methodName: String,
    val instructions: List<CompilerInstruction>,
    val parameterNames: List<String> = emptyList(),
)

internal object CompilerProgramVerifier {
    fun verify(program: CompilerProgram) {
        require(program.className.isNotBlank()) { "IR class name must not be blank" }
        val proceduresByName = program.procedures.groupBy(CompilerProcedure::methodName)
        val duplicate = proceduresByName.entries.firstOrNull { it.value.size > 1 }
        require(duplicate == null) { "Duplicate IR procedure: ${duplicate?.key}" }

        CompilerIrVerifier.verify(program.entryPoint)
        program.procedures.forEach { CompilerIrVerifier.verify(it.instructions) }
        verifyFrames(program.entryPoint)
        program.procedures.forEach { verifyFrames(it.instructions) }
        val procedureNames = proceduresByName.keys
        (program.entryPoint + program.procedures.flatMap(CompilerProcedure::instructions))
            .filterIsInstance<CompilerInstruction.InvokeProcedure>()
            .forEach { call ->
                require(call.methodName in procedureNames) {
                    "Unknown IR procedure: ${call.methodName}"
                }
                val expected = proceduresByName.getValue(call.methodName).single().parameterNames.size
                require(call.argumentCount == expected) {
                    "IR procedure ${call.methodName} expects $expected arguments, but received ${call.argumentCount}"
                }
            }
    }

    private fun verifyFrames(instructions: List<CompilerInstruction>) {
        instructions.forEachIndexed { index, instruction ->
            when (instruction) {
                is CompilerInstruction.EnterFrame -> {
                    require(instruction.parameterNames.size == instruction.arguments.size &&
                        instruction.parameterNames.size == instruction.argumentValues.size) {
                        "IR frame argument arrays must have equal sizes at instruction $index"
                    }
                    require(instruction.parameterNames.distinct().size == instruction.parameterNames.size) {
                        "IR frame parameter names must be unique at instruction $index"
                    }
                    require(instructions.getOrNull(index + 1) is CompilerInstruction.InvokeProcedure) {
                        "IR EnterFrame must be followed by InvokeProcedure at instruction $index"
                    }
                }
                is CompilerInstruction.InvokeProcedure -> {
                    val frame = instructions.getOrNull(index - 1) as? CompilerInstruction.EnterFrame
                        ?: throw IllegalArgumentException("IR InvokeProcedure requires EnterFrame at instruction $index")
                    require(frame.parameterNames.size == instruction.argumentCount) {
                        "IR invocation argument count does not match its frame at instruction $index"
                    }
                    require(instructions.getOrNull(index + 1) == CompilerInstruction.ExitFrame) {
                        "IR InvokeProcedure must be followed by ExitFrame at instruction $index"
                    }
                }
                CompilerInstruction.ExitFrame -> require(instructions.getOrNull(index - 1) is CompilerInstruction.InvokeProcedure) {
                    "IR ExitFrame requires InvokeProcedure at instruction $index"
                }
                else -> Unit
            }
        }
    }
}

/** Backend-neutral instructions produced after grammatical planning. */
internal sealed interface CompilerInstruction {
    data class Constant(val value: SanskritValue) : CompilerInstruction

    data class Load(val name: String) : CompilerInstruction

    data class Store(val name: String) : CompilerInstruction

    data class LoadLocal(val name: String) : CompilerInstruction

    data class StoreLocal(val name: String) : CompilerInstruction

    data object LoadLastResult : CompilerInstruction

    data object Duplicate : CompilerInstruction

    data object Pop : CompilerInstruction

    data class BuildList(val size: Int) : CompilerInstruction

    data class BuildRecord(val schema: String, val fields: List<String>) : CompilerInstruction

    data class LoadField(val name: String) : CompilerInstruction

    data class Collection(val operator: CollectionOperator) : CompilerInstruction

    data class Call(
        val dhatuUpadesha: String,
        val operationName: String,
        val requiredSanadi: String,
        val bindings: Map<Karaka, ExecutionExpression>,
    ) : CompilerInstruction

    data class EnterFrame(
        val parameterNames: List<String>,
        val arguments: List<String>,
        val argumentValues: List<SanskritValue?>,
    ) : CompilerInstruction

    data class InvokeProcedure(val methodName: String, val argumentCount: Int) : CompilerInstruction

    data object ExitFrame : CompilerInstruction

    data class Compare(val operator: ComparisonOperator) : CompilerInstruction

    data class Arithmetic(val operator: ArithmeticOperator) : CompilerInstruction

    /** Re-renders a numeric value as the runtime's canonical cardinal form. */
    data object Cardinalize : CompilerInstruction

    data object Booleanize : CompilerInstruction

    data class Branch(val target: String, val whenTrue: Boolean = false) : CompilerInstruction

    data class Jump(val target: String) : CompilerInstruction

    data class Label(val name: String) : CompilerInstruction

    data object ConsumeBreak : CompilerInstruction

    data object EnterConditionIteration : CompilerInstruction

    data class InitializeLoopCondition(val name: String) : CompilerInstruction

    data class TestLoopCondition(val name: String, val negated: Boolean) : CompilerInstruction

    data object ClearReportedCondition : CompilerInstruction

    data class CaptureReportedCondition(val name: String) : CompilerInstruction

    data object RequestBreak : CompilerInstruction

    data object Return : CompilerInstruction

    data object ReturnIfBreak : CompilerInstruction
}

internal enum class ComparisonOperator {
    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
}

internal enum class ArithmeticOperator {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    REMAINDER,
}

internal enum class CollectionOperator {
    LENGTH,
    REVERSE,
    CONCAT,
    INDEX,
    CONTAINS,
    APPEND,
    POP,
    SLICE,
}

/** Converts resolved grammatical leaves into a stable compiler representation. */
internal object CompilerIrLowering {
    /** Lowers condition-controlled loops, including bounds and reported-result conditions. */
    fun lowerWhileInstructions(
        condition: List<CompilerInstruction>?,
        body: List<CompilerInstruction>,
        maximumIterations: Long? = null,
        exhausted: List<CompilerInstruction> = emptyList(),
        resultTarget: List<CompilerInstruction> = emptyList(),
        usesReportedCondition: Boolean = false,
        negatedReportedCondition: Boolean = false,
        namePrefix: String = "while",
    ): List<CompilerInstruction> {
        require(usesReportedCondition || !condition.isNullOrEmpty()) {
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
            add(numberConstant(0))
            add(CompilerInstruction.StoreLocal(counter))
            if (usesReportedCondition) {
                add(CompilerInstruction.InitializeLoopCondition(reportedCondition))
            }
            add(CompilerInstruction.Label(conditionLabel))
            if (usesReportedCondition) {
                add(CompilerInstruction.TestLoopCondition(reportedCondition, negatedReportedCondition))
            } else {
                addAll(requireNotNull(condition))
            }
            add(CompilerInstruction.Branch(victory))
            if (maximumIterations != null) {
                add(CompilerInstruction.LoadLocal(counter))
                add(numberConstant(maximumIterations))
                add(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN))
                add(CompilerInstruction.Branch(exhaustedLabel))
            }
            add(CompilerInstruction.EnterConditionIteration)
            if (usesReportedCondition) add(CompilerInstruction.ClearReportedCondition)
            addAll(body)
            add(CompilerInstruction.LoadLocal(counter))
            add(numberConstant(1))
            add(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD))
            add(CompilerInstruction.StoreLocal(counter))
            if (usesReportedCondition) {
                add(CompilerInstruction.CaptureReportedCondition(reportedCondition))
            }
            add(CompilerInstruction.ConsumeBreak)
            add(CompilerInstruction.Branch(victory, whenTrue = true))
            add(CompilerInstruction.Jump(conditionLabel))
            add(CompilerInstruction.Label(victory))
            addAll(loopOutcome("विजय", counter))
            add(CompilerInstruction.Jump(target))
            if (maximumIterations != null) {
                add(CompilerInstruction.Label(exhaustedLabel))
                addAll(exhausted)
                addAll(loopOutcome("समाप्ति", counter))
            }
            add(CompilerInstruction.Label(target))
            addAll(resultTarget)
        }.also(CompilerIrVerifier::verify)
    }

    fun lowerWhile(
        condition: CompilerInstruction.Call?,
        body: List<CompilerInstruction>,
        maximumIterations: Long? = null,
        exhausted: List<CompilerInstruction> = emptyList(),
        resultTarget: List<CompilerInstruction> = emptyList(),
        usesReportedCondition: Boolean = false,
        negatedReportedCondition: Boolean = false,
        namePrefix: String = "while",
    ): List<CompilerInstruction> = lowerWhileInstructions(
        condition = condition?.let(::listOf),
        body = body,
        maximumIterations = maximumIterations,
        exhausted = exhausted,
        resultTarget = resultTarget,
        usesReportedCondition = usesReportedCondition,
        negatedReportedCondition = negatedReportedCondition,
        namePrefix = namePrefix,
    )

    /** Lowers bounded repetition using ordinary local value and comparison instructions. */
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
            add(numberConstant(0))
            add(CompilerInstruction.StoreLocal(counter))
            add(CompilerInstruction.Label(start))
            add(CompilerInstruction.LoadLocal(counter))
            add(numberConstant(count.toLong()))
            add(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN))
            add(CompilerInstruction.Branch(exit))
            addAll(body)
            add(CompilerInstruction.ConsumeBreak)
            add(CompilerInstruction.Branch(exit, whenTrue = true))
            add(CompilerInstruction.LoadLocal(counter))
            add(numberConstant(1))
            add(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD))
            add(CompilerInstruction.StoreLocal(counter))
            add(CompilerInstruction.Jump(start))
            add(CompilerInstruction.Label(exit))
        }.also(CompilerIrVerifier::verify)
    }

    private fun numberConstant(value: Long): CompilerInstruction.Constant = CompilerInstruction.Constant(
        SanskritValue.Sankhya(value, dev.panini.execution.renderSankhyaResult(value) ?: value.toString()),
    )

    private fun loopOutcome(outcome: String, counter: String): List<CompilerInstruction> = listOf(
        CompilerInstruction.Constant(SanskritValue.Shabda(outcome)),
        CompilerInstruction.LoadLocal(counter),
        CompilerInstruction.Cardinalize,
        CompilerInstruction.Duplicate,
        CompilerInstruction.Store("प्रयत्नसङ्ख्या"),
        CompilerInstruction.BuildRecord("परिणाम", listOf("अवस्था", "प्रयत्नसङ्ख्या")),
        CompilerInstruction.Duplicate,
        CompilerInstruction.Store("परिणाम"),
        CompilerInstruction.Store("LastResult"),
    )

    /**
     * Lowers a conditional into linear IR. [CompilerInstruction.Branch] jumps
     * when the boolean value produced by [condition] is false.
     */
    fun lowerConditional(
        condition: List<CompilerInstruction>,
        consequent: List<CompilerInstruction>,
        alternate: List<CompilerInstruction> = emptyList(),
        labelPrefix: String = "conditional",
    ): List<CompilerInstruction> {
        require(condition.isNotEmpty()) { "Conditional condition must produce a boolean result" }
        val alternateLabel = "${labelPrefix}_alternate"
        val endLabel = "${labelPrefix}_end"
        return buildList {
            addAll(condition)
            add(CompilerInstruction.Branch(alternateLabel))
            addAll(consequent)
            add(CompilerInstruction.Jump(endLabel))
            add(CompilerInstruction.Label(alternateLabel))
            addAll(alternate)
            add(CompilerInstruction.Label(endLabel))
        }.also(CompilerIrVerifier::verify)
    }

    fun lowerConditional(
        condition: CompilerInstruction.Call,
        consequent: List<CompilerInstruction>,
        alternate: List<CompilerInstruction> = emptyList(),
        labelPrefix: String = "conditional",
    ): List<CompilerInstruction> {
        return lowerConditional(
            listOf(
                condition,
                CompilerInstruction.Duplicate,
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Booleanize,
            ),
            consequent,
            alternate,
            labelPrefix,
        )
    }

    fun lowerLeaf(plan: ExecutionPlan): CompilerInstruction {
        if (plan.resolved.operation.name == "विजयः") {
            return CompilerInstruction.RequestBreak
        }
        return CompilerInstruction.Call(
            dhatuUpadesha = plan.resolved.invocation.dhatu.upadesha,
            operationName = plan.resolved.operation.name,
            requiredSanadi = plan.resolved.operation.trigger.requiredSanadi.sorted().joinToString(","),
            bindings = plan.resolved.context.bindings,
        )
    }

    /** Makes ordinary leaf result flow explicit through LastResult and destination stores. */
    fun lowerLeafValues(plan: ExecutionPlan): List<CompilerInstruction> {
        lowerPrimitiveLeafValues(plan)?.let { return it }
        val lowered = lowerLeaf(plan)
        if (lowered !is CompilerInstruction.Call) return listOf(lowered)
        val bindingKaraka = plan.resolved.operation.resultBindingKaraka
        val destination = bindingKaraka?.let {
            plan.resolved.context.bindings[it]?.bindingName()
                ?: plan.resolved.invocation.bindings[it]?.bindingName()
        }
        return buildList {
            add(lowered)
            if (destination != null) add(CompilerInstruction.Duplicate)
            add(CompilerInstruction.Store("LastResult"))
            destination?.let { add(CompilerInstruction.Store(it)) }
        }.also(CompilerIrVerifier::verify)
    }

    fun lowerLoopTarget(plan: ExecutionPlan): List<CompilerInstruction> = buildList {
        add(CompilerInstruction.Load("परिणाम"))
        add(CompilerInstruction.LoadField("अवस्था"))
        add(CompilerInstruction.Store("चक्रफल"))
        add(lowerLeaf(plan))
        add(CompilerInstruction.Pop)
        add(CompilerInstruction.Load("परिणाम"))
        add(CompilerInstruction.Store("LastResult"))
    }.also(CompilerIrVerifier::verify)

    /** Lowers a numeric comparison into operand loads followed by a boolean comparison. */
    fun lowerCondition(plan: ExecutionPlan): List<CompilerInstruction> {
        if (plan.resolved.operation.name != "सङ्ख्यातुलना") {
            return lowerGenericCondition(plan)
        }
        val operands = plan.resolved.context.bindings[Karaka.KARMAN]
            ?.let(::lowerOperands)
            .orEmpty()
        if (operands.size < 2) return lowerGenericCondition(plan)
        val operator = if ("नि" in plan.resolved.operation.trigger.requiredUpasargas) {
            ComparisonOperator.LESS_THAN
        } else {
            ComparisonOperator.GREATER_THAN
        }
        return buildList {
            addAll(operands[0])
            addAll(operands[1])
            add(CompilerInstruction.Compare(operator))
        }
    }

    private fun lowerGenericCondition(plan: ExecutionPlan): List<CompilerInstruction> = listOf(
        lowerLeaf(plan),
        CompilerInstruction.Duplicate,
        CompilerInstruction.Store("LastResult"),
        CompilerInstruction.Booleanize,
    )

    /** Lowers primitive numeric folds and single-value assignment without action dispatch. */
    private fun lowerPrimitiveLeafValues(plan: ExecutionPlan): List<CompilerInstruction>? {
        val operation = plan.resolved.operation.name
        val arithmetic = when (operation) {
            "सङ्ख्यायोजनम्" -> ArithmeticOperator.ADD
            "सङ्ख्यावियोगः" -> ArithmeticOperator.SUBTRACT
            "सङ्ख्यागुणनम्" -> ArithmeticOperator.MULTIPLY
            "सङ्ख्याहरणम्" -> ArithmeticOperator.DIVIDE
            "सङ्ख्याशेषः" -> ArithmeticOperator.REMAINDER
            else -> null
        }
        val collection = when (operation) {
            "सूच्याकारः" -> CollectionOperator.LENGTH
            "सूचीविलोमः" -> CollectionOperator.REVERSE
            "सूचीसंयोगः" -> CollectionOperator.CONCAT
            "सूचीस्थानम्" -> CollectionOperator.INDEX
            "सूच्यस्तित्वम्" -> CollectionOperator.CONTAINS
            "सूचीनिक्षेपणम्" -> CollectionOperator.APPEND
            "सूच्युद्धरणम्" -> CollectionOperator.POP
            "सूचीविभागः" -> CollectionOperator.SLICE
            else -> null
        }
        val operands = plan.resolved.context.bindings[Karaka.KARMAN]
            ?.let(::lowerOperands)
            ?: return null
        val valueInstructions = when {
            arithmetic != null && operands.isNotEmpty() -> buildList {
                addAll(operands.first())
                operands.drop(1).forEach { operand ->
                    addAll(operand)
                    add(CompilerInstruction.Arithmetic(arithmetic))
                }
            }
            collection in setOf(CollectionOperator.LENGTH, CollectionOperator.REVERSE) && operands.size == 1 -> operands.single() +
                CompilerInstruction.Collection(requireNotNull(collection))
            collection == CollectionOperator.CONCAT -> {
                val separateRight = plan.resolved.context.bindings[Karaka.SAMPRADANA]
                    ?.let(::lowerSingleCollectionValue)
                val (left, right) = if (separateRight != null) {
                    val separateLeft = plan.resolved.context.bindings[Karaka.KARMAN]
                        ?.let(::lowerSingleCollectionValue)
                        ?: return null
                    separateLeft to separateRight
                } else {
                    if (operands.size != 2) return null
                    operands[0] to operands[1]
                }
                left + right + CompilerInstruction.Collection(CollectionOperator.CONCAT)
            }
            collection == CollectionOperator.INDEX -> {
                val list = plan.resolved.context.bindings[Karaka.KARMAN]
                    ?.let(::lowerSingleCollectionValue)
                    ?: return null
                val index = plan.resolved.context.bindings[Karaka.KARANA]
                    ?.let(::lowerOperand)
                    ?: return null
                list + index + CompilerInstruction.Collection(CollectionOperator.INDEX)
            }
            collection == CollectionOperator.CONTAINS -> {
                val list = plan.resolved.context.bindings[Karaka.KARMAN]
                    ?.let(::lowerSingleCollectionValue)
                    ?: return null
                val query = (plan.resolved.context.bindings[Karaka.KARANA]
                    ?: plan.resolved.context.bindings[Karaka.KARTR])
                    ?.let(::lowerOperand)
                    ?: return null
                list + query + CompilerInstruction.Collection(CollectionOperator.CONTAINS)
            }
            collection == CollectionOperator.APPEND -> {
                val expression = plan.resolved.context.bindings[Karaka.KARMAN]
                    ?: plan.resolved.context.bindings[Karaka.ADHIKARANA]
                    ?: return null
                val members = (expression as? ExecutionExpression.Coordination)?.members ?: return null
                if (members.size != 2) return null
                val list = lowerSingleCollectionValue(members[0]) ?: return null
                val item = lowerOperand(members[1]) ?: return null
                list + item + CompilerInstruction.Collection(CollectionOperator.APPEND)
            }
            collection == CollectionOperator.POP -> {
                val list = plan.resolved.context.bindings[Karaka.KARMAN]
                    ?.let(::lowerSingleCollectionValue)
                    ?: return null
                list + CompilerInstruction.Collection(CollectionOperator.POP)
            }
            collection == CollectionOperator.SLICE -> {
                val list = plan.resolved.context.bindings[Karaka.KARMAN]
                    ?.let(::lowerSingleCollectionValue)
                    ?: return null
                val start = plan.resolved.context.bindings[Karaka.KARANA]
                    ?.let(::lowerOperand)
                    ?: return null
                val end = plan.resolved.context.bindings[Karaka.SAMPRADANA]
                    ?.let(::lowerOperand)
                    ?: return null
                list + start + end + CompilerInstruction.Collection(CollectionOperator.SLICE)
            }
            operation == "मूल्यदानम्" -> plan.resolved.context.bindings[Karaka.KARMAN]
                ?.let(::lowerAssignmentOperand)
                ?: return null
            else -> return null
        }
        val destination = plan.resolved.operation.resultBindingKaraka
            ?.let(plan.resolved.context.bindings::get)
            ?.bindingName()
            ?: plan.resolved.operation.resultBindingKaraka
                ?.let(plan.resolved.invocation.bindings::get)
                ?.bindingName()
        return buildList {
            addAll(valueInstructions)
            if (destination != null) add(CompilerInstruction.Duplicate)
            add(CompilerInstruction.Store("LastResult"))
            destination?.let { add(CompilerInstruction.Store(it)) }
        }.also(CompilerIrVerifier::verify)
    }

    private fun lowerOperands(expression: ExecutionExpression): List<List<CompilerInstruction>>? = when (expression) {
        is ExecutionExpression.Coordination -> expression.members.map { member ->
            lowerOperand(member) ?: return null
        }
        else -> listOf(lowerOperand(expression) ?: return null)
    }

    private fun lowerOperand(expression: ExecutionExpression): List<CompilerInstruction>? = when (expression) {
        is ExecutionExpression.Pada -> expression.value?.let {
            listOf(CompilerInstruction.Constant(it))
        } ?: listOf(CompilerInstruction.Load(expression.prakriti))
        is ExecutionExpression.TypedOperand -> listOf(CompilerInstruction.Constant(expression.value))
        is ExecutionExpression.Reference -> listOf(
            if (expression.name == "फल") CompilerInstruction.LoadLastResult
            else CompilerInstruction.Load(expression.name),
        )
        is ExecutionExpression.Coordination -> null
    }

    private fun lowerSingleCollectionValue(expression: ExecutionExpression): List<CompilerInstruction>? = when (expression) {
        is ExecutionExpression.Reference -> lowerOperand(expression)
        is ExecutionExpression.Pada -> when (val value = expression.value) {
            null -> listOf(CompilerInstruction.Load(expression.prakriti))
            is SanskritValue.Suchi, is SanskritValue.Gana -> listOf(CompilerInstruction.Constant(value))
            is SanskritValue.Sankhya -> value.takeIf { it.word == expression.prakriti }
                ?.let { listOf(CompilerInstruction.Load(expression.prakriti)) }
            is SanskritValue.Shabda -> value.takeIf { it.text == expression.prakriti }
                ?.let { listOf(CompilerInstruction.Load(expression.prakriti)) }
            else -> null
        }
        is ExecutionExpression.TypedOperand -> expression.value
            .takeIf { it is SanskritValue.Suchi || it is SanskritValue.Gana }
            ?.let { listOf(CompilerInstruction.Constant(it)) }
        is ExecutionExpression.Coordination -> null
    }

    private fun lowerAssignmentOperand(expression: ExecutionExpression): List<CompilerInstruction>? = when (expression) {
        is ExecutionExpression.Reference -> lowerOperand(expression)
        is ExecutionExpression.Pada -> expression.takeIf { it.value == null }?.let(::lowerOperand)
        is ExecutionExpression.Coordination -> buildList {
            expression.members.forEach { member ->
                addAll(lowerAssignmentOperand(member) ?: return null)
            }
            add(CompilerInstruction.BuildList(expression.members.size))
        }
        is ExecutionExpression.TypedOperand -> null
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

        val localNames = instructions.filterIsInstance<CompilerInstruction.StoreLocal>()
            .mapTo(mutableSetOf()) { it.name }
        instructions.forEach { instruction ->
            val local = (instruction as? CompilerInstruction.LoadLocal)?.name
            require(local == null || local in localNames) { "Unknown IR local: $local" }
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
        val stacks = mutableMapOf(0 to emptyList<ValueKind>())
        val pending = ArrayDeque<Int>().apply { add(0) }
        while (pending.isNotEmpty()) {
            val index = pending.removeFirst()
            if (index !in instructions.indices) continue
            val instruction = instructions[index]
            val before = requireNotNull(stacks[index])
            val after = applyStackEffect(before, instruction, index)
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
                val previous = stacks.putIfAbsent(successor, after)
                require(previous == null || previous == after) {
                    "IR value stack mismatch at instruction $successor: $previous versus $after"
                }
                if (previous == null) pending.add(successor)
            }
            if (successors.isEmpty()) {
                require(after.isEmpty()) {
                    "IR leaves ${after.size} value(s) on the stack at instruction $index"
                }
            }
        }
    }

    private fun applyStackEffect(
        before: List<ValueKind>,
        instruction: CompilerInstruction,
        index: Int,
    ): List<ValueKind> {
        fun pop(expected: ValueKind? = null): Pair<List<ValueKind>, ValueKind> {
            require(before.isNotEmpty()) { "IR value stack underflow at instruction $index: $instruction" }
            val actual = before.last()
            require(expected == null || actual == expected || actual == ValueKind.UNKNOWN) {
                "IR expected $expected but found $actual at instruction $index: $instruction"
            }
            return before.dropLast(1) to actual
        }
        return when (instruction) {
            is CompilerInstruction.Constant -> before + if (instruction.value is SanskritValue.Sankhya) {
                ValueKind.NUMBER
            } else {
                ValueKind.VALUE
            }
            is CompilerInstruction.Load,
            is CompilerInstruction.LoadLocal,
            CompilerInstruction.LoadLastResult,
            -> before + ValueKind.UNKNOWN
            CompilerInstruction.Duplicate -> {
                val value = pop().second
                before + value
            }
            CompilerInstruction.Pop -> pop().first
            is CompilerInstruction.BuildList -> {
                require(instruction.size >= 0) { "IR list size must not be negative at instruction $index" }
                require(before.size >= instruction.size) {
                    "IR value stack underflow at instruction $index: $instruction"
                }
                before.dropLast(instruction.size) + ValueKind.VALUE
            }
            is CompilerInstruction.BuildRecord -> {
                require(instruction.fields.distinct().size == instruction.fields.size) {
                    "IR record fields must be unique at instruction $index"
                }
                require(before.size >= instruction.fields.size) {
                    "IR value stack underflow at instruction $index: $instruction"
                }
                before.dropLast(instruction.fields.size) + ValueKind.VALUE
            }
            is CompilerInstruction.LoadField -> pop().first + ValueKind.UNKNOWN
            is CompilerInstruction.Collection -> {
                val afterRight = pop().first
                val arity = when (instruction.operator) {
                    CollectionOperator.CONCAT,
                    CollectionOperator.INDEX,
                    CollectionOperator.CONTAINS,
                    CollectionOperator.APPEND,
                    -> 2
                    CollectionOperator.SLICE -> 3
                    CollectionOperator.LENGTH,
                    CollectionOperator.REVERSE,
                    CollectionOperator.POP,
                    -> 1
                }
                require(before.size >= arity) {
                        "IR value stack underflow at instruction $index: $instruction"
                }
                val remaining = before.dropLast(arity)
                remaining + if (instruction.operator == CollectionOperator.LENGTH) {
                    ValueKind.NUMBER
                } else {
                    ValueKind.VALUE
                }
            }
            is CompilerInstruction.Store, is CompilerInstruction.StoreLocal -> pop().first
            is CompilerInstruction.Compare -> {
                val afterRight = pop().first
                require(afterRight.isNotEmpty()) {
                    "IR value stack underflow at instruction $index: $instruction"
                }
                afterRight.dropLast(1) + ValueKind.BOOLEAN
            }
            is CompilerInstruction.Arithmetic -> {
                val afterRight = pop(ValueKind.NUMBER).first
                require(afterRight.isNotEmpty()) {
                    "IR value stack underflow at instruction $index: $instruction"
                }
                val left = afterRight.last()
                require(left == ValueKind.NUMBER || left == ValueKind.UNKNOWN) {
                    "IR arithmetic requires numeric operands at instruction $index"
                }
                afterRight.dropLast(1) + ValueKind.NUMBER
            }
            CompilerInstruction.Cardinalize -> pop(ValueKind.NUMBER).first + ValueKind.NUMBER
            CompilerInstruction.Booleanize -> pop().first + ValueKind.BOOLEAN
            is CompilerInstruction.Branch -> pop(ValueKind.BOOLEAN).first
            CompilerInstruction.ConsumeBreak,
            is CompilerInstruction.TestLoopCondition,
            -> before + ValueKind.BOOLEAN
            is CompilerInstruction.Call -> before + ValueKind.UNKNOWN
            else -> before
        }
    }

    private enum class ValueKind {
        VALUE,
        UNKNOWN,
        NUMBER,
        BOOLEAN,
    }
}
