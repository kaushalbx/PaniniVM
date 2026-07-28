package dev.panini.sutra.runtime

import dev.panini.execution.SanskritValue

object SutraIntrospector {
    fun describe(sutra: RuntimeSutra<*>): SanskritValue = record(
        mapOf(
            "id" to SanskritValue.Shabda(sutra.id.value),
            "source" to SanskritValue.Shabda(sutra.source.text),
            "role" to SanskritValue.Shabda(sutra.role::class.simpleName ?: "Unknown"),
            "arthaKind" to SanskritValue.Shabda(sutra.artha.kind),
            "artha" to sutra.artha.toSanskritValue(),
        ),
    )

    fun arthaField(
        sutra: RuntimeSutra<*>,
        field: String,
    ): SanskritValue? = sutra.artha.fields[field]?.toSanskritValue()

    fun SutraArtha.toSanskritValue(): SanskritValue = record(
        mapOf(
            "kind" to SanskritValue.Shabda(kind),
            "fields" to record(fields.mapValues { it.value.toSanskritValue() }),
        ),
    )

    fun SutraArthaValue.toSanskritValue(): SanskritValue = when (this) {
        is SutraArthaValue.Text -> SanskritValue.Shabda(value)
        is SutraArthaValue.Number -> SanskritValue.Sankhya(value, value.toString())
        is SutraArthaValue.Truth -> SanskritValue.Satya(value)
        is SutraArthaValue.Symbol -> SanskritValue.Shabda(name)
        is SutraArthaValue.SutraReference -> SanskritValue.Shabda(id.value)
        is SutraArthaValue.Sequence -> SanskritValue.Suchi(
            values.map { it.toSanskritValue() },
        )
        is SutraArthaValue.Record -> record(
            fields.mapValues { it.value.toSanskritValue() },
        )
    }

    /**
     * Records use the existing list value during migration: each item is a
     * two-element [field, value] pair. A dedicated record SanskritValue can be
     * introduced later without changing SutraArtha.
     */
    private fun record(fields: Map<String, SanskritValue>): SanskritValue.Suchi =
        SanskritValue.Suchi(
            fields.map { (name, value) ->
                SanskritValue.Suchi(listOf(SanskritValue.Shabda(name), value))
            },
        )
}
