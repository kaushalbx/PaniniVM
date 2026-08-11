package dev.panini.execution.binding

import dev.panini.execution.SanskritValue
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import java.util.concurrent.ConcurrentHashMap

/** Gives recognized numeral stems one stable typed identity before execution uses them. */
internal object NumeralAstNormalizer {
    private val recognized = ConcurrentHashMap<String, SanskritValue.Sankhya>()
    private val rejected = ConcurrentHashMap.newKeySet<String>()

    fun normalize(pada: SubantaPada): SubantaPada {
        val typed = resolve(pada.pratipadika) ?: return pada
        return if (typed === pada.pratipadika) pada else pada.copy(pratipadika = typed)
    }

    fun resolve(pratipadika: Pratipadika): SankhyaPratipadika? = when (pratipadika) {
        is SankhyaPratipadika -> pratipadika

        is MulaPratipadika -> recognize(pratipadika.text)?.let {
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

        val value = runCatching {
            sharedSankhyaEvaluator.evaluateStems(listOf(stem)).value
        }.getOrElse {
            rejected += stem
            return null
        }
        return SanskritValue.Sankhya(value, stem).also { recognized[stem] = it }
    }
}
