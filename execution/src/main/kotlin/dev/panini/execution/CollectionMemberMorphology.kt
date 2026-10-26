package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Upasarga
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Preserves and validates substantival member selectors before binding loses morphology. */
object CollectionMemberMorphology {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object Valid : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidMember(val code: String, message: String) : IllegalArgumentException(message)

    private fun requireMember(value: Boolean, code: String, message: () -> String) {
        if (!value) throw InvalidMember(code, message())
    }

    private fun sup(pada: Pada): SupPratyaya? = when (pada) {
        is SubantaPada -> pada.sup
        is SankhyaPuranaPada -> pada.sup
        is SankhyaPada -> pada.sup
        else -> null
    }

    private fun hasSlot(pada: Pada, slot: SupAffix): Boolean =
        sup(pada)?.affixCandidates == listOf(slot)

    fun ordinalExpression(pada: Pada): ExecutionExpression.Pada? {
        val ordinal = PuranaPratyayaResolver.semanticValue(pada) ?: return null
        sup(pada) ?: return null
        requireMember(hasSlot(pada, SupAffix.AM), "COLLECTION_ORDINAL_SELECTOR_AGREEMENT") {
            "Ordinal-member extraction requires a positive singular accusative selector."
        }
        return ExecutionExpression.Pada(pada.sourceText,
            value = ordinal,
            memberSelection = CollectionMemberSelection.ORDINAL)
    }

    /** Preserve member-role morphology before procedure parameters rewrite nouns. */
    fun protectedPadas(padas: List<Pada>): List<Pada> {
        val verb = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return emptyList()
        val projected = (PriorActionLowering.analyze(Invocation(AkhyataVakya(
            padas.joinToString(" ") { it.sourceText }, padas, verb)))
            as? PriorActionLowering.Analysis.Resolved)?.sequence
        if (projected != null) return projected.statements.filterIsInstance<Invocation>()
            .flatMap { protectedPadas(it.vakya.padas) }
        val nominals = padas.filterIsInstance<SubantaPada>()
        if (nominals.none { hasSlot(it, SupAffix.NGAS) }) return emptyList()
        if (verb.canonicalDhatuIdentity() == CanonicalDhatuIdentity.HR && Upasarga.UD in verb.upasargaIdentities) {
            return padas.filter { pada ->
                !hasSlot(pada, SupAffix.NGAS) && (PuranaPratyayaResolver.ordinalValue(pada) != null ||
                    ((pada as? SubantaPada)?.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA)
            }
        }
        val identity = when (verb.canonicalDhatuIdentity()) {
            CanonicalDhatuIdentity.HR -> if (Upasarga.UD in verb.upasargaIdentities) MulaPratipadikaIdentity.ANTIMA else null
            CanonicalDhatuIdentity.YUJ -> MulaPratipadikaIdentity.SANKHYA
            else -> null
        } ?: return emptyList()
        return nominals.filter {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == identity &&
                !hasSlot(it, SupAffix.NGAS) &&
                (identity != MulaPratipadikaIdentity.SANKHYA ||
                    hasSlot(it, SupAffix.AUT) || hasSlot(it, SupAffix.SAS))
        }
    }

    fun analyze(node: Invocation): Analysis = try {
        validateInternal(node)
        Analysis.Valid
    } catch (error: InvalidMember) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), node.relationSpan(), node.sourceText))
    }

    fun validate(node: Invocation) {
        val analysis = analyze(node)
        if (analysis is Analysis.Invalid) throw PvmSemanticException(
            ExecutionDiagnostic.from(analysis.diagnostic),
        )
    }

    private fun validateInternal(node: Invocation) {
        val sentence = node.vakya as? AkhyataVakya ?: return
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.HR ||
            Upasarga.UD !in sentence.tinganta.upasargaIdentities) return
        val nominals = sentence.padas.filterIsInstance<SubantaPada>()
        if (nominals.none { hasSlot(it, SupAffix.NGAS) }) return
        requireMember(nominals.none { pada ->
            val nominal = (pada.pratipadika as? MulaPratipadika)
            !hasSlot(pada, SupAffix.NGAS) &&
                nominal?.lexicalIdentity?.ordinalValue != null &&
                nominal.vikaras.any { !it.isRecognizedStriAffix }
        }, "COLLECTION_DERIVED_ORDINAL_SELECTOR") {
            "A further-derived ordinal is not an unchanged collection member selector."
        }
        val ordinals = sentence.padas.filter {
            !hasSlot(it, SupAffix.NGAS) && PuranaPratyayaResolver.ordinalValue(it) != null
        }
        if (ordinals.isNotEmpty()) {
            requireMember(ordinals.size == 1 && nominals.none {
                !hasSlot(it, SupAffix.NGAS) &&
                    (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA
            }, "COLLECTION_MEMBER_SELECTOR_CARDINALITY") {
                "Collection extraction requires one unambiguous member selector."
            }
            ordinalExpression(ordinals.single())
        }
        val selectors = nominals.filter {
            !hasSlot(it, SupAffix.NGAS) &&
                (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA
        }
        if (selectors.isEmpty()) return
        requireMember(selectors.size == 1, "COLLECTION_FINAL_SELECTOR_CARDINALITY") {
            "Final-member extraction requires one unambiguous selector."
        }
        val selector = selectors.single()
        requireMember(hasSlot(selector, SupAffix.AM), "COLLECTION_FINAL_SELECTOR_AGREEMENT") {
            "Final-member extraction requires a singular accusative object."
        }
        requireMember((selector.pratipadika as MulaPratipadika).vikaras.isEmpty(),
            "COLLECTION_DERIVED_FINAL_SELECTOR") {
            "Derived final-member nouns require their own semantics, not plain अन्तिम selection."
        }
    }
}
