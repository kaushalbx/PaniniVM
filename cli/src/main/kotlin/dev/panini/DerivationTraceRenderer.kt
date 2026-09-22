package dev.panini

import dev.panini.derivation.DerivationResult
import dev.panini.derivation.SvaraAssignmentSource

internal object DerivationTraceRenderer {
    fun appendTo(output: MutableList<String>, result: DerivationResult, includeRole: Boolean = false) {
        output += "----------------------------------------"
        result.applications.forEach { application ->
            val role = if (includeRole) " [${application.role::class.simpleName}]" else ""
            output += "${application.sutra}$role — ${application.after.rawJoinedSurface} (${application.explanation})"
            application.conflictTrace.forEach { output += "  ↳ $it" }
        }
        result.svaraResult?.let { svara ->
            output += "स्वर — ${svara.formattedDevanagari}"
            result.final.svaraAssignments.sortedBy { it.vowelIndex }.forEach { assignment ->
                val source = when (val provenance = assignment.source) {
                    is SvaraAssignmentSource.Sutra -> "sūtra ${provenance.number}"
                    is SvaraAssignmentSource.Lexical -> "lexical ${provenance.source}"
                }
                output += "  ↳ vowel ${assignment.vowelIndex + 1}: ${assignment.accent} ($source)"
            }
        }
    }
}
