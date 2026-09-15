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

        assertEquals("एकस्मात् दश पर्यन्तम् सङ्ख्याम् चिनु ।", rendered)
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
