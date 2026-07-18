package dev.panini.derivation

import dev.panini.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KriLitDerivationTest {
    @Test
    fun `kri perfect third singular derives cakara`() {
        val kri = DhatuPatha.all.first { it.upadesha == "डुकृञ्" }
        val initial = TingantaDerivationRequest(
            dhatu = "कृ",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.EKAVACANA,
            lakara = Lakara.LIT,
        ).initialState(kri)

        val result = DerivationEngine().derive(initial)

        assertEquals("चकार", result.final.surface, result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
        val applied = result.applications.map { it.sutra }.toSet()
        assertTrue(setOf("6.1.8", "7.2.115", "7.4.62", "7.4.66").all { it in applied })
    }
}
