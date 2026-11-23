package dev.panini.sankhya

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

/** Forms the numeral grammar previously derived wrongly, each with the rule that decides it. */
class NumeralFormCorrectionsTest {
    private val generator = SankhyaGenerator()
    private fun plural(value: Long, vibhakti: Vibhakti) = generator.decline(value, vibhakti, Vacana.BAHUVACANA, Linga.PUMS)

    @Test fun `shash genitive plural takes the obligatory nasal of the 8_4_45 varttika`() {
        // षष् + नाम् (7.1.55) → षड् + णाम् (8.2.39, 8.4.41) → षण्णाम् (यरोऽनुनासिके प्रत्यये भाषायां नित्यम्).
        assertEquals("षण्णाम्", plural(6L, Vibhakti.SASTHI))
        // Not before a non-nasal suffix.
        assertEquals("षड्भिः", plural(6L, Vibhakti.TRTIYA))
        assertEquals("षट्सु", plural(6L, Vibhakti.SAPTAMI))
    }

    @Test fun `ashta takes bhis unchanged because 7_1_9 needs short a`() {
        // 7.2.84 अष्टन आ विभक्तौ gives अष्टा; अतः in 7.1.9 is tapara (1.1.70), so no ऐस्.
        assertEquals(
            listOf("अष्टौ", "अष्टौ", "अष्टाभिः", "अष्टाभ्यः", "अष्टाभ्यः", "अष्टानाम्", "अष्टासु"),
            Vibhakti.entries.map { plural(8L, it) },
        )
        // An अ-final stem still takes ऐस्.
        assertEquals("एकैः", generator.decline(1L, Vibhakti.TRTIYA, Vacana.BAHUVACANA, Linga.PUMS))
    }

    @Test fun `a numeral before a consonant-initial taddhita is a pada and loses its n`() {
        // 1.4.17 स्वादिष्वसर्वनामस्थाने, then 8.2.7 नलोपः प्रातिपदिकान्तस्य.
        assertEquals("दशकृत्वः", generator.frequency(10L).final.surface)
        assertEquals("अष्टकृत्वः", generator.frequency(8L).final.surface)
        assertEquals("दशधा", generator.distribution(10L).final.surface)
        assertEquals("अष्टधा", generator.distribution(8L).final.surface)
        assertEquals("पञ्चकृत्वः", generator.frequency(5L).final.surface)
    }

    @Test fun `frequency forms are avyaya padas whose final cluster the Tripadi completes`() {
        // 5.4.19 एक → सकृत् + सुच्; 1.1.38 avyaya; सुँ elided by 2.4.82; 8.2.23 drops the cluster-final स्.
        val once = generator.frequency(1L)
        assertEquals("सकृत्", once.final.surface)
        val onceRules = once.applications.map { it.sutra }
        assertEquals(true, listOf("5.4.19", "1.1.38", "2.4.82", "8.2.23").all { it in onceRules }, onceRules.toString())
        // 5.4.18 चतुर् + सुच्; 8.2.24 रात्सस्य drops स् after र्; 8.3.15 gives the visarga.
        val four = generator.frequency(4L)
        assertEquals("चतुः", four.final.surface)
        assertEquals(true, "8.2.24" in four.applications.map { it.sutra })
        // A compound numeral is one avyaya prātipadika, carried by its head.
        assertEquals("एकादशकृत्वः", generator.frequency(11L).final.surface)
        assertEquals("पञ्चविंशतिकृत्वः", generator.frequency(25L).final.surface)
        assertEquals("षड्धा", generator.distribution(6L).final.surface)
    }

    @Test fun `two three and four take suc in the frequency sense by 5_4_18`() {
        assertEquals("द्विः", generator.frequency(2L).final.surface)
        assertEquals("त्रिः", generator.frequency(3L).final.surface)
        val applied = generator.frequency(2L).applications.map { it.sutra }
        assertEquals(true, "5.4.18" in applied && "5.4.17" !in applied, applied.toString())
    }
}
