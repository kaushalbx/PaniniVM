package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LitEndingSutrasTest {
    @Test
    fun `3 4 81 replaces lit atmanepada ta and jha endings`() {
        val ta = litState("त", "त")
        val jha = litState("झ", "झ")

        assertTrue(LitasTajhayorEshirecSutra.matches(ta))
        assertEquals("ए", LitasTajhayorEshirecSutra.apply(ta).state.terms.last().surface)
        assertTrue(LitasTajhayorEshirecSutra.matches(jha))
        assertEquals("इरे", LitasTajhayorEshirecSutra.apply(jha).state.terms.last().surface)
    }

    @Test
    fun `3 4 82 assigns all nine lit parasmaipada endings`() {
        val expected = mapOf(
            "तिप्" to "अ", "तस्" to "अतुस्", "झि" to "उस्",
            "सिप्" to "थल्", "थस्" to "अथुस्", "थ" to "अ",
            "मिप्" to "अ", "वस्" to "व", "मस्" to "म",
        )

        expected.forEach { (upadesha, surface) ->
            val state = litState(upadesha, upadesha)
            assertTrue(ParasmaipadanamNalatUsusthalathusaNalvamahSutra.matches(state), upadesha)
            assertEquals(surface, ParasmaipadanamNalatUsusthalathusaNalvamahSutra.apply(state).state.terms.last().surface)
        }
    }

    private fun litState(upadesha: String, surface: String) = DerivationState(
        listOf(DerivationTerm("ending", surface, TermKind.PRATYAYA, upadesha = upadesha)),
        context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
    )
}
