package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NamasDvisVisargaTest {
    private val engine = SandhiEngine()
    private fun all(left: SandhiEngine.Pada, right: String) = engine.joinAll(left,
        SandhiEngine.Pada(DerivationTerm("r", right, TermKind.PRATIPADIKA, upadesha = right)))
        .withoutIndependentOptions().map(engine::render).toSet()
    private fun pada(text: String, vararg samjnas: Samjna) =
        SandhiEngine.Pada(DerivationTerm("l", text, TermKind.PRATIPADIKA, upadesha = text), samjnas = samjnas.toSet())

    @Test fun `8_3_40 gives s for a gati namas and puras`() {
        assertEquals(setOf("नमस्करोति"), all(pada("नमस्", Samjna.GATI), "करोति"))
        assertEquals(setOf("पुरस्करोति"), all(pada("पुरस्", Samjna.GATI), "करोति"))
        // Not gati: no स्.
        assertTrue(all(pada("नमस्"), "करोति").none { "नमस्क" in it })
    }

    @Test fun `8_3_43 optionally gives sh for dvis and tris`() {
        val forms = all(pada("द्विस्"), "करोति")
        assertTrue("द्विष्करोति" in forms && forms.any { it.startsWith("द्विः") || it.startsWith("द्विᳵ") }, forms.toString())
        assertTrue("त्रिष्करोति" in all(pada("त्रिस्"), "करोति"))
        // A word given with visarga has no proof of सुच्.
        assertTrue(all(pada("द्विः"), "करोति").none { "ष्" in it })
    }
}
