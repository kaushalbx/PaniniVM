package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdyantauTakitauSutraTest {
    @Test
    fun `tit placement uses exact designation and remaps target designations`() {
        val targetDesignation = designation(1, 3, "ण्")
        val augmentDesignation = designation(1, 3, "ट्")
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(
                    id = "target",
                    surface = "अण्",
                    kind = TermKind.PRATYAYA,
                    itDesignations = listOf(targetDesignation),
                    itProcessingPhase = ItProcessingPhase.DESIGNATED,
                ),
                DerivationTerm(
                    id = "augment",
                    surface = "आट्",
                    kind = TermKind.AGAMA,
                    augmentTargetId = "target",
                    itDesignations = listOf(augmentDesignation),
                    itProcessingPhase = ItProcessingPhase.DESIGNATED,
                ),
            ),
        )

        val result = AdyantauTakitauSutra.apply(state).state.terms.single()

        assertEquals("आट्अण्", result.surface)
        assertEquals(listOf(4 to 6, 1 to 3), result.itDesignations.map { it.start to it.endExclusive })
    }

    @Test
    fun `kit placement uses exact designation and remaps augment designation`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "target", surface = "राम", kind = TermKind.PRATIPADIKA),
                DerivationTerm(
                    id = "augment",
                    surface = "तुक्",
                    kind = TermKind.AGAMA,
                    augmentTargetId = "target",
                    itDesignations = listOf(designation(2, 4, "क्")),
                    itProcessingPhase = ItProcessingPhase.DESIGNATED,
                ),
            ),
        )

        val result = AdyantauTakitauSutra.apply(state).state.terms.single()

        assertEquals("रामतुक्", result.surface)
        assertEquals(listOf(5 to 7), result.itDesignations.map { it.start to it.endExclusive })
    }

    @Test
    fun `marker and spelling cannot substitute for an exact designation`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "target", surface = "राम", kind = TermKind.PRATIPADIKA),
                DerivationTerm(
                    id = "augment",
                    surface = "आट्",
                    kind = TermKind.AGAMA,
                    upadesha = "आट्",
                    itMarkers = setOf(ItMarker.T),
                    augmentTargetId = "target",
                ),
            ),
        )

        assertFalse(AdyantauTakitauSutra.matches(state))
        assertEquals(state, AdyantauTakitauSutra.apply(state).state)
    }

    @Test
    fun `placement can retain an explicit augment boundary`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(
                    id = "augment",
                    surface = "आट्",
                    kind = TermKind.AGAMA,
                    augmentTargetId = "target",
                    mergeIntoAugmentTarget = false,
                    itDesignations = listOf(designation(1, 3, "ट्")),
                    itProcessingPhase = ItProcessingPhase.DESIGNATED,
                ),
                DerivationTerm(id = "target", surface = "अन्त्", kind = TermKind.PRATYAYA),
            ),
        )

        val result = AdyantauTakitauSutra.apply(state).state

        assertEquals(listOf("आट्", "अन्त्"), result.terms.map { it.surface })
        assertEquals("target", result.terms.first().augmentTargetId)
        assertEquals(listOf(1 to 3), result.terms.first().itDesignations.map { it.start to it.endExclusive })
    }

    private fun designation(start: Int, endExclusive: Int, text: String) = ItDesignation(
        start = start,
        endExclusive = endExclusive,
        marker = ItMarker.GENERIC,
        sutra = "1.3.3",
        designatedText = text,
    )
}
