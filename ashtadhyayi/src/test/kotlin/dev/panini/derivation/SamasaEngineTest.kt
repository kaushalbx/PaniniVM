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
    fun `derives Avyayibhava compound upakrsnam`() {
        // उप (Upasarga/Avyaya) + कृष्ण → उपकृष्णम् (2.1.6)
        val result = engine.derive(
            listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("उपकृष्णम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.6" })
    }

    @Test
    fun `derives Shashthi Tatpurusa compound rajapurusah`() {
        // राज (ṣaṣṭhī) + पुरुष → राजपुरुषः (2.2.8)
        val result = engine.derive(
            listOf(
                SamasaPada("राज", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("राजपुरुषः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.8" })
    }

    @Test
    fun `derives Dvitiya Tatpurusa compound krshnashritah`() {
        // कृष्ण (dvitīyā) + श्रित → कृष्णश्रितः (2.1.24)
        val result = engine.derive(
            listOf(
                SamasaPada("कृष्ण", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("कृष्णश्रितः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.24" })
    }

    @Test
    fun `derives Trtiya Tatpurusa compound shankula-khandah`() {
        // शङ्कुल (tṛtīyā) + खण्ड → शङ्कुलाखण्डः (2.1.30)
        val result = engine.derive(
            listOf(
                SamasaPada("शङ्कुल", Vibhakti.TRTIYA),
                SamasaPada("खण्ड", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("शङ्कुलखण्डः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.30" })
    }

    @Test
    fun `derives Pancami Tatpurusa compound chora-bhayam`() {
        // चोर (pañcamī) + भय → चोरभयम् (2.1.37)
        val result = engine.derive(
            listOf(
                SamasaPada("चोर", Vibhakti.PANCHAMI),
                SamasaPada("भय", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("चोरभयः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.37" })
    }

    @Test
    fun `derives Bahuvrihi compound pitambarah`() {
        // पीत + अम्बर → पीताम्बरः (2.2.24)
        val result = engine.derive(
            listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("पीताम्बरः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `derives Dvandva compound ramalaksmanau`() {
        // राम + लक्ष्मण → रामलक्ष्मणौ (2.2.29)
        val result = engine.derive(
            listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("रामलक्ष्मणौ", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.29" })
    }
}
