package dev.sanskrit.derivation

import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraPriority
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType
import dev.sanskrit.sutra.SutraVisibility

interface DerivationSutra {
    val sutra: String
    val krama: Int
    val type: SutraType
    val role: SutraRole
    val action: SutraAction
    val scope: SutraScope
    val nimittaScope: NimittaScope
    val priority: SutraPriority
    val visibility: SutraVisibility
    val optional: Boolean
    val blocks: Set<String>
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
)
