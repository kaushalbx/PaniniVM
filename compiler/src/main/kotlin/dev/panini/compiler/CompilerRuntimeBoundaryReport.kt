package dev.panini.compiler

/** Reports domain actions that still require static or dynamically selected action dispatch. */
internal object CompilerRuntimeBoundaryReport {
    fun operations(program: CompilerProgram): Map<String, Int> =
        (program.entryPoint + program.procedures.flatMap(CompilerProcedure::instructions))
            .mapNotNull { instruction -> when (instruction) {
                is CompilerInstruction.Call -> instruction.operationName
                is CompilerInstruction.CallParticipantGroup -> instruction.selectedOperation
                    ?: "dynamic:${instruction.dhatuUpadesha}"
                else -> null
            } }
            .groupingBy { it }
            .eachCount()
            .toSortedMap()
}
