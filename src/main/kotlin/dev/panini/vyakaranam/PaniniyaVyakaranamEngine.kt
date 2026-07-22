package dev.panini.vyakaranam

import dev.panini.vyakaranam.analysis.PadaAnalyzer
import dev.panini.vyakaranam.analysis.VakyaAnalysis
import dev.panini.vyakaranam.analysis.VakyaAnalyzer
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParser

data class UktiAnalysis(
    val ukti: Ukti,
    val vakyas: List<VakyaAnalysis>,
)

class PaniniyaVyakaranamEngine(
    lexicon: VyakaranamLexicon,
    private val parser: PaniniParser = PaniniParser(),
) {
    private val vakyaAnalyzer = VakyaAnalyzer(
        padaAnalyzer = PadaAnalyzer(lexicon),
    )

    fun parse(source: String): Ukti =
        parser.parse(source)

    fun analyze(source: String): UktiAnalysis {
        val ukti = parse(source)

        return UktiAnalysis(
            ukti = ukti,
            vakyas = ukti.vakyas.map(vakyaAnalyzer::analyze),
        )
    }
}
