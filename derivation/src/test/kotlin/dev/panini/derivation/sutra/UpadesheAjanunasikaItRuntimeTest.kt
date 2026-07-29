package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
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

class UpadesheAjanunasikaItRuntimeTest {
    @Test
    fun `contextual it assignment round trips through canonical blueprint`() {
        val blueprint = UpadesheAjanunasikaItSutra.toBlueprint()
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
    fun `decoded rule assigns it marker without invoking legacy sutra`() {
        val blueprint = UpadesheAjanunasikaItSutra.toBlueprint()
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
                    DerivationTerm("affix", "सुँ", TermKind.PRATYAYA),
                ),
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.2", listOf(runtime)),
                initial,
            ),
        )

        assertTrue(ItMarker.U in result.state.derivation.terms.single().itMarkers)
        assertEquals(listOf(SutraId("1.3.2")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native contextual assignment automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.2")
        }

        assertEquals("contextual-samjna-assignment", runtime.artha.kind)
    }
}
