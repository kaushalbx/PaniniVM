package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class SubantaNumeralDeclensionTest {
    private val engine = SubantaEngine()

    @Test
    fun `subanta engine declines numeral pratipadikas`() {
        val masculineDual = engine.derive(
            SubantaDerivationRequest("द्वि", Vibhakti.DVITIYA, Vacana.DVIVACANA, Linga.PUMS),
        )
        assertEquals(
            "द्वौ",
            masculineDual.final.surface,
            masculineDual.applications.joinToString("\n") {
                "${it.sutra}: ${it.after.terms.map { term -> "${term.surface}:${term.itMarkers}" }}"
            },
        )
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
