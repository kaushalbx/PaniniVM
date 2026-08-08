package dev.panini.execution

import dev.panini.vyakaranam.ast.Ukti

/**
 * Pāṇinian Compound Kriyā Pipeline Engine based on Sūtra 6.1.84 (एकः पूर्वपरयोः).
 *
 * Composites multiple Saṃjñā method operations where the output of the preceding stage (पूर्व)
 * feeds into the succeeding stage (पर) to produce a single combined substitute (एकः पूर्वपरयोः).
 */
object PurvaparaPipelineEngine {

    fun isPipelineDirective(sentenceText: String, preParsedUkti: Ukti? = null): Boolean {
        return PurvaparaPipelineCompiler.compile(sentenceText) != null
    }

    fun executePipeline(
        sentenceText: String,
        vm: PaniniVM,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
    ): List<ExecutionResult> {
        val plan = PurvaparaPipelineCompiler.compile(sentenceText)
        if (plan == null || plan.stages.size < 2) {
            return listOf(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "पूर्वपर-असंगतिः: Expected at least 2 chained Kriyā stages in '6.1.84 एकः पूर्वपरयोः'",
                ),
            )
        }

        var currentArguments = plan.arguments
        var lastSuccess: ExecutionResult.Success? = null

        for (stage in plan.stages) {
            val invocation = registry.resolveStructuredInvocation(
                operationStem = stage.operationStem,
                domainStem = stage.domainStem,
                argumentTerms = currentArguments,
                sourceText = sentenceText,
                callerSourceFile = callerSourceFile,
            )
            if (invocation == null) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "पूर्वपर-असंगतिः: Could not resolve Kriyā stage '${stage.operationStem}' in pipeline",
                    ),
                )
            }

            val stageResults = vm.executeSamjnaInvocation(
                invocation, sessionKey, scope, speaker, listener, registry, callerSourceFile = callerSourceFile,
            )
            val stageSuccess = stageResults.filterIsInstance<ExecutionResult.Success>().lastOrNull()

            if (stageSuccess == null) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "पूर्वपर-असंगतिः: Execution failed at Kriyā stage '${stage.operationStem}'",
                    ),
                )
            }

            lastSuccess = stageSuccess
            val stageVal = stageSuccess.value
            val nextArgs = mutableListOf(stageVal)
            if (plan.arguments.size > 1) {
                nextArgs.addAll(plan.arguments.drop(1))
            }
            currentArguments = nextArgs
        }

        return listOf(
            lastSuccess ?: ExecutionResult.Success(
                operation = "panini.purvapara_pipeline",
                value = "संसिद्धम्",
            ),
        )
    }
}
