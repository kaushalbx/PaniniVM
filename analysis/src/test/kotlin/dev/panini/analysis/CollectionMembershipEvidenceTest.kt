package dev.panini.analysis

import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CollectionMembershipEvidenceTest {
    private fun infer(words: String, root: String = "असँ", context: VakyaSemanticContext = VakyaSemanticContext())
        : Map<Int, SemanticRelationEvidence> {
        val clause = PaniniParser().parse("$words $root + लट् + तिप् ।").grammaticalVakyas().single() as AkhyataVakya
        val nominal = clause.padas.flatMap {
            when (it) {
                is SamuccitaSubanta -> it.members
                is SupBearingPada -> listOf(it)
                else -> emptyList()
            }
        }.map {
            SubantaAnalysis(it, null, SupAffix.candidates(it.sup.text), emptySet())
        }
        return CollectionMembershipEvidence.infer(nominal, clause.padas, clause.tinganta, context)
    }

    @Test fun `coordinated members attach to a collection independently of head position`() {
        for (source in listOf("एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ",
            "सूची + सुँ एक + ङस् द्वि + ओस् त्रि + आम् च")) {
            val evidence = infer(source)
            assertEquals(3, evidence.size)
            assertTrue(evidence.values.all { it.relation == SemanticRelation.SESHA_RELATION &&
                it.source == SemanticEvidenceSource.GOVERNING_CONSTRUCTION })
        }
    }

    @Test fun `bare syncretism missing coordination mixed cases and competing heads do not select membership`() {
        for (source in listOf(
            "द्वि + ओस् सूची + सुँ",
            "द्वि + ओस् द्वि + ओस् च सूची + सुँ",
            "एक + ङस् द्वि + ओस् सूची + सुँ",
            "एक + ङस् द्वि + भ्याम् च सूची + सुँ",
            "च एक + ङस् द्वि + ओस् च सूची + सुँ",
            "एक + ङस् च च द्वि + ओस् च सूची + सुँ",
            "एक + ङस् द्वि + ओस् च सूची + सुँ सूची + सुँ",
            "एक + ङस् द्वि + ओस् च राम + सुँ")) assertTrue(infer(source).isEmpty(), source)
        assertTrue(infer("एक + ङस् द्वि + ओस् च सूची + सुँ", "गम्").isEmpty())
        assertTrue(infer("एक + ङस् द्वि + ओस् च सूची + सुँ", context = VakyaSemanticContext(
            participantRelations = mapOf(1 to setOf(SemanticRelation.LOCATION)))) .isEmpty())
    }

    @Test fun `chained coordination retains each repeated written member occurrence`() {
        val inferred = infer("एक + ङस् द्वि + ओस् एक + ङस् त्रि + आम् च द्वि + ओस् च सूची + सुँ")
        assertEquals(setOf(0, 1, 2, 3, 4), inferred.keys)
        assertTrue(inferred.values.all { it.relation == SemanticRelation.SESHA_RELATION })
    }
}
