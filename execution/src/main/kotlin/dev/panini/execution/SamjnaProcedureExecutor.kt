package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.FrequencyExtractor
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.SubantaPada

/** Executes one reusable saṃjñā procedure independently of script control-flow orchestration. */
internal class SamjnaProcedureExecutor {
    data class Request(
        val invocation: SamjnaInvocation,
        val scope: ExecutionScope,
        val registry: SamjnaKriyaRegistry,
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
        val astResolution = NamedSamjnaArgumentResolver.resolve(
            invocation.argumentSyntax.filterIsInstance<SubantaPada>(),
            signature,
        )
        val hasNamedSyntax = invocation.argumentSyntax.filterIsInstance<SubantaPada>().any {
            SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.SASTHI
        }
        val argumentResolution = when {
            hasNamedSyntax -> astResolution
            invocation.arguments.isNotEmpty() ->
                SamjnaArgumentResolution.Success(invocation.arguments.map(ProcedureArgument::term), false)
            else -> NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
        }
        if (argumentResolution is SamjnaArgumentResolution.Failure) {
            return listOf(ExecutionResult.Failure(ExecutionError.INVALID_VALUE, argumentResolution.message))
        }
        val argTerms = (argumentResolution as SamjnaArgumentResolution.Success).terms
        val callFrame = ProcedureCallFrame.create(
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
        validateGuards(invocation, argTerms)?.let { return listOf(it) }

        val results = mutableListOf<ExecutionResult>()
        val repetitionCount = (invocation.ukti?.body as? Repeat)?.count
            ?: invocation.ukti?.grammaticalVakyas()?.firstOrNull()?.padas
                ?.let(FrequencyExtractor::extractAbhyasaCount)
            ?: 1
        repeat(repetitionCount) {
            invocation.kriya.vidhiSentences.forEach { sentence ->
                val sourceFile = invocation.kriya.sourceFile ?: request.callerSourceFile
                val boundProgram = sentence.program?.let {
                    ProcedureAstArgumentBinder.bind(it, signature.parameters, callFrame.arguments.size)
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
        invocation: SamjnaInvocation,
        signature: SamjnaSignature,
        terms: List<String>,
        frame: ProcedureCallFrame,
    ): ExecutionResult.Failure? {
        if (signature.parameters.isNotEmpty() && signature.parameters.size != terms.size) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "संज्ञा-मानसङ्ख्या: '${invocation.kriya.nameStem}' expects ${signature.parameters.size} arguments, but received ${terms.size}.",
            )
        }
        val mismatch = signature.parameters.zip(terms).withIndex().firstOrNull { (index, pair) ->
            val actual = frame.arguments.getOrNull(index)?.let(SamjnaValueClassifier::classifyValue)
                ?: SamjnaValueClassifier.classifyTerm(pair.second)
            actual != pair.first.type
        } ?: return null
        return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "संज्ञा-मानप्रकारः: '${mismatch.value.first.nameStem}' requires ${mismatch.value.first.type}.",
        )
    }

    private fun validateGuards(
        invocation: SamjnaInvocation,
        terms: List<String>,
    ): ExecutionResult.Failure? {
        invocation.kriya.nishedhaGuards.forEach { guard ->
            var guardText = guard.text
            terms.forEachIndexed { index, argument ->
                guardText = PuranaPratyayaResolver.replacePatterns(guardText, index, argument)
            }
            val requiredType = SamjnaSignatureCompiler.inferGuardType(guardText)
            if (DynamicNishedhaEvaluator.evaluateProhibition(guardText) ||
                requiredType != null && terms.any { SamjnaValueClassifier.classifyTerm(it) != requiredType }
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
        invocation: SamjnaInvocation,
        registry: SamjnaKriyaRegistry,
        results: List<ExecutionResult>,
    ): ExecutionResult.Failure? {
        val signature = invocation.kriya.signature
        signature.resultType?.let { expected ->
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return null
            val actual = SamjnaValueClassifier.classifyValue(
                finalResult.typedValue ?: SanskritValue.of(finalResult.value),
            )
            if (actual != expected) {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामप्रकारः: '${invocation.kriya.nameStem}' declared $expected but returned $actual.",
                )
            }
        }
        signature.resultSchema?.let { expected ->
            val schema = registry.resolveSchema(expected) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "संज्ञा-परिणामरूपम्: No schema named '$expected' is declared.",
            )
            val finalResult = results.lastOrNull() as? ExecutionResult.Success ?: return null
            val structured = finalResult.typedValue as? SanskritValue.Rupa
                ?: return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामरूपम्: '${invocation.kriya.nameStem}' must return '$expected'.",
                )
            if (structured.schema != expected || structured.fields.keys != schema.fields.toSet()) {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "संज्ञा-परिणामरूपम्: '$expected' requires ${schema.fields}, but returned ${structured.fields.keys}.",
                )
            }
        }
        return null
    }
}
