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

    @Test
    fun `bhyam is resolved as apadana for origin of bhu`() {
        assertResolution("प्रभू", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.31", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as apadana for birth source of jan`() {
        assertResolution("जन्", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.30", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as apadana for varay`() {
        assertResolution("वारय", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.27", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as apadana for nili`() {
        assertResolution("निली", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.28", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as sampradana for dharay`() {
        assertResolution("धारय", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.35", "2.3.13")
    }

    @Test
    fun `bhyam is resolved as apadana for paraji`() {
        assertResolution("पराजि", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.26", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as apadana for adhi`() {
        assertResolution("अधी", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.29", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as sampradana for sprha`() {
        assertResolution("स्पृह", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.36", "2.3.13")
    }

    @Test
    fun `location of prefixed vish is resolved as karman`() {
        val dhatu = "अभिनिविश"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("मार्गम्", "मार्गम्"),
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
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.47", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `bhyam is resolved as sampradana for anger target of krudh`() {
        assertResolution("क्रुध्", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.37", "2.3.13")
    }

    @Test
    fun `bhyam is resolved as apadana for fear of bhi`() {
        assertResolution("भी", Karaka.APADANA, Vibhakti.PANCHAMI, "1.4.25", "2.3.28")
    }

    @Test
    fun `bhyam is resolved as sampradana for pleasing ruc`() {
        assertResolution("रुच", Karaka.SAMPRADANA, Vibhakti.CHATURTHI, "1.4.33", "2.3.13")
    }

    @Test
    fun `recipient of prefixed anger verb is resolved as karman`() {
        val dhatu = "अभिक्रुध"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("क्रूरम्", "क्रूरम्"),
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
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.38", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `location of prefixed dwelling verb is resolved as karman`() {
        val dhatu = "अधिवस"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("वैकुण्ठम्", "वैकुण्ठम्"),
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
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.48", "2.3.2"), resolution.evidence.map { it.sutra })
    }

    @Test
    fun `location of adhishi is resolved as karman`() {
        val dhatu = "अधिशी"
        val profile = DhatuKarakaProfiles.forSurface(dhatu)
        val possibleVibhaktis = setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI)
        val participant = ParticipantFacts(
            id = "test_p",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("वैकुण्ठम्", "वैकुण्ठम्"),
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
        assertEquals(Karaka.KARMAN, resolution.resolved)
        assertEquals(listOf("1.4.46", "2.3.2"), resolution.evidence.map { it.sutra })
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
