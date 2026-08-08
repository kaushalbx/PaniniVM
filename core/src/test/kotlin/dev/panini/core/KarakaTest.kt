package dev.panini.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KarakaTest {
    @Test
    fun `resolves canonical pratipadikas and common stem variants`() {
        assertEquals(Karaka.KARTR, Karaka.fromPratipadika("कर्तृ"))
        assertEquals(Karaka.KARMAN, Karaka.fromPratipadika("कर्मन्"))
        assertEquals(Karaka.KARMAN, Karaka.fromPratipadika("कर्म"))
        assertEquals(Karaka.KARANA, Karaka.fromPratipadika("करणम्"))
        assertEquals(Karaka.SAMPRADANA, Karaka.fromPratipadika("सम्प्रदान"))
        assertNull(Karaka.fromPratipadika("राम"))
    }
}
