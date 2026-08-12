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
)
