package dev.panini.execution

enum class PaniniExecutionArchitecture {
    LEGACY,
    SUTRA_MACHINE,
    /**
     * Runs both paths and reports a mismatch. This mode accepts only PURE
     * capability scopes so comparison cannot duplicate external side effects.
     */
    COMPARE,
}
