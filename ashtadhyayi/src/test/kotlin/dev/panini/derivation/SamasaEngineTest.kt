package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class SamasaEngineTest {

    private val engine = SamasaEngine()

    @Test
    fun `non a final indeclinable compound remains stable across outer cases`() {
        for (case in Vibhakti.entries) {
            val result = engine.derive(SamasaDerivationRequest(
                listOf(SamasaPada("अधि", samjnas = setOf(Samjna.AVYAYA)), SamasaPada("हरि")),
                SamasaType.AVYAYIBHAVA,
                outputVibhakti = case,
            ))
            assertEquals("अधिहरि", result.final.surface, "Outer case: $case")
            assertTrue(result.applications.any { it.sutra == "2.4.82" })
        }
    }

    @Test
    fun `non a final avyayibhava deletes sup rather than substituting am`() {
        val result = engine.derive(SamasaDerivationRequest(
            listOf(SamasaPada("अधि", samjnas = setOf(Samjna.AVYAYA)), SamasaPada("हरि")),
            SamasaType.AVYAYIBHAVA,
            outputVibhakti = Vibhakti.SASTHI,
        ))
        assertEquals("अधिहरि", result.final.surface)
        assertFalse(result.applications.any { it.sutra == "7.3.111" })
        assertTrue(result.applications.any { it.sutra == "2.4.82" })
        assertFalse(result.applications.any { it.sutra == "2.4.83" })
    }

    @Test
    fun `a final avyayibhava retains ablative without am substitution or sup lopa`() {
        val result = engine.derive(SamasaDerivationRequest(
            listOf(SamasaPada("उप", samjnas = setOf(Samjna.AVYAYA)), SamasaPada("कुम्भ")),
            SamasaType.AVYAYIBHAVA,
            outputVibhakti = Vibhakti.PANCHAMI,
        ))
        assertEquals("उपकुम्भात्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "7.1.12" })
        assertFalse(result.applications.any { it.sutra == "2.4.83" || it.sutra == "2.4.82" })
    }

    @Test
    fun `outer case does not turn avyayibhava into an ordinary genitive noun`() {
        val request = SamasaDerivationRequest(
            listOf(SamasaPada("उप", samjnas = setOf(Samjna.AVYAYA)), SamasaPada("कृष्ण")),
            SamasaType.AVYAYIBHAVA,
            outputVibhakti = Vibhakti.SASTHI,
        )
        val result = engine.derive(request)
        assertEquals("उपकृष्णम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.4.83" })
    }

    @Test
    fun `typed compound request retains outer case number and rule provenance`() {
        val request = SamasaDerivationRequest(
            listOf(SamasaPada("राज", Vibhakti.SASTHI), SamasaPada("पुरुष")),
            SamasaType.TATPURUSA,
            outputVibhakti = Vibhakti.SASTHI,
        )
        val genitive = engine.derive(request)
        assertEquals("राजपुरुषस्य", genitive.final.surface)
        assertTrue(genitive.applications.any { it.sutra == "2.2.8" })
        assertTrue(genitive.applications.any { it.sutra == "7.1.12" })
        assertEquals("राजपुरुषौ", engine.derive(request.copy(
            outputVibhakti = Vibhakti.PRATHAMA,
            outputVacana = dev.panini.core.Vacana.DVIVACANA,
        )).final.surface)
    }

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
        assertTrue(result.applications.any { it.sutra == "2.2.29" || it.sutra == "2.2.34" })
    }
}
