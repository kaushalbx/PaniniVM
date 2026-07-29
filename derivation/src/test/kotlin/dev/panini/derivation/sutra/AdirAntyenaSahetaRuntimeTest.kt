package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdirAntyenaSahetaSutra
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
import kotlin.test.assertTrue

class AdirAntyenaSahetaRuntimeTest {
    @Test
    fun `pratyahara range principle round trips through canonical blueprint`() {
        val blueprint = AdirAntyenaSahetaSutra.toBlueprint()
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
    fun `decoded pratyahara range principle registers natively`() {
        val blueprint = AdirAntyenaSahetaSutra.toBlueprint()
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
                    DerivationTerm("pratyahara", "अच्", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.1.71", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                InterpretivePrincipleDefinition(
                    principle = InterpretivePrinciple.PRATYAHARA_RANGE,
                    definingSutra = SutraId("1.1.71"),
                ),
            ),
            result.state.interpretivePrinciples,
        )
        assertEquals(listOf(SutraId("1.1.71")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native pratyahara principle automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.71")
        }

        assertEquals("interpretive-principle", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior remains available during migration`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("pratyahara", "अच्", TermKind.PRATIPADIKA),
            ),
        )

        assertTrue(AdirAntyenaSahetaSutra.matches(initial))
        assertTrue("1.1.71" in AdirAntyenaSahetaSutra.apply(initial).state.activeAdhikaras)
    }
}
