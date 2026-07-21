package dev.panini.analysis

import dev.panini.execution.ExecutionSamjna
import dev.panini.parser.ast.ParsedNominalBase
import dev.panini.parser.ast.ParsedSubanta
import dev.panini.parser.ast.SimpleNominalKind

class DefaultSubantaAnalyzer(
    private val supResolver: SupResolver = DefaultSupResolver(),
) : SubantaAnalyzer {

    override fun analyze(
        subanta: ParsedSubanta,
    ): SubantaAnalysis =
        SubantaAnalysis(
            base = subanta.base,
            sup = subanta.supPratyaya?.let(supResolver::resolve),
            samjnas = determineSamjnas(subanta.base),
        )

    private fun determineSamjnas(
        base: ParsedNominalBase,
    ): Set<ExecutionSamjna> =
        buildSet {
            when (base) {
                is ParsedNominalBase.Simple -> {
                    when (base.kind) {
                        SimpleNominalKind.NUMERAL ->
                            add(ExecutionSamjna.SANKHYA)

                        SimpleNominalKind.RESULT_REFERENCE ->
                            add(ExecutionSamjna.REFERENCE)

                        SimpleNominalKind.IDENTIFIER ->
                            Unit
                    }
                }

                is ParsedNominalBase.Kridanta ->
                    add(ExecutionSamjna.KRIDANTA)

                is ParsedNominalBase.Taddhita ->
                    add(ExecutionSamjna.TADDHITA)

                is ParsedNominalBase.Samasa ->
                    add(ExecutionSamjna.SAMASA)

                is ParsedNominalBase.Stri ->
                    add(ExecutionSamjna.STRI_PRATYAYA)
            }
        }
}
