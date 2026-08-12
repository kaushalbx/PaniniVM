package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

/** Substitutes the parsed accusative समवाय collection parameter. */
object SamavayaParameterResolver {
    private val parser = PaniniParser()

    fun replace(source: String, argumentSource: String): String {
        if (argumentSource.isBlank()) return source
        val ukti = parser.parseOrNull(source.trim()) ?: return source
        val parameterSources = ukti.grammaticalVakyas().asSequence()
            .flatMap { it.padas.asSequence() }
            .filterIsInstance<SubantaPada>()
            .filter { pada ->
                (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                    MulaPratipadikaIdentity.SAMAVAYA &&
                    SupAffix.fromUpadesha(pada.sup.text)?.vibhakti == Vibhakti.DVITIYA
            }
            .map { it.sourceText }
            .distinct()
            .toList()
        return parameterSources.fold(source) { result, parameter ->
            result.replace(sourcePattern(parameter), argumentSource)
        }
    }

    private fun sourcePattern(source: String): Regex = Regex(
        source.split('+').joinToString("\\s*\\+\\s*") { Regex.escape(it) },
    )
}
