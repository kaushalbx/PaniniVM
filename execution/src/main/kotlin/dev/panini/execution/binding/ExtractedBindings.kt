package dev.panini.execution.binding

import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ExecutionExpression
import dev.panini.core.Karaka

/**
 * The typed result produced by [KarakaExtractor.extractKarakas] for a single clause.
 *
 * @property bindings   Resolved kāraka → expression map for this clause.
 * @property ambiguous  Pādas whose kāraka could not be uniquely determined.
 * @property trace      Human-readable evidence trail from sūtra application and
 *                      kriyā-qualification notes, for debugging and logging.
 */
internal data class ExtractedBindings(
    val bindings: Map<Karaka, ExecutionExpression>,
    val ambiguous: List<AmbiguousKarakaBinding>,
    val trace: List<String>,
)
