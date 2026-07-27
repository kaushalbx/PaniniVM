package dev.panini.unadipatha

import dev.panini.core.ItMarker

/**
 * UnadiState represents the derivation state of a nominal stem using Unadi rules.
 */
data class UnadiState(
    val root: String,
    val suffix: String? = null,
    val surface: String = root,
    val itMarkers: Set<ItMarker> = emptySet(),
    val stepTrace: List<String> = emptyList()
)

/**
 * UnadiChange represents a single modification step applied by an Unadi rule.
 */
data class UnadiChange(
    val state: UnadiState,
    val explanation: String
)
