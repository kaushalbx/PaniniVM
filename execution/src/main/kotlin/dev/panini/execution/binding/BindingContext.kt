package dev.panini.execution.binding

import dev.panini.analysis.KriyaFrame
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.ValueEnvironment

/**
 * Immutable context for a single kāraka-binding pass over one clause.
 *
 * Bundles the per-clause parameters that were previously threaded individually
 * through [KarakaExtractor.extractKarakas] and [ExpressionBuilder.build],
 * eliminating the 8-argument signature explosion.
 */
internal data class BindingContext(
    /** The ongoing conversation, used for result-history फल resolution. */
    val conversation: SambhashanaContext?,
    /** 0-based index of this clause within the utterance. */
    val clauseIndex: Int,
    /** The verbal root whose kāraka slots are being filled. */
    val dhatu: Dhatu,
    /** The kāraka-frame produced by the vyākaraṇa analyser for this clause. */
    val frame: KriyaFrame,
    /** Kriyā-centred session memory used for grammatical result references. */
    val memory: KriyaMemory = KriyaMemory(),
    /** Ordered list of dhātus from clauses already processed in this utterance. */
    val previousDhatus: List<Dhatu>,
    /** Local variable names introduced by prior clauses' result bindings. */
    val localVariables: Set<String> = emptySet(),
    /** Maps each local variable name to the invocation id that produced it. */
    val localVariableInvocationIds: Map<String, String> = emptyMap(),
    /** Typed operands supplied directly by the execution scope. */
    val environment: ValueEnvironment = ValueEnvironment(),
    val previousActionIdentities: List<dev.panini.core.ActionIdentity?> = emptyList(),
    /** All grammatical owners participating in one composite executable clause. */
    val actionFrames: List<KriyaFrame> = emptyList(),
) {
    val grammaticalFrames: List<KriyaFrame> get() = actionFrames.ifEmpty { listOf(frame) }

    fun ownerOf(pada: dev.panini.vyakaranam.ast.Pada?): KriyaFrame? =
        grammaticalFrames.singleOrNull { owner -> owner.relations.any { it.participant.pada === pada } }

    /**
     * What execution may bind. Grammar reports its reading for every root; a means, reason, causee or ordered
     * object that the instruction did not declare reaches a program as unassigned.
     */
    fun relationFor(pada: dev.panini.vyakaranam.ast.Pada?): dev.panini.analysis.KarakaRelation? {
        val owner = ownerOf(pada) ?: return null
        val index = owner.relations.indexOfFirst { it.participant.pada === pada }
        val relation = owner.relations.getOrNull(index) ?: return null
        val nonBinding = owner.diagnostics.any { it.code == dev.panini.analysis.FrameDiagnosticCode.NON_BINDING_READING && it.nominalIndex == index }
        if (!nonBinding) return relation
        // An instrumental reaches the program as what form leaves open: kartā or karaṇa.
        val instrumental = relation.participant.supCandidates.all { it.vibhakti == dev.panini.core.Vibhakti.TRTIYA }
        return relation.copy(resolution = if (instrumental) dev.panini.analysis.FrameKarakaResolution.Ambiguous(
            setOf(dev.panini.core.Karaka.KARTR, dev.panini.core.Karaka.KARANA))
            else dev.panini.analysis.FrameKarakaResolution.Unassigned("This reading does not bind an operand."))
    }
}
