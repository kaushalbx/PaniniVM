package dev.panini.execution

import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.SemanticDiagnostic
import dev.panini.vyakaranam.ast.SourceLocatedNode

data class PipelineSemanticDiagnostic(
    override val code: String,
    override val message: String,
    override val span: DocumentSourceSpan?,
    override val offendingText: String,
) : SemanticDiagnostic<String>

object PipelineSemanticDiagnostics {
    fun atPipeline(
        pipeline: Pipeline,
        code: String,
        message: String,
        argumentIndex: Int? = null,
    ): PipelineSemanticDiagnostic {
        val selected = argumentIndex?.let(pipeline.argumentPadas::getOrNull)
        val participants = selected?.let(::listOf)
            ?: (pipeline.renderPadas.ifEmpty { pipeline.argumentPadas })
        val spans = participants.mapNotNull { (it as? SourceLocatedNode)?.sourceSpan }
        val span = spans.takeIf { it.isNotEmpty() }?.let {
            DocumentSourceSpan(it.minOf(DocumentSourceSpan::start), it.maxOf(DocumentSourceSpan::endExclusive))
        }
        return PipelineSemanticDiagnostic(
            code,
            message,
            span,
            selected?.sourceText ?: pipeline.sourceText,
        )
    }
}
