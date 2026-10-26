package dev.panini.execution

import dev.panini.core.Lakara
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.SemanticDiagnostic
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.relationSpan

data class ScriptStopDiagnostic(
    override val code: String,
    override val message: String,
    override val span: DocumentSourceSpan?,
    override val offendingText: String,
) : SemanticDiagnostic<String>

/** वि + रम् + लोट् + सिप् (विरम): finish the current script. */
object NaturalScriptStop {
    fun matches(invocation: Invocation): Boolean {
        val head = invocation.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return false
        return invocation.vakya.padas.size == 1 &&
            head.dhatu.mulaDhatu == "रम्" &&
            head.dhatu.sanadiPratyayas.isEmpty() &&
            head.upasargas == listOf("वि") &&
            head.lakara == Lakara.LOT && head.ting.text == "सिप्"
    }

    fun contextDiagnostic(invocation: Invocation, insideProcedure: Boolean): ScriptStopDiagnostic? =
        if (matches(invocation) && insideProcedure) ScriptStopDiagnostic(
            code = "SCRIPT_STOP_IN_PROCEDURE",
            message = "विरम is a top-level script stop; use a procedure result inside a प्रक्रिया.",
            span = invocation.relationSpan(),
            offendingText = invocation.sourceText,
        ) else null
}
