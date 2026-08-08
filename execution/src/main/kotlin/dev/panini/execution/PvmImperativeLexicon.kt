package dev.panini.execution

/** Compatibility LOT–सिप् surfaces pending complete sanādi-aware derivation. */
internal object PvmImperativeLexicon {
    private val CAUSATIVE_SURFACES = mapOf(
        "मुद्र्" to "मुद्रय",
    )
    val derivableCausatives = setOf("युज्", "गण")
    private val SIMPLE_SURFACES = mapOf(
        "दा" to "देहि",
    )

    fun surface(dhatu: String, causative: Boolean): String? =
        if (causative) CAUSATIVE_SURFACES[dhatu] else SIMPLE_SURFACES[dhatu]
}
