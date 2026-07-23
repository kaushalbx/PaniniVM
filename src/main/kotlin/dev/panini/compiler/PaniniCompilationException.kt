package dev.panini.compiler

class PaniniCompilationException(
    val lineIndex: Int,
    val sourceLine: String,
    val errorKind: CompilerErrorKind,
    message: String
) : Exception("Line $lineIndex: [$errorKind] $message\nSource: $sourceLine")

enum class CompilerErrorKind {
    MORPHOLOGY_ERROR,
    MISSING_INPUT,
    AMBIGUOUS_ACTION,
    DEPENDENCY_ERROR,
    OPERATION_NOT_FOUND
}
