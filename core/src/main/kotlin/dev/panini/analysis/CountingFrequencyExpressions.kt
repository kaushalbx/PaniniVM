package dev.panini.analysis

import dev.panini.core.FrequencyAffix
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.vyakaranam.ast.*

/** Explicit counting expressions, separate from qualitative frequency such as sadā or punaḥ. */
object CountingFrequencyExpressions {
    private val lexicalCounts = mapOf("सकृत्" to 1L, "सकृद्" to 1L,
        "द्विः" to 2L, "द्विर्" to 2L, "त्रिः" to 3L, "त्रिर्" to 3L,
        "चतुः" to 4L, "चतुर्" to 4L, "पञ्चकृत्वः" to 5L)

    fun count(pada: AvyayaPada): Long? {
        // A declared derivation takes precedence over a possibly misleading surface spelling.
        return when (val derivation = pada.derivation) {
            null -> lexicalCounts[pada.form]
            is AvyayaTaddhitaDerivation -> {
                val value = PrimitiveSankhya.fromAnnotatedPratipadika(derivation.pratipadika)?.value ?: return null
                when (derivation.pratyaya) {
                    FrequencyAffix.SUC.upadesha -> value.takeIf { it in 1L..4L }
                    FrequencyAffix.KRTVASUC.upadesha -> value.takeIf { it >= 5L }
                    else -> null
                }
            }
            is SankhyaAvyayaDerivation -> {
                if (derivation.kind != "KRITVAS") return null
                derivation.stems.singleOrNull()?.let(PrimitiveSankhya::fromAnnotatedPratipadika)
                    ?.value?.takeIf { it > 0L }
            }
            else -> null
        }
    }
}
