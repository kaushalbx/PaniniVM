package dev.panini.compiler

/** Reports domain actions that still cross the compiler's single generic runtime boundary. */
internal object CompilerRuntimeBoundaryReport {
    fun operations(program: CompilerProgram): Map<String, Int> =
        (program.entryPoint + program.procedures.flatMap(CompilerProcedure::instructions))
            .filterIsInstance<CompilerInstruction.Call>()
            .groupingBy(CompilerInstruction.Call::operationName)
            .eachCount()
            .toSortedMap()
}
