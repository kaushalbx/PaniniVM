package dev.panini.execution

import dev.panini.shiksha.Samjna

/** One authoritative map for values carried through planning and execution. */
data class ValueEnvironment(
    val values: Map<String, SanskritValue> = emptyMap(),
) {
    fun with(name: String, value: SanskritValue): ValueEnvironment =
        copy(values = values + (name to value))

    fun mergedWith(other: ValueEnvironment): ValueEnvironment =
        ValueEnvironment(values + other.values)

    fun displayValues(): Map<String, String> = values.mapValues { it.value.toDisplayText() }

    fun samjnas(): Map<String, Set<Samjna>> = values.mapValues { it.value.samjnas }

    companion object {
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
