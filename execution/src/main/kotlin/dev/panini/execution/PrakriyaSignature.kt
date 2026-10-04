package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.ast.semanticKey

/** Semantic value types used by saṃjñā signatures and overload resolution. */
enum class PrakriyaValueType {
    SANKHYA,
    SHABDA,
    SUCHI,
    ;

    val sanskritName: String
        get() = when (this) {
            SANKHYA -> "सङ्ख्या"
            SHABDA -> "शब्द"
            SUCHI -> "सूची"
        }
}

/** Shared Sanskrit diagnostics for grammatical procedure declarations and calls. */
object PrakriyaDiagnostics {
    fun arity(procedure: String, expected: Int, actual: Int): String =
        "प्रक्रिया-मानसङ्ख्यादोषः: '$procedure' इत्यस्याः प्रक्रियायाः " +
            "$expected मानानि अपेक्षितानि, $actual प्राप्तानि।"

    fun parameterType(parameter: PrakriyaParameter): String =
        "प्रक्रिया-मानप्रकारदोषः: '${parameter.nameStem}' इति मानं " +
            "${parameter.type.sanskritName}-प्रकारम् अपेक्षते।"
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
    fun compile(body: List<PvmScriptStatement.Sentence>): PrakriyaSignature {
        val parameters = body.mapNotNull(PrakriyaSignatureDeclarationParser::parameter)
        val resultDeclarations = body.mapNotNull(PrakriyaSignatureDeclarationParser::result)
        val resultType = resultDeclarations.singleOrNull()?.type
        val resultSchema = resultDeclarations.singleOrNull()?.schema
        val guardedTypes = body.asSequence()
            .filter { it.isNishedha }
            .mapNotNull(::inferGuardType)
            .distinct()
            .toList()
        return PrakriyaSignature(
            argumentType = guardedTypes.singleOrNull(),
            parameters = parameters,
            resultType = resultType,
            resultSchema = resultSchema,
        )
    }

    /** Reads a type predicate from its parsed भाववाचक `त्व` derivation. */
    fun inferGuardType(sentence: PvmScriptStatement.Sentence): PrakriyaValueType? =
        sentence.ukti?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?.filterIsInstance<SubantaPada>()
            ?.mapNotNull { pada ->
                val stem = pada.pratipadika as? MulaPratipadika ?: return@mapNotNull null
                if (stem.vikaras.filterIsInstance<TaddhitaVikara>().none {
                        it.pratyayaClass == TaddhitaPratyayaClass.BHAVA
                    }
                ) {
                    return@mapNotNull null
                }
                when (stem.lexicalIdentity) {
                    MulaPratipadikaIdentity.SANKHYA -> PrakriyaValueType.SANKHYA
                    MulaPratipadikaIdentity.SHABDA -> PrakriyaValueType.SHABDA
                    MulaPratipadikaIdentity.SUCHI -> PrakriyaValueType.SUCHI
                    else -> null
                }
            }
            ?.singleOrNull()
}

/** Parses grammatical signature declarations embedded at the start of a saṃjñā block. */
object PrakriyaSignatureDeclarationParser {
    data class ResultDeclaration(val type: PrakriyaValueType? = null, val schema: String? = null)

    sealed interface Declaration {
        data class Parameter(val value: PrakriyaParameter) : Declaration
        data class Result(val value: ResultDeclaration) : Declaration
    }

    fun declaration(sentence: PvmScriptStatement.Sentence): Declaration? =
        parameterOrNull(sentence)?.let(Declaration::Parameter)
            ?: resultOrNull(sentence)?.let(Declaration::Result)

    fun parameter(sentence: PvmScriptStatement.Sentence): PrakriyaParameter? =
        (declaration(sentence) as? Declaration.Parameter)?.value

    fun result(sentence: PvmScriptStatement.Sentence): ResultDeclaration? =
        (declaration(sentence) as? Declaration.Result)?.value

    private fun parameterOrNull(sentence: PvmScriptStatement.Sentence): PrakriyaParameter? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.lexicalIdentity() != MulaPratipadikaIdentity.MANA || declared.size != 2) return null
        val parameterName = declared[0].pratipadika.semanticKey()
        val parameterType = declared[1].lexicalIdentity()?.let(::typeOrNull) ?: return null
        return PrakriyaParameter(parameterName, parameterType)
    }

    private fun resultOrNull(sentence: PvmScriptStatement.Sentence): ResultDeclaration? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.lexicalIdentity() != MulaPratipadikaIdentity.PARINAMA || declared.size != 1) return null
        val result = declared.single().pratipadika.semanticKey()
        return declared.single().lexicalIdentity()?.let(::typeOrNull)?.let { ResultDeclaration(type = it) }
            ?: ResultDeclaration(schema = result)
    }

    fun resultType(sentence: PvmScriptStatement.Sentence): PrakriyaValueType? = result(sentence)?.type

    fun isDeclaration(sentence: PvmScriptStatement.Sentence): Boolean =
        declaration(sentence) != null

    private fun typeOrNull(identity: MulaPratipadikaIdentity): PrakriyaValueType? = when (identity) {
        MulaPratipadikaIdentity.SANKHYA -> PrakriyaValueType.SANKHYA
        MulaPratipadikaIdentity.SHABDA -> PrakriyaValueType.SHABDA
        MulaPratipadikaIdentity.SUCHI -> PrakriyaValueType.SUCHI
        else -> null
    }

    private fun declarationPadas(
        sentence: PvmScriptStatement.Sentence,
    ): Pair<List<SubantaPada>, SubantaPada>? {
        val declaration = sentence.ukti?.let(ItiDeclarationAnalyzer::analyze) ?: return null
        val declared = declaration.declaredPadas.filterIsInstance<SubantaPada>()
        val marker = declaration.nominativeMarker ?: return null
        if ((declared + marker).any { SupAffix.fromUpadesha(it.sup.text)?.vibhakti != Vibhakti.PRATHAMA }) {
            return null
        }
        return declared to marker
    }

    private fun SubantaPada.lexicalIdentity(): MulaPratipadikaIdentity? =
        (pratipadika as? MulaPratipadika)?.lexicalIdentity
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

    fun classifyPada(pada: dev.panini.vyakaranam.ast.Pada): PrakriyaValueType =
        dev.panini.execution.binding.NumeralPadaBinder.resolveSemanticValue(pada)
            ?.let { PrakriyaValueType.SANKHYA }
            ?: PrakriyaValueType.SHABDA
}
