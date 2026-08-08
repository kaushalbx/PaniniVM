package dev.panini.execution.binding

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubantaVibhaktiTest {

    @Test
    fun `recognizes every genitive number through sup metadata`() {
        assertTrue(subanta("ङस्").hasVibhakti(Vibhakti.SASTHI))
        assertTrue(subanta("ओस्").hasVibhakti(Vibhakti.SASTHI))
        assertTrue(subanta("आम्").hasVibhakti(Vibhakti.SASTHI))
        assertFalse(subanta("ङि").hasVibhakti(Vibhakti.SASTHI))
    }

    private fun subanta(sup: String) = SubantaPada(
        sourceText = "गणित + $sup",
        pratipadika = MulaPratipadika("गणित", "गणित"),
        sup = SupPratyaya(sup, sup),
    )
}
