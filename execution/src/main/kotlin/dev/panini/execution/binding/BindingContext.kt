package dev.panini.execution.binding

import dev.panini.analysis.isNonBinding

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
        val nonBinding = owner.isNonBinding(relation)
        if (!nonBinding) return relation
        // An instrumental reaches the program as what form leaves open: kartā or karaṇa.
        val instrumental = relation.participant.supCandidates.all { it.vibhakti == dev.panini.core.Vibhakti.TRTIYA }
        // गुरुः शिष्येण पञ्च मुद्रयतु: a written prompter over a णिच् verb leaves the instrumental as the one who acts, so it is a kartā.
        val head = (owner.vakya as? dev.panini.vyakaranam.ast.AkhyataVakya)?.tinganta
        val prompter = owner.relations.any { other -> other !== relation &&
            other.participant.supCandidates.isNotEmpty() && other.participant.supCandidates.all { it.vibhakti == dev.panini.core.Vibhakti.PRATHAMA } }
        if (instrumental && prompter && head?.dhatu?.sanadiPratyayas == listOf("णिच्")) return relation.copy(
            resolution = dev.panini.analysis.FrameKarakaResolution.Resolved(dev.panini.core.Karaka.KARTR))
        return relation.copy(resolution = if (instrumental) dev.panini.analysis.FrameKarakaResolution.Ambiguous(
            setOf(dev.panini.core.Karaka.KARTR, dev.panini.core.Karaka.KARANA))
            else dev.panini.analysis.FrameKarakaResolution.Unassigned("This reading does not bind an operand."))
    }
}
