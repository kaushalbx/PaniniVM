package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada

/** Shared AST-first argument ordering for interpreted and compiled prakriyā calls. */
object PrakriyaInvocationArgumentResolver {
    fun resolve(invocation: PrakriyaInvocation,
        knownOperationStems: Set<String> = emptySet()): PrakriyaArgumentResolution {
        if (invocation.arguments.any { it.actionResult?.orderingAgrees == false }) {
            return failure(invocation, "RESULT_ORDERING_AGREEMENT",
                "The ordering qualifier and फल must agree in case and number.")
        }
        val signature = invocation.kriya.signature
        val expectedParameters = try { signature.parameters.ifEmpty {
            PrakriyaSignatureCompiler.positionalParameters(invocation.kriya.body, signature.argumentType,
                knownOperationStems)
        } } catch (failure: IllegalArgumentException) {
            return failure(invocation, "INVALID_PROCEDURE_SIGNATURE",
                failure.message ?: "Invalid procedure parameter morphology.")
        }
        fun checked(result: PrakriyaArgumentResolution.Success): PrakriyaArgumentResolution =
            if (expectedParameters.isNotEmpty() && result.arguments.size != expectedParameters.size) {
                failure(invocation, "PROCEDURE_ARITY_MISMATCH", PrakriyaDiagnostics.arity(
                    invocation.kriya.nameStem, expectedParameters.size, result.arguments.size))
            } else result
        // Attach compatibility payloads before grammatical ordering changes source positions.
        val sourceArguments = invocation.arguments.mapIndexed { index, argument ->
            val fallback = invocation.argumentValues.getOrNull(index)
            if (argument.value == null && fallback != null) argument.copy(value = fallback) else argument
        }
        val syntax = invocation.argumentSyntax.filterIsInstance<SubantaPada>()
        val hasNamedSyntax = syntax.any {
            it.sup.supports(Vibhakti.SAPTAMI) || it.sup.supports(Vibhakti.SASTHI)
        }
        val resolution = NamedPrakriyaArgumentResolver.resolve(syntax, signature)
        if (resolution !is PrakriyaArgumentResolution.Success) return resolution
        if (!hasNamedSyntax && sourceArguments.isNotEmpty()) {
            return checked(PrakriyaArgumentResolution.Success(
                sourceArguments.mapIndexed { index, argument ->
                    ResolvedPrakriyaArgument(
                        parameter = signature.parameters.getOrNull(index),
                        argument = argument,
                        bindingKind = if (argument.origin == PrakriyaArgumentOrigin.PIPE) {
                            PrakriyaArgumentBindingKind.PIPE
                        } else {
                            PrakriyaArgumentBindingKind.POSITIONAL
                        },
                    )
                },
                named = false,
            ))
        }
        val remaining = sourceArguments.toMutableList()
        val resolved = resolution.arguments.mapIndexed { index, grammatical ->
            val term = grammatical.argument.term
            val sourceIndex = remaining.indexOfFirst { source ->
                val grammaticalPada = grammatical.argument.pada
                grammaticalPada != null && source.pada === grammaticalPada
            }.takeIf { it >= 0 } ?: remaining.indexOfFirst { it.term == term }
            val source = if (sourceIndex >= 0) remaining.removeAt(sourceIndex) else {
                grammatical.argument
            }
            ResolvedPrakriyaArgument(
                parameter = grammatical.parameter ?: signature.parameters.getOrNull(index),
                argument = source,
                bindingKind = when {
                    source.origin == PrakriyaArgumentOrigin.PIPE -> PrakriyaArgumentBindingKind.PIPE
                    resolution.named -> PrakriyaArgumentBindingKind.NAMED
                    else -> PrakriyaArgumentBindingKind.POSITIONAL
                },
            )
        }
        // Named ordering must consume every supplied operand, including piped values.
        // Otherwise an extra operand could disappear before the arity check.
        if (remaining.isNotEmpty()) return failure(
            invocation,
            "PROCEDURE_ARITY_MISMATCH",
            PrakriyaDiagnostics.arity(invocation.kriya.nameStem, expectedParameters.size,
                resolved.size + remaining.size),
        )
        return checked(PrakriyaArgumentResolution.Success(resolved, resolution.named))
    }

    private fun failure(
        invocation: PrakriyaInvocation,
        code: String,
        message: String,
    ): PrakriyaArgumentResolution.Failure {
        val diagnostic = PrakriyaCallDiagnostics.atInvocation(invocation, code, message)
        return PrakriyaArgumentResolution.Failure(PrakriyaArgumentDiagnostic(
            diagnostic.code, diagnostic.message, diagnostic.span, diagnostic.offendingText))
    }
}
