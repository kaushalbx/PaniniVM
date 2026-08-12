package dev.panini.execution

/** Conventional scoped range available to actions that omit explicit bounds. */
const val ACTIVE_RANGE_NAME: String = "सीमा"
const val RENDER_ACTIVE_RANGE_METADATA: String = "renderActiveRange"

fun ExecutionContext.activeRange(): SanskritValue.Range? =
    variables[ACTIVE_RANGE_NAME] as? SanskritValue.Range
