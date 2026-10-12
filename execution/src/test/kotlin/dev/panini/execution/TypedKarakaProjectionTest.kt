package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class TypedKarakaProjectionTest {
    @Test fun `instrumental recognition preserves every interpretation of shared endings`() {
        for (ending in SupAffix.entries.map { it.upadesha }.distinct()) {
            assertEquals(
                SupAffix.candidates(ending).any { it.vibhakti == Vibhakti.TRTIYA },
                SubantaKarakaParser.hasTritiyaInstrumental("राम + $ending ।"),
                ending,
            )
        }
    }

    @Test fun `accusative projection is grammatical rather than singular-only`() {
        for (ending in SupAffix.entries.map { it.upadesha }.distinct()) {
            val expected = if (SupAffix.candidates(ending).any { it.vibhakti == Vibhakti.DVITIYA })
                listOf("राम") else emptyList()
            assertEquals(expected, SubantaKarakaParser.extractKarmaTerms("राम + $ending ।"), ending)
        }
    }
}
