package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsonantStemsTest {

    @Test
    fun `test specialized declensions for nadi rajan and vac`() {
        val engine = SubantaEngine()

        val nadya = engine.derive(SubantaDerivationRequest("नदी", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("नद्या", nadya.final.surface)
        kotlin.test.assertTrue(nadya.applications.any { it.sutra == "1.4.3" })
        kotlin.test.assertTrue(nadya.applications.any { it.sutra == "6.1.77" })

        val nadibhih = engine.derive(SubantaDerivationRequest("नदी", Vibhakti.TRTIYA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("नदीभिः", nadibhih.final.surface)
        kotlin.test.assertTrue(nadibhih.applications.any { it.sutra == "1.4.3" })

        val raja = engine.derive(SubantaDerivationRequest("राजन्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("राजा", raja.final.surface)
        kotlin.test.assertTrue(raja.applications.any { it.sutra == "6.4.8" })
        kotlin.test.assertTrue(raja.applications.any { it.sutra == "8.2.7" })

        val rajna = engine.derive(SubantaDerivationRequest("राजन्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.PUMS))
        assertEquals("राज्ञा", rajna.final.surface)
        kotlin.test.assertTrue(rajna.applications.any { it.sutra == "6.4.134" })
        kotlin.test.assertTrue(rajna.applications.any { it.sutra == "8.4.40" })

        val vak = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("वाक्", vak.final.surface)

        val vaca = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.STRI))
        assertEquals("वाचा", vaca.final.surface)

        val vagbhih = engine.derive(SubantaDerivationRequest("वाच्", Vibhakti.TRTIYA, Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("वाग्भिः", vagbhih.final.surface)
    }
}
