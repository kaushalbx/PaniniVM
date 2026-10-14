package dev.panini.analysis

import dev.panini.core.SupAffix
import dev.panini.core.StriAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon
import kotlin.test.*

class OrdinalPadaAnalysisTest {
    @Test fun `cardinal analysis retains native AST identity and stores value as evidence`() {
        val analyzer = PadaAnalyzer(object : VyakaranamLexicon {
            override fun findPratipadika(text: String): PratipadikaEntry? = null
            override fun findDhatu(text: String): Dhatu? = null
        })
        val cardinal = SankhyaPada("द्वि + औ", listOf("द्वि"), 2, SupPratyaya("औ", "औ"))

        val result = assertIs<AnalyzedSubanta>(analyzer.analyze(cardinal))

        assertSame(cardinal, result.pada)
        assertSame(cardinal, result.analysis.pada)
        assertEquals(dev.panini.execution.SanskritValue.Sankhya(2, "द्वि"), result.analysis.semanticValue)
    }

    @Test fun `productive ordinal analysis retains native morphology without cardinal decoding`() {
        val analyzer = PadaAnalyzer(object : VyakaranamLexicon {
            override fun findPratipadika(text: String): PratipadikaEntry? = null
            override fun findDhatu(text: String): Dhatu? = null
        }, sankhyaValueResolver = SankhyaPadaValueResolver {
            error("An ordinal must not enter the cardinal decoder")
        })
        val ordinal = SankhyaPuranaPada("द्वि + तीय + टाप् + अम्",
            listOf("द्वि", "तीय"), 2, SupPratyaya("अम्", "अम्"), StriAffix.TAP)
        val result = assertIs<AnalyzedSubanta>(analyzer.analyze(ordinal))
        assertSame(ordinal, result.pada)
        assertSame(ordinal, result.analysis.pada)
        assertEquals(listOf(SupAffix.AM), result.analysis.supCandidates)
        assertEquals(StriAffix.TAP, assertIs<SankhyaPuranaPada>(result.pada).striAffix)
    }
}
