package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BhuLitDerivationTest {
    @Test
    fun `bhu perfect third singular selects lit and derives babhuva`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.EKAVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूव", result.final.surface)
        assertTrue(setOf("3.2.115", "3.4.78", "3.4.82", "6.4.88", "7.4.59", "7.4.73", "8.4.54").all { it in result.applications.map { application -> application.sutra } })
    }

    @Test
    fun `bhu perfect third dual selects lit and derives babhuvatus`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.DVIVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूवतुः", result.final.surface)
        assertTrue(setOf("3.2.115", "3.4.78", "3.4.82", "6.4.88", "7.4.59", "7.4.73", "8.4.54").all { it in result.applications.map { application -> application.sutra } })
    }
}
