package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada

/** Shared AST-first argument ordering for interpreted and compiled procedure calls. */
object SamjnaInvocationArgumentResolver {
    fun resolve(invocation: SamjnaInvocation): SamjnaArgumentResolution {
        val signature = invocation.kriya.signature
        val syntax = invocation.argumentSyntax.filterIsInstance<SubantaPada>()
        val hasNamedSyntax = syntax.any {
            SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.SASTHI
        }
        return when {
            hasNamedSyntax -> NamedSamjnaArgumentResolver.resolve(syntax, signature)
            invocation.arguments.isNotEmpty() ->
                SamjnaArgumentResolution.Success(invocation.arguments.map(ProcedureArgument::term), false)
            else -> NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
        }
    }
}
