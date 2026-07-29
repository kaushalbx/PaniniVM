package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AnuditSavarnasyaCapratyayahSutra
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

class AnuditSavarnasyaRuntimeTest {
    @Test
    fun `savarna inclusion principle round trips through canonical blueprint`() {
        val blueprint = AnuditSavarnasyaCapratyayahSutra.toBlueprint()
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
    fun `decoded savarna inclusion principle registers natively`() {
        val blueprint = AnuditSavarnasyaCapratyayahSutra.toBlueprint()
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
                    DerivationTerm("varna", "अ", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.1.69", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                InterpretivePrincipleDefinition(
                    principle = InterpretivePrinciple.SAVARNA_INCLUSION_EXCEPT_AFFIX,
                    definingSutra = SutraId("1.1.69"),
                ),
            ),
            result.state.interpretivePrinciples,
        )
        assertEquals(listOf(SutraId("1.1.69")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native savarna principle automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.69")
        }

        assertEquals("interpretive-principle", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior remains available during migration`() {
        assertEquals(true, AnuditSavarnasyaCapratyayahSutra.apply("अ"))
    }
}
