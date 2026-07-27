package dev.panini.unadipatha

import dev.panini.dhatupatha.Dhatu
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraVisibility

/**
 * Base class for all Uṇādi Sūtras.
 */
abstract class UnadiSutra(
    val number: String,
    val text: String,
    val hindiExplanation: String,
    val suffix: String,
    val roots: Set<Dhatu>,
    val englishExplanation: String? = null
) : DerivationSutra {
    open fun matches(context: UnadiState): Boolean {
        return context.suffix == suffix && roots.any {
            it.sourceSurface == context.root || it.upadesha == context.root || it.surfaceAliases.contains(context.root)
        }
    }

    abstract fun apply(context: UnadiState): UnadiChange

    // DerivationSutra defaults
    override val sutra: String get() = "unadi_$number"
    override val krama: Int get() = 900000 + (number.replace(".", "").toIntOrNull() ?: 0)
    override val type: SutraType get() = SutraType.NITYA
    override val role: SutraRole get() = SutraRole.Vidhi
    override val action: SutraAction get() = SutraAction.ADESHA
    override val scope: SutraScope get() = SutraScope.DERIVATION
    override val nimittaScope: NimittaScope get() = NimittaScope.UNKNOWN
    override val priority: SutraPriority get() = SutraPriority.NORMAL
    override val visibility: SutraVisibility get() = SutraVisibility.NORMAL
    override val optional: Boolean get() = false
    override val blocks: Set<String> get() = emptySet()
    override val traceTemplate: String? get() = null

    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "")
}
