package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmParyantaRangeTest {
    @Test
    fun `segmented paryanta range renders as grammatical Sanskrit`() {
        val rendered = PvmUktiSadhaka().sadhayaLine(
            "एक + ङसिँ दशन् + शस् परि + अन्त + अम् सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।",
        )

        assertEquals("एकस्माद् दशपर्यन्तं सङ्ख्यां चिनु ।", rendered)
    }

    @Test
    fun `readable boundary sandhi does not voice final t before an unvoiced consonant`() {
        val rendered = PvmUktiSadhaka().sadhayaLine(
            "त्रिंशत् + अम् च सम् + गण् + णिच् + लोट् + सिप् ।",
        )

        assertEquals("त्रिंशत् च गण् ।", rendered)
    }

    @Test
    fun `chi chooses a number within the typed inclusive range`() {
        repeat(20) {
            val result = PaniniVM().eval(
                "एक + ङसिँ दशन् + शस् परि + अन्त + अम् सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।",
            )
            val value = assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(result).typedValue)
            assertTrue(value.value in 1L..10L)
        }
    }
}
