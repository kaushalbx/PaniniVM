package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanadiEngineTest {

    @Test
    fun `derives desiderative forms for bhu pac and ji`() {
        val bhuDesiderative = SanadiEngine.derive("भू", SanadiType.DESIDERATIVE)
        assertEquals("बुभूष्", bhuDesiderative.derivedStem)
        assertEquals("बुभूषति", bhuDesiderative.conjugatedForm)
        assertTrue(bhuDesiderative.steps.any { it.contains("3.1.7") })

        val pacDesiderative = SanadiEngine.derive("पच्", SanadiType.DESIDERATIVE)
        assertEquals("पिपक्ष्", pacDesiderative.derivedStem)
        assertEquals("पिपक्षति", pacDesiderative.conjugatedForm)

        val jiDesiderative = SanadiEngine.derive("जि", SanadiType.DESIDERATIVE)
        assertEquals("जिगीष्", jiDesiderative.derivedStem)
        assertEquals("जिगीषति", jiDesiderative.conjugatedForm)
    }

    @Test
    fun `derives causative forms for bhu kru and pac`() {
        val bhuCausative = SanadiEngine.derive("भू", SanadiType.CAUSATIVE)
        assertEquals("भावि", bhuCausative.derivedStem)
        assertEquals("भावयति", bhuCausative.conjugatedForm)
        assertTrue(bhuCausative.steps.any { it.contains("3.1.26") })

        val kruCausative = SanadiEngine.derive("कृ", SanadiType.CAUSATIVE)
        assertEquals("कारि", kruCausative.derivedStem)
        assertEquals("कारयति", kruCausative.conjugatedForm)

        val pacCausative = SanadiEngine.derive("पच्", SanadiType.CAUSATIVE)
        assertEquals("पाचि", pacCausative.derivedStem)
        assertEquals("पाचयति", pacCausative.conjugatedForm)
    }

    @Test
    fun `derives intensive form for bhu`() {
        val bhuIntensive = SanadiEngine.derive("भू", SanadiType.INTENSIVE)
        assertEquals("बोभूय्", bhuIntensive.derivedStem)
        assertEquals("बोभूयते", bhuIntensive.conjugatedForm)
        assertTrue(bhuIntensive.steps.any { it.contains("3.1.22") })
    }
}
