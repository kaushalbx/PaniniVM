package dev.panini.sutra

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType

/**
 * Interface implemented by all nominal compound (Samāsa) Sūtras in the Aṣṭādhyāyī.
 *
 * Exposes compound-specific metadata such as [samasaType], whether the rule creates
 * an Aluk compound, and its matching/application methods.
 */
interface SamasaSutra {
    /** The primary compound classification prescribed or governed by this Sūtra. */
    val samasaType: SamasaType

    /** Whether this Sūtra preserves case endings (Aluk Samāsa). Default: false. */
    val isAluk: Boolean get() = samasaType == SamasaType.ALUK_TATPURUSA

    /** Whether this compound is mandatory (Nitya Samāsa). Default: true. */
    val isNitya: Boolean get() = true

    /** Evaluates rule applicability for the given compound context. */
    fun matches(context: SamasaRuleContext): Boolean

    /** Applies the compound transformation rule. */
    fun apply(context: SamasaRuleContext): SamasaRuleResult
}
