package dev.panini.dhatupatha

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.dhatupatha.divadi.JanDhatu
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.TingPratyaya
import kotlin.test.*

class JanDhatuTest {
    @Test fun `result production belongs to causative jan declaration form only`() {
        val entry = JanDhatu()
        val form = TingantaPada(
            sourceText = "जन् + णिच् + लट् + तिप्",
            upasargas = emptyList(),
            dhatu = DhatuPrakriti("जन् + णिच्", "जन्", listOf("णिच्")),
            lakara = Lakara.LAT,
            ting = TingPratyaya("तिप्", "तिप्"),
        )
        assertTrue(entry.denotesResultProduction(form))
        for (vikarana in dev.panini.vyakaranam.ast.Vikarana.entries) {
            assertEquals(vikarana == dev.panini.vyakaranam.ast.Vikarana.SHAP,
                entry.denotesResultProduction(form.copy(vikarana = vikarana)), vikarana.upadesha)
        }
        assertTrue(entry.denotesResultProduction(form.copy(dhatu = form.dhatu.copy(mulaDhatu = entry.upadesha))))
        assertFalse(entry.denotesResultProduction(form.copy(dhatu = form.dhatu.copy(mulaDhatu = "भू"))))
        assertFalse(entry.denotesResultProduction(form.copy(dhatu = form.dhatu.copy(sanadiPratyayas = emptyList()))))
        assertFalse(entry.denotesResultProduction(form.copy(upasargas = listOf("प्र"))))
        assertFalse(entry.denotesResultProduction(form.copy(lakara = Lakara.LOT)))
        assertFalse(entry.denotesResultProduction(form.copy(ting = TingPratyaya("सिप्", "सिप्"))))
    }

    @Test fun `registered jan entry retains canonical lexical metadata`() {
        val entry = assertIs<JanDhatu>(DhatuPatha.all.single { it.id == "04.0044" })
        assertEquals("जनीँ", entry.upadesha)
        assertEquals("जन्", entry.sourceSurface)
        assertEquals("प्रादुर्भावे", entry.artha)
        assertEquals(DhatuGana.DIVADI, entry.gana)
        assertEquals(PadaType.ATMANEPADA, entry.pada)
        assertTrue(entry.operations.isEmpty(), "A declaration predicate must not invent an executable action")
    }
}
