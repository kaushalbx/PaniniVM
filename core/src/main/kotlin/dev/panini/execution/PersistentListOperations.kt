package dev.panini.execution

/** Stable schema and field names for destructive-looking operations that remain persistent. */
const val LIST_REMOVAL_RESULT_SCHEMA: String = "सूचीनिष्कासपरिणाम"
const val LIST_REMOVAL_LIST_FIELD: String = "शेषसूची"
const val LIST_REMOVAL_VALUE_FIELD: String = "निष्कासितमूल्य"

/** Language-level collection failure shared by interpreted and compiled execution. */
class PersistentListOperationException(message: String) : IllegalArgumentException(message)

private fun SanskritValue.asPersistentList(operation: String): SanskritValue.Suchi = this as? SanskritValue.Suchi
    ?: throw PersistentListOperationException("$operation requires one list value.")

private fun SanskritValue.oneBasedIndex(operation: String): Long = when (this) {
    is SanskritValue.Sankhya -> value
    is SanskritValue.Purana -> value
    else -> throw PersistentListOperationException("$operation requires a cardinal or ordinal one-based index.")
}

private fun SanskritValue.Suchi.requireMember(value: SanskritValue, operation: String) {
    if (memberType != null && !memberType.accepts(value)) throw PersistentListOperationException(
        "$operation requires a ${memberType.name.lowercase()} member for this typed list.",
    )
}

fun replaceListMember(list: SanskritValue, index: SanskritValue, replacement: SanskritValue): SanskritValue.Suchi {
    val source = list.asPersistentList("Indexed replacement")
    val position = index.oneBasedIndex("Indexed replacement")
    if (position !in 1L..source.items.size.toLong()) throw PersistentListOperationException(
        "Replacement index $position is outside the one-based bounds 1..${source.items.size}.",
    )
    source.requireMember(replacement, "Indexed replacement")
    return source.copy(items = source.items.toMutableList().also { it[position.toInt() - 1] = replacement })
}

fun exchangeListMembers(list: SanskritValue, firstIndex: SanskritValue, secondIndex: SanskritValue): SanskritValue.Suchi {
    val source = list.asPersistentList("Indexed exchange")
    val first = firstIndex.oneBasedIndex("Indexed exchange")
    val second = secondIndex.oneBasedIndex("Indexed exchange")
    val bounds = 1L..source.items.size.toLong()
    if (first !in bounds || second !in bounds) throw PersistentListOperationException(
        "Exchange positions $first and $second must both be inside the one-based bounds 1..${source.items.size}.",
    )
    if (first == second) return source.copy(items = source.items.toList())
    return source.copy(items = source.items.toMutableList().also { members ->
        val left = first.toInt() - 1
        val right = second.toInt() - 1
        val value = members[left]
        members[left] = members[right]
        members[right] = value
    })
}

fun insertListMember(list: SanskritValue, index: SanskritValue, inserted: SanskritValue): SanskritValue.Suchi {
    val source = list.asPersistentList("Indexed insertion")
    val position = index.oneBasedIndex("Indexed insertion")
    if (position !in 1L..source.items.size.toLong() + 1L) throw PersistentListOperationException(
        "Insertion index $position is outside the one-based bounds 1..${source.items.size + 1}.",
    )
    source.requireMember(inserted, "Indexed insertion")
    return source.copy(items = source.items.toMutableList().also { it.add(position.toInt() - 1, inserted) })
}

fun removeListMember(list: SanskritValue, index: SanskritValue): SanskritValue.Suchi {
    val source = list.asPersistentList("Indexed removal")
    val position = index.oneBasedIndex("Indexed removal")
    if (position !in 1L..source.items.size.toLong()) throw PersistentListOperationException(
        "Removal index $position is outside the one-based bounds 1..${source.items.size}.",
    )
    return source.copy(items = source.items.toMutableList().also { it.removeAt(position.toInt() - 1) })
}

fun removeFinalListMember(list: SanskritValue): SanskritValue.Rupa {
    val source = list.asPersistentList("Final-member removal")
    if (source.items.isEmpty()) throw PersistentListOperationException("Cannot remove the final member of an empty list.")
    return SanskritValue.Rupa(
        LIST_REMOVAL_RESULT_SCHEMA,
        linkedMapOf(
            LIST_REMOVAL_LIST_FIELD to source.copy(items = source.items.dropLast(1)),
            LIST_REMOVAL_VALUE_FIELD to source.items.last(),
        ),
    )
}
