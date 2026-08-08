package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaLexicalIdentity
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

enum class SamjnaDefinitionQualifier {
    SAMJNA,
    APAVADA,
    NITYA,
    ANTARANGA,
}

data class ParsedSamjnaQualifiers(
    val declarationSource: String,
    val qualifiers: Set<SamjnaDefinitionQualifier>,
)

/** Recognizes explicit saṃjñā-definition qualifiers following इति. */
object SamjnaDefinitionMarkerParser {
    private val parser = PaniniParser()

    fun hasExplicitMarker(source: String): Boolean {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return false
        val padas = ukti.vakyas.flatMap { it.padas }
        val itiIndices = padas.indices.filter { index ->
            (padas[index] as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        }
        return itiIndices.any { itiIndex ->
            padas.drop(itiIndex + 1).filterIsInstance<SubantaPada>().any { it.definitionQualifier() != null }
        }
    }

    /** Returns the declaration prefix before the final explicit marker. */
    fun headerPrefix(source: String): String? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val padas = ukti.vakyas.flatMap { it.padas }
        val markerIndex = padas.indices.lastOrNull { index ->
            (padas[index] as? SubantaPada)?.definitionQualifier() != null
        } ?: return null
        val itiIndex = (0 until markerIndex).lastOrNull { index ->
            (padas[index] as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        } ?: return null
        return padas.take(itiIndex)
            .joinToString(" ") { SamjnaInvocationMatcher.normalizeIdentity(it.sourceText) }
            .ifBlank { null }
    }

    fun qualifiers(source: String): ParsedSamjnaQualifiers? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val padas = ukti.vakyas.flatMap { it.padas }
        val firstItiIndex = padas.indexOfFirst {
            (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE
        }
        val declarationPadas = if (firstItiIndex >= 0) padas.take(firstItiIndex) else padas
        val qualifierPadas = if (firstItiIndex >= 0) padas.drop(firstItiIndex + 1) else emptyList()
        val declarationSource = declarationPadas
            .joinToString(" ") { SamjnaInvocationMatcher.normalizeIdentity(it.sourceText) }
            .ifBlank { return null }
        return ParsedSamjnaQualifiers(
            declarationSource = declarationSource,
            qualifiers = qualifierPadas
                .filterIsInstance<SubantaPada>()
                .mapNotNull { it.definitionQualifier() }
                .toSet(),
        )
    }

    private fun SubantaPada.definitionQualifier(): SamjnaDefinitionQualifier? {
        if (SupAffix.fromUpadesha(sup.text)?.vibhakti != Vibhakti.PRATHAMA) return null
        return when (val base = pratipadika) {
            is MulaPratipadika -> QUALIFIER_IDENTITIES[
                SamjnaInvocationMatcher.normalizeIdentity(base.text)
            ]
            is KridantaPratipadika -> SamjnaDefinitionQualifier.APAVADA.takeIf {
                base.lexicalIdentity == KridantaLexicalIdentity.APAVADA
            }
            else -> QUALIFIER_IDENTITIES[base.samjnaIdentity()]
        }
    }

    private val QUALIFIER_IDENTITIES = mapOf(
        "संज्ञा" to SamjnaDefinitionQualifier.SAMJNA,
        "अपवाद" to SamjnaDefinitionQualifier.APAVADA,
        "नित्य" to SamjnaDefinitionQualifier.NITYA,
        "नि + त्य" to SamjnaDefinitionQualifier.NITYA,
        "अन्तरङ्ग" to SamjnaDefinitionQualifier.ANTARANGA,
        "अन्तर् + अङ्ग" to SamjnaDefinitionQualifier.ANTARANGA,
    )
}
