package dev.panini.ganapatha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SarvadiAntarGanaTest {
    @Test
    fun `tyadadi is represented as an antargana of sarvadi`() {
        val tyadadi = SarvadiGana.antarGanas.single { it.name == "त्यदादिः" }
        assertEquals(listOf("त्यद्", "तद्", "यद्", "एतद्", "इदम्", "अदस्", "एक", "द्वि"), tyadadi.members)
        tyadadi.members.forEach { assertTrue(SarvadiGana.contains(it), it) }
        assertFalse(tyadadi.contains("किम्"))
        assertFalse(tyadadi.contains("युष्मद्"))
    }
}
