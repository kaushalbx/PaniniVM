package dev.panini.execution

/** Shared interpreter/JVM binding by actual payload kind, preserving order within a kind. */
object TypedParticipantBinding {
    fun acceptsTypes(expected: List<PrakriyaValueType>, actual: List<PrakriyaValueType>): Boolean {
        if (expected.size != actual.size) return false
        val available = expected.groupingBy { it }.eachCount()
        return actual.filter { it != PrakriyaValueType.UNCONSTRAINED }.groupingBy { it }.eachCount()
            .all { (type, count) -> count <= (available[type] ?: 0) }
    }

    fun orderValues(expected: List<PrakriyaValueType>, values: List<SanskritValue>): List<SanskritValue> {
        val actual = values.map(PrakriyaValueClassifier::classifyValue)
        require(acceptsTypes(expected, actual)) {
            "Procedure typed participant mismatch: expected $expected, received $actual."
        }
        val groups = values.groupBy(PrakriyaValueClassifier::classifyValue)
            .mapValues { (_, values) -> values.iterator() }
        return expected.map { type -> groups.getValue(type).next().also {
            require(PrakriyaValueClassifier.accepts(type, it)) { "Invalid ${type.sanskritName} participant value." }
        } }
    }
}
