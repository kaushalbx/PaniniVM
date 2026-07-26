package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SankhyaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaGeoParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses Jya utterance`() {
        val ukti = parser.parse("ज्या + नवति + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaPada>(vakya.padas.single())

        assertEquals(listOf("ज्या", "नवति"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }

    @Test
    fun `parses Paridhi utterance`() {
        val ukti = parser.parse("परिधि + दशन् + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaPada>(vakya.padas.single())

        assertEquals(listOf("परिधि", "दशन्"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }
}
