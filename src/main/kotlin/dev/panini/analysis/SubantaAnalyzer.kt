package dev.panini.analysis

import dev.panini.parser.ast.ParsedSubanta

interface SubantaAnalyzer {

    fun analyze(
        subanta: ParsedSubanta,
    ): SubantaAnalysis
}
