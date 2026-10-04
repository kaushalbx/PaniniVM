package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.execution.binding.PhalaReference

sealed interface PrakriyaArgumentResolution {
    data class Success(
        val arguments: List<ResolvedPrakriyaArgument>,
        val named: Boolean,
    ) : PrakriyaArgumentResolution {
        val terms: List<String> get() = arguments.map { it.argument.term }

        companion object {
            fun fromPadas(padas: List<SubantaPada>): Success = Success(
                padas.map { pada ->
                    ResolvedPrakriyaArgument(
                        parameter = null,
                        argument = PrakriyaArgument(
                            term = pada.pratipadika.semanticKey(),
                            pada = pada,
                            origin = PrakriyaArgumentOrigin.WRITTEN,
                        ),
                        bindingKind = PrakriyaArgumentBindingKind.POSITIONAL,
                    )
                },
                named = false,
            )
        }
    }
    data class Failure(val message: String) : PrakriyaArgumentResolution
}

enum class PrakriyaArgumentBindingKind { POSITIONAL, NAMED, PIPE }

/** A call operand after grammatical resolution, retaining its declared role and source AST/value. */
data class ResolvedPrakriyaArgument(
    val parameter: PrakriyaParameter?,
    val argument: PrakriyaArgument,
    val bindingKind: PrakriyaArgumentBindingKind,
) {
    /** Whether this operand is the grammatical discourse anaphor for the preceding result. */
    val isPriorResult: Boolean
        get() = when {
            argument.origin == PrakriyaArgumentOrigin.PIPE -> true
            argument.pada is SubantaPada -> NaturalSemanticNormalizer.isPriorResult(argument.pada)
            else -> PrakriyaInvocationMatcher.normalizeIdentity(argument.term) == PhalaReference.KEY
        }

    /** Canonical referent name, derived from the parsed nominal whenever available. */
    val referenceName: String
        get() = (argument.pada as? SubantaPada)
            ?.pratipadika
            ?.semanticKey()
            ?: PrakriyaInvocationMatcher.normalizeIdentity(argument.term)
}

/** Binds natural सप्तमी parameter slots (and legacy षष्ठी labels) to following द्वितीया values. */
object NamedPrakriyaArgumentResolver {
    private data class NamedValue(val name: String, val pada: SubantaPada)

    /** Resolves argument order from the canonical AST without rendering and reparsing it. */
    fun resolve(padas: List<SubantaPada>, signature: PrakriyaSignature): PrakriyaArgumentResolution {
        val positionalPadas = padas.filter { it.vibhakti() == Vibhakti.DVITIYA }
        if (signature.parameters.isEmpty()) {
            return PrakriyaArgumentResolution.Success.fromPadas(positionalPadas)
        }
        return resolve(padas, signature, positionalPadas)
    }

    private fun resolve(
        padas: List<SubantaPada>,
        signature: PrakriyaSignature,
        positionalPadas: List<SubantaPada>,
    ): PrakriyaArgumentResolution {
        if (signature.parameters.isEmpty()) return PrakriyaArgumentResolution.Success.fromPadas(positionalPadas)
        val pairs = padas.mapIndexedNotNull { index, pada ->
            if (pada.vibhakti() !in setOf(Vibhakti.SAPTAMI, Vibhakti.SASTHI)) return@mapIndexedNotNull null
            val value = padas.getOrNull(index + 1)?.takeIf { it.vibhakti() == Vibhakti.DVITIYA }
                ?: return PrakriyaArgumentResolution.Failure(
                    "कारकदोषः: '${pada.stem()}' इति मानस्थानस्य अनन्तरं द्वितीयान्तं मूल्यम् अपेक्षितम्।",
                )
            NamedValue(pada.stem(), value)
        }
        if (pairs.isEmpty()) return PrakriyaArgumentResolution.Success.fromPadas(positionalPadas)
        if (pairs.size != positionalPadas.size) {
            return PrakriyaArgumentResolution.Failure(
                "मानदोषः: स्थाननिर्दिष्टानि क्रमानुसाराणि च मानानि एकत्र न योजनीयानि।",
            )
        }
        val duplicates = pairs.groupBy(NamedValue::name).filterValues { it.size > 1 }.keys
        if (duplicates.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("पुनरुक्तमानानि: $duplicates।")
        }
        val supplied = pairs.associate { it.name to it.pada }
        val expected = signature.parameters.map(PrakriyaParameter::nameStem)
        val unknown = supplied.keys - expected.toSet()
        if (unknown.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("अज्ञातमानानि: $unknown।")
        }
        val missing = expected.filterNot(supplied::containsKey)
        if (missing.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("लुप्तमानानि: $missing।")
        }
        return PrakriyaArgumentResolution.Success(
            signature.parameters.map { parameter ->
                val value = supplied.getValue(parameter.nameStem)
                ResolvedPrakriyaArgument(
                    parameter = parameter,
                    argument = PrakriyaArgument(
                        term = value.pratipadika.semanticKey(),
                        pada = value,
                        origin = PrakriyaArgumentOrigin.WRITTEN,
                    ),
                    bindingKind = PrakriyaArgumentBindingKind.NAMED,
                )
            },
            named = true,
        )
    }

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

    private fun SubantaPada.stem(): String =
        pratipadika.semanticKey()
}
