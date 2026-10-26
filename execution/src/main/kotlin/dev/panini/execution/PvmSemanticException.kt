package dev.panini.execution

/** Compatibility exception at source-loading APIs; semantic consumers retain [diagnostic]. */
class PvmSemanticException(
    val diagnostic: ExecutionDiagnostic,
) : IllegalArgumentException("${diagnostic.code}: ${diagnostic.message}")
