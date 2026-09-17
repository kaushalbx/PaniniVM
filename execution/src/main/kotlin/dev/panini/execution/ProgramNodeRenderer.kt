package dev.panini.execution

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Sequence

/** Diagnostic/source rendering only; execution consumes the canonical AST directly. */
internal object ProgramNodeRenderer {
    fun invocation(invocation: Invocation, pipedKarman: String? = null): String = buildString {
        if (pipedKarman != null) append("$pipedKarman + अम् ")
        append(invocation.vakya.padas.joinToString(" ") { pada ->
            pada.sourceText.replace("+", " + ").replace(Regex("\\s+"), " ").trim()
        })
        append(" ।")
    }

    fun conditional(conditional: Conditional, includePipelineTarget: Boolean = true): String = buildString {
        val hasSharedTarget = includePipelineTarget && conditional.surfacePipelineTarget != null
        val stripLoweredTargets = hasSharedTarget || !includePipelineTarget
        append("यदि ")
        append(program(conditional.condition))
        append(" तर्हि ")
        append(branch(conditional.consequent, stripLoweredTargets))
        conditional.alternate?.let {
            append(" अन्यथा ")
            append(branch(it, stripLoweredTargets))
        }
        if (hasSharedTarget) {
            append(" ततः ")
            append(program(requireNotNull(conditional.surfacePipelineTarget)))
        }
    }

    fun program(node: ProgramNode): String = when (node) {
        is Invocation -> node.implicitValue ?: node.vakya.padas.joinToString(" ") { it.sourceText }
        is Sequence -> node.statements.mapIndexed { index, statement ->
            val connector = if (index == 0) "" else "${node.connectors.getOrNull(index - 1) ?: "।"} "
            connector + program(statement)
        }.joinToString(" ")
        is Conditional -> conditional(node)
        else -> node.sourceText
    }

    private fun branch(node: ProgramNode, stripPipelineTarget: Boolean): String = when {
        !stripPipelineTarget -> program(node)
        node is Conditional -> conditional(node, false)
        node is Sequence && node.connectors.lastOrNull() == "ततः" -> program(node.statements.first())
        else -> program(node)
    }
}
