package dev.panini.execution

enum class PaniniExecutionArchitecture {
    /** Compatibility path retained as an explicit migration rollback mode. */
    LEGACY,
    /** Canonical execution path for bound Sanskrit and sūtra programs. */
    SUTRA_MACHINE,
    /**
     * Runs both paths and reports a mismatch. This mode accepts only PURE
     * capability scopes so comparison cannot duplicate external side effects.
     */
    COMPARE,
}
