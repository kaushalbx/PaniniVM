package dev.panini.unadipatha.sutra

import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.sutra.DerivationAvastha
import dev.panini.derivation.sutra.DerivationSutraEffectInterpreter
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
import dev.panini.sutra.runtime.SutraGranthaCompiler
import dev.panini.sutra.runtime.SutraGranthaLowering
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.toBlueprintGrantha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UnadipathaRuntimeGranthaTest {
    @Test
    fun `unadipatha catalogue grantha executes via sutra machine`() {
        val grantha = UnadipathaRuntimeGrantha.grantha
        val program = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(grantha),
        ).program

        val root = KruDhatu()
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("root", root.sourceSurface, TermKind.DHATU, upadesha = root.upadesha),
            ),
            stage = DerivationStage.INITIAL,
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                program,
                DerivationAvastha(initial),
            ),
        )

        assertTrue(grantha.sutras.size >= 33)
        assertTrue(result.state.appliedSutras.contains(SutraId("1.3")))
        val pratyayaTerm = result.state.derivation.terms.firstOrNull { it.kind == TermKind.PRATYAYA }
        assertTrue(pratyayaTerm != null)
    }

    @Test
    fun `unadipatha grantha has portable inspectable source and round-trips text codec`() {
        val grantha = UnadipathaRuntimeGrantha.grantha
        val blueprint = grantha.toBlueprintGrantha()
        val source = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(blueprint),
        ).text
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(source),
        ).grantha

        assertEquals(blueprint, decoded)
        val sutra1_3 = decoded.sutras.single { it.id == SutraId("1.3") }
        assertEquals("कृसृभ्यामुण्", sutra1_3.source.text)
        assertEquals(SutraArthaValue.Text("1.3"), sutra1_3.artha.fields["number"])
        assertEquals(
            SutraArthaValue.Sequence(listOf(SutraArthaValue.Text("डुकृञ्"), SutraArthaValue.Text("सृ"))),
            sutra1_3.artha.fields["roots"],
        )
    }
}
