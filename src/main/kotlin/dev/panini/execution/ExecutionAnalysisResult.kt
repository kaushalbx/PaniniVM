package dev.panini.execution

sealed interface ExecutionAnalysisResult {
    data class Analyzed(val analysis: ExecutionUtteranceAnalysis, val trace: List<String>) : ExecutionAnalysisResult
    data class NeedsClarification(val question: String) : ExecutionAnalysisResult
    data class Unsupported(val message: String) : ExecutionAnalysisResult
}

