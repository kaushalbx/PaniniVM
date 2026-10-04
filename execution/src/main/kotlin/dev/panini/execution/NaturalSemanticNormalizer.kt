package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.AvyayaKridantaDerivation
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.SankhyaBoundaryPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.execution.binding.referenceKey
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity

/**
 * Backend-independent semantic normalization for natural PVM constructions.
 *
 * This layer deliberately consumes typed morphology rather than rendered or raw
 * source strings. Interpreter planning and compiler lowering can therefore share
 * one definition of constructions whose meaning spans more than one pada.
 */
object NaturalSemanticNormalizer {
    sealed interface Operation {
        data class TruthTest(val stateName: String, val negated: Boolean) : Operation

        /** Tests whether the preceding reported outcome is the named result. */
        data class ReportedOutcomeTest(val outcomeName: String, val negated: Boolean) : Operation

        data class RangeChoice(
            val exclusionName: String?,
            val usesExclusionAbsolutive: Boolean,
        ) : Operation

        /** An otherwise argumentless display verb consumes the discourse result. */
        data object DisplayPriorResult : Operation

        data class ProcedureParameterArithmetic(
            val kind: ArithmeticKind,
            val operands: List<ProcedureOperand>,
        ) : Operation

        /** Adds all members supplied through a declared समवाय collection parameter. */
        data object CollectionParameterSum : Operation
    }

    enum class ArithmeticKind { ADD, MULTIPLY, DIVIDE }

    sealed interface ProcedureOperand {
        data class Parameter(val position: Long) : ProcedureOperand
        data object PriorResult : ProcedureOperand
    }

    fun normalize(invocation: Invocation): Operation? =
        normalizeReportedOutcomeTest(invocation)
            ?: normalizeTruthTest(invocation)
            ?: normalizeRangeChoice(invocation)
            ?: normalizeCollectionParameterSum(invocation)
            ?: normalizeProcedureParameterArithmetic(invocation)
            ?: normalizeImplicitDisplay(invocation)

    /** True when this nominal is the explicit anaphor for the preceding result. */
    fun isPriorResult(pada: SubantaPada): Boolean =
        (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.PHALA

    /** Numeric meaning shared by cardinal and ordinal पर्यन्त boundaries. */
    fun boundaryValue(boundary: SankhyaBoundaryPada): Long? =
        boundary.value ?: when (boundary) {
            is SankhyaPada -> runCatching {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(boundary.stems).value
            }.getOrNull()
            is SankhyaPuranaPada -> PuranaPratyayaResolver.ordinalValue(boundary)
        }

    private fun normalizeReportedOutcomeTest(invocation: Invocation): Operation.ReportedOutcomeTest? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() !in COPULAR_DHATUS) return null
        val subject = padas.filterIsInstance<SubantaPada>().singleOrNull() ?: return null
        if (SupAffix.fromUpadesha(subject.sup.text)?.vibhakti != Vibhakti.PRATHAMA) return null
        if ((subject.pratipadika as? MulaPratipadika)?.lexicalIdentity != MulaPratipadikaIdentity.VIJAYA) {
            return null
        }
        return Operation.ReportedOutcomeTest(
            outcomeName = subject.referenceName(),
            negated = padas.filterIsInstance<AvyayaPada>().any {
                it.function == AvyayaFunction.NISHEDHA
            },
        )
    }

    private fun normalizeTruthTest(invocation: Invocation): Operation.TruthTest? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        // Both अस् (exist/be) and भू (become/be) form natural existential
        // predicates in existing PVM sources. Their tense/aspect distinction does
        // not change the truth-state lookup performed by a condition.
        if (tinganta.canonicalDhatuIdentity() !in COPULAR_DHATUS) return null
        val subject = padas.filterIsInstance<SubantaPada>().singleOrNull() ?: return null
        if (SupAffix.fromUpadesha(subject.sup.text)?.vibhakti != Vibhakti.PRATHAMA) return null
        return Operation.TruthTest(
            stateName = subject.referenceName(),
            negated = padas.filterIsInstance<AvyayaPada>().any {
                it.function == AvyayaFunction.NISHEDHA
            },
        )
    }

    private fun normalizeRangeChoice(invocation: Invocation): Operation.RangeChoice? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.CHI) return null

        val hasExclusionAbsolutive = padas.filterIsInstance<AvyayaPada>().any { avyaya ->
            val derivation = avyaya.derivation as? AvyayaKridantaDerivation
            derivation?.pratyaya?.let(KrtAffix::fromUpadesha) == KrtAffix.KTVA &&
                derivation.dhatu.canonicalDhatuIdentity() == CanonicalDhatuIdentity.VRJ
        }
        val exclusion = if (hasExclusionAbsolutive) {
            padas.filterIsInstance<SubantaPada>().firstOrNull {
                SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.DVITIYA
            }
        } else null
        return Operation.RangeChoice(exclusion?.referenceName(), hasExclusionAbsolutive)
    }

    private fun normalizeImplicitDisplay(invocation: Invocation): Operation.DisplayPriorResult? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.MUDR) return null
        if (padas.filterIsInstance<SubantaPada>().any(::isPriorResult)) return null
        if (padas.filterIsInstance<SubantaPada>().isNotEmpty()) return null
        return Operation.DisplayPriorResult
    }

    private fun normalizeProcedureParameterArithmetic(
        invocation: Invocation,
    ): Operation.ProcedureParameterArithmetic? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        val kind = when (tinganta.canonicalDhatuIdentity()) {
            CanonicalDhatuIdentity.YUJ -> ArithmeticKind.ADD
            CanonicalDhatuIdentity.GAN -> ArithmeticKind.MULTIPLY
            CanonicalDhatuIdentity.BHAJ -> ArithmeticKind.DIVIDE
            else -> return null
        }
        val operands = padas.mapNotNull { pada ->
            PuranaPratyayaResolver.ordinalValue(pada)?.let(ProcedureOperand::Parameter)
                ?: (pada as? SubantaPada)?.takeIf(::isPriorResult)?.let {
                    ProcedureOperand.PriorResult
                }
        }
        if (operands.size < 2 || operands.none { it is ProcedureOperand.Parameter }) return null
        return Operation.ProcedureParameterArithmetic(kind, operands)
    }

    private fun normalizeCollectionParameterSum(invocation: Invocation): Operation.CollectionParameterSum? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.YUJ) return null
        val hasCollectionParameter = padas.filterIsInstance<SubantaPada>().any { pada ->
            (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SAMAVAYA
        }
        return Operation.CollectionParameterSum.takeIf { hasCollectionParameter }
    }

    private fun SubantaPada.referenceName(): String =
        pratipadika.referenceKey()

    private val COPULAR_DHATUS = setOf(
        CanonicalDhatuIdentity.AS,
        CanonicalDhatuIdentity.BHU,
    )
}
