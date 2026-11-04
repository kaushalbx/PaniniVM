package dev.panini.execution

import dev.panini.core.Karaka

/** One grammatical occurrence; list order retains repeated and mixed-role operands. */
data class HistoryParticipant(
    val expression: ExecutionExpression,
    val projection: Karaka?,
    val coexistingKarakas: Set<Karaka> = emptySet(),
    val ambiguousIndex: Int? = null,
    val implicit: Boolean = false,
) {
    init {
        require((projection != null) != (ambiguousIndex != null)) { "A history occurrence needs one projection or ambiguity index." }
        require(ambiguousIndex == null || ambiguousIndex >= 0)
        require(coexistingKarakas.isEmpty() || projection in coexistingKarakas)
        require(!implicit || projection != null)
    }
    val designations: Set<Karaka> get() = coexistingKarakas + setOfNotNull(projection)
}

/** Resolve before executing the action; callers freeze the returned values for storage. */
fun ExecutionContext.historyParticipantValues(): Map<Karaka, List<SanskritValue>> {
    if (historyParticipants.isEmpty()) return bindings.filterKeys { it != Karaka.ANIRDHARITA }
        .mapValues { (_, expression) -> resolveValues(expression) }
    val result = linkedMapOf<Karaka, MutableList<SanskritValue>>()
    historyParticipants.forEach { participant ->
        require(participant.projection != null) { "Unresolved participant occurrence cannot enter action history." }
        val values = requireNotNull(resolveCompleteValues(participant.expression)) { "Unresolved participant value cannot enter action history." }
        participant.designations.forEach { role -> result.getOrPut(role) { mutableListOf() }.addAll(values) }
    }
    return result
}
