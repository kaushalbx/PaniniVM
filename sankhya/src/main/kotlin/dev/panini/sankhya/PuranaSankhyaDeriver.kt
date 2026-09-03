package dev.panini.sankhya

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationConfig
import dev.panini.derivation.DerivationPipeline
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.OptionalRulePolicy
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage

/** Derives the currently implemented pūraṇa numerals through A.5.2.48–56. */
class PuranaSankhyaDeriver(
    private val cardinalDeriver: CardinalSankhyaDeriver,
) {
    private val expressionBuilder = SankhyaExpressionBuilder()
    private val pipeline = DerivationPipeline(
        stages = listOf(
            SutraStage.PRATYAYA_SELECTION,
            SutraStage.IT_PROCESSING,
            SutraStage.PRATYAYA_SELECTION,
            SutraStage.IT_PROCESSING,
            SutraStage.ANGAKARYA,
            SutraStage.PADA_FORMATION,
            SutraStage.THUK_PHONOLOGY,
        ),
        prepareStage = { stage, state ->
            if (stage == SutraStage.PRATYAYA_SELECTION) state else state.copy(stage = DerivationStage.PADA_FORMED)
        },
        configForStage = { stage ->
            if (stage == SutraStage.PRATYAYA_SELECTION) DerivationConfig(OptionalRulePolicy.SKIP_ALL)
            else DerivationConfig()
        },
        isStageEnabled = { stage, _, state ->
            stage != SutraStage.THUK_PHONOLOGY || state.terms.any { it.upadesha == "थुक्" }
        },
        sutrasForStage = Ashtadhyayi::puranaSankhyaSutrasAt,
    )

    fun derive(value: Long): DerivationResult {
        val initial = initialState(value)
        return pipeline.derive(initial)
    }

    fun deriveVariants(value: Long): List<DerivationResult> {
        requireSupported(value)
        val initial = initialState(value)
        return pipeline.deriveAll(initial, setOf(SutraStage.PRATYAYA_SELECTION))
    }

    private fun initialState(value: Long): DerivationState {
        requireSupported(value)

        val base = if (value == 1L) "प्रथम" else PrimitiveSankhya.fromValue(value)?.pratipadika
            ?: cardinalDeriver.derive(value).final.surface
        val underlyingHead = if (value == 1L) base else expressionBuilder.build(value).headPrimitive().pratipadika
        val term = DerivationTerm(
            id = "purana_base",
            surface = base,
            kind = TermKind.PRATIPADIKA,
            upadesha = base,
            compoundHeadUpadesha = underlyingHead,
        )
        val initial = DerivationState(
            terms = listOf(term),
            samjnas = setOf(
                SamjnaAssignment(term.id, Samjna.PRATIPADIKA),
                SamjnaAssignment(term.id, Samjna.SANKHYA),
                SamjnaAssignment(term.id, Samjna.PURANA),
            ),
            activeAdhikaras = setOf("4.1.76"),
            stage = DerivationStage.INITIAL,
        )
        // प्रथम is lexical; the rule engine must preserve it without fabricating a sūtra application.
        return initial
    }

    private fun requireSupported(value: Long) {
        require(value > 0L) { "Pūraṇa numerals require a positive cardinal: $value" }
    }
}
