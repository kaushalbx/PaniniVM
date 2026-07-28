package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.VrddhirAdaicSutra
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.SutraProgram
import dev.panini.sutra.runtime.SutraTraceEntry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DerivationSutraRuntimeAdapterTest {
    @Test
    fun `existing samjna sutra has state parity through shared machine`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm(
                    id = "pratipadika",
                    surface = "राम",
                    kind = TermKind.PRATIPADIKA,
                ),
            ),
            stage = DerivationStage.INITIAL,
        )
        val legacyChange = VrddhirAdaicSutra.apply(initial)
        val runtime = DerivationSutraRuntimeAdapter.adapt(VrddhirAdaicSutra)

        val migrated = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-parity", listOf(runtime)),
                DerivationAvastha(initial),
            ),
        )

        assertEquals(legacyChange.state, migrated.state.derivation)
        assertEquals(listOf(SutraId("1.1.1")), migrated.state.appliedSutras)
        assertTrue(migrated.state.derivation.samjnas.any {
            it.targetId == "pratipadika" && it.samjna == Samjna.VRDDHI
        })
    }

    @Test
    fun `blocked derivation sutra is represented as a blocked decision`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("pratipadika", "राम", TermKind.PRATIPADIKA),
            ),
        ).blockSutra("1.1.1", "test-nishedha")
        val runtime = DerivationSutraRuntimeAdapter.adapt(VrddhirAdaicSutra)

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-blocked", listOf(runtime)),
                DerivationAvastha(initial),
            ),
        )

        val trace = assertIs<SutraTraceEntry.Blocked>(result.trace.single())
        assertEquals(SutraId("test-nishedha"), trace.blocker)
        assertTrue(result.state.appliedSutras.isEmpty())
    }
}
