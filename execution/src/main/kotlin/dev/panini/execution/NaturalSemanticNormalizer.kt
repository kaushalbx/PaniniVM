package dev.panini.execution

import dev.panini.vyakaranam.ast.morphologicalKey

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

    }

    fun normalize(invocation: Invocation): Operation? =
        normalizeReportedOutcomeTest(invocation)
            ?: normalizeTruthTest(invocation)
            ?: normalizeRangeChoice(invocation)
            ?: normalizeImplicitDisplay(invocation)

    /** True when this nominal is the explicit anaphor for the preceding result. */
    fun isPriorResult(pada: SubantaPada): Boolean =
        dev.panini.execution.binding.PhalaReference.isReference(pada)

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
        val subject = simpleCopularSubject(invocation) ?: return null
        val nominal = subject.pratipadika as? MulaPratipadika
        if (nominal?.lexicalIdentity != MulaPratipadikaIdentity.VIJAYA || nominal.vikaras.isNotEmpty()) {
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
        // Both अस् (exist/be) and भू (become/be) form natural existential
        // predicates in existing PVM sources. Their tense/aspect distinction does
        // not change the truth-state lookup performed by a condition.
        val subject = simpleCopularSubject(invocation) ?: return null
        return Operation.TruthTest(
            stateName = (subject.pratipadika as? MulaPratipadika)
                ?.takeIf { it.vikaras.isNotEmpty() }?.morphologicalKey()
                ?: subject.referenceName(),
            negated = padas.filterIsInstance<AvyayaPada>().any {
                it.function == AvyayaFunction.NISHEDHA
            },
        )
    }

    /** A state lookup cannot stand in for a larger or derived predicate. */
    private fun simpleCopularSubject(invocation: Invocation): SubantaPada? {
        val padas = invocation.vakya.padas
        val head = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (head.canonicalDhatuIdentity() !in COPULAR_DHATUS || head.upasargas.isNotEmpty() ||
            head.dhatu.sanadiPratyayas.isNotEmpty() || head.priorAction != null) return null
        val subject = padas.filterIsInstance<SubantaPada>().singleOrNull() ?: return null
        if (!subject.sup.supports(Vibhakti.PRATHAMA)) return null
        val remaining = padas.filter { it !== head && it !== subject }
        if (remaining.isNotEmpty()) {
            val negation = remaining.singleOrNull() as? AvyayaPada ?: return null
            if (negation.form != "न" || negation.derivation != null) return null
        }
        return subject
    }

    private fun normalizeRangeChoice(invocation: Invocation): Operation.RangeChoice? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.CHI) return null

        val hasExclusionAbsolutive = padas.filterIsInstance<AvyayaPada>().any { avyaya ->
            val derivation = avyaya.derivation as? AvyayaKridantaDerivation
            derivation?.krtAffix == KrtAffix.KTVA &&
                derivation.dhatu.canonicalDhatuIdentity() == CanonicalDhatuIdentity.VRJ
        }
        val exclusion = if (hasExclusionAbsolutive) {
            padas.filterIsInstance<SubantaPada>().firstOrNull {
                it.sup.supports(Vibhakti.DVITIYA)
            }
        } else null
        return Operation.RangeChoice(exclusion?.referenceName(), hasExclusionAbsolutive)
    }

    private fun normalizeImplicitDisplay(invocation: Invocation): Operation.DisplayPriorResult? {
        val padas = invocation.vakya.padas
        val tinganta = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.MUDR) return null
        if (padas.filterIsInstance<SubantaPada>().any(::isPriorResult)) return null
        if (padas.any { it !is TingantaPada && it !is AvyayaPada }) return null
        return Operation.DisplayPriorResult
    }

    private fun SubantaPada.referenceName(): String =
        pratipadika.referenceKey()

    private val COPULAR_DHATUS = setOf(
        CanonicalDhatuIdentity.AS,
        CanonicalDhatuIdentity.BHU,
    )
}
