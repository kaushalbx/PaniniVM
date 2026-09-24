package dev.panini.execution

import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.sankhya.CanonicalNumeralStem
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

enum class PrakriyaDiagnosticSeverity { ERROR, WARNING }

data class PrakriyaDiagnostic(
    val offset: Int,
    val length: Int,
    val message: String,
    val severity: PrakriyaDiagnosticSeverity = PrakriyaDiagnosticSeverity.ERROR,
    val replacement: String? = null,
)

/** Performs declaration and call checks without executing the script. */
object PrakriyaScriptValidator {
    fun validate(source: String): List<PrakriyaDiagnostic> {
        val diagnostics = CanonicalNumeralStem.suggestions(source).mapTo(mutableListOf()) { suggestion ->
            PrakriyaDiagnostic(
                offset = suggestion.offset,
                length = suggestion.surface.length,
                message = "Use canonical numeral stem '${suggestion.canonical}' before a segmented suffix.",
                severity = PrakriyaDiagnosticSeverity.WARNING,
                replacement = suggestion.canonical,
            )
        }
        legacySamjnaMarkers(source).forEach { legacy ->
            diagnostics += PrakriyaDiagnostic(
                offset = legacy.first,
                length = legacy.last - legacy.first + 1,
                message = "संज्ञा denotes a grammatical technical term; declare reusable code with 'इति प्रक्रिया अस्ति'.",
                replacement = "इति प्रक्रिया + सुँ असँ + लट् + तिप्",
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
        val statements = runCatching { PvmScript.parse(source) }.getOrElse { error ->
            diagnostics += PrakriyaDiagnostic(
                offset = 0,
                length = source.length.coerceAtLeast(1),
                message = error.message ?: "The reusable प्रक्रिया body is not valid Sanskrit.",
            )
            return diagnostics
        }
        val registry = PrakriyaRegistry()

        statements.filterIsInstance<PvmScriptStatement.Sentence>().mapNotNull { sentence ->
            TaddhitaStructEngine.detectResultSchema(sentence.text, sentence.ukti)
        }.forEach(registry::registerSchema)

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
            val kriya = Prakriya(
                nameSegmented = definition.nameSegmented,
                nameStem = PrakriyaRegistry.stripSupSuffix(definition.nameSegmented),
                body = definition.body,
                domainStem = definition.domainStem,
                visibility = definition.prakriya.modifiers.visibility,
                precedence = definition.prakriya.modifiers.precedence,
            )
            registry.register(kriya)
        }

        statements.filterIsInstance<PvmScriptStatement.Sentence>().forEach { sentence ->
            val pipeline = sentence.program as? Pipeline
            if (pipeline != null) {
                validatePipeline(source, pipeline, registry, diagnostics)
            } else {
                sentence.ukti?.let(registry::detectInvocation)?.let { invocation ->
                    validateCall(source, invocation, diagnostics)
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
        var arguments = pipeline.arguments
        var precedingType: PrakriyaValueType? = null
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
            precedingType = signature.resultType ?: signature.resultSchema?.let { PrakriyaValueType.SHABDA }
            arguments = listOf("फल") + pipeline.arguments.drop(1)
        }
    }

    private fun validateCall(
        source: String,
        invocation: PrakriyaInvocation,
        diagnostics: MutableList<PrakriyaDiagnostic>,
    ) {
        val signature = invocation.kriya.signature
        if (signature.parameters.isEmpty()) return
        val callName = invocation.kriya.nameStem.substringBefore(" + ")
        val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation)
        if (resolution is PrakriyaArgumentResolution.Failure) {
            diagnostics += diagnostic(source, callName, resolution.message)
            return
        }
        val arguments = (resolution as PrakriyaArgumentResolution.Success).terms
        if (arguments.size != signature.parameters.size) {
            diagnostics += diagnostic(
                source,
                callName,
                "'$callName' expects ${signature.parameters.size} arguments, but receives ${arguments.size}.",
            )
            return
        }
        signature.parameters.zip(arguments).firstOrNull { (parameter, argument) ->
            parameter.type != PrakriyaValueClassifier.classifyTerm(argument)
        }?.let { (parameter, _) ->
            diagnostics += diagnostic(source, callName, "Parameter '${parameter.nameStem}' requires ${parameter.type}.")
        }
    }

    private fun legacySamjnaMarkers(source: String): List<IntRange> {
        val legacyMarkers = runCatching { PvmScript.parse(source) }.getOrDefault(emptyList())
            .filterIsInstance<PvmScriptStatement.Sentence>()
            .mapNotNull { sentence ->
                val padas = sentence.ukti?.grammaticalVakyas()?.flatMap { it.padas } ?: return@mapNotNull null
                val iti = padas.indexOfFirst { (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE }
                val marker = padas.getOrNull(iti + 1) as? SubantaPada ?: return@mapNotNull null
                if ((marker.pratipadika as? MulaPratipadika)?.text != "संज्ञा" ||
                    SupAffix.fromUpadesha(marker.sup.text)?.vibhakti != Vibhakti.PRATHAMA
                ) return@mapNotNull null
                "इति${marker.sourceText.filterNot(Char::isWhitespace)}"
            }
        if (legacyMarkers.isEmpty()) return emptyList()
        val compact = compactSource(source)
        var searchFrom = 0
        return legacyMarkers.mapNotNull { marker ->
            val start = compact.text.indexOf(marker, searchFrom)
            if (start < 0) return@mapNotNull null
            val end = start + marker.length - 1
            searchFrom = end + 1
            compact.sourceOffsets[start]..compact.sourceOffsets[end]
        }
    }

    private data class CompactSource(val text: String, val sourceOffsets: List<Int>)

    private fun compactSource(source: String): CompactSource {
        val text = StringBuilder(source.length)
        val offsets = ArrayList<Int>(source.length)
        source.forEachIndexed { index, character ->
            if (!character.isWhitespace()) {
                text.append(character)
                offsets += index
            }
        }
        return CompactSource(text.toString(), offsets)
    }

    private fun diagnostic(source: String, token: String, message: String): PrakriyaDiagnostic {
        val offset = source.indexOf(token).coerceAtLeast(0)
        return PrakriyaDiagnostic(offset, token.length.coerceAtLeast(1), message)
    }
}
