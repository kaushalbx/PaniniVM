package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals

class KarakaInferenceTest {
    @Test
    fun `bhyam retains all kartari karaka possibilities`() {
        assertEquals(
            setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARTARI),
        )
    }

    @Test
    fun `bhyam in passive includes agent and non-agent possibilities`() {
        assertEquals(
            setOf(Karaka.KARTR, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARMANI),
        )
    }

    @Test
    fun `bhyam is resolved as sampradana for giving`() {
        assertResolution("दा", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.32", "2.3.13")
    }

    @Test
    fun `bhyam is resolved as karana for writing`() {
        assertResolution("लिख्", Karaka.KARANA, Vibhakti.TRTIYA, "1.4.42", "2.3.18")
    }

    @Test
    fun `bhyam is resolved as apadana for fleeing`() {
        assertResolution("पलाय्", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.24", "2.3.28")
    }

    private fun assertResolution(
        dhatu: String,
        karaka: Karaka,
        vibhakti: Vibhakti,
        semanticSutra: String,
        vibhaktiSutra: String,
    ) {
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = dev.panini.core.SupAffix.candidates("भ्याम्").mapTo(mutableSetOf()) { it.vibhakti }
        val participant = ParticipantFacts(
            id = "test_p",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("भ्याम्", "भ्याम्"),
            possibleVibhaktis = possibleVibhaktis,
            semanticRelations = profile?.relations.orEmpty(),
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity(dhatu),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
            ),
        )
        assertEquals(karaka, resolution.resolved)
        assertEquals(setOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI), resolution.possibleVibhaktis)
        assertEquals(listOf(semanticSutra, vibhaktiSutra), resolution.evidence.map { it.sutra })
        assertEquals(vibhakti, resolution.possibleVibhaktis.single { candidate ->
            candidate == when (karaka) {
                Karaka.KARANA -> Vibhakti.TRTIYA
                Karaka.SAMPRADANA -> Vibhakti.CHATURTHI
                Karaka.APADANA -> Vibhakti.PANCHAMI
                else -> error("Unexpected test kāraka")
            }
        })
    }
}
