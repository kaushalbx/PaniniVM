package dev.sanskrit.derivation

import dev.sanskrit.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BhuLitDerivationTest {
    @Test
    fun `bhu perfect third singular derives babhuva`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.EKAVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूव", result.final.surface)
        val applied = result.applications.map { it.sutra }.toSet()
        assertTrue(
            setOf("3.2.115", "3.4.78", "3.4.82", "6.1.8", "6.4.88", "7.4.59", "7.4.73", "8.4.54").all { it in applied },
            "Missing required sutras from $applied",
        )
    }

    @Test
    fun `bhu perfect third plural derives babhuvuh`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.BAHUVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूवुः", result.final.surface)
        val applied = result.applications.map { it.sutra }.toSet()
        assertTrue(
            setOf("3.2.115", "3.4.78", "3.4.82", "6.1.8", "6.4.88", "7.4.59", "7.4.73", "8.4.54").all { it in applied },
            "Missing required sutras from $applied",
        )
    }

    @Test
    fun `bhu perfect third dual derives babhuvatuh`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.PRATHAMA,
            vacana = Vacana.DVIVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूवतुः", result.final.surface)
    }

    @Test
    fun `bhu perfect second singular derives babhuvitha`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.MADHYAMA,
            vacana = Vacana.EKAVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूविथ", result.final.surface, result.applications.joinToString { "${it.sutra}:${it.after.surface}" })
    }

    @Test
    fun `bhu perfect second dual derives babhuvathuh`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.MADHYAMA,
            vacana = Vacana.DVIVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूवथुः", result.final.surface)
    }

    @Test
    fun `bhu perfect second plural derives babhuva`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.MADHYAMA,
            vacana = Vacana.BAHUVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूव", result.final.surface)
    }

    @Test
    fun `bhu perfect first singular derives babhuva`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.UTTAMA,
            vacana = Vacana.EKAVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूव", result.final.surface)
    }

    @Test
    fun `bhu perfect first dual derives babhuviva`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.UTTAMA,
            vacana = Vacana.DVIVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूविव", result.final.surface)
    }

    @Test
    fun `bhu perfect first plural derives babhuvima`() {
        val bhu = DhatuPatha.all.first { it.upadesha == "भू" }
        val initial = TingantaDerivationRequest(
            dhatu = "भू",
            purusha = Purusha.UTTAMA,
            vacana = Vacana.BAHUVACANA,
            lakara = Lakara.LIT,
        ).initialState(bhu)

        val result = DerivationEngine().derive(initial)

        assertEquals("बभूविम", result.final.surface)
    }

}
