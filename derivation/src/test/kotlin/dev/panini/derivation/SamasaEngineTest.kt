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
        val result = engine.derive(
            listOf(
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
                SamasaPada("माता", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertTrue(result.applications.any { it.sutra == "2.2.32" })
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
        val sutra = dev.panini.ashtadhyayi.adhyaya2.pada2.KarmaniChaSutra
        val context = dev.panini.analysis.SamasaRuleContext(
            padas = listOf(
                SamasaPada("गो", Vibhakti.SASTHI),
                SamasaPada("दोह", Vibhakti.PRATHAMA),
            ),
            samasaType = SamasaType.TATPURUSA,
        )
        assertTrue(sutra.matches(context))
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
}






