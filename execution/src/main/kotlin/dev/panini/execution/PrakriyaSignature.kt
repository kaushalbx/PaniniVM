package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.execution.binding.referenceKey
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.core.TingAffix
import dev.panini.core.Lakara

/** Semantic value types used by saṃjñā signatures and overload resolution. */
enum class PrakriyaValueType {
    /** Internal absence of a type constraint, not a source-language type noun. */
    UNCONSTRAINED,
    SANKHYA,
    SHABDA,
    SUCHI,
    SIMA,
    RUPA,
    SATYA,
    LOPA,
    ;

    val sanskritName: String
        get() = when (this) {
            UNCONSTRAINED -> "unconstrained"
            SANKHYA -> "सङ्ख्या"
            SHABDA -> "शब्द"
            SUCHI -> "सूची"
            SIMA -> "सीमा"
            RUPA -> "रूप"
            SATYA -> "सत्य"
            LOPA -> "लोप"
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
) {
    init {
        requireUniqueParameters(parameters)
    }
}

internal fun requireUniqueParameters(parameters: List<PrakriyaParameter>) {
    val duplicates = parameters.groupingBy { it.nameStem }.eachCount().filterValues { it > 1 }.keys
    require(duplicates.isEmpty()) { "Procedure parameters declared more than once: $duplicates." }
}

/**
 * Compatibility boundary that compiles surface type guards into a typed signature.
 * Runtime dispatch consumes [PrakriyaSignature] and does not inspect rule-body text.
 */
object PrakriyaSignatureCompiler {
    fun compile(body: List<PvmScriptStatement.Sentence>, headerResult: SubantaPada? = null,
        headerPredicate: TingantaPada? = null): PrakriyaSignature {
        require((headerResult == null) == (headerPredicate == null)) {
            "A result-producing procedure header requires both its result and predicate."
        }
        if (headerResult != null) {
            require(headerResult.sup.supports(Vibhakti.DVITIYA, dev.panini.core.Vacana.EKAVACANA)) {
                "A procedure's declared product requires the accusative singular."
            }
        }
        if (headerResult != null) require(headerPredicate != null &&
            dev.panini.dhatupatha.DhatuPatha.all.any { it.denotesResultProduction(headerPredicate) }) {
            "A typed procedure header requires a result-producing declaration predicate."
        }
        val parameters = body.mapNotNull(PrakriyaSignatureDeclarationParser::parameter)
        val resultDeclarations = listOfNotNull(headerResult?.let(PrakriyaSignatureDeclarationParser::result)) +
            body.mapNotNull(PrakriyaSignatureDeclarationParser::result)
        requireUniqueParameters(parameters)
        require(resultDeclarations.size <= 1) { "A प्रक्रिया may declare only one result." }
        val resultType = resultDeclarations.singleOrNull()?.type
        val resultSchema = resultDeclarations.singleOrNull()?.schema
        val guardedTypes = body.asSequence()
            .filter { it.isNishedha }
            .mapNotNull(::inferGuardType)
            .distinct()
            .toList()
        require(guardedTypes.size <= 1) { "Procedure has conflicting argument type guards: $guardedTypes." }
        return PrakriyaSignature(
            argumentType = guardedTypes.singleOrNull(),
            parameters = parameters,
            resultType = resultType,
            resultSchema = resultSchema,
        )
    }

    /** Reads a type predicate from its parsed भाववाचक `त्व` derivation. */
    fun inferGuardType(sentence: PvmScriptStatement.Sentence): PrakriyaValueType? {
        val padas = sentence.ukti?.grammaticalVakyas()?.singleOrNull()?.padas ?: return null
        val negations = padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>()
            .filter { it.function == dev.panini.vyakaranam.ast.AvyayaFunction.NISHEDHA }
        if (negations.size != 1) return null
        val nominals = padas.filterIsInstance<SubantaPada>()
        if (nominals.size != 2 || padas.size != 3 || nominals.any {
                !it.sup.supports(Vibhakti.DVITIYA)
            }) return null
        return nominals.mapNotNull { pada ->
                val stem = pada.pratipadika as? MulaPratipadika ?: return@mapNotNull null
                val derivation = stem.vikaras.singleOrNull() as? TaddhitaVikara
                if (derivation?.pratyayaClass != TaddhitaPratyayaClass.BHAVA) {
                    return@mapNotNull null
                }
                when (stem.lexicalIdentity) {
                    MulaPratipadikaIdentity.SANKHYA -> PrakriyaValueType.SANKHYA
                    MulaPratipadikaIdentity.SHABDA -> PrakriyaValueType.SHABDA
                    MulaPratipadikaIdentity.SUCHI -> PrakriyaValueType.SUCHI
                    else -> null
                }
            }
            .singleOrNull()
    }
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

    fun parameterDeclaration(sentence: PvmScriptStatement.Sentence): SubantaPada? =
        parameter(sentence)?.let { declarationPadas(sentence)?.first?.firstOrNull() }

    fun result(sentence: PvmScriptStatement.Sentence): ResultDeclaration? =
        (declaration(sentence) as? Declaration.Result)?.value

    private fun parameterOrNull(sentence: PvmScriptStatement.Sentence): PrakriyaParameter? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.lexicalIdentity() != MulaPratipadikaIdentity.MANA || declared.size != 2) return null
        val parameterName = declared[0].pratipadika.referenceKey()
        val parameterType = declared[1].lexicalIdentity()?.let(::typeOrNull) ?: return null
        return PrakriyaParameter(parameterName, parameterType)
    }

    private fun resultOrNull(sentence: PvmScriptStatement.Sentence): ResultDeclaration? {
        val (declared, marker) = declarationPadas(sentence) ?: return null
        if (marker.lexicalIdentity() != MulaPratipadikaIdentity.PARINAMA || declared.size != 1) return null
        return result(declared.single())
    }

    fun result(pada: SubantaPada): ResultDeclaration =
        pada.lexicalIdentity()?.let(::typeOrNull)?.let { ResultDeclaration(type = it) }
            ?: ResultDeclaration(schema = pada.pratipadika.referenceKey())

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
        if (declared.size != declaration.declaredPadas.size) return null
        val marker = declaration.nominativeMarker ?: return null
        val reporting = declaration.markerPadas.filter { it !== marker }
        if (reporting.isNotEmpty()) {
            val copula = reporting.singleOrNull() as? TingantaPada ?: return null
            if (copula.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS ||
                copula.upasargas.isNotEmpty() || copula.dhatu.sanadiPratyayas.isNotEmpty() ||
                copula.lakara != Lakara.LAT || copula.ting.affix != TingAffix.TIP
            ) return null
        }
        // These contracts declare one named slot or one result, even when its
        // payload is a collection. Plural morphology is not a singular contract.
        if ((declared + marker).any {
                !it.sup.supports(Vibhakti.PRATHAMA, dev.panini.core.Vacana.EKAVACANA)
            }) {
            return null
        }
        return declared to marker
    }

    private fun SubantaPada.lexicalIdentity(): MulaPratipadikaIdentity? =
        (pratipadika as? MulaPratipadika)?.takeIf { it.vikaras.isEmpty() }?.lexicalIdentity
}

object PrakriyaValueClassifier {
    private val sankhyaEvaluator = SankhyaEvaluator()

    /** Parsed numeric meaning used only after contextual reference lookup fails. */
    fun numericFallback(pada: dev.panini.vyakaranam.ast.Pada): SanskritValue.Sankhya? =
        NominalValueAnalysis.numericFallback(pada)

    /** Literal evidence precedes environment lookup; lexical ordinals remain contextual. */
    fun literalValue(pada: dev.panini.vyakaranam.ast.Pada): SanskritValue? =
        NominalValueAnalysis.literalValue(pada)

    /** Classification names a payload kind; acceptance additionally checks its validity. */
    fun accepts(expected: PrakriyaValueType, value: SanskritValue): Boolean = when (expected) {
        PrakriyaValueType.UNCONSTRAINED -> true
        PrakriyaValueType.SANKHYA -> OperationValueType.NUMBER.accepts(value)
        else -> classifyValue(value) == expected
    }

    /** A nominal spelling may name a runtime referent; only literal evidence establishes its type. */
    fun classifyStaticPada(pada: dev.panini.vyakaranam.ast.Pada): PrakriyaValueType =
        literalValue(pada)?.let(::classifyValue)
            ?: PrakriyaValueType.UNCONSTRAINED

    fun classifyTerm(term: String): PrakriyaValueType =
        if (term.toLongOrNull() != null ||
            runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull() != null
        ) {
            PrakriyaValueType.SANKHYA
        } else {
            PrakriyaValueType.SHABDA
        }

    fun classifyValue(value: SanskritValue): PrakriyaValueType = when (value) {
        is SanskritValue.Sankhya, is SanskritValue.Rational -> PrakriyaValueType.SANKHYA
        is SanskritValue.Suchi, is SanskritValue.Gana -> PrakriyaValueType.SUCHI
        is SanskritValue.Shabda -> PrakriyaValueType.SHABDA
        is SanskritValue.Range -> PrakriyaValueType.SIMA
        is SanskritValue.Rupa -> PrakriyaValueType.RUPA
        is SanskritValue.Satya -> PrakriyaValueType.SATYA
        SanskritValue.Lopa -> PrakriyaValueType.LOPA
    }

    fun classifyPada(pada: dev.panini.vyakaranam.ast.Pada): PrakriyaValueType =
        (literalValue(pada) ?: numericFallback(pada))?.let(::classifyValue)
            ?: PrakriyaValueType.SHABDA
}
