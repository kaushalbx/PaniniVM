package dev.panini.execution.binding

import dev.panini.analysis.KriyaFrame
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.SambhashanaContext

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
    /** Ordered list of dhātus from clauses already processed in this utterance. */
    val previousDhatus: List<Dhatu>,
    /** Local variable names introduced by prior clauses' result bindings. */
    val localVariables: Set<String> = emptySet(),
    /** Maps each local variable name to the invocation id that produced it. */
    val localVariableInvocationIds: Map<String, String> = emptyMap(),
)
