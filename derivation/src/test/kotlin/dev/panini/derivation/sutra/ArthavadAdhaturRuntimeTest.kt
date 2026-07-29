package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada2.ArthavadAdhaturSutra
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

class ArthavadAdhaturRuntimeTest {
    @Test
    fun `pratipadika definition round trips through canonical blueprint`() {
        val blueprint = ArthavadAdhaturSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint

        assertEquals(blueprint, decoded)
        assertEquals("samjna-definition", decoded.artha.kind)
    }

    @Test
    fun `decoded pratipadika definition registers natively`() {
        val blueprint = ArthavadAdhaturSutra.toBlueprint()
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
                    DerivationTerm("stem", "राम", TermKind.PRATIPADIKA),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.2.45", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                SamjnaDefinition(
                    samjni = Samjni.MEANINGFUL_NON_DHATU_NON_PRATYAYA,
                    samjna = Samjna.PRATIPADIKA,
                    definingSutra = SutraId("1.2.45"),
                ),
            ),
            result.state.samjnaDefinitions,
        )
        assertEquals(listOf(SutraId("1.2.45")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native pratipadika definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.2.45")
        }

        assertEquals("samjna-definition", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior still assigns pratipadika to a stem`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("stem", "राम", TermKind.PRATIPADIKA),
            ),
        )

        assertTrue(ArthavadAdhaturSutra.matches(initial))
        assertTrue(
            ArthavadAdhaturSutra.apply(initial).state.samjnas.any {
                it.targetId == "stem" && it.samjna == Samjna.PRATIPADIKA
            },
        )
    }
}
