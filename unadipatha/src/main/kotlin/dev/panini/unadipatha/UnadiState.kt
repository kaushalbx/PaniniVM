package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.Dhatu
import dev.panini.unadipatha.model.Artha
import dev.panini.shiksha.Samjna

/**
 * UnadiMatch represents the result of querying an Uṇādi sūtra relation.
 */
data class UnadiMatch(
    val sutraNumber: String,
    val sutraText: String,
    val dhatu: Dhatu,
    val pratyaya: String,
    val pratyayaSurface: String,
    val itMarkers: Set<ItMarker>,
    val samjnas: Set<Samjna>,
    val meaning: Artha,
    val hindiExplanation: String? = null
)
