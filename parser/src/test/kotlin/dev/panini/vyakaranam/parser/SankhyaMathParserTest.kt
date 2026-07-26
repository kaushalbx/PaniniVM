package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SankhyaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaMathParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses Dvi Gunita utterance`() {
        val ukti = parser.parse("द्वि + गुणित + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaPada>(vakya.padas.single())

        assertEquals(listOf("द्वि", "गुणित"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }

    @Test
    fun `parses Varga Krita utterance`() {
        val ukti = parser.parse("वर्ग + कृत + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<SankhyaPada>(vakya.padas.single())

        assertEquals(listOf("वर्ग", "कृत"), pada.stems)
        assertEquals("अम्", pada.sup.text)
    }
}
