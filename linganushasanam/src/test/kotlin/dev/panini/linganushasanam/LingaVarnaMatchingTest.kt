package dev.panini.linganushasanam

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LingaVarnaMatchingTest {
    @Test
    fun `matching ignores orthographic shaping controls but preserves phonological distinctions`() {
        assertTrue("द्\u200Dयुभ्यः".phonologicallyEquals("द्युभ्यः"))
        assertTrue("राजसभा".endsWithVarnas("आ"))
        assertFalse("अङ्गुली".phonologicallyEquals("अङ्गुलि"))
    }
}
