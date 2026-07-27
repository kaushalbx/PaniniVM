package dev.panini.unadipatha

import dev.panini.dhatupatha.Dhatu

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
) {
    open fun matches(context: UnadiState): Boolean {
        return context.suffix == suffix && roots.any {
            it.sourceSurface == context.root || it.upadesha == context.root || it.surfaceAliases.contains(context.root)
        }
    }

    abstract fun apply(context: UnadiState): UnadiChange
}
