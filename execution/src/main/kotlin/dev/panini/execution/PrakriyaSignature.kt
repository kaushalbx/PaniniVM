package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.invocations

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

    fun parameter(sentence: PvmScriptStatement.Sentence): PrakriyaParameter? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.singleStem() != "मान" || declared.size != 2) return null
        val parameterName = declared[0].singleStem() ?: return null
        val parameterType = declared[1].singleStem()?.let(::typeOrNull) ?: return null
        return PrakriyaParameter(parameterName, parameterType)
    }

    fun result(sentence: PvmScriptStatement.Sentence): ResultDeclaration? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.singleStem() != "परिणाम" || declared.size != 1) return null
        val result = declared.single().singleStem() ?: return null
        return typeOrNull(result)?.let { ResultDeclaration(type = it) }
            ?: ResultDeclaration(schema = result)
    }

    fun resultType(sentence: PvmScriptStatement.Sentence): PrakriyaValueType? = result(sentence)?.type

    fun isDeclaration(sentence: PvmScriptStatement.Sentence): Boolean =
        parameter(sentence) != null || result(sentence) != null

    private fun typeOrNull(source: String): PrakriyaValueType? = when (source) {
        "सङ्ख्या" -> PrakriyaValueType.SANKHYA
        "शब्द" -> PrakriyaValueType.SHABDA
        "सूची" -> PrakriyaValueType.SUCHI
        else -> null
    }

    private fun declarationPadas(
        sentence: PvmScriptStatement.Sentence,
    ): Pair<List<SubantaPada>, SubantaPada>? {
        val ukti = sentence.ukti ?: return null
        val (declarationPadas, reportingPadas) = (ukti.body as? Quotation)?.let { quotation ->
            quotation.quoted.vakya.padas to quotation.reporting.invocations().flatMap { it.vakya.padas }
        } ?: run {
            val padas = ukti.grammaticalVakyas().flatMap { it.padas }
            val iti = padas.indexOfFirst { (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE }
            if (iti < 0) return null
            padas.take(iti) to padas.drop(iti + 1)
        }
        val declared = declarationPadas.filterIsInstance<SubantaPada>()
        val marker = reportingPadas
            .filterIsInstance<SubantaPada>().singleOrNull() ?: return null
        if ((declared + marker).any { SupAffix.fromUpadesha(it.sup.text)?.vibhakti != Vibhakti.PRATHAMA }) {
            return null
        }
        return declared to marker
    }

    private fun SubantaPada.singleStem(): String? =
        (pratipadika as? MulaPratipadika)?.text
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
