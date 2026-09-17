package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada

/** Shared AST-first argument ordering for interpreted and compiled prakriyā calls. */
object PrakriyaInvocationArgumentResolver {
    fun resolve(invocation: PrakriyaInvocation): PrakriyaArgumentResolution {
        val signature = invocation.kriya.signature
        val syntax = invocation.argumentSyntax.filterIsInstance<SubantaPada>()
        val hasNamedSyntax = syntax.any {
            SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.SASTHI
        }
        return when {
            hasNamedSyntax -> NamedPrakriyaArgumentResolver.resolve(syntax, signature)
            invocation.arguments.isNotEmpty() ->
                PrakriyaArgumentResolution.Success(invocation.arguments.map(PrakriyaArgument::term), false)
            else -> NamedPrakriyaArgumentResolver.resolve(invocation.karmaText, signature)
        }
    }
}
