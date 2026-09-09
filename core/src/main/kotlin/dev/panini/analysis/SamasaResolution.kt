package dev.panini.analysis

import dev.panini.core.SamasaType

/**
 * Encapsulates high-level grammatical resolution metadata for nominal compounds (Samāsa).
 */
data class SamasaResolution(
    val type: SamasaType,
    val laukikaVigraha: String,
    val alaukikaVigraha: String,
    val purvaPada: String,
    val uttaraPada: String,
    val classificationSutra: String,
    val compoundStem: String = "",
    val transformationSutras: List<String> = emptyList(),
    val supLopaSutras: List<String> = emptyList(),
    val sandhiSutras: List<String> = emptyList(),
    val inflectionSutras: List<String> = emptyList(),
    /** Additional valid outputs created by optional samāsa operations. */
    val alternatives: List<SamasaAlternative> = emptyList(),
)

data class SamasaAlternative(
    val compoundStem: String,
    val surface: String,
    /** Optional rules applied on this branch; omission is represented by absence. */
    val transformationSutras: List<String>,
)
