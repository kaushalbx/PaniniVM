package dev.panini.sankhya

import dev.panini.ashtadhyayi.adhyaya5.pada2.DvesTiyahSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.NantadAsankhyaderMatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.NityamShatadiSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.ShashtyadeshCasankhyadehSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.ShatKatiKatipayaChaturamThukSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TasyaPuraneDatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TresSamprasaranamCaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.VimshatyadibhyasTamadAnyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.TehSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.TiVimshaterDitiSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.NaloPratipadikantasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.StunaShtuhSutra
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
import java.math.BigInteger

/** Derives the currently implemented pūraṇa numerals through A.5.2.48–56. */
class PuranaSankhyaDeriver(
    private val cardinalDeriver: CardinalSankhyaDeriver,
) {
    private val expressionBuilder = SankhyaExpressionBuilder()
    private val taddhitaEngine = DerivationEngine(
        listOf(
            ShatKatiKatipayaChaturamThukSutra,
            DvesTiyahSutra,
            TresSamprasaranamCaSutra,
            NantadAsankhyaderMatSutra,
            TasyaPuraneDatSutra,
            VimshatyadibhyasTamadAnyatarasyamSutra,
            NityamShatadiSutra,
            ShashtyadeshCasankhyadehSutra,
        )
    )
    private val angaEngine = DerivationEngine(
        listOf(
            TiVimshaterDitiSutra,
            TehSutra,
            NaloPratipadikantasyaSutra,
        )
    )
    private val thukPhonology = DerivationEngine(listOf(StunaShtuhSutra))

    fun derive(value: BigInteger): DerivationResult {
        val initial = initialState(value)
        val taddhita = taddhitaEngine.derive(initial, DerivationConfig(OptionalRulePolicy.SKIP_ALL))
        return complete(initial, taddhita)
    }

    fun deriveVariants(value: BigInteger): List<DerivationResult> {
        requireSupported(value)
        val initial = initialState(value)
        return taddhitaEngine.deriveAll(initial)
            .map { complete(initial, it) }
            .distinctBy { it.final.surface to it.applications.map(DerivationApplication::sutra) }
    }

    private fun complete(initial: DerivationState, taddhita: DerivationResult): DerivationResult {
        val anga = angaEngine.derive(taddhita.final.copy(stage = DerivationStage.PADA_FORMED))
        val finalOperation = if (anga.final.terms.any { it.upadesha == "थुक्" }) {
            thukPhonology.derive(anga.final.copy(stage = DerivationStage.PADA_FORMED))
        } else {
            DerivationResult(anga.final, anga.final, emptyList(), emptyList())
        }
        val applications = taddhita.applications + anga.applications + finalOperation.applications
        val events = taddhita.events.filterNot { it is DerivationEvent.Completed } +
            anga.events.filterNot { it is DerivationEvent.Completed } +
            finalOperation.events.filterNot { it is DerivationEvent.Completed }
        return DerivationResult(
            initial = initial,
            final = finalOperation.final,
            applications = applications,
            events = events + DerivationEvent.Completed(finalOperation.final, applications.size),
        )
    }

    private fun initialState(value: BigInteger): DerivationState {
        requireSupported(value)

        val base = if (value == BigInteger.ONE) "प्रथम" else PrimitiveSankhya.fromValue(value)?.pratipadika
            ?: cardinalDeriver.derive(value).final.surface
        val underlyingHead = if (value == BigInteger.ONE) base else expressionBuilder.build(value).headPrimitive().pratipadika
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
            stage = DerivationStage.PADA_FORMED,
        )
        // प्रथम is lexical; the rule engine must preserve it without fabricating a sūtra application.
        return initial
    }

    private fun requireSupported(value: BigInteger) {
        require(value.signum() > 0) { "Pūraṇa numerals require a positive cardinal: $value" }
    }

}
