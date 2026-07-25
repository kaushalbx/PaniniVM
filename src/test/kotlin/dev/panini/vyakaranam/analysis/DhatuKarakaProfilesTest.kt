package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DhatuKarakaProfilesTest {

    @Test
    fun `profiles exist for added dhatus`() {
        val dhatus = listOf("कृ", "करोति", "गम्", "गच्छति", "पा", "पिबति", "दृश्", "पश्यति", "लभ्", "लभते", "ज्ञा", "जानाति")
        dhatus.forEach { dhatu ->
            val profile = DhatuKarakaProfiles.forSurface(dhatu)
            assertNotNull(profile, "Profile should exist for $dhatu")
        }
    }

    @Test
    fun `gam resolves destination to motion goal karman`() {
        val profile = DhatuKarakaProfiles.forSurface("गच्छति")
        assertNotNull(profile)
        val participant = ParticipantFacts(
            id = "gramam",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("ग्रामम्", "ग्रामम्"),
            possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = profile.relations,
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("गम्"),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
    }

    @Test
    fun `kru resolves desired object to karman`() {
        val profile = DhatuKarakaProfiles.forSurface("करोति")
        assertNotNull(profile)
        val participant = ParticipantFacts(
            id = "karyam",
            expression = dev.panini.vyakaranam.ast.AvyayaPada("कार्यम्", "कार्यम्"),
            possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            semanticRelations = profile.relations,
        )
        val resolution = KarakaRuleEngine.resolve(
            KarakaRuleContext(
                dhatu = DhatuIdentity("कृ"),
                participant = participant,
                allParticipants = listOf(participant),
                prayoga = Prayoga.KARTARI,
            ),
        )
        assertEquals(Karaka.KARMAN, resolution.resolved)
    }

    @Test
    fun `dhatu instance semantic relations are dynamically merged into profiles`() {
        val yujirDhatu = dev.panini.dhatupatha.DhatuPatha.all.first { it is YujirDhatu }
        val profile = DhatuKarakaProfiles.forSurface(yujirDhatu.upadesha)
        assertNotNull(profile)
        assertTrue(profile.relations.contains(SemanticRelation.DESIRED_OBJECT))
    }
}
