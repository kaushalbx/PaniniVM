package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.Vakya
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.SemanticDiagnostic
import dev.panini.vyakaranam.ast.relationSpan
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.execution.binding.NumeralAstNormalizer
import dev.panini.execution.binding.referenceKey
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.core.TingAffix

/**
 * 5.2.94 तदस्यास्त्यस्मिन्निति मतुप्
 * Pāṇinian Taddhita Structs (मतुप् / वत्) and Genitive Attribute Access Engine.
 */
data class TaddhitaStruct(
    val nameStem: String,
    val attributes: Map<String, String>,
    val typedAttributes: Map<String, SanskritValue> = emptyMap(),
)

data class TaddhitaStructSchema(
    val nameStem: String,
    val fields: List<String>,
) {
    init {
        require(fields.distinct().size == fields.size) {
            "Duplicate fields in Panini schema '$nameStem': ${fields.groupingBy { it }.eachCount().filterValues { it > 1 }.keys}."
        }
    }
}

/** One copular fact: the nominative [fieldStem] of genitive [ownerStem] is [valueStem]. */
data class TaddhitaFieldAssertion(
    val ownerStem: String,
    val fieldStem: String,
    val valueStem: String,
    val valuePada: Pada,
)

data class TaddhitaAttributeAccess(
    val chain: List<String>,
    val resultSup: dev.panini.vyakaranam.ast.SupPratyaya,
)

data class TaddhitaAttributeReference(
    val access: TaddhitaAttributeAccess,
    val padaRange: IntRange,
)

object TaddhitaStructEngine {
    data class FieldAssertionDiagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface FieldAssertionAnalysis {
        data object NotAssertion : FieldAssertionAnalysis
        data class Resolved(val assertion: TaddhitaFieldAssertion) : FieldAssertionAnalysis
        data class Invalid(val diagnostic: FieldAssertionDiagnostic) : FieldAssertionAnalysis
    }

    data class ResultSchemaDiagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface ResultSchemaAnalysis {
        data object NotSchema : ResultSchemaAnalysis
        data class Resolved(val schema: TaddhitaStructSchema) : ResultSchemaAnalysis
        data class Invalid(val diagnostic: ResultSchemaDiagnostic) : ResultSchemaAnalysis
    }

    data class AttributeAccessDiagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface AttributeAccessAnalysis {
        data object NotAccess : AttributeAccessAnalysis
        data class Resolved(val access: TaddhitaAttributeAccess) : AttributeAccessAnalysis
        data class Invalid(val diagnostic: AttributeAccessDiagnostic) : AttributeAccessAnalysis
    }

    data class AttributeReferenceDiagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface AttributeReferenceAnalysis {
        data object NotReference : AttributeReferenceAnalysis
        data class Resolved(val reference: TaddhitaAttributeReference) : AttributeReferenceAnalysis
        data class Invalid(val diagnostic: AttributeReferenceDiagnostic) : AttributeReferenceAnalysis
    }

    private val parser = PaniniParser()

    /** Recognizes “the field of the possessor is value” from a finite copular clause. */
    fun analyzeFieldAssertion(sentenceText: String, preParsedUkti: Ukti? = null): FieldAssertionAnalysis {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return FieldAssertionAnalysis.NotAssertion
        val vakya = ukti.grammaticalVakyas().singleOrNull() ?: return FieldAssertionAnalysis.NotAssertion
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull()
            ?: return FieldAssertionAnalysis.NotAssertion
        if (
            verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS ||
            verb.ting.affix != TingAffix.TIP
        ) return FieldAssertionAnalysis.NotAssertion
        val owner = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada.hasVibhakti(Vibhakti.SASTHI) && pada.pratipadika.isMatup()
        } ?: return FieldAssertionAnalysis.NotAssertion
        val nominatives = vakya.padas.filter { pada ->
            pada !== owner && pada.hasVibhakti(Vibhakti.PRATHAMA)
        }
        if (nominatives.size != 2) return FieldAssertionAnalysis.NotAssertion
        val field = nominatives[0] as? SubantaPada ?: return FieldAssertionAnalysis.NotAssertion
        fun invalid(code: String, message: String) = FieldAssertionAnalysis.Invalid(FieldAssertionDiagnostic(
            code, message, Invocation(vakya).relationSpan(), sentenceText))
        if (!(verb.lakara == dev.panini.core.Lakara.LAT &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty())) return invalid(
            "FIELD_ASSERTION_COPULA_MORPHOLOGY",
            "A record field assertion requires the underived present copula अस्ति.",
        )
        if (vakya.padas.size != 4) return invalid(
            "FIELD_ASSERTION_FRAME",
            "A record field assertion cannot discard negation, extra participants, or modifiers.",
        )
        return FieldAssertionAnalysis.Resolved(TaddhitaFieldAssertion(
            ownerStem = owner.pratipadika.baseIdentity(),
            fieldStem = field.stemIdentity(),
            valueStem = nominatives[1].stemIdentity(),
            valuePada = nominatives[1],
        ))
    }

    fun detectFieldAssertion(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaFieldAssertion? = when (
        val analysis = analyzeFieldAssertion(sentenceText, preParsedUkti)
    ) {
        FieldAssertionAnalysis.NotAssertion -> null
        is FieldAssertionAnalysis.Resolved -> analysis.assertion
        is FieldAssertionAnalysis.Invalid -> throw PvmSemanticException(
            ExecutionDiagnostic.from(analysis.diagnostic))
    }

    /** Interprets an asserted predicate nominal from its parsed morphology. */
    fun assertionValue(assertion: TaddhitaFieldAssertion): SanskritValue = when (val pada = assertion.valuePada) {
        is SubantaPada -> NumeralAstNormalizer.resolve(pada.pratipadika)?.semanticValue
            ?: when (val pratipadika = pada.pratipadika) {
                is MulaPratipadika -> nominalTruthValue(pratipadika)
                    ?: SanskritValue.Shabda(pratipadika.referenceKey())
                else -> SanskritValue.Shabda(pratipadika.referenceKey())
            }
        is SankhyaPada -> {
            val number = pada.value ?: dev.panini.sankhya.SankhyaEvaluator().evaluateStems(pada.stems).value
            SanskritValue.Sankhya(number, assertion.valueStem)
        }
        is SankhyaPuranaPada -> SanskritValue.Sankhya(
            requireNotNull(pada.value) { "An ordinal field value must retain its numeric identity." },
            assertion.valueStem,
        )
        is KatapayadiPada -> SanskritValue.Sankhya(requireNotNull(pada.value), assertion.valueStem)
        is AryabhatiyaPada -> SanskritValue.Sankhya(requireNotNull(pada.value), assertion.valueStem)
        is BhutasamkhyaPada -> SanskritValue.Sankhya(requireNotNull(pada.value), assertion.valueStem)
        else -> SanskritValue.Shabda(assertion.valueStem)
    }

    /** Declares the field order of the automatic परिणाम value without assigning field values. */
    fun analyzeResultSchema(sentenceText: String, preParsedUkti: Ukti? = null): ResultSchemaAnalysis {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return ResultSchemaAnalysis.NotSchema
        return analyzeNaturalResultSchema(ukti, sentenceText)
    }

    fun detectResultSchema(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaStructSchema? = when (
        val analysis = analyzeResultSchema(sentenceText, preParsedUkti)
    ) {
        ResultSchemaAnalysis.NotSchema -> null
        is ResultSchemaAnalysis.Resolved -> analysis.schema
        is ResultSchemaAnalysis.Invalid -> throw PvmSemanticException(
            ExecutionDiagnostic.from(analysis.diagnostic))
    }

    /**
     * Recognizes “X and Y are fields of Z” from ordinary case relations:
     * coordinated nominative fields, a genitive schema, nominative plural क्षेत्र,
     * and a plural finite form of अस्.
     */
    private fun analyzeNaturalResultSchema(ukti: Ukti, sourceText: String): ResultSchemaAnalysis {
        val vakya = ukti.grammaticalVakyas().singleOrNull() ?: return ResultSchemaAnalysis.NotSchema
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull()
            ?: return ResultSchemaAnalysis.NotSchema
        if (
            verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS ||
            verb.ting.affix != TingAffix.JHI
        ) return ResultSchemaAnalysis.NotSchema
        val role = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada.sup.supports(Vibhakti.PRATHAMA, dev.panini.core.Vacana.BAHUVACANA) &&
                (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                MulaPratipadikaIdentity.KSHETRA
        } ?: return ResultSchemaAnalysis.NotSchema
        val schema = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada !== role && pada.hasVibhakti(Vibhakti.SASTHI)
        } ?: return ResultSchemaAnalysis.NotSchema
        fun invalid(code: String, message: String) = ResultSchemaAnalysis.Invalid(ResultSchemaDiagnostic(
            code, message, Invocation(vakya).relationSpan(), sourceText))
        if (!(verb.lakara == dev.panini.core.Lakara.LAT &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty())) return invalid(
            "RESULT_SCHEMA_COPULA_MORPHOLOGY",
            "A record field declaration requires the underived present copula सन्ति.",
        )
        if ((role.pratipadika as MulaPratipadika).vikaras.isNotEmpty()) return invalid(
            "RESULT_SCHEMA_DERIVED_ROLE",
            "A derived क्षेत्र requires its own semantics, not a record field declaration.",
        )
        if (!vakya.padas.all { pada ->
            pada === verb || pada === role || pada === schema || when (pada) {
                is SubantaPada -> pada.hasVibhakti(Vibhakti.PRATHAMA)
                is SamuccitaSubanta -> pada.members.all { it.hasVibhakti(Vibhakti.PRATHAMA) }
                is AvyayaPada -> pada.form == "च"
                else -> false
            }
        }) return invalid(
            "RESULT_SCHEMA_FRAME",
            "A record field declaration cannot discard extra participants or modifiers.",
        )
        val fields = vakya.padas.flatMap { pada ->
            when (pada) {
                is SamuccitaSubanta -> pada.members
                is SubantaPada -> listOf(pada)
                else -> emptyList()
            }
        }.filter { pada ->
            pada !== role && pada !== schema && pada.hasVibhakti(Vibhakti.PRATHAMA)
        }.map { it.stemIdentity() }
        if (fields.size < 2) return invalid(
            "RESULT_SCHEMA_FIELD_CARDINALITY",
            "A record field declaration requires at least two fields.",
        )
        val duplicates = fields.groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        if (duplicates.isNotEmpty()) return invalid(
            "RESULT_SCHEMA_DUPLICATE_FIELDS",
            "Duplicate fields in Panini schema '${schema.pratipadika.referenceKey()}': $duplicates.",
        )
        return ResultSchemaAnalysis.Resolved(TaddhitaStructSchema(schema.pratipadika.referenceKey(), fields))
    }

    /**
     * Detects a finite genitive attribute request: “obtain the key of the possessor”.
     */
    fun detectAttributeAccess(sentenceText: String, preParsedUkti: Ukti? = null): Pair<String, String>? {
        val chain = detectNestedAttributeAccess(sentenceText, preParsedUkti) ?: return null
        return chain.takeIf { it.size == 2 }?.let { it[0] to it[1] }
    }

    /**
     * Detects Multi-level Nested Genitive attribute access query (Sūtra 1.1.49 षष्ठी स्थानेयोगा):
     * e.g. "गाणित + मतुप् + ङस् सङ्ख्या + मतुप् + ङस् मूल्य + अम् ।" -> ["गाणित", "सङ्ख्या", "मूल्य"]
     */
    fun detectNestedAttributeAccess(sentenceText: String, preParsedUkti: Ukti? = null): List<String>? {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return null
        return ukti.grammaticalVakyas().singleOrNull()?.let(::detectAttributeAccess)?.chain
    }

    fun analyzeAttributeAccess(vakya: Vakya): AttributeAccessAnalysis {
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull()
            ?: return AttributeAccessAnalysis.NotAccess
        if (verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.GRAH) return AttributeAccessAnalysis.NotAccess
        val operands = vakya.padas.filter { it !== verb }
        if (operands.none { it is SubantaPada && it.hasVibhakti(Vibhakti.SASTHI) &&
                it.pratipadika.isMatup() }) return AttributeAccessAnalysis.NotAccess
        fun invalid(code: String, message: String) = AttributeAccessAnalysis.Invalid(AttributeAccessDiagnostic(
            code, message, Invocation(vakya).relationSpan(), vakya.sourceText))
        val access = detectAttributeAccess(operands) ?: return invalid(
            "ATTRIBUTE_ACCESS_FRAME",
            "Unsupported genitive field retrieval: extra participants or disconnected modifiers cannot be discarded.",
        )
        if (!(access.resultSup.supports(Vibhakti.DVITIYA) &&
            verb.lakara == dev.panini.core.Lakara.LOT &&
            verb.ting.affix == TingAffix.SIP &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty())) return invalid(
            "ATTRIBUTE_ACCESS_MORPHOLOGY",
            "Field retrieval requires one accusative field and the underived imperative गृहाण.",
        )
        return AttributeAccessAnalysis.Resolved(access)
    }

    fun detectAttributeAccess(vakya: Vakya): TaddhitaAttributeAccess? = when (
        val analysis = analyzeAttributeAccess(vakya)
    ) {
        AttributeAccessAnalysis.NotAccess -> null
        is AttributeAccessAnalysis.Resolved -> analysis.access
        is AttributeAccessAnalysis.Invalid -> throw PvmSemanticException(
            ExecutionDiagnostic.from(analysis.diagnostic))
    }

    fun detectAttributeAccess(padas: List<Pada>): TaddhitaAttributeAccess? {
        if (padas.size < 2 || padas.any { it !is SubantaPada }) return null
        val receivers = padas.dropLast(1).filterIsInstance<SubantaPada>()
        if (receivers.any { !it.hasVibhakti(Vibhakti.SASTHI) || !it.pratipadika.isMatup() }) return null
        val chain = receivers
            .map { it.pratipadika.baseIdentity() }
        val key = padas.last() as SubantaPada
        if (key.sup.affixCandidates.isEmpty()) return null
        return (chain + key.stemIdentity()).takeIf { receivers.isNotEmpty() }?.let {
            TaddhitaAttributeAccess(it, key.sup)
        }
    }

    /** Finds a leading genitive attribute expression embedded in a verbal condition. */
    fun analyzeAttributeReference(
        vakya: Vakya,
        excludedReceivers: Set<Pada> = emptySet(),
    ): AttributeReferenceAnalysis {
        // A recognized member selector belongs to the collection frame; मतुप्
        // on its whole does not turn that relation into record field access.
        if (CollectionMemberMorphology.protectedPadas(vakya.padas).isNotEmpty())
            return AttributeReferenceAnalysis.NotReference
        val padas = vakya.padas
        val receiverIndices = padas.indices.filter { index ->
            val pada = padas[index] as? SubantaPada
            pada != null && pada.hasVibhakti(Vibhakti.SASTHI) && pada.pratipadika.isMatup() &&
                excludedReceivers.none { it === pada }
        }
        val first = receiverIndices.firstOrNull() ?: return AttributeReferenceAnalysis.NotReference
        var lastReceiver = first
        while (lastReceiver + 1 in receiverIndices) lastReceiver++
        val keyIndex = lastReceiver + 1
        fun invalid(code: String, message: String) = AttributeReferenceAnalysis.Invalid(
            AttributeReferenceDiagnostic(code, message, Invocation(vakya).relationSpan(), vakya.sourceText))
        if (padas.getOrNull(keyIndex) !is SubantaPada) return invalid(
            "ATTRIBUTE_REFERENCE_DISCONNECTED",
            "Unsupported disconnected genitive field phrase: intervening words cannot be discarded.",
        )
        val access = detectAttributeAccess(padas.subList(first, keyIndex + 1)) ?: return invalid(
            "ATTRIBUTE_REFERENCE_FRAME",
            "A genitive field reference requires one contiguous possessor chain and field.",
        )
        return AttributeReferenceAnalysis.Resolved(TaddhitaAttributeReference(access, first..keyIndex))
    }

    fun detectAttributeReference(vakya: Vakya, excludedReceivers: Set<Pada> = emptySet()): TaddhitaAttributeReference? =
        when (val analysis = analyzeAttributeReference(vakya, excludedReceivers)) {
            AttributeReferenceAnalysis.NotReference -> null
            is AttributeReferenceAnalysis.Resolved -> analysis.reference
            is AttributeReferenceAnalysis.Invalid -> throw PvmSemanticException(
                ExecutionDiagnostic.from(analysis.diagnostic))
        }

    /**
     * Detects struct method header definition: "<struct> + मतुप् + ङस् <method> + ल्युट् + सुँ"
     * e.g. "गुण + मतुप् + ङस् वृध् + ल्युट् + सुँ"
     */
    fun detectMethodHeader(headerName: String): Pair<String, String>? {
        val ukti = parsed(headerName, null) ?: return null
        return detectMethodHeader(ukti.grammaticalVakyas().flatMap { it.padas })
            ?.let { it.first to it.second.canonicalSource() }
    }

    /** Retains the parsed method nominal when the declaration AST already exists. */
    fun detectMethodHeader(padas: List<Pada>): Pair<String, SubantaPada>? {
        val receiverIndex = padas.indexOfFirst {
            it is SubantaPada && it.hasVibhakti(Vibhakti.SASTHI) && it.pratipadika.isMatup()
        }
        if (receiverIndex < 0) return null
        val receiver = padas[receiverIndex] as SubantaPada
        val method = padas.drop(receiverIndex + 1).filterIsInstance<SubantaPada>().firstOrNull() ?: return null
        return receiver.pratipadika.baseIdentity() to method
    }

    /**
     * Detects struct method invocation: "<karma> <struct> + मतुप् + ङस् <method> + ल्युट् + टा कृ"
     * e.g. "पञ्च + अम् गुण + मतुप् + ङस् वृध् + ल्युट् + टा कृ + लोट् + सिप्"
     */
    fun detectMethodInvocation(sentenceText: String): Triple<String, String, String>? {
        val ukti = parsed(sentenceText, null) ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val receiverIndex = padas.indexOfFirst {
            it is SubantaPada && it.hasVibhakti(Vibhakti.SASTHI) && it.pratipadika.isMatup()
        }
        if (receiverIndex < 0) return null
        val receiver = padas[receiverIndex] as SubantaPada
        val method = padas.drop(receiverIndex + 1).filterIsInstance<SubantaPada>()
            .firstOrNull { it.hasVibhakti(Vibhakti.TRTIYA) } ?: return null
        val karma = padas.take(receiverIndex).joinToString(" ") { it.canonicalSource() }
        return Triple(receiver.pratipadika.baseIdentity(), method.pratipadika.baseIdentity(), karma)
    }

    private fun parsed(text: String, supplied: Ukti?): Ukti? =
        supplied ?: runCatching { parser.parse(text.trim().trimEnd('।', '॥', ' ')) }.getOrNull()

    private fun Pada.hasVibhakti(vibhakti: Vibhakti): Boolean =
        (this as? dev.panini.vyakaranam.ast.SupBearingPada)?.sup?.supports(vibhakti) == true

    private fun Pada.stemIdentity(): String = when (this) {
        is SubantaPada -> pratipadika.referenceKey()
        is SankhyaPada -> stems.joinToString("+")
        is SankhyaPuranaPada -> stems.joinToString("+")
        is SankhyaAbhyasaPada -> stems.joinToString("+")
        is KatapayadiPada -> word
        is AryabhatiyaPada -> word
        is BhutasamkhyaPada -> terms.joinToString("+")
        is SamuccitaSubanta -> members.joinToString("+") { it.stemIdentity() }
        is dev.panini.vyakaranam.ast.ParyantaRangePada ->
            "${lowerLimit.stems.joinToString("+")}..${upperLimit.stems.joinToString("+")}"
        is AvyayaPada -> form
        is TingantaPada -> buildList {
            addAll(upasargas)
            add(dhatu.mulaDhatu)
            addAll(dhatu.sanadiPratyayas)
            add(lakara.upadesha)
            add(ting.text)
        }.joinToString("+")
    }

    private fun Pada.canonicalSource(): String = PrakriyaInvocationMatcher.normalizeIdentity(sourceText)

    private fun SubantaPada.canonicalSource(): String =
        "${pratipadika.sourceText} + ${sup.text}".let(PrakriyaInvocationMatcher::normalizeIdentity)

    private fun Pratipadika.isMatup(): Boolean =
        (allVikaras().lastOrNull() as? TaddhitaVikara)?.pratyayaClass == TaddhitaPratyayaClass.POSSESSIVE

    private fun Pratipadika.baseIdentity(): String {
        if (!isMatup()) return referenceKey()
        val remaining = allVikaras().dropLast(1)
        val base = when (this) {
            is MulaPratipadika -> copy(vikaras = remaining)
            is dev.panini.vyakaranam.ast.KridantaPratipadika -> copy(vikaras = remaining)
            is dev.panini.vyakaranam.ast.UnadyantaPratipadika -> copy(vikaras = remaining)
            is dev.panini.vyakaranam.ast.SamasaPratipadika -> copy(vikaras = remaining)
            is dev.panini.vyakaranam.ast.SankhyaPratipadika -> copy(vikaras = remaining)
        }
        return base.referenceKey()
    }

    private fun Pratipadika.allVikaras(): List<dev.panini.vyakaranam.ast.PratipadikaVikara> = when (this) {
        is MulaPratipadika -> vikaras
        is dev.panini.vyakaranam.ast.KridantaPratipadika -> vikaras
        is dev.panini.vyakaranam.ast.UnadyantaPratipadika -> vikaras
        is dev.panini.vyakaranam.ast.SamasaPratipadika -> vikaras
        is dev.panini.vyakaranam.ast.SankhyaPratipadika -> vikaras
    }

}
