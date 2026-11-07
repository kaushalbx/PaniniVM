package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.prakriyaDefinitionQualifier
import dev.panini.vyakaranam.parser.PaniniParser

/** Compatibility name for the shared AST classifier's typed result. */
typealias PrakriyaDefinitionQualifier = dev.panini.vyakaranam.ast.PrakriyaDefinitionQualifier

data class ParsedPrakriyaQualifiers(
    val declarationSource: String,
    val declarationPadas: List<Pada>,
    val qualifiers: Set<PrakriyaDefinitionQualifier>,
)

/** Recognizes explicit prakriyā-definition qualifiers following नाम. */
object PrakriyaDefinitionMarkerParser {
    private val parser = PaniniParser()

    fun hasExplicitMarker(source: String): Boolean {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return false
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val itiIndices = padas.indices.filter { index ->
            (padas[index] as? AvyayaPada)?.function == AvyayaFunction.NAMING
        }
        return itiIndices.any { itiIndex ->
            padas.drop(itiIndex + 1).filterIsInstance<SubantaPada>()
                .any { it.definitionQualifier() == PrakriyaDefinitionQualifier.PRAKRIYA }
        }
    }

    fun qualifiers(source: String): ParsedPrakriyaQualifiers? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val firstItiIndex = padas.indexOfFirst {
            (it as? AvyayaPada)?.function == AvyayaFunction.NAMING
        }
        val declarationPadas = if (firstItiIndex >= 0) padas.take(firstItiIndex) else padas
        val qualifierPadas = if (firstItiIndex >= 0) padas.drop(firstItiIndex + 1) else emptyList()
        val declarationSource = declarationPadas
            .joinToString(" ") { PrakriyaInvocationMatcher.normalizeIdentity(it.sourceText) }
            .ifBlank { return null }
        return ParsedPrakriyaQualifiers(
            declarationSource = declarationSource,
            declarationPadas = declarationPadas,
            qualifiers = definitionQualifiers(qualifierPadas),
        )
    }

    private fun SubantaPada.definitionQualifier(): PrakriyaDefinitionQualifier? = prakriyaDefinitionQualifier()

    private fun definitionQualifiers(padas: List<Pada>): Set<PrakriyaDefinitionQualifier> {
        val predicate = padas.filterIsInstance<dev.panini.vyakaranam.ast.TingantaPada>().singleOrNull()
        val producesResult = predicate != null && dev.panini.dhatupatha.DhatuPatha.all.any {
            it.denotesResultProduction(predicate)
        }
        val nominals = padas.filterIsInstance<SubantaPada>()
        val product = if (producesResult) {
            requireNotNull(nominals.singleOrNull { it.sup.supports(Vibhakti.DVITIYA, Vacana.EKAVACANA) }) {
                "A result-producing header requires one accusative singular product."
            }
        } else null
        return nominals.filter { it !== product }.map { pada ->
            requireNotNull(pada.definitionQualifier()) {
                "Unsupported procedure qualifier: '${pada.sourceText}'; expected a recognized nominative singular modifier."
            }
        }.toSet()
    }
}
