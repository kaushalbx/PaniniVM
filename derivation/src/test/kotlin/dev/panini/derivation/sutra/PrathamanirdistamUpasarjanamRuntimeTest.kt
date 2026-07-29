package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada2.PrathamanirdistamSamasaUpasarjanamSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Samjni
import dev.panini.sutra.runtime.SutraBlueprintTextCodec
import dev.panini.sutra.runtime.SutraBlueprintTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintTextEncoding
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.SutraProgram
import dev.panini.sutra.runtime.toBlueprint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PrathamanirdistamUpasarjanamRuntimeTest {
    @Test
    fun `upasarjana definition round trips through canonical blueprint`() {
        val blueprint = PrathamanirdistamSamasaUpasarjanamSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("samjna-definition", decoded.artha.kind)
    }

    @Test
    fun `decoded upasarjana definition registers natively`() {
        val blueprint = PrathamanirdistamSamasaUpasarjanamSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint
        val runtime = DerivationBlueprintCompiler.compile(decoded)
        val initial = DerivationAvastha(
            DerivationState(
                terms = listOf(
                    DerivationTerm("member", "राज", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.2.43", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                SamjnaDefinition(
                    samjni = Samjni.PRATHAMA_DESIGNATED_COMPOUND_TERM,
                    samjna = Samjna.UPASARJANA,
                    definingSutra = SutraId("1.2.43"),
                ),
            ),
            result.state.samjnaDefinitions,
        )
        assertEquals(listOf(SutraId("1.2.43")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native upasarjana definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.2.43")
        }

        assertEquals("samjna-definition", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior remains available during migration`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("member", "राज", TermKind.PRATIPADIKA),
            ),
        )

        assertTrue(PrathamanirdistamSamasaUpasarjanamSutra.matches(initial))
        assertTrue(
            "1.2.43" in
                PrathamanirdistamSamasaUpasarjanamSutra.apply(initial).state.activeAdhikaras,
        )
    }
}
