package dev.panini.execution.binding

import dev.panini.execution.SanskritValue
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.isRecognizedStriAffix
import java.util.concurrent.ConcurrentHashMap

/** Gives recognized numeral stems one stable typed identity before execution uses them. */
internal object NumeralAstNormalizer {
    fun hasNonGenderDerivation(pratipadika: Pratipadika): Boolean {
        val vikaras = when (pratipadika) {
            is MulaPratipadika -> pratipadika.vikaras
            is SankhyaPratipadika -> pratipadika.vikaras
            else -> return false
        }
        return vikaras.any { !it.isRecognizedStriAffix }
    }
    private val recognized = ConcurrentHashMap<String, SanskritValue.Sankhya>()
    private val rejected = ConcurrentHashMap.newKeySet<String>()

    fun normalize(pada: SubantaPada): SubantaPada {
        val typed = resolve(pada.pratipadika) ?: return pada
        return if (typed === pada.pratipadika) pada else pada.copy(pratipadika = typed).inheritLocation(pada)
    }

    fun resolve(pratipadika: Pratipadika): SankhyaPratipadika? = when (pratipadika) {
        is SankhyaPratipadika -> pratipadika.takeUnless(::hasNonGenderDerivation)

        is MulaPratipadika -> if (hasNonGenderDerivation(pratipadika) ||
            pratipadika.lexicalIdentity?.ordinalValue != null) null else recognize(pratipadika.text)?.let {
            SankhyaPratipadika(
                sourceText = pratipadika.sourceText,
                semanticValue = it,
                vikaras = pratipadika.vikaras,
            )
        }

        else -> null
    }

    private fun recognize(stem: String): SanskritValue.Sankhya? {
        recognized[stem]?.let { return it }
        if (stem in rejected) return null

        val expression = runCatching {
            sharedSankhyaEvaluator.evaluateStems(listOf(stem))
        }.getOrElse {
            rejected += stem
            return null
        }
        if (expression is dev.panini.sankhya.SankhyaExpression.Purana) {
            rejected += stem
            return null
        }
        val value = expression.value
        return SanskritValue.Sankhya(value, stem).also { recognized[stem] = it }
    }
}
