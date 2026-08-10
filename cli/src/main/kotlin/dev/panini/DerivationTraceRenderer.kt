package dev.panini

import dev.panini.derivation.DerivationResult

internal object DerivationTraceRenderer {
    fun appendTo(output: MutableList<String>, result: DerivationResult, includeRole: Boolean = false) {
        output += "----------------------------------------"
        result.applications.forEach { application ->
            val role = if (includeRole) " [${application.role::class.simpleName}]" else ""
            output += "${application.sutra}$role — ${application.after.rawJoinedSurface} (${application.explanation})"
            application.conflictTrace.forEach { output += "  ↳ $it" }
        }
    }
}
