package dev.panini.execution

import dev.panini.core.SupAffix
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
    val resultAffix: SupAffix,
)

data class TaddhitaAttributeReference(
    val access: TaddhitaAttributeAccess,
    val padaRange: IntRange,
)

object TaddhitaStructEngine {

    private val parser = PaniniParser()

    /** Recognizes “the field of the possessor is value” from a finite copular clause. */
    fun detectFieldAssertion(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaFieldAssertion? {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return null
        val vakya = ukti.grammaticalVakyas().singleOrNull() ?: return null
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (
            verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS ||
            TingAffix.fromUpadesha(verb.ting.text) != TingAffix.TIP
        ) return null
        val owner = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada.vibhakti() == Vibhakti.SASTHI && pada.pratipadika.isMatup()
        } ?: return null
        val nominatives = vakya.padas.filter { pada ->
            pada !== owner && pada.vibhakti() == Vibhakti.PRATHAMA
        }
        if (nominatives.size != 2) return null
        val field = nominatives[0] as? SubantaPada ?: return null
        require(verb.lakara == dev.panini.core.Lakara.LAT &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty()) {
            "A record field assertion requires the underived present copula अस्ति."
        }
        require(vakya.padas.size == 4) {
            "A record field assertion cannot discard negation, extra participants, or modifiers."
        }
        return TaddhitaFieldAssertion(
            ownerStem = owner.pratipadika.baseIdentity(),
            fieldStem = field.stemIdentity(),
            valueStem = nominatives[1].stemIdentity(),
            valuePada = nominatives[1],
        )
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
    fun detectResultSchema(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaStructSchema? {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return null
        return detectNaturalResultSchema(ukti)
    }

    /**
     * Recognizes “X and Y are fields of Z” from ordinary case relations:
     * coordinated nominative fields, a genitive schema, nominative plural क्षेत्र,
     * and a plural finite form of अस्.
     */
    private fun detectNaturalResultSchema(ukti: Ukti): TaddhitaStructSchema? {
        val vakya = ukti.grammaticalVakyas().singleOrNull() ?: return null
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (
            verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS ||
            TingAffix.fromUpadesha(verb.ting.text) != TingAffix.JHI
        ) return null
        val role = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada.vibhakti() == Vibhakti.PRATHAMA &&
                (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                MulaPratipadikaIdentity.KSHETRA &&
                SupAffix.fromUpadesha(pada.sup.text)?.vacana == dev.panini.core.Vacana.BAHUVACANA
        } ?: return null
        val schema = vakya.padas.filterIsInstance<SubantaPada>().singleOrNull { pada ->
            pada !== role && pada.vibhakti() == Vibhakti.SASTHI
        } ?: return null
        require(verb.lakara == dev.panini.core.Lakara.LAT &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty()) {
            "A record field declaration requires the underived present copula सन्ति."
        }
        require((role.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "A derived क्षेत्र requires its own semantics, not a record field declaration."
        }
        require(vakya.padas.all { pada ->
            pada === verb || pada === role || pada === schema || when (pada) {
                is SubantaPada -> pada.vibhakti() == Vibhakti.PRATHAMA
                is SamuccitaSubanta -> pada.members.all { it.vibhakti() == Vibhakti.PRATHAMA }
                is AvyayaPada -> pada.form == "च"
                else -> false
            }
        }) { "A record field declaration cannot discard extra participants or modifiers." }
        val fields = vakya.padas.flatMap { pada ->
            when (pada) {
                is SamuccitaSubanta -> pada.members
                is SubantaPada -> listOf(pada)
                else -> emptyList()
            }
        }.filter { pada ->
            pada !== role && pada !== schema && pada.vibhakti() == Vibhakti.PRATHAMA
        }.map { it.stemIdentity() }
        return TaddhitaStructSchema(schema.pratipadika.referenceKey(), fields)
            .takeIf { fields.size >= 2 }
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

    fun detectAttributeAccess(vakya: Vakya): TaddhitaAttributeAccess? {
        val verb = vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.GRAH) return null
        val operands = vakya.padas.filter { it !== verb }
        if (operands.none { it is SubantaPada && it.vibhakti() == Vibhakti.SASTHI &&
                it.pratipadika.isMatup() }) return null
        val access = requireNotNull(detectAttributeAccess(operands)) {
            "Unsupported genitive field retrieval: extra participants or disconnected modifiers cannot be discarded."
        }
        require(access.resultAffix.vibhakti == Vibhakti.DVITIYA &&
            verb.lakara == dev.panini.core.Lakara.LOT &&
            TingAffix.fromUpadesha(verb.ting.text) == TingAffix.SIP &&
            verb.upasargas.isEmpty() && verb.dhatu.sanadiPratyayas.isEmpty()) {
            "Field retrieval requires one accusative field and the underived imperative गृहाण."
        }
        return access
    }

    fun detectAttributeAccess(padas: List<Pada>): TaddhitaAttributeAccess? {
        if (padas.size < 2 || padas.any { it !is SubantaPada }) return null
        val receivers = padas.dropLast(1).filterIsInstance<SubantaPada>()
        if (receivers.any { it.vibhakti() != Vibhakti.SASTHI || !it.pratipadika.isMatup() }) return null
        val chain = receivers
            .map { it.pratipadika.baseIdentity() }
        val key = padas.last() as SubantaPada
        val affix = SupAffix.fromUpadesha(key.sup.text) ?: return null
        return (chain + key.stemIdentity()).takeIf { receivers.isNotEmpty() }?.let {
            TaddhitaAttributeAccess(it, affix)
        }
    }

    /** Finds a leading genitive attribute expression embedded in a verbal condition. */
    fun detectAttributeReference(vakya: Vakya): TaddhitaAttributeReference? {
        val padas = vakya.padas
        val receiverIndices = padas.indices.filter { index ->
            val pada = padas[index] as? SubantaPada
            pada?.vibhakti() == Vibhakti.SASTHI && pada.pratipadika.isMatup()
        }
        val first = receiverIndices.firstOrNull() ?: return null
        var lastReceiver = first
        while (lastReceiver + 1 in receiverIndices) lastReceiver++
        val keyIndex = lastReceiver + 1
        require(padas.getOrNull(keyIndex) is SubantaPada) {
            "Unsupported disconnected genitive field phrase: intervening words cannot be discarded."
        }
        val access = detectAttributeAccess(padas.subList(first, keyIndex + 1)) ?: return null
        return TaddhitaAttributeReference(access, first..keyIndex)
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
            it is SubantaPada && it.vibhakti() == Vibhakti.SASTHI && it.pratipadika.isMatup()
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
            it is SubantaPada && it.vibhakti() == Vibhakti.SASTHI && it.pratipadika.isMatup()
        }
        if (receiverIndex < 0) return null
        val receiver = padas[receiverIndex] as SubantaPada
        val method = padas.drop(receiverIndex + 1).filterIsInstance<SubantaPada>()
            .firstOrNull { it.vibhakti() == Vibhakti.TRTIYA } ?: return null
        val karma = padas.take(receiverIndex).joinToString(" ") { it.canonicalSource() }
        return Triple(receiver.pratipadika.baseIdentity(), method.pratipadika.baseIdentity(), karma)
    }

    private fun parsed(text: String, supplied: Ukti?): Ukti? =
        supplied ?: runCatching { parser.parse(text.trim().trimEnd('।', '॥', ' ')) }.getOrNull()

    private fun Pada.vibhakti(): Vibhakti? = supText()?.let(SupAffix::fromUpadesha)?.vibhakti

    private fun Pada.supText(): String? = when (this) {
        is SubantaPada -> sup.text
        is SankhyaPada -> sup.text
        is SankhyaPuranaPada -> sup.text
        is KatapayadiPada -> sup.text
        is AryabhatiyaPada -> sup.text
        is BhutasamkhyaPada -> sup.text
        else -> null
    }

    private fun Pada.stemIdentity(): String = when (this) {
        is SubantaPada -> pratipadika.referenceKey()
        is SankhyaPada -> stems.joinToString("+")
        is SankhyaPuranaPada -> stems.joinToString("+")
        is SankhyaAbhyasaPada -> stems.joinToString("+")
        is KatapayadiPada -> word
        is AryabhatiyaPada -> word
        is BhutasamkhyaPada -> terms.joinToString("+")
        is SamuccitaSubanta -> members.joinToString("+") { it.pratipadika.semanticKey() }
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
