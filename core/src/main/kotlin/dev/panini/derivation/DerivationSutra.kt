package dev.panini.derivation

import dev.panini.sutra.NimittaScope
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraVisibility

interface DerivationSutra {
    /** Noncanonical source ownership must be explicit; null retains canonical catalogue ownership. */
    val source: dev.panini.sutra.runtime.SutraSource? get() = null
    val sutra: String
    val krama: Int
    val type: SutraType
    val role: SutraRole
    val action: SutraAction
    val scope: SutraScope
    val stage: SutraStage
    val nimittaScope: NimittaScope
    val priority: SutraPriority
    val visibility: SutraVisibility
    val optional: Boolean
    /** Includes prescribed alternatives where neither outcome declines the rule. */
    val hasAlternatives: Boolean get() = optional
    val blocks: Set<String>
    /** An identity-retention guard protects its own boundary but creates no
     * later phonological state to hide still-pending work on other boundaries. */
    val advancesTripadiFrontier: Boolean get() = true
    val traceTemplate: String?
    fun matches(context: DerivationState): Boolean
    fun apply(context: DerivationState): DerivationChange

    fun isTripadi(): Boolean = krama >= 820000

    /** 6.4.22 - 6.4.129: Asiddhavat section. */
    fun isAbhiya(): Boolean = krama in 640022..640129

    fun tryApply(state: DerivationState): DerivationChange? =
        if (matches(state)) apply(state) else null

    fun applyAll(state: DerivationState): List<DerivationChange> {
        val applied = apply(state)
        return if (optional) {
            listOf(applied, DerivationChange(state, "Optional sutra $sutra declined.", applied = false))
        } else {
            listOf(applied)
        }
    }

    fun renderTrace(): String =
        traceTemplate?.replace("{sutra}", sutra) ?: "$sutra: $action."
}

/** A rule changes grammar-bearing state, never just an output string. */
data class DerivationChange(
    val state: DerivationState,
    val explanation: String,
    /** False means an optional branch deliberately declined this sutra. */
    val applied: Boolean = true,
    /** A retained-sound alternative protects grammar without hiding independent
     * earlier phonological work. Other alternatives of the same rule may advance. */
    val advancesTripadiFrontier: Boolean = true,
)
