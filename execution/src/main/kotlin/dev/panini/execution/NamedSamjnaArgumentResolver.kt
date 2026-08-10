package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

sealed interface SamjnaArgumentResolution {
    data class Success(val terms: List<String>, val named: Boolean) : SamjnaArgumentResolution
    data class Failure(val message: String) : SamjnaArgumentResolution
}

/** Binds षष्ठी parameter names to the द्वितीया values that immediately follow them. */
object NamedSamjnaArgumentResolver {
    private val parser = PaniniParser()

    fun resolve(karmaText: String, signature: SamjnaSignature): SamjnaArgumentResolution {
        val positional = SubantaKarakaParser.extractKarmaTerms(karmaText)
        if (signature.parameters.isEmpty()) return SamjnaArgumentResolution.Success(positional, false)
        val padas = parser.parseOrNull(karmaText.trim())?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?.filterIsInstance<SubantaPada>()
            .orEmpty()
        val pairs = padas.mapIndexedNotNull { index, pada ->
            if (pada.vibhakti() != Vibhakti.SASTHI) return@mapIndexedNotNull null
            val value = padas.getOrNull(index + 1)?.takeIf { it.vibhakti() == Vibhakti.DVITIYA }
                ?: return SamjnaArgumentResolution.Failure(
                    "नामितमानम्: '${pada.stem()}' must be followed by an accusative value.",
                )
            pada.stem() to value.pratipadika.sourceText.trim()
        }
        if (pairs.isEmpty()) return SamjnaArgumentResolution.Success(positional, false)
        if (pairs.size != positional.size) {
            return SamjnaArgumentResolution.Failure("नामितमानम्: Named and positional arguments cannot be mixed.")
        }
        val duplicates = pairs.groupBy { it.first }.filterValues { it.size > 1 }.keys
        if (duplicates.isNotEmpty()) {
            return SamjnaArgumentResolution.Failure("नामितमानम्: Duplicate arguments: $duplicates.")
        }
        val supplied = pairs.toMap()
        val expected = signature.parameters.map(SamjnaParameter::nameStem)
        val unknown = supplied.keys - expected.toSet()
        if (unknown.isNotEmpty()) {
            return SamjnaArgumentResolution.Failure("नामितमानम्: Unknown parameters: $unknown.")
        }
        val missing = expected.filterNot(supplied::containsKey)
        if (missing.isNotEmpty()) {
            return SamjnaArgumentResolution.Failure("नामितमानम्: Missing parameters: $missing.")
        }
        return SamjnaArgumentResolution.Success(expected.map(supplied::getValue), true)
    }

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

    private fun SubantaPada.stem(): String =
        SamjnaInvocationMatcher.normalizeIdentity(pratipadika.sourceText)
}
