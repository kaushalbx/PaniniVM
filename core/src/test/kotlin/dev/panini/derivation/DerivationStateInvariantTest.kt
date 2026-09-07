package dev.panini.derivation

import dev.panini.core.ItMarker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DerivationStateInvariantTest {
    @Test
    fun `marker provenance survives exact designation lopa metadata`() {
        val provenance = ItMarkerProvenance(ItMarker.NIT, "1.3.3", "ण्")
        val term = DerivationTerm(
            "suffix", "अ", TermKind.PRATYAYA,
            itMarkerProvenance = setOf(provenance),
        )

        assertEquals(setOf(provenance), term.itMarkerProvenance)
    }

    @Test
    fun `final state rejects pending it processing`() {
        assertThrows(IllegalArgumentException::class.java) {
            DerivationState(
                terms = listOf(
                    DerivationTerm("suffix", "अण्", TermKind.PRATYAYA, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA),
                ),
                stage = DerivationStage.FINAL,
            ).requireCompleteItProcessing()
        }
    }

    @Test
    fun `final state also rejects pending lifecycle on dropped terms`() {
        assertThrows(IllegalArgumentException::class.java) {
            DerivationState(
                terms = listOf(DerivationTerm("stem", "अ", TermKind.PRATIPADIKA)),
                droppedTerms = listOf(
                    DerivationTerm("suffix", "", TermKind.PRATYAYA, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA),
                ),
                stage = DerivationStage.FINAL,
            ).requireCompleteItProcessing()
        }
    }

    @Test
    fun `final state rejects unconsumed it designation`() {
        assertThrows(IllegalArgumentException::class.java) {
            DerivationState(
                terms = listOf(
                    DerivationTerm(
                        "suffix",
                        "अण्",
                        TermKind.PRATYAYA,
                        itDesignations = listOf(ItDesignation(1, 3, marker = ItMarker.NIT, sutra = "1.3.3", designatedText = "ण्")),
                    ),
                ),
                stage = DerivationStage.FINAL,
            ).requireCompleteItProcessing()
        }
    }
}
