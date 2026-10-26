package dev.panini.execution

import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.SemanticDiagnostic
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.relationSpan

/** Semantic call-contract failure shared by validation, interpretation, and compilation. */
data class PrakriyaCallDiagnostic(
    override val code: String,
    override val message: String,
    override val span: DocumentSourceSpan?,
    override val offendingText: String,
) : SemanticDiagnostic<String>

object PrakriyaCallDiagnostics {
    fun atInvocation(
        invocation: PrakriyaInvocation,
        code: String,
        message: String,
        argumentIndex: Int? = null,
    ): PrakriyaCallDiagnostic {
        val argument = argumentIndex?.let(invocation.arguments::getOrNull)?.pada
        val argumentSpan = (argument as? SourceLocatedNode)?.sourceSpan
        val node = invocation.ukti?.body as? Invocation
        return PrakriyaCallDiagnostic(
            code = code,
            message = message,
            span = argumentSpan ?: node?.relationSpan(),
            offendingText = argument?.sourceText ?: node?.sourceText ?: invocation.fullText.trim(),
        )
    }
}
