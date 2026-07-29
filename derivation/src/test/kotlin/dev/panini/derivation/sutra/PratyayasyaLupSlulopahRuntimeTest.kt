package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.PratyayasyaLupSlulopahSutra
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

class PratyayasyaLupSlulopahRuntimeTest {
    @Test
    fun `typed definition set round trips through canonical blueprint`() {
        val blueprint = PratyayasyaLupSlulopahSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("samjna-set-definition", decoded.artha.kind)
    }

    @Test
    fun `decoded definition registers luk shlu and lup natively`() {
        val blueprint = PratyayasyaLupSlulopahSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint
        val runtime = DerivationBlueprintCompiler.compile(decoded)

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.1.61", listOf(runtime)),
                DerivationAvastha(
                    DerivationState(
                        terms = listOf(
                            DerivationTerm("affix", "सुँ", TermKind.PRATYAYA),
                        ),
                    ),
                ),
            ),
        )

        val assigned = result.state.samjnaDefinitions
            .filter { it.samjni == Samjni.PRATYAYA_ADARSHANA }
            .mapTo(linkedSetOf()) { it.samjna }
        assertEquals(setOf(Samjna.LUK, Samjna.SHLU, Samjna.LUP), assigned)
        assertEquals(listOf(SutraId("1.1.61")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native set definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.61")
        }

        assertEquals("samjna-set-definition", runtime.artha.kind)
    }
}
