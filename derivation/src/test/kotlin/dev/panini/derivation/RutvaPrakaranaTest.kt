package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertTrue

class RutvaPrakaranaTest {
    private val engine = SandhiEngine()
    private fun all(left: String, right: String) = engine.joinAll(left, right).withoutIndependentOptions().map(engine::render).toSet()

    @Test fun `8_3_6 pum takes ru and s before khay am`() {
        val forms = all("पुम्", "कोकिलः")
        assertTrue(forms.isNotEmpty() && forms.all { it in setOf("पुंस्कोकिलः", "पुँस्कोकिलः") }, forms.toString())
        // Not before a khay followed by a consonant.
        assertTrue(all("पुम्", "क्षीरम्").none { "स्" in it }, all("पुम्", "क्षीरम्").toString())
    }

    @Test fun `8_3_10 nrn takes ru before p`() {
        val forms = all("नॄन्", "पाहि")
        assertTrue(forms.isNotEmpty() && forms.none { "न्प" in it || "न् प" in it }, forms.toString())
        assertTrue(forms.any { "ः" in it }, forms.toString())
    }

    @Test fun `8_3_12 kan takes ru before its amredita`() {
        val forms = all("कान्", "कान्")
        assertTrue(forms.isNotEmpty() && forms.all { it in setOf("कांस्कान्", "काँस्कान्") }, forms.toString())
    }
}
