package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.AdirNitudavahSutra
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

class AdirNitudavahRuntimeTest {
    @Test
    fun `initial ni-tu-du assignment round trips through canonical blueprint`() {
        val blueprint = AdirNitudavahSutra.toBlueprint()
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
    fun `decoded rule marks all three initial upadesha forms natively`() {
        val blueprint = AdirNitudavahSutra.toBlueprint()
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
                    DerivationTerm("ni-root", "ञिभिद्", TermKind.DHATU),
                    DerivationTerm("tu-root", "टुकृ", TermKind.DHATU),
                    DerivationTerm("du-root", "डुपच्", TermKind.DHATU),
                ),
                stage = DerivationStage.INITIAL,
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.5", listOf(runtime)),
                initial,
            ),
        )
        val terms = result.state.derivation.terms.associateBy { it.id }

        assertTrue(ItMarker.KIT in terms.getValue("ni-root").itMarkers)
        assertTrue(ItMarker.T in terms.getValue("tu-root").itMarkers)
        assertTrue(ItMarker.KIT in terms.getValue("du-root").itMarkers)
        assertEquals(listOf(SutraId("1.3.5")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native initial assignment automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.5")
        }

        assertEquals("contextual-samjna-assignment", runtime.artha.kind)
    }
}
