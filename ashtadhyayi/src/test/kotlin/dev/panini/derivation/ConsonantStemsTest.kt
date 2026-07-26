package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class ConsonantStemsTest {

    @Test
    fun `test guess method recognizes consonant stems`() {
        assertEquals(SubantaStemClass.IN_STEM_MASCULINE, SubantaStemClass.guess("धनिन्"))
        assertEquals(SubantaStemClass.IN_STEM_MASCULINE, SubantaStemClass.guess("योगिन्"))
        assertEquals(SubantaStemClass.MATUP_STEM, SubantaStemClass.guess("भगवत्"))
        assertEquals(SubantaStemClass.MATUP_STEM, SubantaStemClass.guess("धीमत्"))
        assertEquals(SubantaStemClass.T_STEM, SubantaStemClass.guess("मरुत्"))
        assertEquals(SubantaStemClass.D_STEM, SubantaStemClass.guess("सम्पद्"))
        assertEquals(SubantaStemClass.C_STEM, SubantaStemClass.guess("वाच्"))
    }

    @Test
    fun `test specialized declensions for nadi rajan and vac`() {
        val engine = SubantaEngine()

        val nadya = engine.derive(SubantaDerivationRequest("नदी", dev.panini.core.Vibhakti.TRTIYA, dev.panini.core.Vacana.EKAVACANA, SubantaStemClass.II_STEM_FEMININE))
        assertEquals("नद्या", nadya.final.surface)

        val nadibhih = engine.derive(SubantaDerivationRequest("नदी", dev.panini.core.Vibhakti.TRTIYA, dev.panini.core.Vacana.BAHUVACANA, SubantaStemClass.II_STEM_FEMININE))
        assertEquals("नदीभिः", nadibhih.final.surface)

        val raja = engine.derive(SubantaDerivationRequest("राजन्", dev.panini.core.Vibhakti.PRATHAMA, dev.panini.core.Vacana.EKAVACANA, SubantaStemClass.N_STEM_MASCULINE))
        assertEquals("राजा", raja.final.surface)

        val rajna = engine.derive(SubantaDerivationRequest("राजन्", dev.panini.core.Vibhakti.TRTIYA, dev.panini.core.Vacana.EKAVACANA, SubantaStemClass.N_STEM_MASCULINE))
        assertEquals("राज्ञा", rajna.final.surface)

        val vak = engine.derive(SubantaDerivationRequest("वाच्", dev.panini.core.Vibhakti.PRATHAMA, dev.panini.core.Vacana.EKAVACANA, SubantaStemClass.C_STEM))
        assertEquals("वाक्", vak.final.surface)

        val vaca = engine.derive(SubantaDerivationRequest("वाच्", dev.panini.core.Vibhakti.TRTIYA, dev.panini.core.Vacana.EKAVACANA, SubantaStemClass.C_STEM))
        assertEquals("वाचा", vaca.final.surface)

        val vagbhih = engine.derive(SubantaDerivationRequest("वाच्", dev.panini.core.Vibhakti.TRTIYA, dev.panini.core.Vacana.BAHUVACANA, SubantaStemClass.C_STEM))
        assertEquals("वाग्भिः", vagbhih.final.surface)
    }
}
