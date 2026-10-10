package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.PrathamayohPurvaSavarnahSutra
import dev.panini.core.SupAffix
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.*
import kotlin.test.*

class PurvasavarnaDualTest {
    @Test fun `dual coalescence preserves stem annotations and typed suffix provenance`() {
        for (identity in listOf(SupAffix.AU, SupAffix.AUT)) {
            val stem = DerivationTerm("stem", "कँविँऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
            val suffix = DerivationTerm("arbitrary-id", "औ", TermKind.PRATYAYA,
                upadesha = identity.upadesha, sourceSupAffix = identity)
            val before = DerivationState(listOf(stem, suffix), stage = DerivationStage.IT_PROCESSED)
            assertTrue(PrathamayohPurvaSavarnahSutra.matches(before))
            val after = PrathamayohPurvaSavarnahSutra.apply(before).state
            val result = after.terms.single()
            assertEquals("कँवीँऽ", result.surface)
            assertEquals(stem.id, result.id)
            assertEquals(stem.createdBySutra, result.createdBySutra)
            assertEquals(setOf(identity.upadesha), result.sourceSuffixUpadeshas)
            assertEquals(identity, after.droppedTerms.single().sourceSupAffix)
            assertEquals(stem.id, after.droppedTerms.single().mergedIntoTermId)
            assertEquals(0, after.droppedTerms.single().mergedAffixVowelFromEnd)
            assertEquals(suffix.surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
            assertEquals(listOf(stem.varnas.lastIndex, 0), after.substitutions.map { it.sourceVarnaIndex })
        }
    }

    @Test fun `short ik duals derive through canonical purvasavarna`() {
        val engine = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras)
        for ((stem, expected) in listOf("अग्नि" to "अग्नी", "वायु" to "वायू")) {
            for (vibhakti in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
                val result = engine.derive(SubantaDerivationRequest(stem, vibhakti,
                    Vacana.DVIVACANA, Linga.PUMS).initialState())
                assertEquals(expected, result.final.surface)
                assertTrue(result.applications.any { it.sutra == "6.1.102" })
                assertFalse(result.applications.any { it.sutra == "7.3.123" })
            }
        }
    }
}
