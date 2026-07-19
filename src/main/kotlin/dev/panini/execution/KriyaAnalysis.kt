package dev.panini.execution

data class KriyaAnalysis(
    val id: String,
    /** Stable Dhātupāṭha identity; surface spelling is not sufficient. */
    val dhatuId: String,
    val karakas: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)
