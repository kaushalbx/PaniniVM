package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SubantaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class SankhyaParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses segmented sankhyaPada`() {
        val ukti = parser.parse("द्वि + विंशति + जस् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val sankhyaPada = assertIs<SankhyaPada>(vakya.padas.single())

        assertEquals(listOf("द्वि", "विंशति"), sankhyaPada.stems)
        assertEquals("जस्", sankhyaPada.sup.text)
    }

    @Test
    fun `parses single primitive sankhya subantaPada`() {
        val ukti = parser.parse("षष् + जस् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SubantaPada>(vakya.padas.single())
        val pratipadika = assertIs<MulaPratipadika>(pada.pratipadika)

        assertEquals("षष्", pratipadika.text)
    }

    @Test
    fun `parses sentence containing Adhika avyaya`() {
        val ukti = parser.parse("द्वि + विंशति + जस् शत + अम् अधिक ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())

        assertEquals(3, vakya.padas.size)
        val p1 = assertIs<SankhyaPada>(vakya.padas[0])
        val p2 = assertIs<SubantaPada>(vakya.padas[1])
        val p3 = vakya.padas[2]

        assertEquals(listOf("द्वि", "विंशति"), p1.stems)
        assertIs<MulaPratipadika>(p2.pratipadika)
        assertNotNull(p3)
    }
}
