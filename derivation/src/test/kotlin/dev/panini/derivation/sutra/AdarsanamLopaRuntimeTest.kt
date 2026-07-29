package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdarsanamLopaSutra
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
import dev.panini.sutra.runtime.SutraSource
import dev.panini.sutra.runtime.toBlueprint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AdarsanamLopaRuntimeTest {
    @Test
    fun `canonical blueprint preserves segmented source`() {
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(AdarsanamLopaSutra.toBlueprint()),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(AdarsanamLopaSutra.toBlueprint(), decoded)
        val source = assertIs<SutraSource.Ashtadhyayi>(decoded.source)
        assertEquals("अदर्शनं लोपः", source.text)
        assertEquals("नञ् - दृश् + ल्युट् + अम् लोप + सुँ ।", source.segmentedSource)
    }

    @Test
    fun `decoded definition executes natively through sutra machine`() {
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(AdarsanamLopaSutra.toBlueprint()),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint
        val runtime = DerivationBlueprintCompiler.compile(decoded)
        val program = SutraProgram("ashtadhyayi-1.1.60", listOf(runtime))

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                program,
                DerivationAvastha(
                    DerivationState(
                        terms = listOf(
                            DerivationTerm("definition-context", "अदर्शन", TermKind.PRATIPADIKA),
                        ),
                    ),
                ),
            ),
        )

        assertTrue(
            SamjnaDefinition(
                samjni = Samjni.ADARSHANA,
                samjna = Samjna.LOPA,
                definingSutra = SutraId("1.1.60"),
            ) in result.state.samjnaDefinitions,
        )
        assertEquals(listOf(SutraId("1.1.60")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha uses native definition runtime`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.60")
        }

        assertEquals("samjna-definition", runtime.artha.kind)
        assertEquals(AdarsanamLopaSutra.toBlueprint().source, runtime.source)
    }
}
