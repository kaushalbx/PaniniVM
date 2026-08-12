package dev.panini.execution

import dev.panini.shiksha.Samjna

/** Decoder for typed values written before binary VALUE_V2 persistence. */
internal object LegacySanskritValueCodec {
    private const val LOPA = "LOPA"
    private const val SHABDA = "SHABDA"
    private const val GANA = "GANA"
    private const val SANKHYA_PREFIX = "SANKHYA:"
    private const val RATIONAL_PREFIX = "RATIONAL:"
    private const val SATYA_PREFIX = "SATYA:"

    fun decode(type: String, display: String, samjnas: Set<Samjna>): SanskritValue? = when {
        type == LOPA -> SanskritValue.Lopa
        type.startsWith(SANKHYA_PREFIX) ->
            SanskritValue.Sankhya(type.removePrefix(SANKHYA_PREFIX).toLong(), display)
        type.startsWith(RATIONAL_PREFIX) -> {
            val (numerator, denominator) = type.removePrefix(RATIONAL_PREFIX)
                .split('/')
                .map(String::toLong)
            SanskritValue.Rational(numerator, denominator, display)
        }
        type.startsWith(SATYA_PREFIX) ->
            SanskritValue.Satya(type.removePrefix(SATYA_PREFIX).toBooleanStrict())
        type == SHABDA || type == GANA -> SanskritValue.Shabda(display, samjnas)
        else -> null
    }
}
