package dev.panini.derivation.sutra

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.SutraProgram
import dev.panini.sutra.runtime.SutraTraceEntry
import dev.panini.sutra.runtime.toBlueprintGrantha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ConflictSamjnaRuntimeGranthaTest {
    @Test
    fun `conflict samjna sutra 1-1-5 blocks 1-1-3 when kit or ngit affix is present`() {
        val openingSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(OpeningSamjnaRuntimeGrantha.grantha),
        ).program.sutras
        val conflictSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(ConflictSamjnaRuntimeGrantha.grantha),
        ).program.sutras

        val combinedProgram = SutraProgram(
            "ashtadhyayi-1.1.1-1.1.6",
            openingSutras + conflictSutras,
        )

        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("root", "चि", TermKind.DHATU),
                DerivationTerm(
                    "affix",
                    "क्त",
                    TermKind.PRATYAYA,
                    itMarkers = setOf(ItMarker.KIT),
                ),
            ),
            stage = DerivationStage.INITIAL,
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                combinedProgram,
                DerivationAvastha(initial),
            ),
        )

        val kngitiCaTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.5") }
        assertIs<SutraTraceEntry.Applied>(kngitiCaTrace)

        val ikoGunaVrddhiTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.3") }
        val blockedEntry = assertIs<SutraTraceEntry.Blocked>(ikoGunaVrddhiTrace)
        assertEquals(SutraId("1.1.5"), blockedEntry.blocker)
    }

    @Test
    fun `conflict samjna sutra 1-1-4 blocks 1-1-3 in dhatu lopa ardhadhatuka environment`() {
        val openingSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(OpeningSamjnaRuntimeGrantha.grantha),
        ).program.sutras
        val conflictSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(ConflictSamjnaRuntimeGrantha.grantha),
        ).program.sutras

        val combinedProgram = SutraProgram(
            "ashtadhyayi-1.1.1-1.1.6",
            openingSutras + conflictSutras,
        )

        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("root", "लोप्", TermKind.DHATU),
            ),
            context = DerivationalContext(
                environments = setOf(
                    DerivationalEnvironment.DHATU_LOPA,
                    DerivationalEnvironment.ARDHADHATUKA,
                ),
            ),
            stage = DerivationStage.INITIAL,
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                combinedProgram,
                DerivationAvastha(initial),
            ),
        )

        val naDhatulopaTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.4") }
        assertIs<SutraTraceEntry.Applied>(naDhatulopaTrace)

        val ikoGunaVrddhiTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.3") }
        val blockedEntry = assertIs<SutraTraceEntry.Blocked>(ikoGunaVrddhiTrace)
        assertEquals(SutraId("1.1.4"), blockedEntry.blocker)
    }

    @Test
    fun `conflict samjna sutra 1-1-6 blocks 1-1-3 for didhi and vevi roots`() {
        val openingSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(OpeningSamjnaRuntimeGrantha.grantha),
        ).program.sutras
        val conflictSutras = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(ConflictSamjnaRuntimeGrantha.grantha),
        ).program.sutras

        val combinedProgram = SutraProgram(
            "ashtadhyayi-1.1.1-1.1.6",
            openingSutras + conflictSutras,
        )

        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("root", "दीधी", TermKind.DHATU),
            ),
            stage = DerivationStage.INITIAL,
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                combinedProgram,
                DerivationAvastha(initial),
            ),
        )

        val didhivevitamTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.6") }
        assertIs<SutraTraceEntry.Applied>(didhivevitamTrace)

        val ikoGunaVrddhiTrace = result.trace.firstOrNull { it.sutraId == SutraId("1.1.3") }
        val blockedEntry = assertIs<SutraTraceEntry.Blocked>(ikoGunaVrddhiTrace)
        assertEquals(SutraId("1.1.6"), blockedEntry.blocker)
    }

    @Test
    fun `conflict samjna grantha has portable inspectable source`() {
        val grantha = ConflictSamjnaRuntimeGrantha.grantha
        val blueprint = grantha.toBlueprintGrantha()
        val source = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(source),
        ).grantha

        assertEquals(blueprint, decoded)
        assertEquals(listOf("1.1.4", "1.1.5", "1.1.6"), grantha.sutras.map { it.id.value })
        assertTrue(MigratedAshtadhyayiGranthas.registry.granthas.contains(grantha))
    }
}
