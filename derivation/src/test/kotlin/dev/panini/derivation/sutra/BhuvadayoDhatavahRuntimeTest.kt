package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.BhuvadayoDhatavahSutra
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

class BhuvadayoDhatavahRuntimeTest {
    @Test
    fun `dhatu definition round trips through canonical blueprint`() {
        val blueprint = BhuvadayoDhatavahSutra.toBlueprint()
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
    fun `decoded dhatu definition registers natively`() {
        val blueprint = BhuvadayoDhatavahSutra.toBlueprint()
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
                    DerivationTerm("dhatu", "भू", TermKind.DHATU),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.1", listOf(runtime)),
                initial,
            ),
        )

        assertEquals(
            setOf(
                SamjnaDefinition(
                    samjni = Samjni.BHU_ADI_VERBAL_ROOT,
                    samjna = Samjna.DHATU,
                    definingSutra = SutraId("1.3.1"),
                ),
            ),
            result.state.samjnaDefinitions,
        )
        assertEquals(listOf(SutraId("1.3.1")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native dhatu definition automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.1")
        }

        assertEquals("samjna-definition", runtime.artha.kind)
    }

    @Test
    fun `legacy direct behavior still assigns dhatu to a verbal root`() {
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("dhatu", "भू", TermKind.DHATU),
            ),
        )

        assertTrue(BhuvadayoDhatavahSutra.matches(initial))
        assertTrue(
            BhuvadayoDhatavahSutra.apply(initial).state.samjnas.any {
                it.targetId == "dhatu" && it.samjna == Samjna.DHATU
            },
        )
    }
}
