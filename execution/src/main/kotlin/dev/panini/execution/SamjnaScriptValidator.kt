package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.sankhya.CanonicalNumeralStem

enum class SamjnaDiagnosticSeverity { ERROR, WARNING }

data class SamjnaDiagnostic(
    val offset: Int,
    val length: Int,
    val message: String,
    val severity: SamjnaDiagnosticSeverity = SamjnaDiagnosticSeverity.ERROR,
    val replacement: String? = null,
)

/** Performs declaration and call checks without executing the script. */
object SamjnaScriptValidator {
    fun validate(source: String): List<SamjnaDiagnostic> {
        val diagnostics = CanonicalNumeralStem.suggestions(source).mapTo(mutableListOf()) { suggestion ->
            SamjnaDiagnostic(
                offset = suggestion.offset,
                length = suggestion.surface.length,
                message = "Use canonical numeral stem '${suggestion.canonical}' before a segmented suffix.",
                severity = SamjnaDiagnosticSeverity.WARNING,
                replacement = suggestion.canonical,
            )
        }
        DirectResultAssignment.suggestions(source).forEach { suggestion ->
            diagnostics += SamjnaDiagnostic(
                offset = suggestion.offset,
                length = suggestion.length,
                message = "The preceding ततः result can be assigned directly without an explicit फल lookup.",
                severity = SamjnaDiagnosticSeverity.WARNING,
                replacement = suggestion.replacement,
            )
        }
        val statements = runCatching { PvmScript.parse(source) }.getOrElse { return diagnostics }
        val registry = SamjnaKriyaRegistry()

        statements.filterIsInstance<PvmScriptStatement.Sentence>().mapNotNull { sentence ->
            TaddhitaStructEngine.detectResultSchema(sentence.text, sentence.ukti)
        }.forEach(registry::registerSchema)

        statements.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { definition ->
            val parameters = definition.body.mapNotNull(SamjnaSignatureDeclarationParser::parameter)
            parameters.groupBy(SamjnaParameter::nameStem).filterValues { it.size > 1 }.keys.forEach { name ->
                diagnostics += diagnostic(source, name, "The parameter '$name' is declared more than once.")
            }
            val results = definition.body.mapNotNull(SamjnaSignatureDeclarationParser::result)
            if (results.size > 1) {
                diagnostics += diagnostic(source, "परिणाम", "A संज्ञा-क्रिया may declare only one result.")
            }
            results.singleOrNull()?.schema?.let { schema ->
                if (registry.resolveSchema(schema) == null) {
                    diagnostics += diagnostic(source, schema, "No परिणाम schema named '$schema' is declared.")
                }
            }
            val kriya = SamjnaKriya(
                nameSegmented = definition.nameSegmented,
                nameStem = SamjnaKriyaRegistry.stripSupSuffix(definition.nameSegmented),
                body = definition.body,
                domainStem = definition.domainStem,
                isInternal = definition.isInternal,
                isApavada = definition.isApavada,
                isAntaranga = definition.isAntaranga,
                isNitya = definition.isNitya,
            )
            registry.register(kriya)
        }

        statements.filterIsInstance<PvmScriptStatement.Sentence>().forEach { sentence ->
            val pipeline = sentence.program as? Pipeline
            if (pipeline != null) {
                validatePipeline(source, pipeline, registry, diagnostics)
            } else {
                registry.detectInvocation(sentence.text, preParsedUkti = sentence.ukti)?.let { invocation ->
                    validateCall(source, invocation, diagnostics)
                }
            }
        }
        return diagnostics.distinct()
    }

    private fun validatePipeline(
        source: String,
        pipeline: Pipeline,
        registry: SamjnaKriyaRegistry,
        diagnostics: MutableList<SamjnaDiagnostic>,
    ) {
        var arguments = pipeline.arguments
        var precedingType: SamjnaValueType? = null
        pipeline.stages.forEach { stage ->
            val invocation = registry.resolveStructuredInvocation(
                stage.operationStem,
                stage.domainStem,
                arguments,
                pipeline.sourceText,
            ) ?: return@forEach
            val signature = invocation.kriya.signature
            if (signature.parameters.isNotEmpty()) {
                if (signature.parameters.size != arguments.size) {
                    diagnostics += diagnostic(
                        source,
                        stage.operationStem.substringBefore(" + "),
                        "'${stage.operationStem}' expects ${signature.parameters.size} arguments, but receives ${arguments.size}.",
                    )
                } else if (precedingType != null && signature.parameters.first().type != precedingType) {
                    diagnostics += diagnostic(
                        source,
                        stage.operationStem.substringBefore(" + "),
                        "Pipeline type mismatch: ${signature.parameters.first().type} cannot consume $precedingType.",
                    )
                }
            }
            precedingType = signature.resultType ?: signature.resultSchema?.let { SamjnaValueType.SHABDA }
            arguments = listOf("फल") + pipeline.arguments.drop(1)
        }
    }

    private fun validateCall(
        source: String,
        invocation: SamjnaInvocation,
        diagnostics: MutableList<SamjnaDiagnostic>,
    ) {
        val signature = invocation.kriya.signature
        if (signature.parameters.isEmpty()) return
        val callName = invocation.kriya.nameStem.substringBefore(" + ")
        val resolution = NamedSamjnaArgumentResolver.resolve(invocation.karmaText, signature)
        if (resolution is SamjnaArgumentResolution.Failure) {
            diagnostics += diagnostic(source, callName, resolution.message)
            return
        }
        val arguments = (resolution as SamjnaArgumentResolution.Success).terms
        if (arguments.size != signature.parameters.size) {
            diagnostics += diagnostic(
                source,
                callName,
                "'$callName' expects ${signature.parameters.size} arguments, but receives ${arguments.size}.",
            )
            return
        }
        signature.parameters.zip(arguments).firstOrNull { (parameter, argument) ->
            parameter.type != SamjnaValueClassifier.classifyTerm(argument)
        }?.let { (parameter, _) ->
            diagnostics += diagnostic(source, callName, "Parameter '${parameter.nameStem}' requires ${parameter.type}.")
        }
    }

    private fun diagnostic(source: String, token: String, message: String): SamjnaDiagnostic {
        val offset = source.indexOf(token).coerceAtLeast(0)
        return SamjnaDiagnostic(offset, token.length.coerceAtLeast(1), message)
    }
}
