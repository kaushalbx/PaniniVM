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
    private var resultEpoch = Any()
    // Runtime history is separate from user variables and LastResult. Records
    // must be emitted only after an action actually completes successfully.
    private data class CompletedAction(
        val result: SanskritValue,
        val participants: Map<dev.panini.core.Karaka, List<SanskritValue>>,
    )
    private val actionResults = LinkedHashMap<String, MutableList<CompletedAction>>()
    private var conditionIterations = 0L
    private var breakRequested = false
    private var reportedCondition: Boolean? = null

    fun isBreakRequested(): Boolean = breakRequested

    fun resolveRecordReference(value: SanskritValue): SanskritValue {
        val name = (value as? SanskritValue.Shabda)?.text
            ?: throw IllegalArgumentException("An intermediate genitive field must name a record.")
        return values[name] as? SanskritValue.Rupa
            ?: throw IllegalArgumentException("No record named '$name' exists for the nested genitive reference.")
    }

    fun consumeBreak(): Boolean = breakRequested.also { breakRequested = false }

    fun requestBreak(): SanskritValue = SanskritValue.Shabda("विजयः").also {
        breakRequested = true
        storeValue("LastResult", it)
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

    fun checkCollectionIteration(index: SanskritValue) {
        val iteration = (index as SanskritValue.Sankhya).value
        val limit = maxConditionIterations
        if (limit != null && iteration > limit) throw CompiledExecutionLimitExceededException(limit)
    }

    fun enterFrame(names: Array<String>, argumentValues: Array<SanskritValue>) =
        enterFrame(names, argumentValues, positionalAliases = true)

    private fun enterFrame(names: Array<String>, argumentValues: Array<SanskritValue>, positionalAliases: Boolean) {
        require(names.size == argumentValues.size) {
            "Compiled saṃjñā argument values must match its parameter count."
        }
        val parameterValues = linkedMapOf<String, SanskritValue>()
        val aliases = linkedMapOf<String, Set<String>>()
        names.indices.forEach { index ->
            val group = if (positionalAliases) setOf(names[index],
                dev.panini.execution.PrakriyaAstArgumentBinder.referenceKey(index)) else setOf(names[index])
            group.forEach { name -> parameterValues[name] = argumentValues[index]; aliases[name] = group }
        }
        parameterFrames.addLast(ParameterFrame(parameterValues, resultEpoch, aliases = aliases))
    }

    /** A caller's existing discourse result does not satisfy a callee's contract. */
    fun requireProcedureResult(kind: String) {
        // A break is a control effect, not a normal procedure return.
        if (breakRequested) return
        val frame = parameterFrames.lastOrNull()
            ?: error("No compiled procedure frame is active.")
        if (frame.resultEpoch === resultEpoch) throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "Procedure declared a result but produced none.")
        CompilerValueOperations.requireResultKind(loadValue("LastResult"), kind)
    }

    fun requireProcedureResultSchema(schema: String, fields: Array<String>) {
        if (breakRequested) return
        requireProcedureResult("RECORD")
        val result = loadValue("LastResult") as SanskritValue.Rupa
        if (result.schema != schema || result.fields.keys != fields.toSet()) {
            throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "Procedure must return schema '$schema' with fields ${fields.toList()}, " +
                    "not '${result.schema}' with fields ${result.fields.keys}.")
        }
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
        val frame = parameterFrames.removeLast()
        frame.savedBindings?.forEach { (name, value) ->
            if (value == null) values.remove(name) else values[name] = value
        }
    }

    fun enterBindingFrame(names: Array<String>, argumentValues: Array<SanskritValue>) {
        enterFrame(names, argumentValues, positionalAliases = false)
        val frame = parameterFrames.removeLast()
        parameterFrames.addLast(frame.copy(savedBindings = names.associateWith { values[it] }))
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
        is ExecutionExpression.TypedOperand -> referenceName?.let(::runtimeValue)?.let { copy(value = it) } ?: this
    }

    private fun runtimeValue(name: String): SanskritValue? =
        parameterFrames.reversed().firstNotNullOfOrNull { it.parameterValues[name] }
            ?: values[name]

    fun snapshot(): Map<String, SanskritValue> = LinkedHashMap(values)

    internal fun resolveValue(name: String): SanskritValue? = runtimeValue(name)

    /** [dhatuUpadesha] is a canonical lexical identity supplied by the compiler. */
    fun recordActionResult(dhatuUpadesha: String, value: SanskritValue) {
        recordActionFrame(dhatuUpadesha, value, emptyMap())
    }

    /** Records one successful action atomically, keeping result and participants aligned. */
    fun recordActionFrame(
        dhatuUpadesha: String,
        value: SanskritValue,
        participants: Map<dev.panini.core.Karaka, List<SanskritValue>>,
    ) {
        require(dhatuUpadesha.isNotBlank()) { "An action result requires a canonical dhatu identity." }
        require(dev.panini.core.Karaka.ANIRDHARITA !in participants) { "Action participants require resolved kāraka relations." }
        val snapshot = participants.mapValues { (_, members) -> members.map(::historySnapshot) }
        val completed = CompletedAction(historySnapshot(value), snapshot)
        actionResults.getOrPut(dhatuUpadesha, ::mutableListOf).add(completed)
    }

    /** JVM entry point for atomically recording ordered typed participants. */
    fun recordActionFrameValues(
        dhatuUpadesha: String, result: SanskritValue,
        roles: Array<dev.panini.core.Karaka>, participants: Array<SanskritValue>,
    ) {
        require(roles.size == participants.size) { "Participant values and roles must align." }
        val bindings = roles.indices.groupBy { roles[it] }.mapValues { (_, indices) -> indices.map { participants[it] } }
        recordActionFrame(dhatuUpadesha, result, bindings)
    }

    /** One-based recency within this dhatu's successful results, across call frames. */
    fun loadActionResult(dhatuUpadesha: String, occurrenceFromLatest: Int): SanskritValue {
        require(occurrenceFromLatest > 0) { "Action-result recency must be one-based." }
        val history = actionResults[dhatuUpadesha]
        return history?.getOrNull(history.size - occurrenceFromLatest)?.result?.let(::historySnapshot)
            ?: throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "No completed result $occurrenceFromLatest is available for dhatu '$dhatuUpadesha'.",
            )
    }

    fun loadOrdinalActionResult(dhatuUpadesha: String, ordinalFromOldest: Long): SanskritValue {
        require(ordinalFromOldest > 0) { "Action-result ordinal must be one-based." }
        val history = actionResults[dhatuUpadesha]
        return history?.takeIf { ordinalFromOldest <= it.size.toLong() }?.get((ordinalFromOldest - 1).toInt())?.result?.let(::historySnapshot)
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "No completed result at ordinal $ordinalFromOldest is available for dhatu '$dhatuUpadesha'.")
    }

    fun loadOrdinalActionParticipants(
        dhatuUpadesha: String, ordinalFromOldest: Long, karaka: dev.panini.core.Karaka,
    ): List<SanskritValue> {
        require(ordinalFromOldest > 0) { "Action-participant ordinal must be one-based." }
        val history = actionResults[dhatuUpadesha]
        val action = history?.takeIf { ordinalFromOldest <= it.size.toLong() }
            ?.get((ordinalFromOldest - 1).toInt())
        return actionParticipants(action, dhatuUpadesha, karaka)
    }

    fun loadActionParticipants(
        dhatuUpadesha: String, occurrenceFromLatest: Int, karaka: dev.panini.core.Karaka,
    ): List<SanskritValue> {
        require(occurrenceFromLatest > 0) { "Action-participant recency must be one-based." }
        val history = actionResults[dhatuUpadesha]
        return actionParticipants(history?.getOrNull(history.size - occurrenceFromLatest), dhatuUpadesha, karaka)
    }

    private fun actionParticipants(
        action: CompletedAction?, dhatuUpadesha: String, karaka: dev.panini.core.Karaka,
    ): List<SanskritValue> = action?.participants?.get(karaka)?.takeIf { it.isNotEmpty() }?.map(::historySnapshot)
        ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
            "No ${karaka.sanskritName} participants are available for the selected action '$dhatuUpadesha'.")

    fun loadActionParticipantValue(dhatuUpadesha: String, recency: Int, karaka: dev.panini.core.Karaka): SanskritValue =
        participantValue(loadActionParticipants(dhatuUpadesha, recency, karaka))

    fun loadOrdinalActionParticipantValue(dhatuUpadesha: String, ordinal: Long, karaka: dev.panini.core.Karaka): SanskritValue =
        participantValue(loadOrdinalActionParticipants(dhatuUpadesha, ordinal, karaka))

    private fun participantValue(members: List<SanskritValue>): SanskritValue =
        if (members.size == 1) members.single() else SanskritValue.Gana(members)

    fun loadActionParticipantGroup(dhatuUpadesha: String, recency: Int, karaka: dev.panini.core.Karaka): SanskritValue =
        SanskritValue.Gana(loadActionParticipants(dhatuUpadesha, recency, karaka))

    fun loadOrdinalActionParticipantGroup(dhatuUpadesha: String, ordinal: Long, karaka: dev.panini.core.Karaka): SanskritValue =
        SanskritValue.Gana(loadOrdinalActionParticipants(dhatuUpadesha, ordinal, karaka))

    private fun historySnapshot(value: SanskritValue): SanskritValue {
        val active = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SanskritValue, Boolean>())
        fun copy(current: SanskritValue): SanskritValue {
            if (!active.add(current)) throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "A cyclic structured value cannot be recorded in action history.")
            return try {
                when (current) {
                    is SanskritValue.Gana -> SanskritValue.Gana(current.elements.map(::copy))
                    is SanskritValue.Suchi -> current.copy(items = current.items.map(::copy))
                    is SanskritValue.Rupa -> SanskritValue.Rupa(current.schema, current.fields.mapValues { copy(it.value) })
                    is SanskritValue.Shabda -> current.copy(samjnas = current.samjnas.toSet())
                    else -> current
                }
            } finally { active.remove(current) }
        }
        return copy(value)
    }

    fun loadValue(name: String): SanskritValue = runtimeValue(name)
        ?: error("No compiled value is bound to '$name'.")

    fun storeValue(name: String, value: SanskritValue) {
        if (name != "LastResult") {
            parameterFrames.reversed().firstOrNull { name in it.parameterValues }?.let { frame ->
                frame.aliases.getValue(name).forEach { frame.parameterValues[it] = value }
                return
            }
        }
        values[name] = value
        if (name == "LastResult") resultEpoch = Any()
        if (name == "LastResult" && value is SanskritValue.Satya) {
            reportedCondition = value.boolean
        }
    }

    private data class ParameterFrame(
        val parameterValues: MutableMap<String, SanskritValue>,
        val resultEpoch: Any,
        val savedBindings: Map<String, SanskritValue?>? = null,
        val aliases: Map<String, Set<String>>,
    )
}

class CompiledExecutionLimitExceededException(limit: Long) : IllegalStateException(
    "Compiled condition-controlled execution exhausted its host budget of $limit iterations.",
)
