package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.NaVibhaktauTusmahSutra
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
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

class NaVibhaktauTusmahRuntimeTest {
    @Test
    fun `contextual prohibition round trips through canonical blueprint`() {
        val blueprint = NaVibhaktauTusmahSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("contextual-prohibition", decoded.artha.kind)
    }

    @Test
    fun `decoded prohibition blocks halantyam without invoking legacy sutra`() {
        val blueprint = NaVibhaktauTusmahSutra.toBlueprint()
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
                    DerivationTerm("vibhakti", "सस्", TermKind.PRATYAYA),
                ),
                stage = DerivationStage.PRATYAYA_SELECTED,
                samjnas = setOf(SamjnaAssignment("vibhakti", Samjna.PRATYAYA)),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.4", listOf(runtime)),
                initial,
            ),
        )

        assertEquals("1.3.4", result.state.derivation.blockedSutras["1.3.3"])
        assertEquals(listOf(SutraId("1.3.4")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native contextual prohibition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.4")
        }

        assertEquals("contextual-prohibition", runtime.artha.kind)
    }
}
