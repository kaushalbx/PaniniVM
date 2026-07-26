package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.NamaVakya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BhutasamkhyaParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses Bhutasamkhya option A utterance`() {
        val ukti = parser.parse("भूतसङ्ख्या नेत्र + वेद + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<BhutasamkhyaPada>(vakya.padas.single())

        assertEquals(listOf("नेत्र", "वेद"), pada.terms)
        assertEquals("अम्", pada.sup.text)
    }
}
