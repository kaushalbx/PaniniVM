package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.NamaVakya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class KatapayadiParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses Katapayadi option A utterance`() {
        val ukti = parser.parse("कटपय माधव + अम् ।")
        val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
        val pada = assertIs<KatapayadiPada>(vakya.padas.single())

        assertEquals("माधव", pada.word)
        assertEquals("अम्", pada.sup.text)
    }
}
