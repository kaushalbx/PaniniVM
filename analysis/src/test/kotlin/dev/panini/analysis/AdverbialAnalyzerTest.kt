package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class AdverbialAnalyzerTest {
    @Test fun `parsed counting pada retains derivation through pada analysis`() {
        val analyzer = PadaAnalyzer(object : dev.panini.vyakaranam.lexicon.VyakaranamLexicon {
            override fun findPratipadika(text: String) = null
            override fun findDhatu(text: String) = null
        })
        fun count(stem: String, affix: String): Long? {
            val pada = dev.panini.vyakaranam.ast.SankhyaAbhyasaPada(
                "$stem+$affix", listOf(stem, affix))
            return AdverbialAnalyzer.analyze(KriyaId("count"), listOf(analyzer.analyze(pada)))
                .single().semantics?.repetitionCount
        }
        assertEquals(9L, count("नवन्", "कृत्वसुच्"))
        assertEquals(2L, count("द्वि", "सुच्"))
        assertEquals(null, count("नवन्", "धा"))
        assertEquals(null, count("पञ्चन्", "सुच्"))
    }

    @Test fun `counting frequency retains its number and does not turn every recurrence into a count`() {
        val five = AvyayaPada("पञ्चकृत्वः", "पञ्चकृत्वः",
            dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("पञ्चन्", "कृत्वसुच्"))
        val analyses = listOf(AnalyzedAvyaya(five), avyaya("द्विः"), avyaya("सकृत्"), avyaya("पुनः"))
        val results = AdverbialAnalyzer.analyze(KriyaId("frequency"), analyses)
        assertEquals(listOf(5L, 2L, 1L, null), results.map { it.semantics?.repetitionCount })
        assertEquals(true, results.all { it.kind == KriyaQualificationKind.FREQUENCY })
        val invalid = AvyayaPada("not-a-frequency", "not-a-frequency",
            dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("उत्पल", "कृत्वसुच्"))
        assertEquals(null, CountingFrequencyExpressions.count(invalid))
        val abstractCount = five.copy(derivation = dev.panini.vyakaranam.ast.SankhyaAvyayaDerivation("KRITVAS", listOf("पञ्चन्")))
        assertEquals(5L, CountingFrequencyExpressions.count(abstractCount))
        assertEquals(2L, CountingFrequencyExpressions.count(five.copy(derivation =
            dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("द्वि", "सुच्"))))
        assertEquals(null, CountingFrequencyExpressions.count(five.copy(derivation =
            dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation("पञ्चन्", "सुच्"))))
        assertEquals(null, CountingFrequencyExpressions.count(five.copy(derivation =
            dev.panini.vyakaranam.ast.SankhyaAvyayaDerivation("KRITVAS", listOf("शून्य")))))
    }

    private val id = KriyaId("test")
    private fun avyaya(word: String) = AnalyzedAvyaya(AvyayaPada(word, word))
    @Test fun `degree does not prematurely choose an event over a constituent`() {
        listOf("बहु", "अल्पम्", "भृशम्", "अत्यन्तम्").forEach { word ->
            val result = AdverbialAnalyzer.analyze(id, listOf(avyaya(word))).single()
            assertEquals(setOf(AdverbialScope.EVENT, AdverbialScope.CONSTITUENT), result.semantics?.scopes)
        }
    }

    @Test fun `relative place does not invent a paired clause or turn there into a relative`() {
        val results = AdverbialAnalyzer.analyze(id, listOf(avyaya("यत्र"), avyaya("तत्र")))
        assertEquals(KriyaQualificationKind.PLACE, results[0].kind)
        assertEquals(AdverbialFunction.RELATIVE, results[0].semantics?.function)
        assertEquals(setOf(AdverbialScope.CLAUSE_RELATION), results[0].semantics?.scopes)
        assertEquals(AdverbialFunction.MODIFICATION, results[1].semantics?.function)
        assertEquals(setOf(AdverbialScope.EVENT), results[1].semantics?.scopes)
    }
    @Test fun `interrogatives retain both dimension and question function`() {
        listOf("कदा" to KriyaQualificationKind.TIME, "कुत्र" to KriyaQualificationKind.PLACE,
            "कथम्" to KriyaQualificationKind.MANNER).forEach { (word, kind) ->
            val result = AdverbialAnalyzer.analyze(id, listOf(avyaya(word))).single()
            assertEquals(kind, result.kind)
            assertEquals(AdverbialFunction.QUESTION, result.semantics?.function)
        }
    }
    @Test fun `negation retains scope alternatives and prohibition is distinct`() {
        val results = AdverbialAnalyzer.analyze(id, listOf(avyaya("न"), avyaya("मा")))
        assertEquals(setOf(AdverbialScope.PROPOSITION, AdverbialScope.CONSTITUENT), results.first().semantics?.scopes)
        assertEquals(AdverbialFunction.PROHIBITION, results.last().semantics?.function)
        assertEquals(listOf(0, 1), results.map { it.sourceIndex })
    }
    @Test fun `distribution is not repetition and correlation is not an event time value`() {
        val results = AdverbialAnalyzer.analyze(id, listOf(avyaya("प्रत्येकम्"), avyaya("पुनः"), avyaya("यदा"), avyaya("तदा")))
        assertEquals(KriyaQualificationKind.DISTRIBUTION, results[0].kind)
        assertEquals(KriyaQualificationKind.FREQUENCY, results[1].kind)
        assertEquals(setOf(AdverbialScope.CLAUSE_RELATION), results[2].semantics?.scopes)
        assertEquals(AdverbialFunction.CORRELATIVE, results[3].semantics?.function)
    }
    @Test fun `known accusative adverbial reading preserves nominal alternative`() {
        fun nominal(stem: String, affix: SupAffix = SupAffix.AM) = SubantaAnalysis(
            SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(affix.upadesha, affix.upadesha)),
            PratipadikaEntry(stem, setOf(Linga.NAPUMSAKA)), listOf(affix), setOf(Linga.NAPUMSAKA),
        ).let { AnalyzedSubanta(it.pada, it) }
        val result = AdverbialAnalyzer.analyze(id, listOf(nominal("शीघ्र"))).single()
        assertEquals(AdverbialMorphology.CASE_FORM, result.morphology)
        assertEquals(true, result.competingNominalReading)
        assertEquals(KriyaQualificationKind.MANNER, result.kind)
        assertEquals(KriyaQualificationKind.DEGREE,
            AdverbialAnalyzer.analyze(id, listOf(nominal("बहु"))).single().kind)
        // A surface adverb is not a nominal stem merely because it is known.
        assertEquals(emptyList(), AdverbialAnalyzer.analyze(id, listOf(nominal("अधुना"))))
        assertEquals(emptyList(), AdverbialAnalyzer.analyze(id, listOf(nominal("उत्पल"))))
        assertEquals(emptyList(), AdverbialAnalyzer.analyze(id, listOf(nominal("शीघ्र", SupAffix.TA))))
    }
    @Test fun `repeated words retain separate source occurrences`() {
        assertEquals(listOf(0, 1), AdverbialAnalyzer.analyze(id, listOf(avyaya("शनैः"), avyaya("शनैः"))).map { it.sourceIndex })
    }

    @Test fun `unknown indeclinables preserve source without invented semantics`() {
        val result = AdverbialAnalyzer.analyze(id, listOf(avyaya("अज्ञाताव्ययम्"))).single()
        assertEquals(KriyaQualificationKind.OTHER, result.kind)
        assertEquals(null, result.semantics)
        assertEquals(id, result.kriyaId)
    }
}
