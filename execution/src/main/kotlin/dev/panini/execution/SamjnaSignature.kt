package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator

/** Semantic value types used by saṃjñā signatures and overload resolution. */
enum class SamjnaValueType {
    SANKHYA,
    SHABDA,
    SUCHI,
}

data class SamjnaSignature(
    val argumentType: SamjnaValueType? = null,
)

/**
 * Compatibility boundary that compiles surface type guards into a typed signature.
 * Runtime dispatch consumes [SamjnaSignature] and does not inspect rule-body text.
 */
object SamjnaSignatureCompiler {
    private val typeMarkers = linkedMapOf(
        SamjnaValueType.SANKHYA to listOf("सङ्ख्या + त्व", "सङ्ख्यात्व"),
        SamjnaValueType.SHABDA to listOf("शब्द + त्व", "शब्दत्व"),
        SamjnaValueType.SUCHI to listOf("सूची + त्व", "सूचीत्व"),
    )

    fun compile(body: List<PvmScriptStatement.Sentence>): SamjnaSignature {
        val guardedTypes = body.asSequence()
            .filter { it.isNishedha }
            .mapNotNull { inferGuardType(it.text) }
            .distinct()
            .toList()
        return SamjnaSignature(argumentType = guardedTypes.singleOrNull())
    }

    fun inferGuardType(text: String): SamjnaValueType? =
        typeMarkers.entries.firstOrNull { (_, markers) -> markers.any(text::contains) }?.key
}

object SamjnaValueClassifier {
    private val sankhyaEvaluator = SankhyaEvaluator()

    fun classifyTerm(term: String): SamjnaValueType =
        if (term.toLongOrNull() != null ||
            runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull() != null
        ) {
            SamjnaValueType.SANKHYA
        } else {
            SamjnaValueType.SHABDA
        }
}

enum class SamjnaPrecedence(val rank: Int) {
    DEFAULT(0),
    NITYA(1),
    ANTARANGA(2),
    APAVADA(3),
}
