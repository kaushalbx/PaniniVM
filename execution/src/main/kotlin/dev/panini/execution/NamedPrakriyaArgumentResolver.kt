package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.parser.PaniniParser

sealed interface PrakriyaArgumentResolution {
    data class Success(val terms: List<String>, val named: Boolean) : PrakriyaArgumentResolution
    data class Failure(val message: String) : PrakriyaArgumentResolution
}

/** Binds षष्ठी parameter names to the द्वितीया values that immediately follow them. */
object NamedPrakriyaArgumentResolver {
    private val parser = PaniniParser()

    fun resolve(karmaText: String, signature: PrakriyaSignature): PrakriyaArgumentResolution {
        val positional = SubantaKarakaParser.extractKarmaTerms(karmaText)
        if (signature.parameters.isEmpty()) return PrakriyaArgumentResolution.Success(positional, false)
        val padas = parser.parseOrNull(karmaText.trim())?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?.flatMap {
                when (it) {
                    is SubantaPada -> listOf(it)
                    is SamuccitaSubanta -> it.members
                    else -> emptyList()
                }
            }
            .orEmpty()
        return resolve(padas, signature, positional)
    }

    /** Resolves argument order from the canonical AST without rendering and reparsing it. */
    fun resolve(padas: List<SubantaPada>, signature: PrakriyaSignature): PrakriyaArgumentResolution {
        val positional = padas
            .filter { it.vibhakti() == Vibhakti.DVITIYA }
            .map { it.pratipadika.sourceText.trim() }
        return resolve(padas, signature, positional)
    }

    private fun resolve(
        padas: List<SubantaPada>,
        signature: PrakriyaSignature,
        positional: List<String>,
    ): PrakriyaArgumentResolution {
        if (signature.parameters.isEmpty()) return PrakriyaArgumentResolution.Success(positional, false)
        val pairs = padas.mapIndexedNotNull { index, pada ->
            if (pada.vibhakti() != Vibhakti.SASTHI) return@mapIndexedNotNull null
            val value = padas.getOrNull(index + 1)?.takeIf { it.vibhakti() == Vibhakti.DVITIYA }
                ?: return PrakriyaArgumentResolution.Failure(
                    "नामितमानम्: '${pada.stem()}' must be followed by an accusative value.",
                )
            pada.stem() to value.pratipadika.sourceText.trim()
        }
        if (pairs.isEmpty()) return PrakriyaArgumentResolution.Success(positional, false)
        if (pairs.size != positional.size) {
            return PrakriyaArgumentResolution.Failure("नामितमानम्: Named and positional arguments cannot be mixed.")
        }
        val duplicates = pairs.groupBy { it.first }.filterValues { it.size > 1 }.keys
        if (duplicates.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("नामितमानम्: Duplicate arguments: $duplicates.")
        }
        val supplied = pairs.toMap()
        val expected = signature.parameters.map(PrakriyaParameter::nameStem)
        val unknown = supplied.keys - expected.toSet()
        if (unknown.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("नामितमानम्: Unknown parameters: $unknown.")
        }
        val missing = expected.filterNot(supplied::containsKey)
        if (missing.isNotEmpty()) {
            return PrakriyaArgumentResolution.Failure("नामितमानम्: Missing parameters: $missing.")
        }
        return PrakriyaArgumentResolution.Success(expected.map(supplied::getValue), true)
    }

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

    private fun SubantaPada.stem(): String =
        PrakriyaInvocationMatcher.normalizeIdentity(pratipadika.sourceText)
}
