package dev.panini.execution

import dev.panini.shiksha.Samjna

/** One authoritative map for values carried through planning and execution. */
data class ValueEnvironment(
    val values: Map<String, SanskritValue> = emptyMap(),
    val constraints: Map<String, SemanticValueConstraint> = emptyMap(),
) {
    fun with(name: String, value: SanskritValue): ValueEnvironment =
        mergedWith(ValueEnvironment(mapOf(name to value)))

    fun mergedWith(other: ValueEnvironment): ValueEnvironment =
        ValueEnvironment(
            if (values is LiveValueMap || other.values is LiveValueMap)
                ValueOverlay(values, other.values) else values + other.values,
            constraints + other.constraints,
        )

    fun displayValues(): Map<String, String> = values.mapValues { it.value.toDisplayText() }

    fun samjnas(): Map<String, Set<Samjna>> = values.mapValues { it.value.samjnas }

    companion object {
        internal fun live(values: Map<String, SanskritValue>): ValueEnvironment =
            ValueEnvironment(LiveValueView(values))

        fun from(
            displayValues: Map<String, String>,
            samjnas: Map<String, Set<Samjna>> = emptyMap(),
            typedValues: Map<String, SanskritValue> = emptyMap(),
        ): ValueEnvironment = ValueEnvironment(
            displayValues.mapValues { (name, value) ->
                SanskritValue.of(value, samjnas[name].orEmpty())
            } + typedValues,
        )
    }
}

/** Facts known during planning without fabricating a runtime payload. */
data class SemanticValueConstraint(
    val valueType: OperationValueType? = null,
    val samjnas: Set<Samjna> = emptySet(),
)

/** Preserve live lexical slots while allowing inner bindings to shadow them. */
private interface LiveValueMap : Map<String, SanskritValue>
private class LiveValueView(private val source: Map<String, SanskritValue>) :
    AbstractMap<String, SanskritValue>(), LiveValueMap {
    override fun get(key: String): SanskritValue? = source[key]
    override val entries: Set<Map.Entry<String, SanskritValue>> get() = source.entries
}
private class ValueOverlay(outer: Map<String, SanskritValue>, inner: Map<String, SanskritValue>) :
    AbstractMap<String, SanskritValue>(), LiveValueMap {
    private val layers: List<Map<String, SanskritValue>> = (if (outer is ValueOverlay) outer.layers else listOf(outer)) +
        (if (inner is ValueOverlay) inner.layers else listOf(inner))
    override fun get(key: String): SanskritValue? = layers.asReversed().firstNotNullOfOrNull { it[key] }
    override val entries: Set<Map.Entry<String, SanskritValue>> get() = buildMap {
        layers.forEach { putAll(it) }
    }.entries
}
