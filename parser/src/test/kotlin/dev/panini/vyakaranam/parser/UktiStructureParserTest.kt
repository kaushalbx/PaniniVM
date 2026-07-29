package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.UktiStructure
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
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
    fun `yavat remains an ordinary avyaya rather than control syntax`() {
        val ukti = parser.parse(
            "यावत् राम + सुँ गम् + लट् + तिप् ।",
        )

        assertEquals(UktiStructure.Sequence, ukti.structure)
        val vakya = assertIs<AkhyataVakya>(ukti.vakyas.single())
        assertEquals("यावत्", assertIs<AvyayaPada>(vakya.padas.first()).form)
    }
}
