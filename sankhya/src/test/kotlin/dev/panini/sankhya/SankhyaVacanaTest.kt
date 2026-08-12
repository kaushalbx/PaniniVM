package dev.panini.sankhya

import dev.panini.core.Vacana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SankhyaVacanaTest {
    @Test
    fun `cardinals select their intrinsic vacana`() {
        assertEquals(Vacana.EKAVACANA, SankhyaVacana.requiredFor(1))
        assertEquals(Vacana.DVIVACANA, SankhyaVacana.requiredFor(2))
        assertEquals(Vacana.BAHUVACANA, SankhyaVacana.requiredFor(3))
        assertEquals(Vacana.BAHUVACANA, SankhyaVacana.requiredFor(100))
        assertFailsWith<IllegalArgumentException> {
            SankhyaVacana.requireCompatible(2, Vacana.EKAVACANA)
        }
    }
}
