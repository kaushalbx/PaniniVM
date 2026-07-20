package dev.panini.analysis

import dev.panini.parser.ast.ParsedTinganta

interface TingantaAnalyzer {

    fun analyze(
        tinganta: ParsedTinganta,
    ): TingantaAnalysis
}
