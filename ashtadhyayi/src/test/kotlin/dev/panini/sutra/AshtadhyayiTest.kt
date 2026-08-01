package dev.panini.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AshtadhyayiTest {

    @Test
    fun `catalog and executable sutra identities are unique`() {
        val catalogNumbers = Ashtadhyayi.cataloguedSutras.map { it.sutra }
        val executableNumbers = Ashtadhyayi.executableSutras.map { it.sutra }

        assertEquals(catalogNumbers.size, catalogNumbers.distinct().size, "Duplicate catalogued sūtra number")
        assertEquals(executableNumbers.size, executableNumbers.distinct().size, "Duplicate executable sūtra number")
    }

    @Test
    fun `stage views contain only explicitly classified rules in canonical order`() {
        SutraStage.entries.filterNot { it == SutraStage.UNSPECIFIED }.forEach { stage ->
            val staged = Ashtadhyayi.executableSutrasAt(stage)

            assertTrue(staged.all { it.stage == stage }, "Incorrect rule returned for $stage")
            assertTrue(staged.none { it.stage == SutraStage.UNSPECIFIED }, "Unclassified rule returned for $stage")
            assertEquals(staged.sortedBy { it.krama }, staged, "$stage is not in canonical order")
        }
    }

    @Test
    fun `metadata routed derivation stages remain populated`() {
        val requiredStages = setOf(
            SutraStage.PRATYAYA_SELECTION,
            SutraStage.ANGAKARYA,
            SutraStage.PADA_FORMATION,
            SutraStage.THUK_PHONOLOGY,
        ) + SutraStage.sandhiPhases.filterNot { it == SutraStage.SANDHI }

        requiredStages.forEach { stage ->
            val sutras = Ashtadhyayi.executableSutrasAt(stage)
            assertTrue(sutras.isNotEmpty(), "No executable sūtras registered for $stage")
            sutras.forEach { sutra ->
                assertTrue(sutra.role !is SutraRole.Adhikara, "${sutra.sutra} routes an adhikāra as an operation")
            }
        }
    }

    @Test
    fun `sandhi phase order follows grammatical dependencies`() {
        assertEquals(
            listOf(
                SutraStage.VOWEL_SANDHI,
                SutraStage.RUTVA,
                SutraStage.POST_RUTVA,
                SutraStage.FINAL_CONSONANT_SANDHI,
                SutraStage.VISARJANIYA,
                SutraStage.SIBILANT_SANDHI,
                SutraStage.THUK_PHONOLOGY,
                SutraStage.SANDHI,
            ),
            SutraStage.sandhiPhases,
        )
    }

    @Test
    fun `derives executable views from adhikara metadata`() {
        val feminine = Ashtadhyayi.executableSutrasUnder("4.1.3").map { it.sutra }

        assertTrue("4.1.3" in feminine)
        assertTrue("4.1.41" in feminine)
        assertTrue("4.1.73" in feminine)
        assertTrue("4.1.76" !in feminine)
    }

    @Test
    fun `exposes executable derivation sutras by sutra number`() {
        val sutra = Ashtadhyayi.registry.require("6.1.101")

        assertEquals("अकः सवर्णे दीर्घः", sutra.sutraText)
        assertEquals("6.1.101", sutra.sutra)
    }

    @Test
    fun `navigates loaded sutras by purpose and dependency`() {
        assertEquals(
            listOf("1.1.3"),
            Ashtadhyayi.registry.dependentsOf("1.1.2").map { it.sutra },
        )
        assertEquals(
            emptyList(),
            Ashtadhyayi.registry.dependenciesOf("6.1.87").map { it.sutra },
        )
        assertTrue(Ashtadhyayi.registry.withAction(SutraAction.NISHEDHA).isNotEmpty())
        assertEquals("4.1.2", Ashtadhyayi.registry.require("4.1.2").sutra)
        val vidhiSutras = Ashtadhyayi.registry.withRole(SutraRole.Vidhi).map { it.sutra }
        assertTrue(
            vidhiSutras.containsAll(
                listOf("1.3.9", "4.1.2", "6.1.101", "7.1.54", "7.3.103", "8.3.59", "8.4.58"),
            ),
        )
    }

}
