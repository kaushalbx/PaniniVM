package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.NamaVakya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AryabhatiyaParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses Aryabhatiya option A utterance`() {
        val ukti = parser.parse("आर्यभटीय गि + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<AryabhatiyaPada>(vakya.padas.single())

        assertEquals("गि", pada.word)
        assertEquals("अम्", pada.sup.text)
    }
}
