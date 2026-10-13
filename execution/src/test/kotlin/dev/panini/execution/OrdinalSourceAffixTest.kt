package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class OrdinalSourceAffixTest {
    private fun ordinal(segments: String): SankhyaPuranaPada {
        val invocation = PaniniParser().parse("$segments + अम् मुद्र् + णिच् + लोट् + सिप् ।").body as Invocation
        return invocation.vakya.padas.filterIsInstance<SankhyaPuranaPada>().single()
    }

    @Test fun `bare ordinal values cannot replace unsupported source affixes`() {
        for (segments in listOf("द्वि + तम", "एक + अमच्")) {
            val pada = ordinal(segments)
            val failure = assertFailsWith<IllegalArgumentException> {
                PuranaPratyayaResolver.semanticValue(pada)
            }
            assertTrue(failure.message.orEmpty().contains("ordinal derivation"))
            assertEquals(pada.sourceText, PvmUktiSadhaka().sadhayaSankhyaPurana(pada))
        }
    }

    @Test fun `supported source affixes produce ordinal values and inflected rendering`() {
        val pada = ordinal("द्वि + तीय")
        assertEquals(SanskritValue.Purana(2, "द्वितीय"), PuranaPratyayaResolver.semanticValue(pada))
        assertEquals("द्वितीयम्", PvmUktiSadhaka().sadhayaSankhyaPurana(pada))
    }

    @Test fun `cached rank cannot override source morphology`() {
        val pada = ordinal("द्वि + तीय").copy(value = 3)
        assertFailsWith<IllegalArgumentException> { PuranaPratyayaResolver.semanticValue(pada) }
    }
}
