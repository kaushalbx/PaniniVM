package dev.panini.analysis

/** Established object mediation for the intended recipient in canonical 1.4.32. */
sealed interface IntendedObject {
    data class Written(val participantId: String) : IntendedObject {
        init { require(participantId.isNotBlank()) }
    }
    /** Supplied omitted-karman premise, never inferred from participant count. */
    data object Implied : IntendedObject
}
