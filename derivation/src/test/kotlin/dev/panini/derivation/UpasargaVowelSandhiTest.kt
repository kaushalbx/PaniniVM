package dev.panini.derivation

import dev.panini.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals

class UpasargaVowelSandhiTest {
    private val engine = SandhiEngine()
    private fun root(upadesha: String) = DhatuPatha.all.first { it.upadesha == upadesha }

    @Test fun `6_1_94 and 6_1_91 apply through the sutras at a prefix`() {
        assertEquals("उपेजते", engine.joinPrefix("उप", "एजते"))
        // 6.1.91 gives आर्; the optional 8.4.65 then drops च् before छ by default (प्रार्च्छति is the other form).
        assertEquals("प्रार्छति", engine.joinPrefix("प्र", "ऋच्छति"))
        // आ is the upasarga आङ्: no dangling mātrā.
        assertEquals("एजते", engine.joinPrefix("आ", "एजते"))
    }

    @Test fun `6_1_89 needs the root of the verbal form`() {
        assertEquals("उपैति", engine.joinPrefix("उप", "एति", root("इण्")))
        assertEquals("परैति", engine.joinPrefix("परा", "एति", root("इण्")))
        assertEquals("उपैधते", engine.joinPrefix("उप", "एधते", root("एधँ")))
        // Guṇa before the इ of इण्: 6.1.89 needs an एच्.
        assertEquals("उपेतः", engine.joinPrefix("उप", "इतः", root("इण्")))
        // Without a proven root the typed form takes 6.1.94.
        assertEquals("उपेति", engine.joinPrefix("उप", "एति"))
    }

    @Test fun `an ordinary word after a prefix-like word is no prefix boundary`() {
        assertEquals("प्रर्षभ", engine.join("प्र", "ऋषभ").final.surface)
    }

    @Test fun `multiple prefixes fold from the inside`() {
        assertEquals("उपेहि", engine.joinPrefix("उप", engine.joinPrefix("आ", "इहि")))
    }
}
