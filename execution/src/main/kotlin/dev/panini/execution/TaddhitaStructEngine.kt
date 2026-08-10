package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.Vakya
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * 5.2.94 तदस्यास्त्यस्मिन्निति मतुप्
 * Pāṇinian Taddhita Structs (मतुप् / वत्) and Genitive Attribute Access Engine.
 */
data class TaddhitaStruct(
    val nameStem: String,
    val attributes: Map<String, String>,
    val typedAttributes: Map<String, SanskritValue> = emptyMap(),
)

data class TaddhitaAttributeAccess(
    val chain: List<String>,
    val resultAffix: SupAffix,
)

object TaddhitaStructEngine {

    private val parser = PaniniParser()

    /**
     * Detects struct construction sentence ending with "<struct> + वत् + सुँ" or "<struct> + मत् + सुँ".
     * e.g. "दश + अम् मूल्य + अम् पञ्च + अम् परिमाण + अम् गुण + वत् + सुँ ।"
     */
    fun detectStructConstruction(sentenceText: String, preParsedUkti: Ukti? = null): TaddhitaStruct? {
        val ukti = parsed(sentenceText, preParsedUkti) ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val declaration = padas.filterIsInstance<SubantaPada>()
            .lastOrNull { it.vibhakti() == Vibhakti.PRATHAMA && it.pratipadika.isMatup() }
            ?: return null
        val karmaStems = padas.filter { it !== declaration && it.vibhakti() == Vibhakti.DVITIYA }
            .map { it.stemIdentity() }
        if (karmaStems.size < 2) return null
        return TaddhitaStruct(
            nameStem = declaration.pratipadika.baseIdentity(),
            attributes = karmaStems.chunked(2).mapNotNull { pair ->
                pair.takeIf { it.size == 2 }?.let { (value, key) -> key to value }
            }.toMap(),
        )
    }

    /**
     * Detects Genitive attribute access query: "<struct> + मतुप् + ङस् <key> + अम्"
     * e.g. "गुण + मतुप् + ङस् मूल्य + अम् ।"
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
        if (vakya.padas.any { it is TingantaPada }) return null
        return detectAttributeAccess(vakya.padas)
    }

    fun detectAttributeAccess(padas: List<Pada>): TaddhitaAttributeAccess? {
        val receivers = padas.filterIsInstance<SubantaPada>()
            .filter { it.vibhakti() == Vibhakti.SASTHI && it.pratipadika.isMatup() }
            .map { it.pratipadika.baseIdentity() }
        val key = padas.filterIsInstance<SubantaPada>().lastOrNull() ?: return null
        val affix = SupAffix.fromUpadesha(key.sup.text) ?: return null
        return (receivers + key.stemIdentity()).takeIf { receivers.isNotEmpty() }?.let {
            TaddhitaAttributeAccess(it, affix)
        }
    }

    /**
     * Detects struct method header definition: "<struct> + मतुप् + ङस् <method> + ल्युट् + सुँ"
     * e.g. "गुण + मतुप् + ङस् वृध् + ल्युट् + सुँ"
     */
    fun detectMethodHeader(headerName: String): Pair<String, String>? {
        val ukti = parsed(headerName, null) ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val receiverIndex = padas.indexOfFirst {
            it is SubantaPada && it.vibhakti() == Vibhakti.SASTHI && it.pratipadika.isMatup()
        }
        if (receiverIndex < 0) return null
        val receiver = padas[receiverIndex] as SubantaPada
        val method = padas.drop(receiverIndex + 1).filterIsInstance<SubantaPada>().firstOrNull() ?: return null
        return receiver.pratipadika.baseIdentity() to method.canonicalSource()
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
        is SubantaPada -> pratipadika.baseIdentity()
        else -> canonicalSource().substringBeforeLast(" + ${supText()}").trim()
    }

    private fun Pada.canonicalSource(): String = SamjnaInvocationMatcher.normalizeIdentity(sourceText)

    private fun SubantaPada.canonicalSource(): String =
        "${pratipadika.sourceText} + ${sup.text}".let(SamjnaInvocationMatcher::normalizeIdentity)

    private fun Pratipadika.isMatup(): Boolean =
        vikaras().any { it.pratyayaClass == TaddhitaPratyayaClass.POSSESSIVE }

    private fun Pratipadika.baseIdentity(): String = when (this) {
        is MulaPratipadika -> text
        else -> SamjnaInvocationMatcher.normalizeIdentity(sourceText).substringBefore(" + ").trim()
    }

    private fun Pratipadika.vikaras(): List<TaddhitaVikara> = when (this) {
        is dev.panini.vyakaranam.ast.MulaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
        is dev.panini.vyakaranam.ast.KridantaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
        is dev.panini.vyakaranam.ast.UnadyantaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
        is dev.panini.vyakaranam.ast.SamasaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
        is dev.panini.vyakaranam.ast.SankhyaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
    }
}
