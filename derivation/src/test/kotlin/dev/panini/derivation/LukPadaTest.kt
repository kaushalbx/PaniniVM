package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.*

class LukPadaTest {
    private fun state(stem: String, v: Vibhakti) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, v, Vacana.EKAVACANA, Linga.NAPUMSAKA)).final

    @Test fun `1_4_14 with 1_1_62 keeps a pada after luk of am`() {
        for (stem in listOf("किम्", "वारि")) {
            val acc = state(stem, Vibhakti.DVITIYA)
            assertTrue("7.1.23" in acc.appliedSutras)
            assertTrue(acc.samjnas.any { it.targetId == acc.terms.last().id && it.samjna == Samjna.PADA }, acc.samjnas.toString())
            // Same pada status as the प्रथमा, whose सु is also elided.
            val nom = state(stem, Vibhakti.PRATHAMA)
            assertTrue(nom.samjnas.any { it.targetId == nom.terms.last().id && it.samjna == Samjna.PADA })
        }
    }
}
