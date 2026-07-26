package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class PronominalStemsTest {

    private val engine = SubantaEngine()

    @Test
    fun `derives tad pronominal paradigm forms`() {
        val sah = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("सः", sah.final.surface)

        val tau = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.DVIVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("तौ", tau.final.surface)

        val te = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PRATHAMA, Vacana.BAHUVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("ते", te.final.surface)

        val tasmai = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.CHATURTHI, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("तस्मै", tasmai.final.surface)

        val tasmat = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.PANCHAMI, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("तस्मात्", tasmat.final.surface)

        val tasmin = engine.derive(SubantaDerivationRequest("तद्", Vibhakti.SAPTAMI, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("तस्मिन्", tasmin.final.surface)
    }

    @Test
    fun `derives yad kim and idam pronominal forms`() {
        val yah = engine.derive(SubantaDerivationRequest("यद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("यः", yah.final.surface)

        val kah = engine.derive(SubantaDerivationRequest("किम्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("कः", kah.final.surface)

        val ayam = engine.derive(SubantaDerivationRequest("इदम्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, SubantaStemClass.PRONOMINAL_PERSONAL))
        assertEquals("अयम्", ayam.final.surface)
    }
}
