package dev.panini.analysis

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ActionNominalRelationEvidenceTest {
    private fun padas(source: String): List<SubantaAnalysis> = PaniniParser().parse(source)
        .grammaticalVakyas().single().padas.filterIsInstance<SubantaPada>().map {
            SubantaAnalysis(it, null, SupAffix.candidates(it.sup.text), emptySet())
        }

    @Test fun `written action result relation is independent of verb and nominal order`() {
        for (source in listOf(
            "शिष् + घञ् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "युज् + णिच् + ल्युट् + ङस् कर्मन् + औट् मुद्र् + णिच् + लोट् + सिप् ।",
            "फल + अम् मुद्र् + णिच् + लोट् + सिप् शिष् + घञ् + ङस् ।")) {
            val nouns = padas(source)
            val inferred = ActionNominalRelationEvidence.infer(nouns, VakyaSemanticContext())
            val evidence = inferred.values.single()
            assertEquals(SemanticRelation.SESHA_RELATION, evidence.relation)
            assertEquals(SemanticEvidenceSource.GOVERNING_CONSTRUCTION, evidence.source)
            assertTrue(nouns[inferred.keys.single()].pada.sourceText.contains("ङस्"))
        }
    }

    @Test fun `competing genitives result heads and syncretic endings do not establish a selected relation`() {
        for (words in listOf(
            "शिष् + घञ् + ङस् फल + अम् फल + अम्",
            "शिष् + घञ् + ङस् युज् + ल्युट् + ङस् फल + अम्",
            "शिष् + घञ् + ओस् फल + अम्",
            "युज् + ल्युट् + ङस् कर्मन् + औट् फल + अम्",
            "युज् + ल्युट् + ओस् कर्मन् + औट्",
            "राम + ङस् फल + अम्",
            "शिष् + घञ् + ङस्")) {
            assertTrue(ActionNominalRelationEvidence.infer(padas("$words मुद्र् + णिच् + लोट् + सिप् ।"),
                VakyaSemanticContext()).isEmpty(), words)
        }
    }
}
