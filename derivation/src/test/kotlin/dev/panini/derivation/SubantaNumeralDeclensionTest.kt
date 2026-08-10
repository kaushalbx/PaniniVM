package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class SubantaNumeralDeclensionTest {
    private val engine = SubantaEngine()

    @Test
    fun `subanta engine declines numeral pratipadikas`() {
        assertEquals(
            "द्वाभ्याम्",
            engine.derive(SubantaDerivationRequest("द्वि", Vibhakti.TRTIYA, Vacana.DVIVACANA)).final.surface,
        )
        assertEquals(
            "त्रिभिः",
            engine.derive(SubantaDerivationRequest("त्रि", Vibhakti.TRTIYA, Vacana.BAHUVACANA)).final.surface,
        )
        assertEquals(
            "चतुर्णाम्",
            engine.derive(SubantaDerivationRequest("चतुर्", Vibhakti.SASTHI, Vacana.BAHUVACANA)).final.surface,
        )
    }
}
