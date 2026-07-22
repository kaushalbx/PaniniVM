package dev.panini.execution

import dev.panini.core.Karaka

sealed interface OperationResolution {
    data class Resolved(val value: ResolvedOperation) : OperationResolution
    data class MissingInput(val karakas: Set<Karaka>, val message: String) : OperationResolution
    data class Invalid(val error: ExecutionError, val message: String) : OperationResolution
    data class Ambiguous(val operations: List<String>, val message: String) : OperationResolution
}
