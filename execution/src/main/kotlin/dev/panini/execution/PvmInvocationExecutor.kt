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
        val persistSession: Boolean,
        val injectedKarman: InjectedKarmanBinding?,
        val onResult: ((ExecutionResult) -> Unit)?,
        val executePrakriya: (PrakriyaInvocation) -> List<ExecutionResult>,
    )

    fun execute(request: Request): List<ExecutionResult> {
        // An explicit फल already names the preceding result. It must not also
        // arrive as an injected operand, including after a conditional branch.
        val explicitResult = request.node.vakya.padas.any { pada ->
            when (pada) {
                is dev.panini.vyakaranam.ast.SubantaPada -> NaturalSemanticNormalizer.isPriorResult(pada)
                is dev.panini.vyakaranam.ast.SamuccitaSubanta -> pada.members.any(NaturalSemanticNormalizer::isPriorResult)
                else -> false
            }
        }
        val injectedKarman = request.injectedKarman.takeUnless { explicitResult }
        val text = request.node.vakya.sourceText.trim().trimEnd('।', '॥').trim() + " ।"
        val parsedUkti = Ukti(sourceText = text, body = request.node)
        val invocation = request.registry.detectInvocation(
            parsedUkti,
            callerSourceFile = request.sourceFile,
            injectedKarman = injectedKarman,
            resolveActionResult = { reference ->
                val memory = vm.kriyaMemory(request.sessionKey)
                if (reference.ordinalFromOldest != null) {
                    memory.ordinalKriya(reference.ordinalFromOldest, reference.dhatuUpadesha)?.phala
                } else memory.latestKriya(reference.dhatuUpadesha, if (reference.previous) 1 else 0)?.phala
            },
        )
        if (invocation != null) {
            if (invocation.arguments.any { it.actionResult?.orderingAgrees == false }) return listOf(
                ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "The ordering qualifier and फल must agree in case and number."),
            )
            val memory = vm.kriyaMemory(request.sessionKey)
            val arguments = invocation.arguments.map { argument ->
                val reference = argument.actionResult ?: return@map argument
                val result = if (reference.ordinalFromOldest != null) {
                    memory.ordinalKriya(reference.ordinalFromOldest, reference.dhatuUpadesha)
                } else memory.latestKriya(reference.dhatuUpadesha, if (reference.previous) 1 else 0)
                val value = result?.phala ?: return listOf(ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE, "No completed named action result is available for this procedure argument.",
                ))
                argument.copy(value = value)
            }
            return request.executePrakriya(invocation.copy(arguments = arguments, argumentValues = arguments.map { it.value }))
        }

        return listOf(
            vm.evalParsed(
                parsedUkti,
                request.sessionKey,
                request.scope,
                request.speaker,
                request.listener,
                evaluateCondition = request.conditionEvaluation,
                persistSession = request.persistSession,
                injectedBindings = injectedKarman?.let {
                    mapOf(Karaka.KARMAN to ExecutionExpression.Reference(it.reference))
                }.orEmpty(),
            ),
        ).also { produced -> produced.forEach { request.onResult?.invoke(it) } }
    }
}
