package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.SvamRupamSabdasyasabdasamjnaSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.InterpretivePrinciple
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

class SvamRupamRuntimeTest {
    @Test
    fun `self-form principle round trips through canonical blueprint`() {
        val blueprint = SvamRupamSabdasyasabdasamjnaSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("interpretive-principle", decoded.artha.kind)
    }

    @Test
    fun `decoded self-form principle registers natively`() {
        val blueprint = SvamRupamSabdasyasabdasamjnaSutra.toBlueprint()
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
                    DerivationTerm("word", "अग्नि", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.1.68", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                InterpretivePrincipleDefinition(
                    principle = InterpretivePrinciple.SELF_FORM_REFERENCE,
                    definingSutra = SutraId("1.1.68"),
                ),
            ),
            result.state.interpretivePrinciples,
        )
        assertEquals(listOf(SutraId("1.1.68")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native self-form principle automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.68")
        }

        assertEquals("interpretive-principle", runtime.artha.kind)
    }
}
