package dev.panini.vyakaranam.ast

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import kotlin.test.*

class VikaranaConstraintTest {
    @Test fun `known active causative contradictions do not claim to reject passive morphology`() {
        for (affix in Vikarana.entries) {
            val form = causative.copy(vikarana = affix)
            assertEquals(affix != Vikarana.SHAP, form.hasContradictoryActiveCausativeVikarana())
            assertFalse(form.copy(ting = TingPratyaya("त", "त")).hasContradictoryActiveCausativeVikarana())
        }
        assertFalse(causative.copy(vikarana = null).hasContradictoryActiveCausativeVikarana())
    }
    private val causative = TingantaPada(
        "deliberately non-semantic source text", emptyList(),
        DhatuPrakriti("ignored", "युज्", listOf("णिच्")),
        Lakara.LOT, TingPratyaya("सिप्", "सिप्"), Vikarana.SHAP,
    )

    @Test fun `constraint uses parsed morphology and only relaxes supported causative shap`() {
        assertNull(causative.lexicalGanaConstraint())
        assertNull(causative.copy(lakara = Lakara.LAT).lexicalGanaConstraint())
        assertEquals(DhatuGana.BHVADI, causative.copy(
            dhatu = causative.dhatu.copy(sanadiPratyayas = emptyList())).lexicalGanaConstraint())
        assertEquals(DhatuGana.BHVADI, causative.copy(
            dhatu = causative.dhatu.copy(sanadiPratyayas = listOf("सन्", "णिच्"))).lexicalGanaConstraint())
        assertEquals(DhatuGana.BHVADI, causative.copy(lakara = Lakara.LIT).lexicalGanaConstraint())
        for (affix in Vikarana.entries.filter { it != Vikarana.SHAP }) {
            assertEquals(affix.gana, causative.copy(vikarana = affix).lexicalGanaConstraint(), affix.upadesha)
        }
    }
}
