package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SankhyaBhinnaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaBhinnaParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses mixed rational Saardha Dvi utterance`() {
        val ukti = parser.parse("सार्ध + द्वि + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaBhinnaPada>(vakya.padas.single())

        assertEquals(listOf("सार्ध", "द्वि"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }

    @Test
    fun `parses fraction Tri Pada utterance`() {
        val ukti = parser.parse("त्रि + पाद + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaBhinnaPada>(vakya.padas.single())

        assertEquals(listOf("त्रि", "पाद"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }
}
