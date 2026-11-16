package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.*

class SambuddhiTest {
    private fun voc(stem: String, linga: Linga = Linga.PUMS, vacana: Vacana = Vacana.EKAVACANA) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.PRATHAMA, vacana, linga, sambodhana = true)).final

    @Test fun `6_1_69 deletes the hal of a sambuddhi after a short vowel or eng`() {
        assertEquals("राम", voc("राम").surface)
        assertTrue("6.1.69" in voc("राम").appliedSutras)
        // 7.3.108 guṇa first, then 6.1.69 after एङ्.
        assertEquals("हरे", voc("हरि").surface)
        assertEquals("गुरो", voc("गुरु").surface)
    }

    @Test fun `6_1_69 deletes the m of am merged by 6_1_107`() {
        val phala = voc("फल", Linga.NAPUMSAKA)
        assertEquals("फल", phala.surface)
        assertTrue(listOf("7.1.24", "6.1.107", "6.1.69").all { it in phala.appliedSutras })
    }

    @Test fun `8_2_8 keeps the n of a pratipadika before sambuddhi`() {
        assertEquals("राजन्", voc("राजन्").surface)
        // Without address 8.2.7 deletes it after 6.4.8: राजा.
        assertEquals("राजा", SubantaEngine().derive(SubantaDerivationRequest("राजन्", Vibhakti.PRATHAMA, Vacana.EKAVACANA)).final.surface)
    }

    @Test fun `asambuddhau blocks 6_4_10 and 7_1_94`() {
        val puman = voc("पुम्स्")
        assertEquals("पुमन्", puman.surface)
        assertFalse("6.4.10" in puman.appliedSutras)
        assertEquals("पितः", voc("पितृ").surface)
        assertFalse("7.1.94" in voc("पितृ").appliedSutras)
    }

    @Test fun `plural address is not sambuddhi`() {
        assertEquals("रामाः", voc("राम", vacana = Vacana.BAHUVACANA).surface)
        assertEquals("पुमांसः", voc("पुम्स्", vacana = Vacana.BAHUVACANA).surface)
    }

    @Test fun `address requires prathama`() {
        assertFailsWith<IllegalArgumentException> {
            SubantaDerivationRequest("राम", Vibhakti.DVITIYA, Vacana.EKAVACANA, sambodhana = true)
        }
    }
}
