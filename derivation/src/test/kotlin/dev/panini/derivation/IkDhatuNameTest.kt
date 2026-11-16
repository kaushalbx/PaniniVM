package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IkDhatuNameTest {
    private fun name(dhatu: String) =
        (KrdantaEngine().deriveSourceStem(dhatu, "इक्") as KrdantaSourceStem.Productive)

    @Test fun `a dhatu is named with ik and keeps its vowel`() {
        assertEquals("मृजि", name("मृजूँ").surface)
        assertEquals("वचि", name("वचँ").surface)
        assertEquals("जी", name("जि").surface)   // 6.1.101 joins the two इ
        assertTrue(name("मृजूँ").derivation.applications.any { it.sutra == "vartika:3.3.108:ik-shtipau" })
    }

    @Test fun `ik names are offered as stems`() {
        val names = DhatuPratyayaNamePratipadikas.ikNames()
        assertTrue("मृजि" in names && "वचि" in names)
    }
}
