package dev.panini.execution

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.binding.AnalyzedExecutionBinding
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.memory.FileKriyaMemoryStore
import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.execution.memory.withMemoryId
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.execution.sutra.SutraPipelineContinuation
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.core.Karaka
import java.util.concurrent.ConcurrentHashMap

/** Owns conversational session state, continuation lifecycle, and kriyā memory. */
internal class SessionRuntime(
    private val store: StateStore,
    private val kriyaMemoryStore: FileKriyaMemoryStore,
    private val externalDispatcher: ExternalCapabilityDispatcher,
) {
    private val sessions = ConcurrentHashMap<String, SambhashanaContext>()
    private val parser = dev.panini.vyakaranam.parser.PaniniParser()
    // Retain lexical cells, never capabilities, across an in-memory suspension.
    private data class SuspendedLocals(val bindings: ProcedureLocalBindings, val sessionKey: String?)
    private val suspendedLocals = WeakIdentityMap<Any, SuspendedLocals>()
    private val kriyaMemories = ConcurrentHashMap<String, KriyaMemory>()
    private val consumedContinuations = WeakIdentityMap<Any, Unit>()

    private fun consumeContinuation(continuation: Any): Boolean =
        consumedContinuations.putIfAbsent(continuation, Unit) == null

    fun kriyaMemory(sessionKey: String): KriyaMemory = kriyaMemories.computeIfAbsent(sessionKey) {
        kriyaMemoryStore.load(sessionKey) { source ->
            VyakaranamExecutionAdapter.analyzeForMemory(source)?.frames.orEmpty()
        } ?: KriyaMemory()
    }

    fun eval(
        utterance: String,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        evaluateCondition: Boolean = false,
        persistSession: Boolean = true,
    ): ExecutionResult {
        if (utterance.isBlank()) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
            "The Sanskrit utterance is empty.")
        val parsed = try { parser.parse(utterance) }
        catch (failure: dev.panini.vyakaranam.parser.PaniniParseException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Invalid annotated Sanskrit morphology.")
        }
        return eval(parsed.copy(sourceText = utterance), sessionKey, scope, speaker, listener, evaluateCondition,
            persistSession = persistSession)
    }

    /** Executes a canonical AST directly, without a render-and-reparse cycle. */
    fun eval(
        ukti: Ukti,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        evaluateCondition: Boolean = false,
        injectedBindings: Map<Karaka, ExecutionExpression> = emptyMap(),
        persistSession: Boolean = true,
    ): ExecutionResult {
        val bound = try { LocalAnaphorBinding.resolveLinear(ukti.body, scope,
            scope.prakriyaRegistry?.all()?.mapTo(mutableSetOf()) { operation -> operation.nameStem }.orEmpty()) }
        catch (failure: IllegalArgumentException) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                failure.message ?: "Unresolved pronoun.")
        }
        val projected = ukti.copy(body = bound.program)
        return evaluate(
            ukti.sourceText.ifBlank { ukti.body.sourceText },
            sessionKey, bound.scope, speaker, listener, evaluateCondition, persistSession,
        ) { input, context, memory, environment ->
            VyakaranamExecutionAdapter.bindWithAnalysis(
                input, projected, context, memory, environment, injectedBindings,
            )
        }
    }

    private fun evaluate(
        text: String,
        sessionKey: String?,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        evaluateCondition: Boolean,
        persistSession: Boolean,
        bind: (
            SanskritUktiInput,
            SambhashanaContext,
            KriyaMemory,
            ValueEnvironment,
        ) -> AnalyzedExecutionBinding,
    ): ExecutionResult {
        val activeContext = if (sessionKey != null) {
            sessions.getOrPut(sessionKey) {
                (if (persistSession) store.load(sessionKey) else null)
                    ?: SambhashanaContext(speaker = speaker, listener = listener)
            }
        } else {
            SambhashanaContext(speaker = speaker, listener = listener)
        }
        val input = SanskritUktiInput(
            text = text,
            speaker = activeContext.speaker,
            listener = activeContext.listener,
        )
        val effectiveScope = effectiveScope(scope)
        val memory = sessionKey?.let {
            if (persistSession) kriyaMemory(it) else kriyaMemories.computeIfAbsent(it) { KriyaMemory() }
        } ?: KriyaMemory()
        val analyzedBinding = bind(input, activeContext, memory, effectiveScope.environment)
        val turn = executeBinding(
            analyzedBinding.binding, activeContext, effectiveScope, memory, evaluateCondition,
        )
        val phala = turn.response.phala
        if (phala is Phala.Siddha) {
            val publishedContext = publishBindings(turn.context, activeContext, phala, effectiveScope)
            if (sessionKey != null) recordSuccessfulTurn(sessionKey, publishedContext, persistSession)
            analyzedBinding.analysis?.let {
                if (sessionKey != null) rememberKriyas(sessionKey, turn.context.turnNumber, it.frames, phala, persistSession)
            }
        }
        return retainSuspendedLocals(phala.toExecutionResult("panini.eval"), effectiveScope, sessionKey)
    }

    fun resume(continuation: Any, sessionKey: String?, scope: ExecutionScope): ExecutionResult {
        val typedContinuation = continuation as? SutraPipelineContinuation
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Invalid continuation object provided.")
        val owner = suspendedLocals[typedContinuation]
        if (owner != null && owner.sessionKey != sessionKey) return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "A suspended lexical frame belongs to a different session.")
        if (!consumeContinuation(typedContinuation)) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Continuation has already been resumed.")
        }

        val lexical = suspendedLocals.remove(typedContinuation)?.bindings
        val resumedScope = effectiveScope(if (lexical == null) scope else scope.copy(
            procedureBindings = lexical,
            environment = scope.environment.mergedWith(ValueEnvironment.live(lexical.values))))
        val turn = SutraExecutionPipeline.resumeTurn(typedContinuation, resumedScope)
        val phala = turn.response.phala
        if (phala is Phala.Siddha) {
            val publishedContext = publishBindings(turn.context, typedContinuation.conversation, phala, resumedScope)
            if (sessionKey != null) {
                recordSuccessfulTurn(sessionKey, publishedContext, persistSession = true)
                VyakaranamExecutionAdapter.analyzeForMemory(typedContinuation.input.text)?.let { analysis ->
                    rememberKriyas(sessionKey, turn.context.turnNumber, analysis.frames, phala)
                }
            }
        }
        return retainSuspendedLocals(phala.toExecutionResult("panini.resume"), resumedScope, sessionKey)
    }

    private fun retainSuspendedLocals(result: ExecutionResult, scope: ExecutionScope, sessionKey: String?): ExecutionResult {
        val continuation = when (result) {
            is ExecutionResult.NeedsApproval -> result.continuation
            is ExecutionResult.NeedsAcceptance -> result.continuation
            else -> null
        }
        if (continuation != null && scope.procedureBindings != null)
            suspendedLocals[continuation] = SuspendedLocals(scope.procedureBindings, sessionKey)
        return result
    }

    private fun publishBindings(current: SambhashanaContext, previous: SambhashanaContext,
        phala: Phala.Siddha, scope: ExecutionScope): SambhashanaContext {
        val latest = phala.values.entries.lastOrNull()?.let { (id, display) ->
            phala.typedValues[id] ?: SanskritValue.Shabda(display)
        }
        val key = dev.panini.execution.binding.PhalaReference.RUNTIME_KEY
        val published = latest?.let { current.copy(
            previousResults = current.previousResults + (key to it.toDisplayText()),
            previousTypedResults = current.previousTypedResults + (key to it),
            previousResultSamjnas = current.previousResultSamjnas + (key to it.samjnas),
        ) } ?: current
        val locals = scope.procedureBindings ?: return published
        locals.accept(phala.localBindings)
        fun <V> restore(values: Map<String, V>, old: Map<String, V>): Map<String, V> =
            (values - locals.names) + old.filterKeys { it in locals.names }
        return published.copy(
            previousResults = restore(published.previousResults, previous.previousResults),
            previousTypedResults = restore(published.previousTypedResults, previous.previousTypedResults),
            previousResultSamjnas = restore(published.previousResultSamjnas, previous.previousResultSamjnas),
        )
    }

    private fun executeBinding(
        binding: ExecutionBindingResult,
        context: SambhashanaContext,
        scope: ExecutionScope,
        memory: KriyaMemory,
        evaluateCondition: Boolean,
    ): SambhashanaTurn = when (binding) {
        is ExecutionBindingResult.Bound -> SutraExecutionPipeline.executeTurn(
            binding.ukti, binding.trace, context, scope, memory, evaluateCondition,
        )
        is ExecutionBindingResult.NeedsInput -> SambhashanaTurn(
            SanskritPrativacanaRenderer.render(
                Phala.Asiddha(ExecutionResult.NeedsInput(emptySet(), binding.message), emptyList()),
            ),
            context,
        )
        is ExecutionBindingResult.Invalid -> SambhashanaTurn(
            SanskritPrativacanaRenderer.render(
                Phala.Asiddha(
                    ExecutionResult.Failure(ExecutionError.INVALID_VALUE, binding.message),
                    emptyList(),
                ),
            ),
            context,
        )
    }

    fun load(sessionKey: String): SambhashanaContext? = store.load(sessionKey)?.also {
        sessions[sessionKey] = it
    }

    fun value(sessionKey: String, name: String): SanskritValue? =
        sessions[sessionKey]?.previousTypedResults?.get(name)

    /** Restore only the lexical member binding; retain body results and other state. */
    fun <T> withRestoredBinding(sessionKey: String, name: String, persistSession: Boolean, body: () -> T): T {
        val before = sessions[sessionKey]
        fun <V> restore(current: Map<String, V>, old: Map<String, V>?): Map<String, V> =
            (current - name) + old.orEmpty().filterKeys { it == name }
        return try { body() } finally {
            sessions[sessionKey]?.let { current ->
                val restored = current.copy(
                    previousResults = restore(current.previousResults, before?.previousResults),
                    previousTypedResults = restore(current.previousTypedResults, before?.previousTypedResults),
                    previousResultSamjnas = restore(current.previousResultSamjnas, before?.previousResultSamjnas),
                )
                if (restored != current) recordSuccessfulTurn(sessionKey, restored, persistSession)
            }
        }
    }

    /** Retains a typed result produced by a structured AST operation outside the action pipeline. */
    fun retainStructuredResult(sessionKey: String, value: SanskritValue, speaker: String, listener: String,
        persistSession: Boolean) {
        val context = sessions[sessionKey] ?: SambhashanaContext(speaker, listener)
        val values = mapOf(dev.panini.execution.binding.PhalaReference.RUNTIME_KEY to value)
        recordSuccessfulTurn(sessionKey, context.copy(
            previousResults = context.previousResults + values.mapValues { it.value.toDisplayText() },
            previousResultSamjnas = context.previousResultSamjnas + values.mapValues { it.value.samjnas },
            previousTypedResults = context.previousTypedResults + values,
        ), persistSession)
    }

    fun save(sessionKey: String) {
        sessions[sessionKey]?.let { store.save(sessionKey, it) }
    }

    fun listKeys(): List<String> = store.listKeys()

    fun checkpoint(sessionKey: String): SessionCheckpoint = SessionCheckpoint(
        context = sessions[sessionKey] ?: store.load(sessionKey),
        kriyaMemory = kriyaMemories[sessionKey] ?: kriyaMemoryStore.load(sessionKey) { source ->
            VyakaranamExecutionAdapter.analyzeForMemory(source)?.frames.orEmpty()
        },
    )

    fun restore(sessionKey: String, checkpoint: SessionCheckpoint) {
        checkpoint.context?.let {
            sessions[sessionKey] = it
            store.save(sessionKey, it)
        } ?: run {
            sessions.remove(sessionKey)
            store.delete(sessionKey)
        }
        checkpoint.kriyaMemory?.let {
            kriyaMemories[sessionKey] = it
            kriyaMemoryStore.save(sessionKey, it)
        } ?: run {
            kriyaMemories.remove(sessionKey)
            kriyaMemoryStore.delete(sessionKey)
        }
    }

    private fun effectiveScope(scope: ExecutionScope): ExecutionScope = scope.copy(
        stateStore = scope.stateStore ?: store,
        externalDispatcher = scope.externalDispatcher ?: externalDispatcher,
    )

    private fun recordSuccessfulTurn(sessionKey: String, context: SambhashanaContext, persistSession: Boolean) {
        sessions[sessionKey] = context
        if (persistSession) store.save(sessionKey, context)
    }

    private fun rememberKriyas(
        sessionKey: String,
        turn: Int,
        frames: List<KriyaFrame>,
        phala: Phala.Siddha,
        persistSession: Boolean = true,
    ) {
        if (frames.isEmpty()) return
        val invocationValues = phala.typedValues.entries
            .mapNotNull { (id, value) -> KriyaInvocationId.indexOf(id)?.let { Triple(it, id, value) } }
            .sortedBy { (index) -> index }
        val remembered = invocationValues.mapIndexed { index, (_, invocationId, value) ->
            RememberedKriya(
                turn = turn,
                frame = frames[index % frames.size].withMemoryId(KriyaId("turn-$turn-kriya-${index + 1}")),
                phala = value,
                participants = phala.participantValues[invocationId].orEmpty(),
            )
        }
        if (remembered.isNotEmpty()) {
            val memory = kriyaMemory(sessionKey).remember(remembered)
            kriyaMemories[sessionKey] = memory
            if (persistSession) kriyaMemoryStore.save(sessionKey, memory)
        }
    }
}

data class SessionCheckpoint(
    val context: SambhashanaContext?,
    val kriyaMemory: KriyaMemory?,
)
