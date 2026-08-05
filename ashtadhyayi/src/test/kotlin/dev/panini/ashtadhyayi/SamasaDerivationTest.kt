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
import dev.panini.ashtadhyayi.adhyaya2.pada1.MayuravyamsakadayascaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.UpamananiSamanyavacanaihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.UpamitamVyaghradibhihSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.VisesanamVisesyenaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.NanjSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.UpapadamAtingSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.AlukUttarapadeSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.AtmanascaPuraneSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.PutreNyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.TatpuruseKrtiBahulamSutra
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
    fun `test SamkhyayaAsannaduradhikaSutra forms approximate Bahuvrihi compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA),
                SamasaPada("पञ्चाशत्", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.SANKHYA)),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(dev.panini.ashtadhyayi.adhyaya2.pada2.SamkhyayaAsannaduradhikaSutra.matches(context))
        val result = dev.panini.ashtadhyayi.adhyaya2.pada2.SamkhyayaAsannaduradhikaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("उपपञ्चाशत्", result.compoundStem)
    }

    @Test
    fun `test KumarahShramanadibhihSutra forms Tatpurusa compound with Shramanadi members`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("कुमार", Vibhakti.PRATHAMA),
                SamasaPada("श्रमणा", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(dev.panini.ashtadhyayi.adhyaya2.pada1.KumarahShramanadibhihSutra.matches(context))
        val result = dev.panini.ashtadhyayi.adhyaya2.pada1.KumarahShramanadibhihSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("कुमारश्रमणा", result.compoundStem)
    }

    @Test
    fun `test PatresamitadiSutra forms Tatpurusa compound with Patresamitadi members`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("कूप", Vibhakti.PRATHAMA),
                SamasaPada("मण्डूक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(dev.panini.ashtadhyayi.adhyaya2.pada1.PatresamitadiSutra.matches(context))
        val result = dev.panini.ashtadhyayi.adhyaya2.pada1.PatresamitadiSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("कूपमण्डूक", result.compoundStem)
    }

    @Test
    fun `test NanjSutra matches Nañ purvapada and applies nalopa and nut`() {
        val halContext = SamasaRuleContext(
            padas = listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ब्राह्मण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.NAN_TATPURUSA,
        )
        assertTrue(NanjSutra.matches(halContext))
        val halResult = NanjSutra.apply(halContext) as SamasaRuleResult.Formed
        assertEquals("अब्राह्मण", halResult.compoundStem)

        val acContext = SamasaRuleContext(
            padas = listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("अश्व", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.NAN_TATPURUSA,
        )
        assertTrue(NanjSutra.matches(acContext))
        val acResult = NanjSutra.apply(acContext) as SamasaRuleResult.Formed
        assertEquals("अनश्व", acResult.compoundStem)
    }

    @Test
    fun `test UpamananiSamanyavacanaihSutra matches and forms Upamana compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("घन", Vibhakti.PRATHAMA),
                SamasaPada("श्याम", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(UpamananiSamanyavacanaihSutra.matches(context))
        val result = UpamananiSamanyavacanaihSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("घनश्याम", result.compoundStem)
    }

    @Test
    fun `test UpamitamVyaghradibhihSutra matches and forms Upamita compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
                SamasaPada("व्याघ्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(UpamitamVyaghradibhihSutra.matches(context))
        val result = UpamitamVyaghradibhihSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("पुरुषव्याघ्र", result.compoundStem)
    }

    @Test
    fun `test UpapadamAtingSutra matches and forms Upapada compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("कुम्भ", Vibhakti.DVITIYA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.UPAPADA_TATPURUSA,
        )
        assertTrue(UpapadamAtingSutra.matches(context))
        val result = UpapadamAtingSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("कुम्भकार", result.compoundStem)
    }

    @Test
    fun `test TatpuruseKrtiBahulamSutra matches and forms Saptami Aluk compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("युधि", Vibhakti.SAPTAMI),
                SamasaPada("स्थिर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(TatpuruseKrtiBahulamSutra.matches(context))
        val result = TatpuruseKrtiBahulamSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("युधिष्ठिर", result.compoundStem)
    }

    @Test
    fun `test MayuravyamsakadayascaSutra matches and forms Mayuravyamsakadi compound`() {
        val context = SamasaRuleContext(
            padas = listOf(
                SamasaPada("मयूर", Vibhakti.PRATHAMA),
                SamasaPada("व्यंसक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.MAYURAVYAMSAKADI,
        )
        assertTrue(MayuravyamsakadayascaSutra.matches(context))
        val result = MayuravyamsakadayascaSutra.apply(context) as SamasaRuleResult.Formed
        assertEquals("मयूरव्यंसक", result.compoundStem)
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
