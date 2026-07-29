package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AloAntyatPurvaUpadhaSutra
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

class AloAntyatPurvaUpadhaRuntimeTest {
    @Test
    fun `typed upadha definition round trips through canonical blueprint`() {
        val blueprint = AloAntyatPurvaUpadhaSutra.toBlueprint()
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
    fun `decoded definition registers upadha natively`() {
        val blueprint = AloAntyatPurvaUpadhaSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint
        val runtime = DerivationBlueprintCompiler.compile(decoded)

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.1.65", listOf(runtime)),
                DerivationAvastha(
                    DerivationState(
                        terms = listOf(
                            DerivationTerm("stem", "abc", TermKind.PRATIPADIKA),
                        ),
                    ),
                ),
            ),
        )

        assertEquals(
            setOf(
                SamjnaDefinition(
                    samjni = Samjni.PENULTIMATE_SOUND,
                    samjna = Samjna.UPADHA,
                    definingSutra = SutraId("1.1.65"),
                ),
            ),
            result.state.samjnaDefinitions,
        )
        assertEquals(listOf(SutraId("1.1.65")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native upadha definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.1.65")
        }

        assertEquals("samjna-definition", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior still selects the penultimate character`() {
        assertEquals('b', AloAntyatPurvaUpadhaSutra.apply("abc"))
    }
}
