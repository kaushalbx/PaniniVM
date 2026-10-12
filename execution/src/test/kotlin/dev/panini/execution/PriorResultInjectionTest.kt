package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class PriorResultInjectionTest {
    private fun parse(source: String) = assertIs<Invocation>(PaniniParser().parse(source).body)

    @Test fun `display object recognition is grammatical and covers nominal AST forms`() {
        val original = parse("राम + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val sentence = original.vakya as AkhyataVakya
        val noun = sentence.padas.filterIsInstance<SubantaPada>().single()
        val accusative = noun.sup
        val forms = listOf<Pada>(noun,
            SankhyaPada("एक + अम्", listOf("एक"), 1L, accusative),
            SankhyaPuranaPada("प्रथम + अम्", listOf("प्रथम"), 1L, accusative),
            SamuccitaSubanta("", listOf(noun, noun)))
        for (form in forms) {
            val node = original.copy(vakya = sentence.copy(padas = listOf(form, sentence.tinganta)))
            assertTrue(PriorResultInjection.hasExplicitDisplayObject(node), form.toString())
        }
        assertFalse(PriorResultInjection.hasExplicitDisplayObject(original.copy(vakya =
            sentence.copy(padas = listOf(noun.copy(sup = SupPratyaya("ङे", "ङे")), sentence.tinganta)))))
        assertFalse(PriorResultInjection.hasExplicitDisplayObject(original.copy(vakya =
            sentence.copy(padas = listOf(sentence.tinganta)))))
        assertFalse(PriorResultInjection.hasExplicitDisplayObject(
            parse("एक + अम् द्वि + अम् च युज् + लोट् + सिप् ।")))
    }
}
