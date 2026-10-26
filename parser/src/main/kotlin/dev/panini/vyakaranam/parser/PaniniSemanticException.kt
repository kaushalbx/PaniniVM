package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.DocumentSourceSpan

enum class PaniniSemanticDiagnosticCode {
    COLLECTION_SOURCE_CASE,
    DISTRIBUTIVE_AGREEMENT,
    CURRENT_MEMBER_CASE,
    RELATIVE_INSTRUMENTAL,
    RELATIVE_COMPOUND,
    RELATIVE_ANCHOR,
    RELATIVE_DIRECTION,
    MEMBER_TYPE_MISMATCH,
    GENDER_MISMATCH,
    PRAKRIYA_HEADER_CARDINALITY,
    PRAKRIYA_OPERATION_AGREEMENT,
    PRAKRIYA_DOMAIN_AGREEMENT,
    PRAKRIYA_RESULT_AGREEMENT,
    PRAKRIYA_DECLARATION_PREDICATE,
    PRAKRIYA_QUALIFIER_AGREEMENT,
    PRAKRIYA_UNSUPPORTED_QUALIFIER,
    PRAKRIYA_MISSING_MARKER,
}

data class PaniniSemanticDiagnostic(
    override val code: PaniniSemanticDiagnosticCode,
    override val message: String,
    /** Absolute half-open offsets in the submitted source. */
    override val span: DocumentSourceSpan,
    val line: Int,
    val column: Int,
    override val offendingText: String,
) : dev.panini.vyakaranam.ast.SemanticDiagnostic<PaniniSemanticDiagnosticCode>

class PaniniSemanticException(
    val diagnostic: PaniniSemanticDiagnostic,
) : IllegalArgumentException(
    "${diagnostic.code} at line ${diagnostic.line}, column ${diagnostic.column}: " +
        "${diagnostic.message} [${diagnostic.offendingText}]",
)
