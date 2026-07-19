package dev.panini.execution

sealed interface VakyaAnalysisResult {
    data class Analyzed(val analysis: VakyaAnalysis, val trace: List<String>) : VakyaAnalysisResult
    data class NeedsClarification(val question: String) : VakyaAnalysisResult
    data class Unsupported(val message: String) : VakyaAnalysisResult
}
