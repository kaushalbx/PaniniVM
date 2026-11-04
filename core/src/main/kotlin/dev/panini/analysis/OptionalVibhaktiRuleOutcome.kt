package dev.panini.analysis

import dev.panini.core.Vibhakti

/** Explicit case alternatives; optionality alone does not specify their values. */
interface OptionalVibhaktiRuleOutcome {
    val permittedCases: Set<Vibhakti>
    fun permittedCases(context: VibhaktiRuleContext): Set<Vibhakti> = permittedCases
    /** A construction may distribute its cases across several canonical sūtras. */
    fun alternativeRuleNumbers(context: VibhaktiRuleContext): Set<String>? = null
}
