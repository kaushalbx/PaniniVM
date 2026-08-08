package dev.panini.execution

/** Compatibility LOT–सिप् surfaces pending complete irregular derivation. */
internal object PvmImperativeLexicon {
    private val SIMPLE_SURFACES = mapOf(
        "दा" to "देहि",
    )

    fun surface(dhatu: String): String? = SIMPLE_SURFACES[dhatu]
}
