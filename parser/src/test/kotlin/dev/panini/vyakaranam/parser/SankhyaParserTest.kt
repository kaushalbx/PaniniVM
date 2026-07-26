package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SankhyaParserTest {

    private val parser = PaniniParser()

    @Test
    fun `parses segmented sankhyaAbhyasaPada`() {
        try {
            val ukti = parser.parse("पञ्च + कृत्वः ।")
            val vakya = assertIs<NamaVakya>(ukti.vakyas.single())
            val abhyasaPada = assertIs<SankhyaAbhyasaPada>(vakya.padas.single())
            assertEquals(listOf("पञ्च", "कृत्वः"), abhyasaPada.stems)
        } catch (e: Exception) {
            println("PARSER_ERROR: ${e.message}")
            throw e
        }
    }
}
