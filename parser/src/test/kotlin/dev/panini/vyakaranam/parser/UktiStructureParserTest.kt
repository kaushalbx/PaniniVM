package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.UktiStructure
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UktiStructureParserTest {
    private val parser = PaniniParser()

    @Test
    fun `retains conditional construction after parsing`() {
        val ukti = parser.parse(
            "यदि पूर्वफल + अम् तर्हि दश + अम् पञ्च + औट् च युज् + णिच् + लोट् + सिप् ।",
        )

        assertEquals(2, ukti.vakyas.size)
        assertEquals(UktiStructure.Conditional(hasAlternate = false), ukti.structure)
    }

    @Test
    fun `retains yavat tavat construction after parsing`() {
        val ukti = parser.parse(
            "यावत् गणना + अम् विद् + लोट् + सिप् तावत् पूर्व + अम् युज् + लोट् + सिप् ।",
        )

        assertEquals(2, ukti.vakyas.size)
        assertIs<UktiStructure.YavatTavat>(ukti.structure)
    }
}
