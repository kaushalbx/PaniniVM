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
        val resolveValue: (String) -> SanskritValue? = { null },
        val executeBody: (
            program: ProgramNode,
            scope: ExecutionScope,
            sourceFile: String?,
        ) -> List<ExecutionResult>,
    )

    fun execute(request: Request): List<ExecutionResult> {
        val invocation = request.invocation
        invocation.resolutionError?.let {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE, it))
        }
        val signature = invocation.kriya.signature
        val argumentResolution = PrakriyaInvocationArgumentResolver.resolve(invocation)
        if (argumentResolution is PrakriyaArgumentResolution.Failure) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE, argumentResolution.message))
        }
        val resolvedArguments = (argumentResolution as PrakriyaArgumentResolution.Success).arguments
        val argTerms = argumentResolution.terms
        val callFrame = PrakriyaCallFrame.createResolved(
            invocation,
            resolvedArguments,
            request.scope,
            request.resolveValue,
        )

        validateArguments(invocation, signature, argTerms, callFrame)?.let { return listOf(it) }
        validateGuards(invocation, argTerms, callFrame)?.let { return listOf(it) }

        val results = mutableListOf<ExecutionResult>()
        val repetitionCount = (invocation.ukti?.body as? Repeat)?.count
            ?: invocation.ukti?.grammaticalVakyas()?.firstOrNull()?.padas
                ?.let(FrequencyExtractor::extractAbhyasaCount)
            ?: 1
        repeat(repetitionCount) {
            invocation.kriya.vidhiSentences.forEach { sentence ->
                val sourceFile = invocation.kriya.sourceFile ?: request.callerSourceFile
                val program = requireNotNull(sentence.program) {
                    "A reusable प्रक्रिया body must contain a parsed Sanskrit program: '${sentence.text}'."
                }
                val boundProgram = PrakriyaAstArgumentBinder.bind(
                    program, signature.parameters, callFrame.arguments.size,
                    request.registry.all().mapTo(mutableSetOf()) { it.nameStem },
                )
                val produced = request.executeBody(boundProgram, callFrame.localScope, sourceFile)
                results += produced
                // Failure and suspension are not completed procedure results.
                if (produced.any { it !is ExecutionResult.Success }) return results
                if (produced.any { it is ExecutionResult.Success &&
                        it.controlSignal == ExecutionControlSignal.RETURN_PROCEDURE }) {
                    validateResult(invocation, request.registry, results)?.let { results += it }
                    return results.map { result ->
                        if (result is ExecutionResult.Success &&
                            result.controlSignal == ExecutionControlSignal.RETURN_PROCEDURE)
                            result.copy(controlSignal = null) else result
                    }
                }
                if (produced.any { it is ExecutionResult.Success &&
                        it.controlSignal == ExecutionControlSignal.BREAK_LOOP }) {
                    // Legacy break propagation must not bypass the result contract.
                    validateResult(invocation, request.registry, results)?.let { results += it }
                    return results
                }
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
                PrakriyaDiagnostics.arity(invocation.kriya.nameStem, signature.parameters.size, terms.size),
            )
        }
        val mismatch = signature.parameters.zip(terms).withIndex().firstOrNull { (index, pair) ->
            val value = frame.arguments.getOrNull(index)
            if (value != null) !PrakriyaValueClassifier.accepts(pair.first.type, value)
            else pair.first.type != PrakriyaValueType.UNCONSTRAINED &&
                PrakriyaValueClassifier.classifyTerm(pair.second) != pair.first.type
        } ?: return null
        return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            PrakriyaDiagnostics.parameterType(mismatch.value.first),
        )
    }

    private fun validateGuards(
        invocation: PrakriyaInvocation,
        terms: List<String>,
        frame: PrakriyaCallFrame,
    ): ExecutionResult.Failure? {
        invocation.kriya.nishedhaGuards.forEach { guard ->
            if (NishedhaGuardEvaluator.numericProhibition(guard,
                    invocation.kriya.signature.parameters, frame.arguments.size) == null &&
                PrakriyaSignatureCompiler.inferGuardType(guard) == null) {
                return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "Unsupported procedure prohibition: '${guard.text.trim()}'")
            }
            val requiredType = invocation.kriya.signature.argumentType
            if (NishedhaGuardEvaluator.isProhibited(
                    guard,
                    invocation.kriya.signature.parameters,
                    frame.arguments,
                ) || requiredType != null && frame.arguments.any {
                    !PrakriyaValueClassifier.accepts(requiredType, it)
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
        if ((signature.resultType != null || signature.resultSchema != null) && results.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "प्रक्रिया-परिणामः: '${invocation.kriya.nameStem}' declared a result but produced none.",
            )
        }
        signature.resultType?.let { expected ->
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return null
            val value = finalResult.typedValue ?: SanskritValue.of(finalResult.value)
            val actual = PrakriyaValueClassifier.classifyValue(value)
            if (!PrakriyaValueClassifier.accepts(expected, value)) {
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
