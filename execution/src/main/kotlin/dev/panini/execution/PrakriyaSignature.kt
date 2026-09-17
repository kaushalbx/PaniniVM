package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator

/** Semantic value types used by saṃjñā signatures and overload resolution. */
enum class PrakriyaValueType {
    SANKHYA,
    SHABDA,
    SUCHI,
}

data class PrakriyaParameter(
    val nameStem: String,
    val type: PrakriyaValueType,
)

data class PrakriyaSignature(
    val argumentType: PrakriyaValueType? = null,
    val parameters: List<PrakriyaParameter> = emptyList(),
    val resultType: PrakriyaValueType? = null,
    val resultSchema: String? = null,
)

/**
 * Compatibility boundary that compiles surface type guards into a typed signature.
 * Runtime dispatch consumes [PrakriyaSignature] and does not inspect rule-body text.
 */
object PrakriyaSignatureCompiler {
    private val typeMarkers = linkedMapOf(
        PrakriyaValueType.SANKHYA to listOf("सङ्ख्या + त्व", "सङ्ख्यात्व"),
        PrakriyaValueType.SHABDA to listOf("शब्द + त्व", "शब्दत्व"),
        PrakriyaValueType.SUCHI to listOf("सूची + त्व", "सूचीत्व"),
    )

    fun compile(body: List<PvmScriptStatement.Sentence>): PrakriyaSignature {
        val parameters = body.mapNotNull(PrakriyaSignatureDeclarationParser::parameter)
        val resultDeclarations = body.mapNotNull(PrakriyaSignatureDeclarationParser::result)
        val resultType = resultDeclarations.singleOrNull()?.type
        val resultSchema = resultDeclarations.singleOrNull()?.schema
        val guardedTypes = body.asSequence()
            .filter { it.isNishedha }
            .mapNotNull { inferGuardType(it.text) }
            .distinct()
            .toList()
        return PrakriyaSignature(
            argumentType = guardedTypes.singleOrNull(),
            parameters = parameters,
            resultType = resultType,
            resultSchema = resultSchema,
        )
    }

    fun inferGuardType(text: String): PrakriyaValueType? =
        typeMarkers.entries.firstOrNull { (_, markers) -> markers.any(text::contains) }?.key
}

/** Parses grammatical signature declarations embedded at the start of a saṃjñā block. */
object PrakriyaSignatureDeclarationParser {
    data class ResultDeclaration(val type: PrakriyaValueType? = null, val schema: String? = null)
    private val typeSource = "(सङ्ख्या|शब्द|सूची)"
    private val parameterPattern = Regex(
        "^\\s*(.+?)\\s*\\+\\s*सुँ\\s+$typeSource\\s*\\+\\s*सुँ\\s+इति\\s+मान\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )
    private val resultPattern = Regex(
        "^\\s*$typeSource\\s*\\+\\s*सुँ\\s+इति\\s+परिणाम\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )
    private val schemaResultPattern = Regex(
        "^\\s*(.+?)\\s*\\+\\s*सुँ\\s+इति\\s+परिणाम\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )

    fun parameter(sentence: PvmScriptStatement.Sentence): PrakriyaParameter? {
        val match = parameterPattern.matchEntire(sentence.text) ?: return null
        return PrakriyaParameter(match.groupValues[1].trim(), type(match.groupValues[2]))
    }

    fun result(sentence: PvmScriptStatement.Sentence): ResultDeclaration? {
        resultPattern.matchEntire(sentence.text)?.groupValues?.get(1)?.let {
            return ResultDeclaration(type = type(it))
        }
        val schema = schemaResultPattern.matchEntire(sentence.text)?.groupValues?.get(1)?.trim() ?: return null
        if (schema in setOf("सङ्ख्या", "शब्द", "सूची")) return null
        return ResultDeclaration(schema = schema)
    }

    fun resultType(sentence: PvmScriptStatement.Sentence): PrakriyaValueType? = result(sentence)?.type

    fun isDeclaration(sentence: PvmScriptStatement.Sentence): Boolean =
        parameter(sentence) != null || result(sentence) != null

    private fun type(source: String): PrakriyaValueType = when (source) {
        "सङ्ख्या" -> PrakriyaValueType.SANKHYA
        "शब्द" -> PrakriyaValueType.SHABDA
        "सूची" -> PrakriyaValueType.SUCHI
        else -> error("Unsupported saṃjñā value type: $source")
    }
}

object PrakriyaValueClassifier {
    private val sankhyaEvaluator = SankhyaEvaluator()

    fun classifyTerm(term: String): PrakriyaValueType =
        if (term.toLongOrNull() != null ||
            runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull() != null
        ) {
            PrakriyaValueType.SANKHYA
        } else {
            PrakriyaValueType.SHABDA
        }

    fun classifyValue(value: SanskritValue): PrakriyaValueType = when (value) {
        is SanskritValue.Sankhya, is SanskritValue.Rational, is SanskritValue.Range -> PrakriyaValueType.SANKHYA
        is SanskritValue.Suchi, is SanskritValue.Gana -> PrakriyaValueType.SUCHI
        else -> PrakriyaValueType.SHABDA
    }
}
