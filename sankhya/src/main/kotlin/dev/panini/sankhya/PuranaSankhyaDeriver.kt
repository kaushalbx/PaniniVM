package dev.panini.sankhya

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationApplication
import dev.panini.derivation.DerivationConfig
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationEvent
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
    private val taddhitaEngine = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.PRATYAYA_SELECTION))
    private val angaEngine = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.ANGAKARYA))
    private val padaFormationEngine = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.PADA_FORMATION))
    private val thukPhonology = DerivationEngine(Ashtadhyayi.executableSutrasAt(SutraStage.THUK_PHONOLOGY))

    fun derive(value: Long): DerivationResult {
        val initial = initialState(value)
        val taddhita = taddhitaEngine.derive(initial, DerivationConfig(OptionalRulePolicy.SKIP_ALL))
        return complete(initial, taddhita)
    }

    fun deriveVariants(value: Long): List<DerivationResult> {
        requireSupported(value)
        val initial = initialState(value)
        return taddhitaEngine.deriveAll(initial)
            .map { complete(initial, it) }
            .distinctBy { it.final.surface to it.applications.map(DerivationApplication::sutra) }
    }

    private fun complete(initial: DerivationState, taddhita: DerivationResult): DerivationResult {
        val anga = angaEngine.derive(taddhita.final.copy(stage = DerivationStage.PADA_FORMED))
        val padaFormation = padaFormationEngine.derive(anga.final.copy(stage = DerivationStage.PADA_FORMED))
        val finalOperation = if (padaFormation.final.terms.any { it.upadesha == "थुक्" }) {
            thukPhonology.derive(padaFormation.final.copy(stage = DerivationStage.PADA_FORMED))
        } else {
            DerivationResult(padaFormation.final, padaFormation.final, emptyList(), emptyList())
        }
        val applications = taddhita.applications + anga.applications + padaFormation.applications + finalOperation.applications
        val events = taddhita.events.filterNot { it is DerivationEvent.Completed } +
            anga.events.filterNot { it is DerivationEvent.Completed } +
            padaFormation.events.filterNot { it is DerivationEvent.Completed } +
            finalOperation.events.filterNot { it is DerivationEvent.Completed }
        return DerivationResult(
            initial = initial,
            final = finalOperation.final,
            applications = applications,
            events = events + DerivationEvent.Completed(finalOperation.final, applications.size),
        )
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
