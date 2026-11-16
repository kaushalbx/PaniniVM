package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals

/** 8.4.34 न भाभूपूकमिगमिप्यायीवेपाम्: no ṇatva of a kṛt न after an upasarga for these roots. */
class NaBhaBhuNatvaTest {
    private fun lyut(upasarga: String, root: String) =
        KrdantaEngine().derive(KrdantaDerivationRequest(root, Samjna.LYUT, upasarga = upasarga)).final.surface

    @Test fun `listed roots keep dental na`() {
        assertEquals("प्रभवन", lyut("प्र", "भू"))
        assertEquals("प्रभान", lyut("प्र", "भा"))
        assertEquals("प्रगमन", lyut("प्र", "गमॢँ"))
        assertEquals("प्रकमन", lyut("प्र", "कमुँ"))
    }

    @Test fun `other roots still take natva`() {
        assertEquals("प्रयाण", lyut("प्र", "या"))
        assertEquals("प्रवपण", lyut("प्र", "वप्"))
        assertEquals("निर्याण", lyut("निर्", "या"))
    }
}
