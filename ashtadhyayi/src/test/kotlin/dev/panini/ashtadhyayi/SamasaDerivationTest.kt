package dev.panini.ashtadhyayi

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.ashtadhyayi.adhyaya2.pada1.AvyayamVibhaktiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.CaturthiTadarthartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.DvitiyaShritatitaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.PancamiBhayenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.SankhyapurvoDviguhSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.SaptamiSaundaihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.TrtiyaTatkrtharthenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.VisesanamVisesyenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiSutra
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Unit tests for individual Samāsa Sūtras using [SamasaRuleContext].
 */
class SamasaDerivationTest {

    @Test
    fun `test AvyayamVibhaktiSutra matches Avyaya pada and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(AvyayamVibhaktiSutra.matches(context))
        val result = AvyayamVibhaktiSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("उपकृष्ण", result.compoundStem)
    }

    @Test
    fun `test DvitiyaShritatitaSutra matches DVITIYA vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("कृष्ण", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(DvitiyaShritatitaSutra.matches(context))
        val result = DvitiyaShritatitaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("कृष्णश्रित", result.compoundStem)
    }

    @Test
    fun `test PancamiBhayenaSutra matches PANCHAMI vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("चोर", Vibhakti.PANCHAMI),
                SamasaPada("भय", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(PancamiBhayenaSutra.matches(context))
        val result = PancamiBhayenaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("चोरभय", result.compoundStem)
    }

    @Test
    fun `test TrtiyaTatkrtharthenaSutra matches TRTIYA vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("शङ्कुल", Vibhakti.TRTIYA),
                SamasaPada("खण्ड", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(TrtiyaTatkrtharthenaSutra.matches(context))
        val result = TrtiyaTatkrtharthenaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("शङ्कुलखण्ड", result.compoundStem)
    }

    @Test
    fun `test ShashthiSutra matches SASTHI vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(ShashthiSutra.matches(context))
        val result = ShashthiSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("राजपुरुष", result.compoundStem)
    }

    @Test
    fun `test CartheDvandvahSutra matches two padas and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(CartheDvandvahSutra.matches(context))
        val result = CartheDvandvahSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("रामकृष्ण", result.compoundStem)
    }

    @Test
    fun `test AnekamAnyapadartheSutra matches PRATHAMA padas and forms Bahuvrihi`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(AnekamAnyapadartheSutra.matches(context))
        val result = AnekamAnyapadartheSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("पीतअम्बर", result.compoundStem)
    }

    @Test
    fun `test CaturthiTadarthartheSutra matches CHATURTHI vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("यूप", Vibhakti.CHATURTHI),
                SamasaPada("दारु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(CaturthiTadarthartheSutra.matches(context))
        val result = CaturthiTadarthartheSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("यूपदारु", result.compoundStem)
    }

    @Test
    fun `test SaptamiSaundaihSutra matches SAPTAMI vibhakti and forms compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("अक्ष", Vibhakti.SAPTAMI),
                SamasaPada("शौण्ड", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(SaptamiSaundaihSutra.matches(context))
        val result = SaptamiSaundaihSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("अक्षशौण्ड", result.compoundStem)
    }

    @Test
    fun `test VisesanamVisesyenaSutra matches two padas and forms Karmadharaya compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("नील", Vibhakti.PRATHAMA),
                SamasaPada("उत्पल", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(VisesanamVisesyenaSutra.matches(context))
        val result = VisesanamVisesyenaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("नीलउत्पल", result.compoundStem)
    }

    @Test
    fun `test SankhyapurvoDviguhSutra matches Sankhya purvapada and forms Dvigu compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.SANKHYA)),
                SamasaPada("भुवन", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(SankhyapurvoDviguhSutra.matches(context))
        val result = SankhyapurvoDviguhSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("त्रिभुवन", result.compoundStem)
    }

    @Test
    fun `test AvyayamVibhaktiSutra does not match non-avyaya pada`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA), // no AVYAYA samjna
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        // must not match — first pada has no AVYAYA/UPASARGA samjna
        assertTrue(!AvyayamVibhaktiSutra.matches(context))
    }
}
