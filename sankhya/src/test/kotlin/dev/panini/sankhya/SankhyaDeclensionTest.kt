package dev.panini.sankhya

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaDeclensionTest {
    @Test
    fun `generator declines cardinals through the reusable API`() {
        val generator = SankhyaGenerator()

        assertEquals("द्वाभ्याम्", generator.decline(2, Vibhakti.TRTIYA, Vacana.DVIVACANA))
        assertEquals("त्रिभिः", generator.decline(3, Vibhakti.TRTIYA, Vacana.BAHUVACANA))
        assertEquals("चतुर्णाम्", generator.decline(4, Vibhakti.SASTHI, Vacana.BAHUVACANA))
    }
}
