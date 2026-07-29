package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.runtime.AshtadhyayiCompiler
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

class LasakvataddhiteRuntimeTest {
    @Test
    fun `la-sha-ku assignment round trips through canonical blueprint`() {
        val blueprint = LasakvataddhiteSutra.toBlueprint()
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
    fun `decoded rule delegates la-sha-ku marking to its sutra`() {
        val blueprint = LasakvataddhiteSutra.toBlueprint()
        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded),
        ).blueprint
        val runtime = AshtadhyayiCompiler.compile(decoded)
        val initial = DerivationAvastha(
            DerivationState(
                terms = listOf(
                    DerivationTerm("la-affix", "ल्युट्", TermKind.PRATYAYA),
                    DerivationTerm("sha-affix", "शप्", TermKind.PRATYAYA),
                    DerivationTerm("nga-affix", "ङीप्", TermKind.PRATYAYA),
                ),
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                SutraProgram("ashtadhyayi-1.3.8", listOf(runtime)),
                initial,
            ),
        )
        val terms = result.state.derivation.terms.associateBy { it.id }

        assertTrue(ItMarker.KIT in terms.getValue("la-affix").itMarkers)
        assertTrue(ItMarker.SH in terms.getValue("sha-affix").itMarkers)
        assertTrue(ItMarker.NGIT in terms.getValue("nga-affix").itMarkers)
        assertEquals(listOf(SutraId("1.3.8")), result.state.appliedSutras)
    }

    @Test
    fun `ashtadhyayi grantha selects native la-sha-ku assignment automatically`() {
        val runtime = AshtadhyayiRuntimeGrantha.grantha.sutras.single {
            it.id == SutraId("1.3.8")
        }

        assertEquals("contextual-samjna-assignment", runtime.artha.kind)
    }
}
