package dev.panini.execution

import dev.panini.core.Karaka

/** One execution-oriented verbal occurrence with a stable Dhātupāṭha identity. */
data class ExecutionKriyaAnalysis(
    val id: String,
    val dhatuId: String,
    val karakas: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)
