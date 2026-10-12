package dev.panini.vyakaranam.ast

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame

class SupBearingPadaTest {
    @Test fun `all nominal value encodings expose the original sup morphology`() {
        val sup = SupPratyaya("भ्याम्", "भ्याम्")
        val padas: List<SupBearingPada> = listOf(
            SubantaPada("राम", MulaPratipadika("राम", "राम"), sup),
            SankhyaPada("एक", listOf("एक"), 1L, sup),
            SankhyaPuranaPada("प्रथम", listOf("प्रथम"), 1L, sup),
            KatapayadiPada("क", "क", 1L, sup),
            AryabhatiyaPada("क", "क", 1L, sup),
            BhutasamkhyaPada("चन्द्र", listOf("चन्द्र"), 1L, sup),
        )
        for (pada in padas) {
            assertSame(sup, pada.sup)
            assertEquals(listOf(SupAffix.BHYAM_3, SupAffix.BHYAM_4, SupAffix.BHYAM_5), pada.sup.affixCandidates)
            assertFalse(pada.sup.supports(Vibhakti.DVITIYA))
        }
    }
}
