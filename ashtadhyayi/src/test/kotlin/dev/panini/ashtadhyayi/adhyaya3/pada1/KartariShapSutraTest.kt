package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class KartariShapSutraTest {
    @Test
    fun `3 1 68 introduces shap as a raw upadesha with provenance`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("ting", "ति", TermKind.PRATYAYA, upadesha = "तिप्"),
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT)),
        )

        val shap = KartariShapSutra.apply(state).state.terms.single { it.id == "shap" }

        assertEquals("शप्", shap.surface)
        assertEquals("शप्", shap.upadesha)
        assertEquals("3.1.68", shap.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, shap.itProcessingPhase)
    }

    @Test
    fun `3 1 68 does not introduce shap before an ardhadhatuka lit termination`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("ting", "अ", TermKind.PRATYAYA, upadesha = "तिप्"),
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertFalse(KartariShapSutra.matches(state))
    }
}
