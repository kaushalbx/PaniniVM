package dev.panini.sankhya

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

/** Vārttika on 6.3.109, षष उत्वं दतृदशधासूत्तरपदादेः ष्टुत्वं च, for the numeral uttarapada दश. */
class ShashaUtvaVarttikaTest {
    private val evaluator = SankhyaEvaluator()
    private val generator = SankhyaGenerator()
    private fun stem(vararg stems: String) = generator.pratipadika(evaluator.evaluateStems(stems.toList()))

    @Test fun `shash before dasha becomes shodasha`() {
        assertEquals("षोडशन्", stem("षष्", "दशन्"))
        val sixteen = evaluator.evaluateStems(listOf("षष्", "दशन्"))
        assertEquals(16L, sixteen.value)
        assertEquals("षोडशभिः", generator.decline(sixteen, Vibhakti.TRTIYA, Vacana.BAHUVACANA))
        assertEquals("षोडशसु", generator.decline(sixteen, Vibhakti.SAPTAMI, Vacana.BAHUVACANA))
    }

    @Test fun `other units before dasha are unaffected`() {
        assertEquals("एकादशन्", stem("एक", "दशन्"))
        assertEquals("त्रयोदशन्", stem("त्रि", "दशन्"))
        assertEquals("पञ्चदशन्", stem("पञ्चन्", "दशन्"))
        assertEquals("अष्टादशन्", stem("अष्टन्", "दशन्"))
    }

    @Test fun `shash before another ten keeps its own sandhi`() {
        assertEquals(26L, evaluator.evaluateStems(listOf("षष्", "विंशति")).value)
        assertEquals(false, stem("षष्", "विंशति").startsWith("षो"))
    }
}
