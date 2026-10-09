package dev.panini.analysis

import dev.panini.core.Prayoga
import dev.panini.dhatupatha.bhvadi.BhuDhatu
import dev.panini.dhatupatha.kryadi.GrahDhatu
import dev.panini.dhatupatha.bhvadi.VridhDhatu
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ParsedAgreementTest {
    private val parser = PaniniParser()
    private val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
        emptyList(), listOf(BhuDhatu(), GrahDhatu(), VridhDhatu()),
    )))

    private fun check(source: String, prayoga: Prayoga, mismatches: Int) {
        val invocation = assertIs<Invocation>(parser.parse(source).body)
        val frame = analyzer.analyze(invocation.vakya)
        assertEquals(prayoga, frame.prayoga, source)
        assertEquals(mismatches, frame.diagnostics.count {
            it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH
        }, source)
    }

    @Test
    fun `parsed coordination controls active and passive number`() {
        for ((ending, errors) in listOf("तस्" to 0, "तिप्" to 1)) {
            check("राम + सुँ च श्याम + सुँ च भू + शप् + लट् + $ending ।", Prayoga.KARTARI, errors)
        }
        for ((ending, errors) in listOf("आताम्" to 0, "त" to 1)) {
            check("फल + सुँ च पुष्प + सुँ च राम + टा ग्रहँ + यक् + लट् + $ending ।", Prayoga.KARMANI, errors)
        }
    }

    @Test
    fun `parsed bhave and passive causative preserve distinct voice semantics`() {
        check("बाल + भ्याम् वृधुँ + यक् + लट् + त ।", Prayoga.BHAVE, 0)
        check("बाल + भ्याम् वृधुँ + यक् + लट् + आताम् ।", Prayoga.BHAVE, 1)
        check("राम + टा बाल + औ भू + णिच् + यक् + लट् + आताम् ।", Prayoga.KARMANI, 0)
        check("राम + टा बाल + औ भू + णिच् + यक् + लट् + त ।", Prayoga.KARMANI, 1)
    }
}
