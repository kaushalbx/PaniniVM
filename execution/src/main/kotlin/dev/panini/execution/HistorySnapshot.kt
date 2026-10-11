package dev.panini.execution

/** Shared subgraphs are valid; only a path back to an active ancestor is cyclic. */
internal fun validateAcyclicValues(values: Iterable<SanskritValue>) {
    val active = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SanskritValue, Boolean>())
    val complete = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SanskritValue, Boolean>())
    fun visit(value: SanskritValue) {
        if (value in complete) return
        require(active.add(value)) { "A cyclic value cannot be supplied to language execution." }
        when (value) {
            is SanskritValue.Gana -> value.elements.forEach(::visit)
            is SanskritValue.Suchi -> value.items.forEach(::visit)
            is SanskritValue.Rupa -> value.fields.values.forEach(::visit)
            else -> Unit
        }
        active.remove(value)
        complete.add(value)
    }
    values.forEach(::visit)
}

/** Detaches remembered payloads from mutable collection/record storage. */
internal fun SanskritValue.historySnapshot(): SanskritValue {
    val active = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<SanskritValue, Boolean>())
    fun snapshot(value: SanskritValue): SanskritValue {
        require(active.add(value)) { "A cyclic value cannot be recorded in action history." }
        return try {
            when (value) {
                is SanskritValue.Gana -> SanskritValue.Gana(value.elements.map(::snapshot))
                is SanskritValue.Suchi -> value.copy(items = value.items.map(::snapshot))
                is SanskritValue.Rupa -> SanskritValue.Rupa(value.schema, value.fields.mapValues { snapshot(it.value) })
                is SanskritValue.Shabda -> value.copy(samjnas = value.samjnas.toSet())
                else -> value
            }
        } finally { active.remove(value) }
    }
    return snapshot(this)
}
