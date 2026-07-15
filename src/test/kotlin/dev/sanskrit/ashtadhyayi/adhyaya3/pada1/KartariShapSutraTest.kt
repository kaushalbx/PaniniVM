package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationalContext
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Rupa
import dev.sanskrit.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertFalse

class KartariShapSutraTest {
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
