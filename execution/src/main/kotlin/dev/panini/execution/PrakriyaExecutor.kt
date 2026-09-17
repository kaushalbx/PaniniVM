package dev.panini.execution

import dev.panini.execution.binding.FrequencyExtractor
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Repeat

/** Executes one reusable prakriyā independently of script control-flow orchestration. */
internal class PrakriyaExecutor {
    data class Request(
        val invocation: PrakriyaInvocation,
        val scope: ExecutionScope,
        val registry: PrakriyaRegistry,
        val callerSourceFile: String?,
        val executeBody: (
            program: ProgramNode,
            scope: ExecutionScope,
            sourceFile: String?,
            sourceText: String,
        ) -> List<ExecutionResult>,
        val evaluateFallback: (text: String, scope: ExecutionScope) -> ExecutionResult,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val invocation = request.invocation
        val signature = invocation.kriya.signature
        val argumentResolution = PrakriyaInvocationArgumentResolver.resolve(invocation)
        if (argumentResolution is PrakriyaArgumentResolution.Failure) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE, argumentResolution.message))
        }
        val argTerms = (argumentResolution as PrakriyaArgumentResolution.Success).terms
        val callFrame = PrakriyaCallFrame.create(
            invocation,
            argTerms,
            request.scope,
            request.callerSourceFile,
        )

        validateArguments(invocation, signature, argTerms, callFrame)?.let { return listOf(it) }
        if (invocation.kriya.isMemoized) {
            request.registry.getCachedResult(invocation.kriya.nameStem, invocation.karmaText)?.let {
                return listOf(it)
            }
        }
        validateGuards(invocation, argTerms, callFrame)?.let { return listOf(it) }

        val results = mutableListOf<ExecutionResult>()
        val repetitionCount = (invocation.ukti?.body as? Repeat)?.count
            ?: invocation.ukti?.grammaticalVakyas()?.firstOrNull()?.padas
                ?.let(FrequencyExtractor::extractAbhyasaCount)
            ?: 1
        repeat(repetitionCount) {
            invocation.kriya.vidhiSentences.forEach { sentence ->
                val sourceFile = invocation.kriya.sourceFile ?: request.callerSourceFile
                val boundProgram = sentence.program?.let {
                    PrakriyaAstArgumentBinder.bind(it, signature.parameters, callFrame.arguments.size)
                }
                results += if (boundProgram != null) {
                    request.executeBody(boundProgram, callFrame.localScope, sourceFile, sentence.text)
                } else {
                    listOf(request.evaluateFallback(sentence.text, callFrame.localScope))
                }
            }
            if (results.any {
                    it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.BREAK_LOOP
                }
            ) return results
        }

        if (invocation.kriya.isMemoized) {
            (results.lastOrNull() as? ExecutionResult.Success)?.let {
                request.registry.cacheResult(invocation.kriya.nameStem, invocation.karmaText, it)
            }
        }
        validateResult(invocation, request.registry, results)?.let { results += it }
        return results
    }

    private fun validateArguments(
        invocation: PrakriyaInvocation,
        signature: PrakriyaSignature,
        terms: List<String>,
        frame: PrakriyaCallFrame,
    ): ExecutionResult.Failure? {
        if (signature.parameters.isNotEmpty() && signature.parameters.size != terms.size) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "प्रक्रिया-मानसङ्ख्या: '${invocation.kriya.nameStem}' expects ${signature.parameters.size} arguments, but received ${terms.size}.",
            )
        }
        val mismatch = signature.parameters.zip(terms).withIndex().firstOrNull { (index, pair) ->
            val actual = frame.arguments.getOrNull(index)?.let(PrakriyaValueClassifier::classifyValue)
                ?: PrakriyaValueClassifier.classifyTerm(pair.second)
            actual != pair.first.type
        } ?: return null
        return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "प्रक्रिया-मानप्रकारः: '${mismatch.value.first.nameStem}' requires ${mismatch.value.first.type}.",
        )
    }

    private fun validateGuards(
        invocation: PrakriyaInvocation,
        terms: List<String>,
        frame: PrakriyaCallFrame,
    ): ExecutionResult.Failure? {
        invocation.kriya.nishedhaGuards.forEach { guard ->
            val requiredType = invocation.kriya.signature.argumentType
            if (NishedhaGuardEvaluator.isProhibited(
                    guard,
                    invocation.kriya.signature.parameters,
                    terms,
                    frame.arguments,
                ) || requiredType != null && frame.arguments.any {
                    PrakriyaValueClassifier.classifyValue(it) != requiredType
                }
            ) {
                return ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'",
                )
            }
        }
        return null
    }

    private fun validateResult(
        invocation: PrakriyaInvocation,
        registry: PrakriyaRegistry,
        results: List<ExecutionResult>,
    ): ExecutionResult.Failure? {
        val signature = invocation.kriya.signature
        signature.resultType?.let { expected ->
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return null
            val actual = PrakriyaValueClassifier.classifyValue(
                finalResult.typedValue ?: SanskritValue.of(finalResult.value),
            )
            if (actual != expected) {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "प्रक्रिया-परिणामप्रकारः: '${invocation.kriya.nameStem}' declared $expected but returned $actual.",
                )
            }
        }
        signature.resultSchema?.let { expected ->
            val schema = registry.resolveSchema(expected) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "प्रक्रिया-परिणामरूपम्: No schema named '$expected' is declared.",
            )
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return null
            val structured = finalResult.typedValue as? SanskritValue.Rupa
                ?: return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "प्रक्रिया-परिणामरूपम्: '${invocation.kriya.nameStem}' must return '$expected'.",
                )
            if (structured.schema != expected || structured.fields.keys != schema.fields.toSet()) {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "प्रक्रिया-परिणामरूपम्: '$expected' requires ${schema.fields}, but returned ${structured.fields.keys}.",
                )
            }
        }
        return null
    }
}
