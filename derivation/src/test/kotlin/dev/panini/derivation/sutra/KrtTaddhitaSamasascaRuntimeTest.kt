package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada2.KrtTaddhitaSamasascaSutra
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

class KrtTaddhitaSamasascaRuntimeTest {
    @Test
    fun `pratipadika source set round trips through canonical blueprint`() {
        val blueprint = KrtTaddhitaSamasascaSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("samjni-set-definition", decoded.artha.kind)
    }

    @Test
    fun `decoded source set registers three pratipadika definitions natively`() {
        val blueprint = KrtTaddhitaSamasascaSutra.toBlueprint()
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
                    DerivationTerm("stem", "पाचक", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.2.46", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(Samjni.KRT_ENDING, Samjni.TADDHITA_ENDING, Samjni.SAMASA),
            result.state.samjnaDefinitions.mapTo(linkedSetOf()) { it.samjni },
        )
        assertTrue(result.state.samjnaDefinitions.all { it.samjna == Samjna.PRATIPADIKA })
        assertEquals(listOf(SutraId("1.2.46")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native source-set definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.2.46")
        }

        assertEquals("samjni-set-definition", runtime.artha.kind)
    }
}
