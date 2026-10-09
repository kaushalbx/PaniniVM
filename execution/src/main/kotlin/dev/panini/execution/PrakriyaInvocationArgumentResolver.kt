package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada

/** Shared AST-first argument ordering for interpreted and compiled prakriyā calls. */
object PrakriyaInvocationArgumentResolver {
    fun resolve(invocation: PrakriyaInvocation): PrakriyaArgumentResolution {
        if (invocation.arguments.any { it.actionResult?.orderingAgrees == false }) {
            return PrakriyaArgumentResolution.Failure("The ordering qualifier and फल must agree in case and number.")
        }
        val signature = invocation.kriya.signature
        val syntax = invocation.argumentSyntax.filterIsInstance<SubantaPada>()
        val hasNamedSyntax = syntax.any {
            SupAffix.fromUpadesha(it.sup.text)?.vibhakti in setOf(Vibhakti.SAPTAMI, Vibhakti.SASTHI)
        }
        if (!hasNamedSyntax && invocation.arguments.isNotEmpty()) {
            return PrakriyaArgumentResolution.Success(
                invocation.arguments.mapIndexed { index, argument ->
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
            )
        }
        val resolution = NamedPrakriyaArgumentResolver.resolve(syntax, signature)
        if (resolution !is PrakriyaArgumentResolution.Success) return resolution

        val remaining = invocation.arguments.toMutableList()
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
        return PrakriyaArgumentResolution.Success(resolved, resolution.named)
    }
}
