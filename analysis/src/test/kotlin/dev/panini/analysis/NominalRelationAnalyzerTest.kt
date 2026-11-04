package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import kotlin.test.Test
import kotlin.test.assertEquals

class NominalRelationAnalyzerTest {
    private fun word(stem: String, modifier: Boolean = false, gender: Linga = Linga.NAPUMSAKA,
                     affixes: List<SupAffix> = listOf(SupAffix.AM)) = SubantaAnalysis(
        SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya("अम्", "अम्")),
        PratipadikaEntry(stem, setOf(gender),
            nominalUse = if (modifier) NominalUse.MODIFIER else NominalUse.ENTITY,
            nominalMeanings = if (modifier) setOf(NominalMeaning.PROPERTY) else emptySet()),
        affixes, setOf(gender),
    )

    @Test fun `attachment permits either order and intervening nominal`() {
        val adjective = word("नील", true)
        val noun = word("उत्पल")
        val intervening = word("राम", gender = Linga.PUMS, affixes = listOf(SupAffix.TA))
        assertEquals(listOf(0 to 2), NominalRelationAnalyzer.analyze(listOf(adjective, intervening, noun))
            .candidates.map { it.modifier to it.head })
        assertEquals(listOf(2 to 0), NominalRelationAnalyzer.analyze(listOf(noun, intervening, adjective))
            .candidates.map { it.modifier to it.head })
    }

    @Test fun `matching noun alternatives and identical occurrences remain distinct`() {
        val result = NominalRelationAnalyzer.analyze(listOf(word("नील", true), word("उत्पल"), word("उत्पल")))
        assertEquals(setOf(1, 2), result.candidates.map { it.head }.toSet())
    }

    @Test fun `gender number and case mismatch prevent ordinary agreement`() {
        listOf(
            word("राम", gender = Linga.PUMS),
            word("उत्पल", affixes = listOf(SupAffix.SAS)),
            word("उत्पल", affixes = listOf(SupAffix.TA)),
        ).forEach { incompatible ->
            val result = NominalRelationAnalyzer.analyze(listOf(word("नील", true), incompatible))
            assertEquals(emptyList(), result.candidates)
            assertEquals(setOf(0), result.unattachedModifiers)
        }
    }

    @Test fun `nominal predication remains an alternative to attributive reading`() {
        val result = NominalRelationAnalyzer.analyze(listOf(
            word("नील", true, affixes = listOf(SupAffix.SU)),
            word("उत्पल", affixes = listOf(SupAffix.SU)),
        ), permitsPredication = true)
        assertEquals(setOf(NominalRelationKind.ATTRIBUTIVE, NominalRelationKind.PREDICATIVE),
            result.candidates.map { it.kind }.toSet())
    }

    @Test fun `agreement preserves correlated sup readings`() {
        val result = NominalRelationAnalyzer.analyze(listOf(
            word("नील", true, affixes = listOf(SupAffix.OS_6, SupAffix.OS_7)),
            word("उत्पल", affixes = listOf(SupAffix.OS_7)),
        ))
        assertEquals(setOf(dev.panini.core.Vibhakti.SAPTAMI), result.candidates.single().agreements.map { it.vibhakti }.toSet())
    }
}
