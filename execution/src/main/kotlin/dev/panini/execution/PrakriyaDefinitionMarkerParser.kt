package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaLexicalIdentity
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.invocations
import dev.panini.vyakaranam.parser.PaniniParser

enum class PrakriyaDefinitionQualifier {
    PRAKRIYA,
    APAVADA,
    NITYA,
    ANTARANGA,
}

data class ParsedPrakriyaQualifiers(
    val declarationSource: String,
    val qualifiers: Set<PrakriyaDefinitionQualifier>,
)

/** Recognizes explicit prakriyā-definition qualifiers following इति. */
object PrakriyaDefinitionMarkerParser {
    private val parser = PaniniParser()

    fun hasExplicitMarker(source: String): Boolean {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return false
        val quotation = ukti.body as? Quotation
        if (quotation != null) {
            return quotation.reporting.invocations().flatMap { it.vakya.padas }
                .filterIsInstance<SubantaPada>()
                .any { it.definitionQualifier() == PrakriyaDefinitionQualifier.PRAKRIYA }
        }
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val itiIndices = padas.indices.filter { index ->
            (padas[index] as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        }
        return itiIndices.any { itiIndex ->
            padas.drop(itiIndex + 1).filterIsInstance<SubantaPada>()
                .any { it.definitionQualifier() == PrakriyaDefinitionQualifier.PRAKRIYA }
        }
    }

    /** Returns the declaration prefix before the final explicit marker. */
    fun headerPrefix(source: String): String? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val quotation = ukti.body as? Quotation
        if (quotation != null && quotation.reporting.invocations().flatMap { it.vakya.padas }
                .filterIsInstance<SubantaPada>()
                .any { it.definitionQualifier() == PrakriyaDefinitionQualifier.PRAKRIYA }
        ) {
            return quotation.quoted.vakya.padas
                .joinToString(" ") { PrakriyaInvocationMatcher.normalizeIdentity(it.sourceText) }
                .ifBlank { null }
        }
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val markerIndex = padas.indices.lastOrNull { index ->
            (padas[index] as? SubantaPada)?.definitionQualifier() == PrakriyaDefinitionQualifier.PRAKRIYA
        } ?: return null
        val itiIndex = (0 until markerIndex).lastOrNull { index ->
            (padas[index] as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        } ?: return null
        return padas.take(itiIndex)
            .joinToString(" ") { PrakriyaInvocationMatcher.normalizeIdentity(it.sourceText) }
            .ifBlank { null }
    }

    fun qualifiers(source: String): ParsedPrakriyaQualifiers? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val quotation = ukti.body as? Quotation
        if (quotation != null) {
            val qualifierPadas = quotation.reporting.invocations().flatMap { it.vakya.padas }
            val declarationSource = quotation.quoted.vakya.padas
                .joinToString(" ") { PrakriyaInvocationMatcher.normalizeIdentity(it.sourceText) }
                .ifBlank { return null }
            return ParsedPrakriyaQualifiers(
                declarationSource = declarationSource,
                qualifiers = qualifierPadas.filterIsInstance<SubantaPada>()
                    .mapNotNull { it.definitionQualifier() }.toSet(),
            )
        }
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val firstItiIndex = padas.indexOfFirst {
            (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        }
        val declarationPadas = if (firstItiIndex >= 0) padas.take(firstItiIndex) else padas
        val qualifierPadas = if (firstItiIndex >= 0) padas.drop(firstItiIndex + 1) else emptyList()
        val declarationSource = declarationPadas
            .joinToString(" ") { PrakriyaInvocationMatcher.normalizeIdentity(it.sourceText) }
            .ifBlank { return null }
        return ParsedPrakriyaQualifiers(
            declarationSource = declarationSource,
            qualifiers = qualifierPadas
                .filterIsInstance<SubantaPada>()
                .mapNotNull { it.definitionQualifier() }
                .toSet(),
        )
    }

    private fun SubantaPada.definitionQualifier(): PrakriyaDefinitionQualifier? {
        if (SupAffix.fromUpadesha(sup.text)?.vibhakti != Vibhakti.PRATHAMA) return null
        return when (val base = pratipadika) {
            is MulaPratipadika -> when (base.lexicalIdentity) {
                MulaPratipadikaIdentity.PRAKRIYA -> PrakriyaDefinitionQualifier.PRAKRIYA
                MulaPratipadikaIdentity.APAVADA -> PrakriyaDefinitionQualifier.APAVADA
                MulaPratipadikaIdentity.NITYA -> PrakriyaDefinitionQualifier.NITYA
                MulaPratipadikaIdentity.ANTARANGA -> PrakriyaDefinitionQualifier.ANTARANGA
                else -> null
            }
            is KridantaPratipadika -> PrakriyaDefinitionQualifier.APAVADA.takeIf {
                base.lexicalIdentity == KridantaLexicalIdentity.APAVADA
            }
            else -> null
        }
    }
}
