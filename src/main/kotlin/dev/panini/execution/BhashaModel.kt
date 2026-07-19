package dev.panini.execution

import dev.panini.derivation.Lakara
import dev.panini.dhatupatha.Dhatu

enum class VakyaPrayojana {
    VIDHANA, PRASHNA, AJNA, PRARTHANA, NISHEDHA, ASHAMSA, ANUMATI, NIMANTRANA,
}

enum class Polarity { POSITIVE, NEGATIVE }

enum class ExecutionDisposition {
    EXECUTE, REQUEST_EXECUTION, QUERY, DECLARE, CONSTRAIN, DESIRE, GRANT, OFFER,
}

/** Trusted conversational situation surrounding an utterance. */
data class SambhashanaContext(
    val speaker: String,
    val listener: String,
    val mentionedEntities: Map<String, String> = emptyMap(),
    val mentionedEntitySamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    val previousResults: Map<String, String> = emptyMap(),
    val previousResultSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    val resultHistory: List<SmrtaPhala> = emptyList(),
    val turnNumber: Int = 0,
    val metadata: Map<String, String> = emptyMap(),
) {
    init {
        require(speaker.isNotBlank()) { "A conversation requires a speaker." }
        require(listener.isNotBlank()) { "A conversation requires a listener." }
    }
}

/** A stable, turn-qualified result retained in discourse memory. */
data class SmrtaPhala(
    val id: String,
    val turnNumber: Int,
    val invocationId: String,
    val value: String,
    val samjnas: Set<ExecutionSamjna>,
)

/** One occurrence of a verb in an utterance. */
data class DhatuInvocation(
    val id: String,
    val dhatu: Dhatu,
    val bindings: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
) {
    init {
        require(id.isNotBlank()) { "A dhātu invocation requires an id." }
    }

    fun executionContext(
        variables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    ): ExecutionContext = ExecutionContext(
        bindings = bindings,
        selectedOperation = selectedOperation,
        variables = variables,
        variableSamjnas = variableSamjnas,
        metadata = metadata,
    )
}

data class Ukti(
    val speaker: String,
    val listener: String,
    val text: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val invocations: List<DhatuInvocation>,
    val dependencies: Set<ActionDependency> = invocations.zipWithNext { before, after ->
        ActionDependency(before.id, after.id)
    }.toSet(),
) {
    init {
        require(text.isNotBlank()) { "An utterance requires text." }
        require(invocations.isNotEmpty()) { "An executable utterance requires at least one dhātu invocation." }
    }
}

data class Nirdesha(
    val speaker: String,
    val listener: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity,
    val lakara: Lakara?,
    val invocations: List<DhatuInvocation>,
    val sourceText: String,
)

sealed interface UktiInterpretation {
    data class Understood(val nirdesha: Nirdesha, val trace: List<String>) : UktiInterpretation
    data class NeedsClarification(val question: String) : UktiInterpretation
    data class Contradictory(val reason: String) : UktiInterpretation
}

data class ActionDependency(val before: String, val after: String)

data class BhashaProgram(
    val nirdesha: Nirdesha,
    val invocations: List<DhatuInvocation>,
    val dependencies: Set<ActionDependency> = emptySet(),
)

/** Capabilities come from the host, never from claims inside the utterance. */
data class ExecutionScope(
    val capabilities: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val variables: Map<String, String> = emptyMap(),
    val variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    /** Verified identities whose commands the listener is configured to obey. */
    val authorizedSpeakers: Set<String> = emptySet(),
    /** Requests accepted by the listener; acceptance is not inferred from the sentence. */
    val acceptedInvocations: Set<String> = emptySet(),
)

data class ResolvedOperation(
    val invocation: DhatuInvocation,
    val operation: DhatuOperation,
    val context: ExecutionContext,
    val resolutionTrace: List<String>,
)

data class ExecutionPlan(
    val invocationId: String,
    val resolved: ResolvedOperation,
    val disposition: ExecutionDisposition,
    val requiredEffects: Set<ExecutionEffect>,
    val speaker: String,
    val listener: String,
)

/** Immutable state required to resume a paused program without repeating work. */
data class ExecutionContinuation(
    val planning: PlanningResult.Planned,
    val nextPlanIndex: Int,
    val values: Map<String, String>,
    val valueSamjnas: Map<String, Set<ExecutionSamjna>>,
    val trace: List<String>,
)

sealed interface Phala {
    data class Siddha(
        val values: Map<String, String>,
        val samjnas: Map<String, Set<ExecutionSamjna>>,
        val trace: List<String>,
    ) : Phala
    data class Asiddha(val result: ExecutionResult, val trace: List<String>) : Phala
    data class AnumatiApekshita(
        val invocationId: String,
        val effects: Set<ExecutionEffect>,
        val continuation: ExecutionContinuation,
    ) : Phala
    data class SvikaraApekshita(
        val invocationId: String,
        val speaker: String,
        val listener: String,
        val continuation: ExecutionContinuation,
    ) : Phala
    data class Nirasta(val invocationId: String, val reason: String) : Phala
    /** The utterance was understood and planned, but its purpose does not request performance. */
    data class Avagata(
        val disposition: ExecutionDisposition,
        val plans: List<ExecutionPlan>,
        val trace: List<String>,
    ) : Phala
}

data class SambhashanaTurn(
    val response: Prativacana,
    val context: SambhashanaContext,
)
