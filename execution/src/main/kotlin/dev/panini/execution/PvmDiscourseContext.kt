package dev.panini.execution

/** Typed discourse referents introduced by declarations in a parsed PVM script. */
data class PvmDiscourseContext(
    val activeRange: SanskritValue.Range? = null,
) {
    /** Makes declared referents available to ordinary grammatical binding. */
    fun valueEnvironment(): ValueEnvironment = ValueEnvironment(
        activeRange?.let { mapOf(ACTIVE_RANGE_NAME to it) }.orEmpty(),
    )

    companion object {
        fun from(statements: Iterable<PvmScriptStatement>): PvmDiscourseContext = PvmDiscourseContext(
            activeRange = statements.filterIsInstance<PvmScriptStatement.RangeDefinition>()
                .lastOrNull()
                ?.range,
        )
    }
}
