package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
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

class HalantyamRuntimeTest {
    @Test
    fun `final-consonant assignment round trips through canonical blueprint`() {
        val blueprint = HalantyamSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("contextual-samjna-assignment", decoded.artha.kind)
    }

    @Test
    fun `decoded rule delegates final consonant marking to its registered sutra`() {
        val blueprint = HalantyamSutra.toBlueprint()
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
                    DerivationTerm("affix", "तिप्", TermKind.PRATYAYA),
                ),
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.3", listOf(runtime)),
                initial,
            ),
        )

        assertTrue(ItMarker.KIT in result.state.derivation.terms.single().itMarkers)
        assertEquals(listOf(SutraId("1.3.3")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native final-consonant assignment automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.3")
        }

        assertEquals("contextual-samjna-assignment", runtime.artha.kind)
    }

    @Test
    fun `native rule preserves resolved agama exclusion`() {
        val runtime = DerivationBlueprintCompiler.compile(HalantyamSutra.toBlueprint())
        val initial = DerivationAvastha(
            DerivationState(
                terms = listOf(
                    DerivationTerm("vuk", "वुक्", TermKind.PRATYAYA),
                ),
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.3-exclusion", listOf(runtime)),
                initial,
            ),
        )

        assertTrue(result.state.derivation.terms.single().itMarkers.isEmpty())
        assertTrue(result.state.appliedSutras.isEmpty())
    }
}
