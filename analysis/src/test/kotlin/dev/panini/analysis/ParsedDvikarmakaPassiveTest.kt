package dev.panini.analysis

import dev.panini.core.*
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ParsedDvikarmakaPassiveTest {
    private val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(),
        listOf("दुह्", "नी", "कथ्").map { root -> Dhatu(id = "passive.$root", krama = 1,
            upadesha = root, sourceSurface = root, artha = "fixture", arthaHindi = "fixture", arthaEnglish = "fixture",
            gana = DhatuGana.BHVADI, karmatva = Karmatva.SAKARMAKA) })))

    @Test fun `parsed two-object passive preserves both case proofs and the correct agreement controller`() {
        for (secondaryPromoted in listOf(false, true)) for ((ending, errors) in listOf("आताम्" to 0, "त" to 1)) {
            val source = if (secondaryPromoted) "धेनु + औ पयस् + अम् दुह् + यक् + लट् + $ending ।"
                else "अजा + औ ग्राम + अम् नी + यक् + लट् + $ending ।"
            val clause = assertIs<Invocation>(PaniniParser().parse(source).body).vakya
            val first = if (secondaryPromoted) SemanticRelation.SECONDARY_OBJECT else SemanticRelation.DESIRED_OBJECT
            val second = if (secondaryPromoted) SemanticRelation.DESIRED_OBJECT else SemanticRelation.SECONDARY_OBJECT
            val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(mapOf(0 to setOf(first), 1 to setOf(second))))
            val designation = if (secondaryPromoted) listOf("1.4.51", "1.4.49") else listOf("1.4.49", "1.4.51")
            assertEquals(listOf(listOf(designation[0], "2.3.46"), listOf(designation[1], "2.3.2")),
                frame.relations.map { it.evidence.map { proof -> proof.sutra } })
            assertEquals(errors, frame.diagnostics.count { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
            assertEquals(listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA), frame.relations.map { it.resolvedVibhakti })
            frame.relations.forEach { relation -> assertTrue(clause.padas.any { it === relation.participant.pada }) }
        }
    }

    @Test fun `unknown promotion cannot create a nominative controller from its ending`() {
        val clause = assertIs<Invocation>(PaniniParser().parse(
            "माणवक + औ धर्म + अम् कथ् + यक् + लट् + त ।").body).vakya
        val frame = analyzer.analyze(clause, semanticContext = VakyaSemanticContext(mapOf(
            0 to setOf(SemanticRelation.SECONDARY_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE),
            1 to setOf(SemanticRelation.DESIRED_OBJECT))))
        assertEquals(listOf(listOf("1.4.51"), listOf("1.4.49")), frame.relations.map { it.evidence.map { proof -> proof.sutra } })
        assertTrue(frame.relations.all { it.resolvedVibhakti == null })
        assertTrue(frame.diagnostics.none { it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH })
        assertEquals(2, frame.diagnostics.count { it.code == FrameDiagnosticCode.UNVALIDATED_VIBHAKTI })
    }
}
