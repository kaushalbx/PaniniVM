package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Ukti

/** Executes one invocation node or delegates a named reusable prakriyā call. */
internal class PvmInvocationExecutor(private val vm: PaniniVM) {
    data class Request(
        val node: Invocation,
        val sessionKey: String,
        val scope: ExecutionScope,
        val speaker: String,
        val listener: String,
        val registry: PrakriyaRegistry,
        val sourceFile: String?,
        val conditionEvaluation: Boolean,
        val injectedKarman: InjectedKarmanBinding?,
        val onResult: ((ExecutionResult) -> Unit)?,
        val executePrakriya: (PrakriyaInvocation) -> List<ExecutionResult>,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val text = request.node.vakya.sourceText.trim().trimEnd('।', '॥').trim() + " ।"
        val parsedUkti = Ukti(sourceText = text, body = request.node)
        val invocation = request.registry.detectInvocation(
            parsedUkti,
            callerSourceFile = request.sourceFile,
            injectedKarman = request.injectedKarman,
        )
        if (invocation != null) return request.executePrakriya(invocation)

        return listOf(
            vm.evalParsed(
                parsedUkti,
                request.sessionKey,
                request.scope,
                request.speaker,
                request.listener,
                evaluateCondition = request.conditionEvaluation,
                injectedBindings = request.injectedKarman?.let {
                    mapOf(Karaka.KARMAN to ExecutionExpression.Reference(it.reference))
                }.orEmpty(),
            ),
        ).also { produced -> produced.forEach { request.onResult?.invoke(it) } }
    }
}
