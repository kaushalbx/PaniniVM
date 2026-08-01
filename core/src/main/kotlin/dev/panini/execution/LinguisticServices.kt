package dev.panini.execution

/** Host-supplied grammar services used by linguistic actions. */
data class LinguisticServices(
    val joinSandhi: ((String, String) -> String)? = null,
    val deriveSubanta: ((String) -> String)? = null,
)
