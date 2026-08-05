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
        assertTrue(result.applications.any { it.sutra == "2.2.29" })
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
}
