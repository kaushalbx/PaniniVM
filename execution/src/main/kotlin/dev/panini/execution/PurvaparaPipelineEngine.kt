package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline

/**
 * Pāṇinian Compound Kriyā Pipeline Engine based on Sūtra 6.1.84 (एकः पूर्वपरयोः).
 *
 * Composites multiple Saṃjñā method operations where the output of the preceding stage (पूर्व)
 * feeds into the succeeding stage (पर) to produce a single combined substitute (एकः पूर्वपरयोः).
 */
object PurvaparaPipelineEngine {
    fun executePipeline(
        pipeline: Pipeline,
        vm: PaniniVM,
        sessionKey: String,
        scope: ExecutionScope,
        speaker: String,
        listener: String,
        registry: SamjnaKriyaRegistry,
        callerSourceFile: String? = null,
    ): List<ExecutionResult> {
        if (pipeline.stages.size < 2) {
            return listOf(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "पूर्वपर-असंगतिः: Expected at least 2 chained Kriyā stages in '6.1.84 एकः पूर्वपरयोः'",
                ),
            )
        }

        var currentArguments = pipeline.arguments
        var currentValues: List<SanskritValue?> = List(currentArguments.size) { null }
        var lastSuccess: ExecutionResult.Success? = null

        for (stage in pipeline.stages) {
            val invocation = registry.resolveStructuredInvocation(
                operationStem = stage.operationStem,
                domainStem = stage.domainStem,
                argumentTerms = currentArguments,
                sourceText = pipeline.sourceText,
                callerSourceFile = callerSourceFile,
                argumentValues = currentValues,
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
                return stageResults.takeIf { it.isNotEmpty() } ?: listOf(
                    ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "पूर्वपर-असंगतिः: Execution failed at Kriyā stage '${stage.operationStem}'",
                    ),
                )
            }

            lastSuccess = stageSuccess
            val stageVal = stageSuccess.value
            val nextArgs = mutableListOf(stageVal)
            if (pipeline.arguments.size > 1) {
                nextArgs.addAll(pipeline.arguments.drop(1))
            }
            currentArguments = nextArgs
            currentValues = listOf(stageSuccess.typedValue) +
                List((pipeline.arguments.size - 1).coerceAtLeast(0)) { null }
        }

        return listOf(
            lastSuccess ?: ExecutionResult.Success(
                operation = "panini.purvapara_pipeline",
                value = "संसिद्धम्",
            ),
        )
    }
}
