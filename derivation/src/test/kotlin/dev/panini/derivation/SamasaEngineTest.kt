package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaEngineTest {
    private val engine = SamasaEngine()

    @Test
    fun `test Avyayibhava compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("उपकृष्णम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.1.6" })
    }

    @Test
    fun `test Shashthi Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राज", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("राजपुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.2.8" })
    }

    @Test
    fun `test Dvitiya Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कृष्ण", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("कृष्णश्रितः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.24" })
    }

    @Test
    fun `test Bahuvrihi compound derivation with Sandhi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("पीताम्बरः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `test Dvandva compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("रामलक्ष्मणौ", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.29" || it.sutra == "2.2.34" })
    }

    @Test
    fun `test Caturthi Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("यूप", Vibhakti.CHATURTHI),
                SamasaPada("दारु", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("यूपदारुः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.36" })
    }

    @Test
    fun `test Saptami Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अक्ष", Vibhakti.SAPTAMI),
                SamasaPada("शौण्ड", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("अक्षशौण्डः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.40" })
    }

    @Test
    fun `test Karmadharaya compound derivation with Sandhi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("नील", Vibhakti.PRATHAMA),
                SamasaPada("उत्पल", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("नीलोत्पलः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.57" })
    }

    @Test
    fun `test Dvigu compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.SANKHYA)),
                SamasaPada("भुवन", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        val actual = result.final.terms.last().surface
        val apps = result.applications.joinToString("; ") { "${it.sutra}: ${it.explanation}" }
        assertEquals("त्रिभुवनम्", actual, "Failed! Actual: '$actual', Applications: $apps")
        assertTrue(result.applications.any { it.sutra == "2.1.52" })
    }

    @Test
    fun `test Nanj Tatpurusha compound derivation for consonant-initial stem`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("ब्राह्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अब्राह्मणः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test Nanj Tatpurusha compound derivation for vowel-initial stem`() {
        val result = engine.derive(
            listOf(
                SamasaPada("न", Vibhakti.PRATHAMA),
                SamasaPada("अश्व", Vibhakti.PRATHAMA),
            ),
            SamasaType.NAN_TATPURUSA,
        )
        assertEquals("अनश्वः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.6" })
    }

    @Test
    fun `test Upamana Karmadharaya compound derivation (2 1 55)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("घन", Vibhakti.PRATHAMA),
                SamasaPada("श्याम", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("घनश्यामः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.55" })
    }

    @Test
    fun `test Upamita Karmadharaya compound derivation (2 1 56)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
                SamasaPada("व्याघ्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("पुरुषव्याघ्रः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.56" })
    }

    @Test
    fun `test Upapada Tatpurusha compound derivation (2 2 19)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कुम्भ", Vibhakti.DVITIYA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("कुम्भकारः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test Upapada Tatpurusha compound derivation for Samaga (2 2 19)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("साम", Vibhakti.DVITIYA),
                SamasaPada("ग", Vibhakti.PRATHAMA),
            ),
            SamasaType.UPAPADA_TATPURUSA,
        )
        assertEquals("सामगः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.19" })
    }

    @Test
    fun `test Aluk Tatpurusha compound derivation for Yudhishthira (6 3 14)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("युधि", Vibhakti.SAPTAMI),
                SamasaPada("स्थिर", Vibhakti.PRATHAMA),
            ),
            SamasaType.ALUK_TATPURUSA,
        )
        assertEquals("युधिष्ठिरः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "6.3.14" })
    }

    @Test
    fun `test Aluk Tatpurusha compound derivation for Atmanepadam (6 3 21)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("आत्मने", Vibhakti.CHATURTHI),
                SamasaPada("पद", Vibhakti.PRATHAMA),
            ),
            SamasaType.ALUK_TATPURUSA,
        )
        assertEquals("आत्मनेपदम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "6.3.21" })
    }

    @Test
    fun `test Mayuravyamsakadi compound derivation (2 1 72)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मयूर", Vibhakti.PRATHAMA),
                SamasaPada("व्यंसका", Vibhakti.PRATHAMA),
            ),
            SamasaType.MAYURAVYAMSAKADI,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.72" })
    }

    @Test
    fun `test Bahuvrihi Saha to Sa substitution (2 2 28, 6 3 82)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सह", Vibhakti.TRTIYA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("सपुत्रः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "6.3.82" || it.sutra == "2.2.28" })
    }

    @Test
    fun `test Bahuvrihi Urahprabhrti Kap pratyaya (5 4 151)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("व्यूढ", Vibhakti.PRATHAMA),
                SamasaPada("उरस्", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("व्यूढोरस्कः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "5.4.151" })
    }

    @Test
    fun `test Bahuvrihi Nadyrta Kap pratyaya (5 4 153)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("बहु", Vibhakti.PRATHAMA),
                SamasaPada("कुमारी", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("बहुकुमारीकः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "5.4.153" })
    }

    @Test
    fun `test Bahuvrihi NanoAstyarthanam Kap pratyaya (5 4 154)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अ", Vibhakti.PRATHAMA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("अपुत्रकः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "5.4.154" })
    }

    @Test
    fun `test Dvandva Abhyarhitam order (2 2 32)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AbhyarhitamChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
                SamasaPada("माता", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvandva Ajadyadantam order (2 2 33)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
                SamasaPada("ईश", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.33" })
    }

    @Test
    fun `test Dvandva Alpactaram order (2 2 34)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("केशव", Vibhakti.PRATHAMA),
                SamasaPada("शिव", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.34" })
    }

    @Test
    fun `test Dvandva Praniturya Samahara (2 4 2)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पाणि", Vibhakti.PRATHAMA),
                SamasaPada("पाद", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("पाणिपादम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.4.2" })
    }

    @Test
    fun `test Dvandva JatirApraninam Samahara (2 4 6)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("धाना", Vibhakti.PRATHAMA),
                SamasaPada("शष्कुलि", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.4.6" })
    }

    @Test
    fun `test Avyayibhava ApapariBahirAncavah (2 1 12)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("बहिर्", Vibhakti.PANCHAMI),
                SamasaPada("ग्राम", Vibhakti.PANCHAMI),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.12" })
    }

    @Test
    fun `test Avyayibhava AngMaryadabhividhyoh (2 1 13)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("आ", Vibhakti.PANCHAMI),
                SamasaPada("मुक्ति", Vibhakti.PANCHAMI),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.13" })
    }

    @Test
    fun `test Avyayibhava LaksanenAbhiprati (2 1 14)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अग्नि", Vibhakti.PRATHAMA),
                SamasaPada("अभि", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.14" })
    }

    @Test
    fun `test Avyayibhava PareMadhye (2 1 18)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पारे", Vibhakti.SASTHI),
                SamasaPada("गङ्गा", Vibhakti.SASTHI),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.18" })
    }

    @Test
    fun `test Avyayibhava SankhyaVamsyena (2 1 19)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("मुनि", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.19" })
    }

    @Test
    fun `test Karmadharaya Purvakaladi (2 1 58)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("एक", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("एकपुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.58" })
    }

    @Test
    fun `test Karmadharaya KtenaNanjVisistena (2 1 60)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कृत", Vibhakti.PRATHAMA),
                SamasaPada("अकृत", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("कृताकृतम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.60" })
    }

    @Test
    fun `test Karmadharaya SanMahatParamottama (2 1 61)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महापुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.61" })
    }

    @Test
    fun `test Karmadharaya PapakeKutsitaih (2 1 68)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पाप", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("पापपुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.68" })
    }

    @Test
    fun `test Tatpurusha Yajakadibhishcha (2 2 9)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("ब्राह्मण", Vibhakti.SASTHI),
                SamasaPada("याजक", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("ब्राह्मणयाजकः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.9" })
    }

    @Test
    fun `test Dvitiya Tatpurusha Sritatita (2 1 24)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कष्ट", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("कष्टश्रितः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.24" })
    }

    @Test
    fun `test Trtiya Tatpurusha Tatkrtarthena (2 1 30)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("शङ्कुला", Vibhakti.TRTIYA),
                SamasaPada("खण्ड", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("शङ्कुलाखण्डः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.30" })
    }

    @Test
    fun `test Trtiya Tatpurusha KartrkaraneKrta (2 1 32)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("हरि", Vibhakti.TRTIYA),
                SamasaPada("त्रात", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("हरित्रातः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.32" })
    }

    @Test
    fun `test Saptami Tatpurusha Siddhasuska (2 1 41)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("आतप", Vibhakti.SAPTAMI),
                SamasaPada("शुष्क", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("आतपशुष्कः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.41" })
    }

    @Test
    fun `test Karmadharaya VisesanamVisesyena (2 1 57)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
                SamasaPada("सर्प", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("कृष्णसर्पः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.57" })
    }

    @Test
    fun `test Dvitiya Tatpurusha KalaAtyantasamyoge (2 1 28)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मास", Vibhakti.DVITIYA),
                SamasaPada("कल्याणी", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("मासकल्याणी", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.28" })
    }

    @Test
    fun `test Trtiya Tatpurusha AnnasenaVyanjanam (2 1 34)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("दधि", Vibhakti.TRTIYA),
                SamasaPada("ओदन", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("दध्योदनः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.34" })
    }

    @Test
    fun `test Pancami Tatpurusha ApetaApodha (2 1 38)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सुख", Vibhakti.PANCHAMI),
                SamasaPada("अपेत", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("सुखापेतः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.38" })
    }

    @Test
    fun `test Pancami Tatpurusha Stokantika (2 1 39)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("स्तोक", Vibhakti.PANCHAMI),
                SamasaPada("मुक्त", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("स्तोकमुक्तः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.39" })
    }

    @Test
    fun `test Ekadesin Tatpurusha Purvaparakadharottaram (2 2 1)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पूर्व", Vibhakti.PRATHAMA),
                SamasaPada("काय", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("पूर्वकायः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.1" })
    }

    @Test
    fun `test Svayam Tatpurusha (2 1 27)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("स्वयम्", Vibhakti.PRATHAMA),
                SamasaPada("कृत", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.27" })
    }

    @Test
    fun `test Bhaksyena Mishrikaranam Tatpurusha (2 1 35)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("गुड", Vibhakti.TRTIYA),
                SamasaPada("मिश्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.35" })
    }

    @Test
    fun `test Yuva Khalati Karmadharaya (2 1 67)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("युवा", Vibhakti.PRATHAMA),
                SamasaPada("खलति", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.67" })
    }

    @Test
    fun `test Nitya Kridajivikayoh Tatpurusha (2 2 17)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("दन्त", Vibhakti.SASTHI),
                SamasaPada("लेखक", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.17" })
    }

    @Test
    fun `test KuGatiPradayah Tatpurusha (2 2 18)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कु", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.18" })
    }

    @Test
    fun `test PraptapannasCha Dvitiyaya Tatpurusha (2 1 26)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("प्राप्त", Vibhakti.PRATHAMA),
                SamasaPada("जीविका", Vibhakti.DVITIYA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.26" })
    }

    @Test
    fun `test KrtyairRne Tatpurusha (2 1 33)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कुशाग्र", Vibhakti.TRTIYA),
                SamasaPada("छेद्य", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.33" })
    }

    @Test
    fun `test KataraKatambhau Karmadharaya (2 1 65)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कतर", Vibhakti.PRATHAMA),
                SamasaPada("कठ", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.65" })
    }

    @Test
    fun `test TrtiyaprabhrtinyAnyatarasyam Ekadesin (2 2 3)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पूर्व", Vibhakti.TRTIYA),
                SamasaPada("अह्न", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.3" })
    }

    @Test
    fun `test TatraTenedamitiSarupe Bahuvrihi (2 2 23)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("केश", Vibhakti.PRATHAMA),
                SamasaPada("केश", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.23" })
    }

    @Test
    fun `test Atyadaya Krantyadyarthe Tatpurusha (2 1 25)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अति", Vibhakti.PRATHAMA),
                SamasaPada("कोकिल", Vibhakti.DVITIYA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.25" })
    }

    @Test
    fun `test Purva Sadrsha Trtiya Tatpurusha (2 1 31)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पितृ", Vibhakti.TRTIYA),
                SamasaPada("सदृश", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.31" })
    }

    @Test
    fun `test Taddhitartha Uttarapada Samahara Dvigu (2 1 51)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पञ्च", Vibhakti.PRATHAMA),
                SamasaPada("कपाल", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVIGU,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.51" || it.sutra == "2.1.52" })
    }

    @Test
    fun `test Prasamsavacanaisca Karmadharaya (2 1 66)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("गो", Vibhakti.PRATHAMA),
                SamasaPada("प्रकाण्ड", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.66" })
    }

    @Test
    fun `test Antaram Bahiryoge Tatpurusha (2 2 4)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("गृह", Vibhakti.PRATHAMA),
                SamasaPada("अन्तर", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.4" })
    }

    @Test
    fun `test Kalat Dvitiya Tatpurusha (2 1 29)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KalatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("मास", Vibhakti.DVITIYA),
                SamasaPada("कल्याणी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Kavacahara Trtiya Tatpurusha (2 1 48)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("वयस", Vibhakti.TRTIYA),
                SamasaPada("कवचहर", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.48" })
    }

    @Test
    fun `test Kutsitani Kutsitaih Karmadharaya (2 1 53)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("वैयाकरण", Vibhakti.PRATHAMA),
                SamasaPada("खसूचि", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.53" })
    }

    @Test
    fun `test Na Nirdhare Shashthi Prohibition (2 2 10)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.NaNirdhareSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                dev.panini.analysis.SamasaPada("नृ", dev.panini.core.Vibhakti.SASTHI),
                dev.panini.analysis.SamasaPada("द्विज", dev.panini.core.Vibhakti.PRATHAMA),
            ),
            samasaType = dev.panini.core.SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Upasarjanam Purvam (2 2 30)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.UpasarjanamPurvamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                dev.panini.analysis.SamasaPada("राज", dev.panini.core.Vibhakti.SASTHI),
                dev.panini.analysis.SamasaPada("पुरुष", dev.panini.core.Vibhakti.PRATHAMA),
            ),
            samasaType = dev.panini.core.SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Khatva Ksepe Dvitiya Tatpurusha (2 1 20)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("खट्वा", Vibhakti.DVITIYA),
                SamasaPada("आरूढ", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.20" })
    }

    @Test
    fun `test Sami Tatpurusha (2 1 22)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सामि", Vibhakti.PRATHAMA),
                SamasaPada("कृत", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.22" })
    }

    @Test
    fun `test Ksepe Prasamsayam Saptami Tatpurusha (2 1 47)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पात्र", Vibhakti.SAPTAMI),
                SamasaPada("सम्मित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.47" })
    }

    @Test
    fun `test Karmani Cha Prohibition (2 2 14)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KarmaniChaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गो", Vibhakti.SASTHI),
                SamasaPada("दोह", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Amaivavyayena Tatpurusha (2 2 20)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("स्वाहाकृतम्", Vibhakti.PRATHAMA),
                SamasaPada("कृ", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.20" })
    }

    @Test
    fun `test Yatha Sadrshye Avyayibhava (2 1 7)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("यथा", Vibhakti.PRATHAMA),
                SamasaPada("शक्ति", Vibhakti.DVITIYA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.7" })
    }

    @Test
    fun `test Anur Yat Samaya Avyayibhava (2 1 9)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA),
                SamasaPada("वन", Vibhakti.SASTHI),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.9" })
    }

    @Test
    fun `test Ksepena Saptami Tatpurusha (2 1 43)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KsepenaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गेहे", Vibhakti.SAPTAMI),
                SamasaPada("क्षेडी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ktena Cha Pujayam Prohibition (2 2 12)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KtenaChAPujayamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("पूजित", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Adhikaranavacinas Cha Prohibition (2 2 13)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AdhikaranavacinasChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.SASTHI),
                SamasaPada("मत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Yasya Chayamah Avyayibhava (2 1 10)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.YasyaChayamahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA),
                SamasaPada("गङ्गा", Vibhakti.SASTHI),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Dosa Cha Avyayibhava (2 1 15)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("दोषा", Vibhakti.PRATHAMA),
                SamasaPada("कृत", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.15" })
    }

    @Test
    fun `test Gater Anantaram Tatpurusha (2 1 45)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("प्र", Vibhakti.PRATHAMA),
                SamasaPada("अनन्तर", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.45" })
    }

    @Test
    fun `test Trjjakabhyam Kartari Prohibition (2 2 15)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.TrjjakabhyamKartariSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रिभुवन", Vibhakti.SASTHI),
                SamasaPada("स्रष्टृ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Kartari Cha Prohibition (2 2 16)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KartariChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("भवत्", Vibhakti.SASTHI),
                SamasaPada("शायिका", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Atyanta Samyoge Cha Tatpurusha (2 1 49)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मुहूर्त", Vibhakti.DVITIYA),
                SamasaPada("सुख", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.49" || it.sutra == "2.1.28" })
    }

    @Test
    fun `test Rasa Varjyam Trtiya Tatpurusha (2 1 44)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.RasaVarjyamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("धान्य", Vibhakti.TRTIYA),
                SamasaPada("अर्थ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Apeksitam Saptami Tatpurusha (2 1 46)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कार्य", Vibhakti.SAPTAMI),
                SamasaPada("अपेक्षित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.46" })
    }

    @Test
    fun `test Esad Aka Pratyaya Prohibition (2 2 7)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.EsadAkaPratyayaYukteSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अ", Vibhakti.PRATHAMA),
                SamasaPada("ईषत्कत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.NAN_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Purana Guna Suhita Prohibition (2 2 11)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.PuranaGunasuhitaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("काक", Vibhakti.SASTHI),
                SamasaPada("वार्ष्ण्य", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Gunavacanesu Chayayam Avyayibhava (2 1 5)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("इक्षु", Vibhakti.SASTHI),
                SamasaPada("छाया", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.5" })
    }

    @Test
    fun `test Yavad Avadharane Avyayibhava (2 1 99)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.YavadAvadharaneSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("यावत्", Vibhakti.PRATHAMA),
                SamasaPada("अमोक्ष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Dharksyat Saptami Tatpurusha (2 1 42)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("साङ्काश्य", Vibhakti.SAPTAMI),
                SamasaPada("साङ्काश्यक", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.42" })
    }

    @Test
    fun `test Praptapanne Cha Dvitiyaya Tatpurusha (2 2 5)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.PraptapanneChADvitiyayaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("प्राप्त", Vibhakti.PRATHAMA),
                SamasaPada("जीविका", Vibhakti.DVITIYA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Trtiyaprabhrtiny Anyatarasyam Ext Avyayibhava (2 2 21)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.TrtiyaprabhrtinyAnyatarasyamExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA),
                SamasaPada("कृष्णा", Vibhakti.TRTIYA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Ktena Trtiya Tatpurusha (2 1 86)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अहि", Vibhakti.TRTIYA),
                SamasaPada("हत", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.86" })
    }

    @Test
    fun `test Taddhitartha Uttarapada Dvigu (2 1 81)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.TaddhitarthaUttarapadaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पञ्च", Vibhakti.PRATHAMA),
                SamasaPada("पूली", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Rajadantadisu Tatpurusha (2 2 31)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.RajadantadisuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.PRATHAMA),
                SamasaPada("दन्त", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Saptami Visesane Bahuvrihi (2 2 35)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.SaptamiVisesaneBahuvrihauSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कण्ठे", Vibhakti.SAPTAMI),
                SamasaPada("काल", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Nistha Bahuvrihi (2 2 36)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.NisthaBahuvrihauSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कृत", Vibhakti.PRATHAMA),
                SamasaPada("कृत्य", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.Formed)
    }

    @Test
    fun `test Gahane Kathina Tatpurusha (2 1 91)`() {
        val result = engine.derive(
            listOf(
                SamasaPada("गहन", Vibhakti.DVITIYA),
                SamasaPada("कठिन", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertTrue(result.applications.any { it.sutra == "2.1.91" })
    }

    @Test
    fun `test Sankhyapurvo Dvigu Ext (2 1 92)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.SankhyapurvoDviguExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("भुवन", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Upamana Samanya Ext (2 1 93)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.UpamanaSamanyaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("घन", Vibhakti.PRATHAMA),
                SamasaPada("श्याम", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Visesana Visesya Ext (2 1 94)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.VisesanaVisesyaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("नील", Vibhakti.PRATHAMA),
                SamasaPada("उत्पल", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ktena Nanj Ext (2 1 95)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KtenaNanjExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कृत", Vibhakti.PRATHAMA),
                SamasaPada("अकृत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Upapadam Ating Ext (2 2 89)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.UpapadamAtingExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कुम्भ", Vibhakti.DVITIYA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.UPAPADA_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Anekam Anyapadarthe Ext (2 2 90)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Saha Supa General Samasa (2 2 91)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.SahaSupaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("भूत", Vibhakti.PRATHAMA),
                SamasaPada("पूर्व", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Carthe Dvandva Ext (2 2 92)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Vahitagnyadisu Bahuvrihi (2 2 37)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.VahitagnyadisuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("आहिताग्नि", Vibhakti.PRATHAMA),
                SamasaPada("अग्निहित", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Vibhasa Samasa (2 1 96)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.VibhasaSamasaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Apapari Bahir Ext (2 1 97)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.ApapariBahirExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अप", Vibhakti.PRATHAMA),
                SamasaPada("त्रिगर्त", Vibhakti.PANCHAMI),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tisthadguprabhrtini (2 1 98)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.TisthadguprabhrtiniSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("तिष्ठद्गु", Vibhakti.PRATHAMA),
                SamasaPada("", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Trtiya Tatkrtarthena Ext (2 1 100)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.TrtiyaTatkrtarthenaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शङ्कुला", Vibhakti.TRTIYA),
                SamasaPada("खण्ड", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kutsitani Kutsitaih Ext (2 1 101)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KutsitaniKutsitaihExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("वैयाकरण", Vibhakti.PRATHAMA),
                SamasaPada("खसूचि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Shashthi Ext (2 2 93)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.ShashthiExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Seso Bahuvrihi (2 2 94)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.SesoBahuvrihiSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Anekam Anyapadarthe Header (2 2 95)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheHeaderSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Alpactaram Ext (2 2 96)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AlpactaramExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शिव", Vibhakti.PRATHAMA),
                SamasaPada("केशव", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kadaradah Karmadharaye (2 2 38)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KadaradahKarmadharayeSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कडार", Vibhakti.PRATHAMA),
                SamasaPada("हाटक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Maryadabhividhyoh Ext (2 1 102)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.MaryadabhividhyohExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("आ", Vibhakti.PRATHAMA),
                SamasaPada("कुमार", Vibhakti.PANCHAMI),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Pare Madhye Shashthya Va Ext (2 1 103)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.PareMadhyeShashthyaVaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पारे", Vibhakti.SAPTAMI),
                SamasaPada("गङ्गा", Vibhakti.SASTHI),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Sankhya Vamsyena Ext (2 1 104)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.SankhyaVamsyenaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("मुनि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Trtiya Tatkrtarthena General (2 1 105)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.TrtiyaTatkrtarthenaGeneralSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शङ्कुला", Vibhakti.TRTIYA),
                SamasaPada("खण्ड", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Mayuravyamsakadayasca Ext (2 1 106)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.MayuravyamsakadayascaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("मयूर", Vibhakti.PRATHAMA),
                SamasaPada("व्यंसक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.MAYURAVYAMSAKADI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Nityam Kridajivikayoh Ext (2 2 97)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.NityamKridajivikayohExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उद्दालकपुष्प", Vibhakti.PRATHAMA),
                SamasaPada("भञ्जिका", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.UPAPADA_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ku Gati Pradayah Ext (2 2 98)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KuGatiPradayahExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कु", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tena Saheti Tulyayoge Ext (2 2 99)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.TenaSahetiTulyayogeExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सह", Vibhakti.PRATHAMA),
                SamasaPada("पत्नी", Vibhakti.TRTIYA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tatra Tenedamiti Sarupe Ext (2 2 100)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.TatraTenedamitiSarupeExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("केश", Vibhakti.SAPTAMI),
                SamasaPada("केश", Vibhakti.SAPTAMI),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ajadyadantam Ext (2 2 101)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AjadyadantamExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("ईश", Vibhakti.PRATHAMA),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test LaksanenAbhiprati Ext (2 1 107)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.LaksanenAbhipratiExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अभि", Vibhakti.PRATHAMA),
                SamasaPada("अग्नि", Vibhakti.DVITIYA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kalah General (2 1 108)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KalahGeneralSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("मास", Vibhakti.PRATHAMA),
                SamasaPada("प्रमित", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ksepe General (2 1 109)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.KsepeGeneralSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("तीर्थ", Vibhakti.SAPTAMI),
                SamasaPada("काक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Upamanani Samanya General (2 1 110)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.UpamananiSamanyaGeneralSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
                SamasaPada("व्याघ्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Purvakaladi Ext (2 1 111)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada1.PurvakaladiExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पूर्व", Vibhakti.PRATHAMA),
                SamasaPada("जात", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Karmani Cha Nishedha (2 2 102)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KarmaniChaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गो", Vibhakti.SASTHI),
                SamasaPada("दोह", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        val res = sutra.apply(context)
        assertTrue(res is dev.panini.analysis.SamasaRuleResult.NotApplicable)
    }

    @Test
    fun `test Amaivavyayena Ext (2 2 103)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AmaivavyayenaExt2Sutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("स्वाद्", Vibhakti.PRATHAMA),
                SamasaPada("कारम्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.UPAPADA_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tena Saheti Header (2 2 104)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.TenaSahetiHeaderSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सह", Vibhakti.PRATHAMA),
                SamasaPada("पत्नी", Vibhakti.TRTIYA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Abhyarhitam Cha (2 2 105)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.AbhyarhitamChaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("माता", Vibhakti.PRATHAMA),
                SamasaPada("पिता", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Caturthi Tadartharthe Ext (2 2 106)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.CaturthiTadarthartheExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("यूप", Vibhakti.CHATURTHI),
                SamasaPada("दारु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test RkPurAbDhurPatham (5 4 68)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RkPurAbDhurPathamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("विष्णु", Vibhakti.PRATHAMA),
                SamasaPada("पुर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AksnoAdarsanat (5 4 69)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AksnoAdarsanatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पर", Vibhakti.PRATHAMA),
                SamasaPada("अक्षि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AncerUpasargasya (5 4 70)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AncerUpasargasyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("प्र", Vibhakti.PRATHAMA),
                SamasaPada("अञ्च्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test TypUpasargasya (5 4 71)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.TypUpasargasyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA),
                SamasaPada("गम्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test APacadibhyah (5 4 73)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.APacadibhyahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("देव", Vibhakti.PRATHAMA),
                SamasaPada("कृ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test RksamabhyamThac (5 4 74)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RksamabhyamThacSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("ऋच्", Vibhakti.PRATHAMA),
                SamasaPada("सामन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AchPratyagatamah (5 4 77)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AchPratyagatamahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.PRATHAMA),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AntaramAparavyoktamAnatmani (6 3 4)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.AntaramAparavyoktamAnatmaniSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अन्तर", Vibhakti.PRATHAMA),
                SamasaPada("गृह", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test PancamyahStokadibhyah (6 3 2)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.PancamyahStokadibhyahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("स्तोक", Vibhakti.PANCHAMI),
                SamasaPada("मुक्त", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test BhandeBandheCha (6 3 3)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.BhandeBandheChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("भण्ड", Vibhakti.PRATHAMA),
                SamasaPada("बन्ध", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AnupasargadAdhvanah (5 4 78)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AnupasargadAdhvanahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("रम्य", Vibhakti.PRATHAMA),
                SamasaPada("अध्वन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test NaPujanat (5 4 79)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.NaPujanatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test KimanhKsepe (5 4 80)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.KimanhKsepeSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("किम्", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test NityamAsimase (5 4 81)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.NityamAsimaseSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("समीप", Vibhakti.PRATHAMA),
                SamasaPada("असीमास", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Alpakhyayam (5 4 82)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AlpakhyayamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अल्प", Vibhakti.PRATHAMA),
                SamasaPada("जल", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AmurdhamastakatSvangadAkame (6 3 5)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.AmurdhamastakatSvangadAkameSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कण्ठ", Vibhakti.PRATHAMA),
                SamasaPada("काल", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test AtmanascaPurane Ext (6 3 106)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.AtmanascaPuraneExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("आत्मना", Vibhakti.TRTIYA),
                SamasaPada("पञ्चम", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Vayasi Cha (6 3 7)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.VayasiChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("वृद्ध", Vibhakti.PRATHAMA),
                SamasaPada("वयस्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Manasah Samjnayam (6 3 8)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.ManasahSamjnayamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("मनसि", Vibhakti.SAPTAMI),
                SamasaPada("ज", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Saras Ca Gihpati (6 3 9)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.SarasCaGihpatiPrabhrtisuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सरसि", Vibhakti.SAPTAMI),
                SamasaPada("ज", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dhanusas Cha (5 4 83)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.DhanusasChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शार्ङ्ग", Vibhakti.PRATHAMA),
                SamasaPada("धनुष्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Jayaya Ning (5 4 84)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.JayayaNingSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सीता", Vibhakti.PRATHAMA),
                SamasaPada("जाया", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Gandhasyet Idutpurvat (5 4 85)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.GandhasyetIdutpurvatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("गन्ध", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Upamanac Cha (5 4 86)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.UpamanacChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पद्म", Vibhakti.PRATHAMA),
                SamasaPada("गन्ध", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Putisurabhi Mukhya (5 4 87)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.PutisurabhiMukhyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पूति", Vibhakti.PRATHAMA),
                SamasaPada("गन्ध", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Gospada Gostha (6 3 10)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.GospadaGosthaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गोष्पद", Vibhakti.PRATHAMA),
                SamasaPada("खम्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ksetre Pasusamase (6 3 11)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.KsetrePasusamaseSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("क्षेत्र", Vibhakti.PRATHAMA),
                SamasaPada("पशु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Khyatyai Naksatre (6 3 12)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.KhyatyaiNaksatreSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("रोहिणी", Vibhakti.PRATHAMA),
                SamasaPada("तारक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Vahah Karanat (6 3 13)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.VahahKaranatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("जल", Vibhakti.PRATHAMA),
                SamasaPada("वाह्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Karo NamnyAmatnge (6 3 15)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.KaroNamnyAmatngeSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दिन", Vibhakti.PRATHAMA),
                SamasaPada("कार", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ahno Hna Etebhyah (5 4 88)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AhnoHnaEtebhyahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सर्व", Vibhakti.PRATHAMA),
                SamasaPada("अहन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Na Samkhyader Ahnah (5 4 89)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.NaSamkhyaderAhnahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("अहन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Uttama Ekabhyam (5 4 90)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.UttamaEkabhyamChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उत्तम", Vibhakti.PRATHAMA),
                SamasaPada("अहन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Rajahah Sakhibhyas Tac (5 4 91)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RajahahSakhibhyasTacSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("महा", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Aksnoh Tac (5 4 92)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AksnohTacChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गवा", Vibhakti.PRATHAMA),
                SamasaPada("अक्षि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvigu Praptapanna (6 3 16)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DviguPraptapannaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("गु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Parimanakhyayam Sarvasya (6 3 17)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.ParimanakhyayamSarvasyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सर्व", Vibhakti.PRATHAMA),
                SamasaPada("प्रमाण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Parimane Pratyayasthasya (6 3 18)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.ParimanePratyayasthasyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अल्प", Vibhakti.PRATHAMA),
                SamasaPada("मात्रा", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Anugavyam (6 3 20)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.AnugavyamChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA),
                SamasaPada("गो", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ad Upadesesutra (6 3 22)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.AdUpadesesutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("विश्वा", Vibhakti.PRATHAMA),
                SamasaPada("मित्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Gramakataksabhyam Tac (5 4 93)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.GramakataksabhyamTacSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.PRATHAMA),
                SamasaPada("ग्रामक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Anor Avatyat (5 4 94)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AnorAvatyatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("परम", Vibhakti.PRATHAMA),
                SamasaPada("अणु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Giriscer Sena (5 4 95)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.GiriscerSenaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("छाया", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ratrer Ahnaha (5 4 96)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RatrerAhnahaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सर्व", Vibhakti.PRATHAMA),
                SamasaPada("रात्रि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Samkhyapurvam Ratram Klibam (5 4 97)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.SamkhyapurvamRatramKlibamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("रात्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvigu Praptapanna Ext (6 3 23)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DviguPraptapannaExtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("गु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Trer Ut (6 3 24)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.TrerUtSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("दश", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Treh Striyam (6 3 25)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.TrehStriyamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("रात्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvistrayor Indre (6 3 26)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DvistrayorIndreSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("इन्द्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Somas Tug Dhanyasu (6 3 27)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.SomasTugDhanyasuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("धान्य", Vibhakti.PRATHAMA),
                SamasaPada("सोम", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Uttarapadalopi Dvigu (5 4 98)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.UttarapadalopiDviguSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("गु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Navyas Cha (5 4 99)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.NavyasChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अर्ध", Vibhakti.PRATHAMA),
                SamasaPada("नौ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ardhac Cha (5 4 100)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.ArdhacChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अर्ध", Vibhakti.PRATHAMA),
                SamasaPada("नौ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kharyah Pracam (5 4 101)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.KharyahPracamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अर्ध", Vibhakti.PRATHAMA),
                SamasaPada("खारी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvitribhyam Anjaleh (5 4 102)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.DvitribhyamAnjalehSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("अञ्जलि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Id Agnes Chardisi (6 3 28)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.IdAgnesChardisiSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अग्नि", Vibhakti.PRATHAMA),
                SamasaPada("सोम", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Diva Vijaye (6 3 29)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DivaVijayeSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दिव्", Vibhakti.PRATHAMA),
                SamasaPada("विजय", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.ALUK_TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Divas Cha (6 3 30)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DivasChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दिव्", Vibhakti.PRATHAMA),
                SamasaPada("पृथ्वी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Usasa Usasah (6 3 31)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.UsasaUsasahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उषस्", Vibhakti.PRATHAMA),
                SamasaPada("नक्ता", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Matari Pitari Cha (6 3 32)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.MatariPitariChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Manasa Alikhye (5 4 103)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.ManasaAlikhyeSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("मनस्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Brahmastayoh Samjnayam (5 4 104)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.BrahmastayohSamjnayamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("महा", Vibhakti.PRATHAMA),
                SamasaPada("ब्रह्मन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kuhu Kamsayor Avispastayoh (5 4 105)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.KuhuKamsayorAvispastayohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अपर", Vibhakti.PRATHAMA),
                SamasaPada("कुहू", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tricaturbhyam Usnoh (5 4 106)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.TricaturbhyamUsnohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("उष्ण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Purah Prasadat (5 4 107)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.PurahPrasadatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पुरस्", Vibhakti.PRATHAMA),
                SamasaPada("प्रसाद", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Pitra Mata (6 3 33)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.PitramataSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Rto Vidya Yoni (6 3 34)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.RtoVidyaYoniSambandhebhyahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("होतृ", Vibhakti.PRATHAMA),
                SamasaPada("पोतृ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVANDVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tacchilye Vah (6 3 35)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.TacchilyeVahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शील", Vibhakti.PRATHAMA),
                SamasaPada("वह", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kvano Dic Cha (6 3 36)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.KvanoDicChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("वीणा", Vibhakti.PRATHAMA),
                SamasaPada("क्वण", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Na Samase (6 3 37)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.NaSamaseSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("परम", Vibhakti.PRATHAMA),
                SamasaPada("राज", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Angulam Kham (5 4 108)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AngulamKhamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("अङ्गुलि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.DVIGU,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tatsamaksat (5 4 109)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.TatsamaksatSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("तत्", Vibhakti.PRATHAMA),
                SamasaPada("समक्ष", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ahno Vibhasa (5 4 110)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AhnoVibhasaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("परम", Vibhakti.PRATHAMA),
                SamasaPada("अहन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Ratrer Antas (5 4 111)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RatrerAntasSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दीर्घ", Vibhakti.PRATHAMA),
                SamasaPada("रात्रि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Rajnah Kharyam (5 4 112)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.RajnahKharyamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
                SamasaPada("खारी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Kyun Nadyor Maninyam (6 3 38)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.KyunNadyorManinyamSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दर्शनीय", Vibhakti.PRATHAMA),
                SamasaPada("मानिनी", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Gopa Striyam (6 3 39)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.GopaStriyamSirVighateSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गोप", Vibhakti.PRATHAMA),
                SamasaPada("स्त्री", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Bhaktya An (6 3 40)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.BhaktyaAnSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("हरि", Vibhakti.PRATHAMA),
                SamasaPada("भक्ति", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Striyah Pumvatbhavas (6 3 41)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.StriyahPumvatbhavasSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दर्शनीया", Vibhakti.PRATHAMA),
                SamasaPada("भार्या", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Tasimat Svarnthesv Abhavah (6 3 42)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.TasimatSvarnthesvAbhavahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("स्त्री", Vibhakti.PRATHAMA),
                SamasaPada("तस्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Bahuvrihau Sakthyaksnoh (5 4 113)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.BahuvrihauSakthyaksnohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कमल", Vibhakti.PRATHAMA),
                SamasaPada("अक्षि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Isaddhussusu (5 4 114)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.IsaddhussusuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("राजा", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Prasamadibhyah (5 4 115)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.PrasamadibhyahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("प्रासमा", Vibhakti.PRATHAMA),
                SamasaPada("कृत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Apac Chakilase (5 4 116)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.ApacChakilaseSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अप", Vibhakti.PRATHAMA),
                SamasaPada("किलास", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Pramane DvayasajDaghnan (5 4 117)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.PramaneDvayasajDaghnanSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उरु", Vibhakti.PRATHAMA),
                SamasaPada("द्वयस", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Garudhyai Chatmanah (6 3 43)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.GarudhyaiChatmanahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("आत्मन्", Vibhakti.PRATHAMA),
                SamasaPada("गरुधि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Apsarasah (6 3 44)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.ApsarasahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अप्सरस्", Vibhakti.PRATHAMA),
                SamasaPada("पति", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Na Ghadharoh (6 3 45)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.NaGhadharohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("दर्शनीया", Vibhakti.PRATHAMA),
                SamasaPada("तरा", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Mahatah Samanadhikarana (6 3 46)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.MahatahSamanadhikaranaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.KARMADHARAYA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvigu Dhanurdanta (6 3 47)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.DviguDhanurdantaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("शार्ङ्ग", Vibhakti.PRATHAMA),
                SamasaPada("धनुष्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Dvitribhyam Sa Cha (5 4 118)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.DvitribhyamSaChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("अङ्गुलि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Angulyah Samasantah (5 4 119)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AngulyahSamasantahSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("त्रि", Vibhakti.PRATHAMA),
                SamasaPada("अङ्गुलि", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Advardvis (5 4 120)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.AdvardvisSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Bahuvrihav Anuktoc (5 4 121)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.BahuvrihavAnuktocSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("चित्रा", Vibhakti.PRATHAMA),
                SamasaPada("गु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Nityam Sac Cha (5 4 122)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.NityamSacChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उत्तम", Vibhakti.PRATHAMA),
                SamasaPada("मूर्धन्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Nadyah Sese (6 3 48)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.NadyahSeseSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("नदी", Vibhakti.PRATHAMA),
                SamasaPada("माता", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Na Samjnayoh (6 3 49)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.NaSamjnayohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("कावेरी", Vibhakti.PRATHAMA),
                SamasaPada("तीर", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Hrasvo Napumsake Pratipadikasya (6 3 50)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.HrasvoNapumsakePratipadikasyaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA),
                SamasaPada("गु", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.AVYAYIBHAVA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Gopanusarayos Tu (6 3 51)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.GopanusarayosTuSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("राज", Vibhakti.PRATHAMA),
                SamasaPada("गोप", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Padasyangalopo Ahastini (6 3 52)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.PadasyangalopoAhastiniSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पाद", Vibhakti.PRATHAMA),
                SamasaPada("शत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test JharsavAdekac Cap (5 4 123)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.JharsavAdekacCapSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("वाच्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Suryad Astam Ite (5 4 124)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.SuryadAstamIteSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सूर्य", Vibhakti.PRATHAMA),
                SamasaPada("अस्त", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Suhrd Durhrdau Mitramitrayoh (5 4 125)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.SuhrdDurhrdauMitramitrayohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("हृदय", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Prakrtya Abhigamane (5 4 126)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.PrakrtyaAbhigamaneSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अभि", Vibhakti.PRATHAMA),
                SamasaPada("गम्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Apat Thac (5 4 127)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya5.pada4.ApatThacSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("अप", Vibhakti.PRATHAMA),
                SamasaPada("रथ", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Paddhannomasdhrn (6 3 53)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.PaddhannomasdhrnSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पाद", Vibhakti.PRATHAMA),
                SamasaPada("रुज्", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Mamsapakasthayoh (6 3 54)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.MamsapakasthayohSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("पाक", Vibhakti.PRATHAMA),
                SamasaPada("मांसपाक", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Padasya Pad (6 3 55)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.PadasyaPadSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("द्वि", Vibhakti.PRATHAMA),
                SamasaPada("पाद", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Hrdayasya Hrd (6 3 56)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.HrdayasyaHrdSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("हृदय", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.BAHUVRIHI,
        )
        assertTrue(sutra.matches(context))
    }

    @Test
    fun `test Nisthayam Cha (6 3 57)`() {
        val sutra = dev.panini.ashtadhyayi.adhyaya6.pada3.NisthayamChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("सु", Vibhakti.PRATHAMA),
                SamasaPada("कृत", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
    }
}











