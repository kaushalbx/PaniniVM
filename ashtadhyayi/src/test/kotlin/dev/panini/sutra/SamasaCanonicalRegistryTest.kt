package dev.panini.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import kotlin.test.Test
import kotlin.test.assertEquals

class SamasaCanonicalRegistryTest {
    @Test
    fun `registered samasa sutra numbers are unique`() {
        val registrations = Ashtadhyayi.cataloguedSutras
            .filterIsInstance<SamasaSutra>()
            .map { it as Sutra<*, *> }
            .groupBy { it.number }
        val duplicates = registrations
            .filterValues { it.size > 1 }
            .mapValues { (_, sutras) -> sutras.map { it.text } }

        assertEquals(emptyMap(), duplicates)
    }
}
