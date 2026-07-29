package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaComparison
import dev.panini.shiksha.Samjna
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

class AshtadhyayiRuntimeGranthaTest {
    @Test
    fun `opening samjna sutras execute as one runtime grantha`() {
        val grantha = AshtadhyayiRuntimeGrantha.grantha
        val program = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(grantha),
        ).program
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("vowel", "आ", TermKind.PRATIPADIKA),
            ),
            stage = DerivationStage.INITIAL,
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                program,
                DerivationAvastha(initial),
            ),
        )

        assertEquals(
            Ashtadhyayi.runtimeSutras.map { it.number },
            grantha.sutras.map { it.id.value },
        )
        assertEquals(grantha.sutras.mapTo(linkedSetOf()) { it.id }, grantha.exports)
        assertEquals(
            Ashtadhyayi.runtimeSutras.map { it.number },
            grantha.sutras.map { it.id.value },
        )
        assertTrue(SutraId("1.1.1") in result.state.appliedSutras)
        assertTrue(result.state.derivation.samjnas.any {
            it.targetId == "vowel" && it.samjna == Samjna.VRDDHI
        })
        assertEquals(grantha.sutras.size, result.trace.size)
    }

    @Test
    fun `opening samjna grantha has portable inspectable source`() {
        val openingIds = (1..3).mapTo(linkedSetOf()) { SutraId("1.1.$it") }.apply {
            add(SutraId("1.1.50"))
        }
        val blueprint = AshtadhyayiRuntimeGrantha.grantha.toBlueprintGrantha().let { grantha ->
            grantha.copy(
                sutras = grantha.sutras.filter { it.id in openingIds },
                exports = openingIds,
            )
        }
        val source = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(blueprint),
        ).text
        val decoding = SutraBlueprintGranthaTextCodec.decode(source)
        assertTrue(
            decoding is SutraBlueprintGranthaTextDecoding.Success,
            (decoding as? SutraBlueprintGranthaTextDecoding.Invalid)
                ?.diagnostics?.joinToString("\n") { it.message },
        )
        val decoded = (decoding as SutraBlueprintGranthaTextDecoding.Success).grantha

        assertEquals(blueprint, decoded)
        val iko = decoded.sutras.single { it.id == SutraId("1.1.3") }
        assertEquals("इको गुणवृद्धी", iko.source.text)
        assertEquals(
            SutraArthaValue.Sequence(
                listOf("1.1.1", "1.1.2", "1.1.50").map {
                    SutraArthaValue.SutraReference(SutraId(it))
                },
            ),
            iko.artha.fields["dependencies"],
        )
    }

    @Test
    fun `phonological samjna sutras execute together without conflict scheduling`() {
        val grantha = AshtadhyayiRuntimeGrantha.grantha
        val program = assertIs<SutraGranthaLowering.Success<DerivationAvastha>>(
            SutraGranthaCompiler.lower(grantha),
        ).program
        val initial = DerivationState(
            terms = listOf(
                DerivationTerm("cluster", "क्त", TermKind.PRATIPADIKA),
                DerivationTerm("nasal", "अं", TermKind.PRATIPADIKA),
                DerivationTerm("left", "अ", TermKind.PRATIPADIKA),
                DerivationTerm("right", "आ", TermKind.PRATIPADIKA),
            ),
            varnaComparisons = setOf(
                VarnaComparison("left", "right", 'अ', 'आ', true, true, true),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<DerivationAvastha>>(
            SutraMachine(DerivationSutraEffectInterpreter).process(
                program,
                DerivationAvastha(initial),
            ),
        )

        assertTrue(result.state.appliedSutras.map { it.value }.containsAll(listOf("1.1.7", "1.1.8", "1.1.9")))
        assertTrue(result.state.derivation.samjnas.any {
            it.targetId == "cluster" && it.samjna == Samjna.SAMYOGA
        })
        assertTrue(result.state.derivation.samjnas.any {
            it.targetId == "nasal" && it.samjna == Samjna.ANUNASIKA
        })
        assertTrue(result.state.derivation.samjnas.any {
            it.targetId == "left" && it.samjna == Samjna.SAVARNA
        })
        assertEquals(
            Ashtadhyayi.runtimeSutras.map { it.number },
            grantha.sutras.map { it.id.value },
        )
    }
}
