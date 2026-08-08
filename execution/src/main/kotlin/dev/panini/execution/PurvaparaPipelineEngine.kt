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
        return sentenceText.contains("पूर्व + पर") || sentenceText.contains("पूर्वपरयोः") || sentenceText.contains("पूर्वपर")
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
        val cleanDirective = sentenceText
            .replace("पूर्व + पर + ङस्", "")
            .replace("पूर्व + पर", "")
            .replace("पूर्वपरयोः", "")
            .replace("एका + सुँ", "")
            .replace("एकः", "")
            .trim()

        // Extract invocation statements connected before action verb (कृ + लोट् + सिप्)
        val verbIdx = cleanDirective.indexOf("कृ + लोट् + सिप्")
        val mainText = if (verbIdx > 0) cleanDirective.substring(0, verbIdx).trim() else cleanDirective

        val cIdx = mainText.indexOf(" च ")
        val argsPrefix = if (cIdx > 0) mainText.substring(0, cIdx + 3).trim() else ""
        val kriyaChainText = if (cIdx > 0) mainText.substring(cIdx + 3).trim() else mainText

        val rawTokens = kriyaChainText.split("+ ङस्").map { it.trim() }.filter { it.isNotEmpty() }
        val stageDirectives = mutableListOf<String>()
        var idx = 0
        while (idx < rawTokens.size) {
            val token = rawTokens[idx]
            if (idx + 1 < rawTokens.size && !token.contains(" ") && rawTokens[idx + 1].contains("+")) {
                stageDirectives += "$token + ङस् ${rawTokens[idx + 1]}"
                idx += 2
            } else {
                stageDirectives += token
                idx += 1
            }
        }

        if (stageDirectives.size < 2) {
            return listOf(
                ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "पूर्वपर-असंगतिः: Expected at least 2 chained Kriyā stages in '6.1.84 एकः पूर्वपरयोः'",
                ),
            )
        }

        var currentArgsText = argsPrefix
        var lastSuccess: ExecutionResult.Success? = null

        for (kriyaPart in stageDirectives) {
            val stageInvocationText = "$currentArgsText $kriyaPart + टा कृ + लोट् + सिप् ।"
            val invocation = registry.detectInvocation(stageInvocationText, callerSourceFile = callerSourceFile)
            if (invocation == null) {
                return listOf(
                    ExecutionResult.Failure(
                        ExecutionError.INVALID_VALUE,
                        "पूर्वपर-असंगतिः: Could not resolve Kriyā stage '$kriyaPart' in pipeline",
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
                        "पूर्वपर-असंगतिः: Execution failed at Kriyā stage '$kriyaPart'",
                    ),
                )
            }

            lastSuccess = stageSuccess
            val stageVal = stageSuccess.value
            val originalArgTerms = SubantaKarakaParser.extractKarmaTerms(argsPrefix, null)
            val nextArgs = mutableListOf(stageVal)
            if (originalArgTerms.size > 1) {
                nextArgs.addAll(originalArgTerms.drop(1))
            }
            currentArgsText = nextArgs.joinToString(" ") { "$it + अम्" } + " च"
        }

        return listOf(
            lastSuccess ?: ExecutionResult.Success(
                operation = "panini.purvapara_pipeline",
                value = "संसिद्धम्",
            ),
        )
    }
}
