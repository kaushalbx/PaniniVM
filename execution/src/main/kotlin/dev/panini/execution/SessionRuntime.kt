package dev.panini.execution

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.memory.FileKriyaMemoryStore
import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.execution.memory.withMemoryId
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.execution.sutra.SutraPipelineContinuation
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap

/** Owns conversational session state, continuation lifecycle, and kriyā memory. */
internal class SessionRuntime(
    private val store: StateStore,
    private val kriyaMemoryStore: FileKriyaMemoryStore,
    private val externalDispatcher: ExternalCapabilityDispatcher,
) {
    private val sessions = ConcurrentHashMap<String, SambhashanaContext>()
    private val kriyaMemories = ConcurrentHashMap<String, KriyaMemory>()
    private val consumedContinuations = Collections.synchronizedSet(
        Collections.newSetFromMap(WeakHashMap<Any, Boolean>()),
    )

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
    ): ExecutionResult {
        val activeContext = if (sessionKey != null) {
            sessions.getOrPut(sessionKey) {
                store.load(sessionKey) ?: SambhashanaContext(speaker = speaker, listener = listener)
            }
        } else {
            SambhashanaContext(speaker = speaker, listener = listener)
        }
        val input = SanskritUktiInput(text = utterance, speaker = activeContext.speaker, listener = activeContext.listener)
        val analysis = VyakaranamExecutionAdapter.analyzeForMemory(utterance)
        val effectiveScope = effectiveScope(scope)
        val memory = sessionKey?.let(::kriyaMemory) ?: KriyaMemory()
        val turn = SutraExecutionPipeline.executeTurn(input, activeContext, effectiveScope, memory)
        val phala = turn.response.phala

        if (phala is Phala.Siddha && sessionKey != null) {
            persistSuccessfulTurn(sessionKey, turn.context)
            analysis?.let { rememberKriyas(sessionKey, turn.context.turnNumber, it.frames, phala) }
        }
        return phala.toExecutionResult("panini.eval")
    }

    fun resume(continuation: Any, sessionKey: String?, scope: ExecutionScope): ExecutionResult {
        val typedContinuation = continuation as? SutraPipelineContinuation
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Invalid continuation object provided.")
        if (!consumedContinuations.add(typedContinuation)) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Continuation has already been resumed.")
        }

        val turn = SutraExecutionPipeline.resumeTurn(typedContinuation, effectiveScope(scope))
        val phala = turn.response.phala
        if (phala is Phala.Siddha && sessionKey != null) {
            persistSuccessfulTurn(sessionKey, turn.context)
            VyakaranamExecutionAdapter.analyzeForMemory(typedContinuation.input.text)?.let { analysis ->
                rememberKriyas(sessionKey, turn.context.turnNumber, analysis.frames, phala)
            }
        }
        return phala.toExecutionResult("panini.resume")
    }

    fun load(sessionKey: String): SambhashanaContext? = store.load(sessionKey)?.also {
        sessions[sessionKey] = it
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

    private fun persistSuccessfulTurn(sessionKey: String, context: SambhashanaContext) {
        sessions[sessionKey] = context
        store.save(sessionKey, context)
    }

    private fun rememberKriyas(
        sessionKey: String,
        turn: Int,
        frames: List<KriyaFrame>,
        phala: Phala.Siddha,
    ) {
        if (frames.isEmpty()) return
        val invocationValues = phala.typedValues.entries
            .mapNotNull { (id, value) -> KriyaInvocationId.indexOf(id)?.let { Triple(it, id, value) } }
            .sortedBy { (index) -> index }
        val remembered = invocationValues.mapIndexed { index, (_, _, value) ->
            RememberedKriya(
                turn = turn,
                frame = frames[index % frames.size].withMemoryId(KriyaId("turn-$turn-kriya-${index + 1}")),
                phala = value,
            )
        }
        if (remembered.isNotEmpty()) {
            val memory = kriyaMemory(sessionKey).remember(remembered)
            kriyaMemories[sessionKey] = memory
            kriyaMemoryStore.save(sessionKey, memory)
        }
    }
}

data class SessionCheckpoint(
    val context: SambhashanaContext?,
    val kriyaMemory: KriyaMemory?,
)
