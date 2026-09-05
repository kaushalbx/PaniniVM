package dev.panini.derivation

import dev.panini.core.ItMarker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WholeAffixReplacementTest {
    private val designated = DerivationTerm(
        id = "affix", surface = "अप्", kind = TermKind.PRATYAYA, upadesha = "अप्",
        itMarkers = setOf(ItMarker.P), itProcessingPhase = ItProcessingPhase.DESIGNATED,
        itDesignations = listOf(ItDesignation(1, 3, marker = ItMarker.P, sutra = "1.3.3", designatedText = "प्")),
    )

    @Test fun `preserve policy remaps the exact surviving segment`() {
        val result = designated.replaceWholeAffix(
            "इप्", "इप्", "x", WholeAffixDesignationPolicy.PreserveAndRemap(
                listOf(ItDesignationRemap(1, 3, 1, 3)),
            ),
        )
        assertEquals("प्", result.itDesignations.single().designatedText)
        assertEquals(1, result.itDesignations.single().start)
        assertEquals(ItProcessingPhase.DESIGNATED, result.itProcessingPhase)
    }

    @Test fun `preserve policy rejects an unaccounted designation`() {
        assertFailsWith<IllegalArgumentException> {
            designated.replaceWholeAffix("इ", "इ", "x", WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()))
        }
    }

    @Test fun `consume policy supersedes exact designations but preserves marker provenance`() {
        val result = designated.replaceWholeAffix("इ", "इ", "x", WholeAffixDesignationPolicy.Consume)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(ItProcessingPhase.PROCESSED, result.itProcessingPhase)
        assertEquals(setOf(ItMarker.P), result.sthaniProps?.itMarkers)
    }

    @Test fun `preserve policy can consume one superseded designation and remap another`() {
        val term = designated.copy(
            surface = "फक्",
            upadesha = "फक्",
            itDesignations = listOf(
                ItDesignation(0, 1, marker = ItMarker.T, sutra = "1.3.7", designatedText = "फ"),
                ItDesignation(1, 3, marker = ItMarker.KIT, sutra = "1.3.3", designatedText = "क्"),
            ),
        )
        val result = term.replaceWholeAffix(
            "आयन्क्",
            "फक्",
            "7.1.2",
            WholeAffixDesignationPolicy.PreserveAndRemap(
                remaps = listOf(ItDesignationRemap(1, 3, 4, 6)),
                consumed = listOf(ItDesignationConsumption(0, 1)),
            ),
        )

        assertEquals(listOf(ItMarker.KIT), result.itDesignations.map { it.marker })
        assertEquals(4 to 6, result.itDesignations.single().let { it.start to it.endExclusive })
    }

    @Test fun `fresh upadesha requires a new it processing pass`() {
        val result = designated.replaceWholeAffix("यप्", "यप्", "x", WholeAffixDesignationPolicy.FreshUpadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, result.itProcessingPhase)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(emptySet(), result.itMarkers)
    }

    @Test fun `consume for drop records provenance and leaves no pending designation`() {
        val result = consumeAffixForDrop(designated, "7.1.1")
        assertEquals("", result.surface)
        assertEquals("अप्", result.originalSurfaceBeforeDrop)
        assertEquals("7.1.1", result.droppedBySutra)
        assertEquals(ItProcessingPhase.PROCESSED, result.itProcessingPhase)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(setOf(ItMarker.P), result.sthaniProps?.itMarkers)
    }

    @Test fun `removeTerm consumes affix designations through the lifecycle`() {
        val stem = DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)
        val result = DerivationState(listOf(stem, designated)).removeTerm("affix", "7.1.1")
        val dropped = result.droppedTerms.single()
        assertEquals(ItProcessingPhase.PROCESSED, dropped.itProcessingPhase)
        assertEquals(emptyList(), dropped.itDesignations)
        assertEquals(setOf(ItMarker.P), dropped.sthaniProps?.itMarkers)
        assertEquals("7.1.1", dropped.droppedBySutra)
    }

    @Test fun `affix removal without a sutra is rejected`() {
        val stem = DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(stem, designated)).removeTerm("affix")
        }
    }

    @Test fun `segment substitution preserves an exact unaffected designation and records provenance`() {
        val result = DerivationState(listOf(designated)).substituteTermSurface(
            id = "affix", surface = "इप्", source = 'अ', replacement = "इ", sutra = "x",
        )
        assertEquals("प्", result.terms.single().itDesignations.single().designatedText)
        assertEquals(VarnaSubstitution("affix", 'अ', "इ", "x"), result.substitutions.single())
    }

    @Test fun `segment substitution cannot stale an exact designation`() {
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(designated)).substituteTermSurface(
                id = "affix", surface = "इ", source = 'प', replacement = "", sutra = "x",
            )
        }
    }

    @Test fun `term merger records substitution and lifecycle consumes an affix`() {
        val stem = DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)
        val result = DerivationState(listOf(stem, designated)).mergeTermsByVarnaSubstitution(
            survivorId = "stem", consumedId = "affix", surface = "रामे",
            source = 'अ', replacement = "ए", sutra = "x",
        )
        assertEquals(listOf("stem"), result.terms.map { it.id })
        assertEquals("रामे", result.surface)
        assertEquals(ItProcessingPhase.PROCESSED, result.droppedTerms.single().itProcessingPhase)
        assertEquals("x", result.droppedTerms.single().droppedBySutra)
        assertEquals(VarnaSubstitution("stem", 'अ', "ए", "x"), result.substitutions.single())
    }

    @Test fun `adjacent redistribution preserves both terms and records one substitution`() {
        val left = DerivationTerm("left", "नी", TermKind.DHATU)
        val right = DerivationTerm("siyut", "इय", TermKind.AGAMA)
        val result = DerivationState(listOf(left, right)).redistributeAdjacentTermsByVarnaSubstitution(
            leftId = "left", rightId = "siyut", leftSurface = "नीय", rightSurface = "य",
            source = 'ई', replacement = "य्", sutra = "6.1.77",
        )
        assertEquals(listOf("left", "siyut"), result.terms.map { it.id })
        assertEquals(listOf("नीय", "य"), result.terms.map { it.surface })
        assertEquals(VarnaSubstitution("left", 'ई', "य्", "6.1.77"), result.substitutions.single())
        assertEquals(emptyList(), result.droppedTerms)
    }
}
