package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.core.Vibhakti
import dev.panini.sankhya.CanonicalNumeralStem
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.SemanticDiagnostic
import dev.panini.vyakaranam.parser.PaniniSemanticException

enum class PrakriyaDiagnosticSeverity { ERROR, WARNING }

data class PrakriyaDiagnostic(
    val offset: Int,
    val length: Int,
    override val message: String,
    val severity: PrakriyaDiagnosticSeverity = PrakriyaDiagnosticSeverity.ERROR,
    val replacement: String? = null,
    override val code: String = "VALIDATION",
    override val offendingText: String = "",
) : SemanticDiagnostic<String> {
    override val span: DocumentSourceSpan = DocumentSourceSpan(offset, offset + length)
}

/** Performs declaration and call checks without executing the script. */
object PrakriyaScriptValidator {
    fun validate(source: String): List<PrakriyaDiagnostic> = validateWith(source, PvmScript::parse)

    /** Inspects old block syntax to produce migration diagnostics; never used by execution. */
    fun validateLegacy(source: String): List<PrakriyaDiagnostic> = validateWith(source, PvmScript::parseLegacy)

    private fun validateWith(source: String, parseSource: (String) -> List<PvmScriptStatement>): List<PrakriyaDiagnostic> {
        val diagnostics = CanonicalNumeralStem.suggestions(source).mapTo(mutableListOf()) { suggestion ->
            PrakriyaDiagnostic(
                offset = suggestion.offset,
                length = suggestion.surface.length,
                message = "Use canonical numeral stem '${suggestion.canonical}' before a segmented suffix.",
                severity = PrakriyaDiagnosticSeverity.WARNING,
                replacement = suggestion.canonical,
            )
        }
        DirectResultAssignment.suggestions(source).forEach { suggestion ->
            diagnostics += PrakriyaDiagnostic(
                offset = suggestion.offset,
                length = suggestion.length,
                message = "The preceding ततः result can be assigned directly without an explicit फल lookup.",
                severity = PrakriyaDiagnosticSeverity.WARNING,
                replacement = suggestion.replacement,
            )
        }
        val statements = runCatching { parseSource(source) }.getOrElse { error ->
            val semantic = (error as? PaniniSemanticException)?.diagnostic
            diagnostics += PrakriyaDiagnostic(
                offset = semantic?.span?.start ?: 0,
                length = semantic?.span?.let { it.endExclusive - it.start } ?: source.length.coerceAtLeast(1),
                message = "Source contains invalid Sanskrit: ${error.message.orEmpty()}",
                code = semantic?.code?.name ?: "INVALID_SOURCE",
                offendingText = semantic?.offendingText.orEmpty(),
            )
            return diagnostics
        }
        val registry = PrakriyaRegistry()

        val sourceMap = SourceTextMap(source)
        var headerSearchFrom = 0
        statements.filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().forEach { definition ->
            if (definition.body.isEmpty()) return@forEach
            val header = definition.headerSource?.trim()?.trimEnd('।', '॥', '.')?.trimEnd()
                ?: return@forEach
            val located = sourceMap.locate(header, headerSearchFrom) ?: return@forEach
            headerSearchFrom = located.nextCompactOffset
            if (!PrakriyaDefinitionMarkerParser.hasExplicitMarker(header)) {
                diagnostics += PrakriyaDiagnostic(
                    offset = located.span.start,
                    length = located.span.length,
                    message = "A bare action noun is a compatibility header; declare reusable code explicitly with 'इति प्रक्रिया अस्ति'.",
                    severity = PrakriyaDiagnosticSeverity.WARNING,
                    replacement = header.trim().trimEnd('।', '॥', '.').trimEnd() +
                        " इति प्रक्रिया + सुँ असँ + लट् + तिप्",
                )
            }
        }

        legacySamjnaMarkers(source, statements).forEach { legacy ->
            diagnostics += PrakriyaDiagnostic(
                offset = legacy.first,
                length = legacy.last - legacy.first + 1,
                message = "संज्ञा denotes a grammatical technical term; declare reusable code with 'इति प्रक्रिया अस्ति'.",
                replacement = "इति प्रक्रिया + सुँ असँ + लट् + तिप्",
            )
        }

        statements.filterIsInstance<PvmScriptStatement.Sentence>().forEach { sentence ->
            val declaration = sentence.ukti?.let(ItiDeclarationAnalyzer::analyze) ?: return@forEach
            val marker = declaration.nominativeMarker ?: return@forEach
            if ((marker.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SIMA &&
                declaration.declaredPadas.none { it is ParyantaRangePada }
            ) {
                diagnostics += diagnostic(
                    source,
                    "सीमा",
                    "A सीमा requires an ablative lower bound and a segmented पर्यन्त upper boundary.",
                )
            }
        }

        statements.filterIsInstance<PvmScriptStatement.Sentence>().forEach { sentence ->
            try {
                TaddhitaStructEngine.detectResultSchema(sentence.text, sentence.ukti)?.let(registry::registerSchema)
            } catch (failure: IllegalArgumentException) {
                diagnostics += diagnostic(source, sentence.text, failure.message ?: "Invalid schema declaration.")
            }
        }

        statements.filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().forEach { definition ->
            val parameters = definition.body.mapNotNull(PrakriyaSignatureDeclarationParser::parameter)
            parameters.groupBy(PrakriyaParameter::nameStem).filterValues { it.size > 1 }.keys.forEach { name ->
                diagnostics += diagnostic(source, name, "The parameter '$name' is declared more than once.")
            }
            val results = definition.body.mapNotNull(PrakriyaSignatureDeclarationParser::result)
            if (results.size > 1) {
                diagnostics += diagnostic(source, "परिणाम", "A प्रक्रिया may declare only one result.")
            }
            results.singleOrNull()?.schema?.let { schema ->
                if (registry.resolveSchema(schema) == null) {
                    diagnostics += diagnostic(source, schema, "No परिणाम schema named '$schema' is declared.")
                }
            }
            // Do not derive call contracts from a contradictory declaration.
            if (parameters.map { it.nameStem }.distinct().size != parameters.size || results.size > 1) {
                return@forEach
            }
            val signature = runCatching { PrakriyaSignatureCompiler.compile(definition.body,
                definition.prakriya.declaredResult, definition.prakriya.resultPredicate) }.getOrElse { error ->
                diagnostics += diagnostic(source, definition.nameSegmented,
                    error.message ?: "Invalid procedure signature.")
                return@forEach
            }
            val kriya = Prakriya(
                nameSegmented = definition.nameSegmented,
                nameStem = definition.prakriya.nameIdentity,
                body = definition.body,
                domainStem = definition.domainStem,
                visibility = definition.prakriya.modifiers.visibility,
                precedence = definition.prakriya.modifiers.precedence,
                signatureOverride = signature,
            )
            registry.register(kriya)
        }

        statements.filterIsInstance<PvmScriptStatement.Sentence>().forEach { sentence ->
            val pipeline = sentence.program as? Pipeline
            if (pipeline != null) {
                validatePipeline(source, pipeline, registry, diagnostics)
            } else {
                sentence.ukti?.let { registry.detectInvocation(it, staticAnalysis = true) }?.let { invocation ->
                    validateCall(source, invocation, diagnostics,
                        registry.all().mapTo(mutableSetOf()) { it.nameStem })
                }
            }
        }
        return diagnostics.distinct()
    }

    private fun validatePipeline(
        source: String,
        pipeline: Pipeline,
        registry: PrakriyaRegistry,
        diagnostics: MutableList<PrakriyaDiagnostic>,
    ) {
        val originalArguments = pipeline.arguments.mapIndexed { index, term ->
            PrakriyaArgument(
                term = term,
                pada = pipeline.argumentPadas.getOrNull(index),
                origin = PrakriyaArgumentOrigin.WRITTEN,
            )
        }
        var arguments = originalArguments
        var precedingType: PrakriyaValueType? = null
        val originalTypes = originalArguments.map { argument ->
            argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada) ?: PrakriyaValueType.UNCONSTRAINED
        }
        var argumentTypes = originalTypes
        pipeline.stages.forEach { stage ->
            val invocation = try { registry.resolveStructuredInvocation(
                stage.operationStem,
                stage.domainStem,
                arguments,
                pipeline.sourceText,
                analyzedArgumentTypes = argumentTypes,
            ) } catch (failure: IllegalArgumentException) {
                diagnostics += diagnostic(source, stage.operationStem,
                    PipelineSemanticDiagnostics.atPipeline(
                        pipeline,
                        "INVALID_PIPELINE_TYPE_EVIDENCE",
                        failure.message ?: "Invalid pipeline type evidence.",
                    ))
                return
            } ?: return@forEach
            invocation.resolutionError?.let { message ->
                diagnostics += diagnostic(source, stage.operationStem,
                    PipelineSemanticDiagnostics.atPipeline(
                        pipeline, "AMBIGUOUS_PIPELINE_STAGE", message))
                return
            }
            val signature = invocation.kriya.signature
            if (signature.parameters.isNotEmpty()) {
                if (signature.parameters.size != arguments.size) {
                    diagnostics += diagnostic(source, stage.operationStem,
                        PipelineSemanticDiagnostics.atPipeline(
                            pipeline,
                            "PROCEDURE_ARITY_MISMATCH",
                            PrakriyaDiagnostics.arity(
                                stage.operationStem, signature.parameters.size, arguments.size),
                        ))
                } else {
                    if (signature.hasTypedParticipants && !TypedParticipantBinding.acceptsTypes(
                            signature.parameters.map { it.type }, argumentTypes)) {
                        diagnostics += diagnostic(source, stage.operationStem,
                            PipelineSemanticDiagnostics.atPipeline(
                                pipeline,
                                "PROCEDURE_PARTICIPANT_TYPE_MISMATCH",
                                "Procedure typed participant mismatch.",
                            ))
                    }
                    signature.parameters.zip(argumentTypes).takeUnless { signature.hasTypedParticipants }
                        .orEmpty().withIndex().firstOrNull { (_, pair) ->
                        val (parameter, actual) = pair
                        parameter.type != PrakriyaValueType.UNCONSTRAINED &&
                            actual != PrakriyaValueType.UNCONSTRAINED && parameter.type != actual
                    }?.let { mismatch ->
                        val (parameter, actual) = mismatch.value
                        diagnostics += diagnostic(source, stage.operationStem,
                            PipelineSemanticDiagnostics.atPipeline(
                                pipeline,
                                "PIPELINE_ARGUMENT_TYPE_MISMATCH",
                                "Pipeline type mismatch: ${parameter.type} cannot consume $actual for '${parameter.nameStem}'.",
                                mismatch.index,
                            ))
                    }
                }
            }
            precedingType = signature.resultType ?: signature.resultSchema?.let { PrakriyaValueType.RUPA }
            argumentTypes = listOf(precedingType ?: PrakriyaValueType.UNCONSTRAINED) + originalTypes.drop(1)
            arguments = listOf(
                PrakriyaArgument(
                    term = "फल",
                    origin = PrakriyaArgumentOrigin.PIPE,
                ),
            ) + originalArguments.drop(1)
        }
    }

    private fun validateCall(
        source: String,
        invocation: PrakriyaInvocation,
        diagnostics: MutableList<PrakriyaDiagnostic>,
        knownOperationStems: Set<String>,
    ) {
        invocation.resolutionError?.let { message ->
            diagnostics += diagnostic(source, invocation.kriya.nameStem,
                PrakriyaCallDiagnostics.atInvocation(
                    invocation, "AMBIGUOUS_PROCEDURE_OVERLOAD", message))
            return
        }
        val signature = invocation.kriya.signature
        val callName = invocation.kriya.nameStem
        val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation, knownOperationStems)
        if (resolution is PrakriyaArgumentResolution.Failure) {
            val semantic = resolution.diagnostic
            diagnostics += semantic.span?.let { span ->
                PrakriyaDiagnostic(
                    offset = span.start,
                    length = (span.endExclusive - span.start).coerceAtLeast(1),
                    message = semantic.message,
                    code = semantic.code,
                    offendingText = semantic.offendingText,
                )
            } ?: diagnostic(source, callName, semantic.message).copy(
                code = semantic.code,
                offendingText = semantic.offendingText,
            )
            return
        }
        if (signature.parameters.isEmpty()) return
        val arguments = (resolution as PrakriyaArgumentResolution.Success).arguments
        if (arguments.size != signature.parameters.size) {
            diagnostics += diagnostic(source, callName, PrakriyaCallDiagnostics.atInvocation(
                invocation,
                "PROCEDURE_ARITY_MISMATCH",
                PrakriyaDiagnostics.arity(callName, signature.parameters.size, arguments.size),
            ))
            return
        }
        if (signature.hasTypedParticipants) {
            val types = arguments.map { argument ->
                argument.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                    ?: argument.argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                    ?: PrakriyaValueType.UNCONSTRAINED
            }
            if (!TypedParticipantBinding.acceptsTypes(signature.parameters.map { it.type }, types))
                diagnostics += diagnostic(source, callName, PrakriyaCallDiagnostics.atInvocation(
                    invocation,
                    "PROCEDURE_PARTICIPANT_TYPE_MISMATCH",
                    "Procedure typed participant mismatch.",
                ))
            return
        }
        signature.parameters.zip(arguments).withIndex().firstOrNull { (_, pair) ->
            val (parameter, argument) = pair
            val actual = argument.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                ?: argument.argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                ?: PrakriyaValueType.UNCONSTRAINED
            parameter.type != PrakriyaValueType.UNCONSTRAINED &&
                actual != PrakriyaValueType.UNCONSTRAINED && parameter.type != actual
        }?.let { mismatch ->
            val parameter = mismatch.value.first
            diagnostics += diagnostic(source, callName, PrakriyaCallDiagnostics.atInvocation(
                invocation,
                "PROCEDURE_ARGUMENT_TYPE_MISMATCH",
                PrakriyaDiagnostics.parameterType(parameter),
                mismatch.index,
            ))
        }
    }

    private fun legacySamjnaMarkers(
        source: String,
        statements: List<PvmScriptStatement>,
    ): List<IntRange> {
        val legacyMarkers = statements
            .filterIsInstance<PvmScriptStatement.Sentence>()
            .mapNotNull { sentence ->
                val declaration = sentence.ukti?.let(ItiDeclarationAnalyzer::analyze) ?: return@mapNotNull null
                val marker = declaration.nominativeMarker ?: return@mapNotNull null
                if ((marker.pratipadika as? MulaPratipadika)?.lexicalIdentity != MulaPratipadikaIdentity.SAMJNA ||
                    !marker.sup.supports(Vibhakti.PRATHAMA)
                ) return@mapNotNull null
                "इति${marker.sourceText.filterNot(Char::isWhitespace)}"
            }
        if (legacyMarkers.isEmpty()) return emptyList()
        val sourceMap = SourceTextMap(source)
        var searchFrom = 0
        return legacyMarkers.mapNotNull { marker ->
            val located = sourceMap.locate(marker, searchFrom) ?: return@mapNotNull null
            searchFrom = located.nextCompactOffset
            located.span.start until located.span.endExclusive
        }
    }

    private fun diagnostic(source: String, token: String, message: String): PrakriyaDiagnostic {
        val offset = source.indexOf(token).coerceAtLeast(0)
        return PrakriyaDiagnostic(offset, token.length.coerceAtLeast(1), message)
    }

    private fun diagnostic(
        source: String,
        token: String,
        semantic: SemanticDiagnostic<String>,
    ): PrakriyaDiagnostic = semantic.span?.let { span ->
        PrakriyaDiagnostic(
            offset = span.start,
            length = (span.endExclusive - span.start).coerceAtLeast(1),
            message = semantic.message,
            code = semantic.code,
            offendingText = semantic.offendingText,
        )
    } ?: diagnostic(source, token, semantic.message).copy(
        code = semantic.code,
        offendingText = semantic.offendingText,
    )
}
