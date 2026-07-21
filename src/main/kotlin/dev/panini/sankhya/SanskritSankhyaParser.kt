package dev.panini.sankhya

import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationResult
import java.math.BigInteger

data class SankhyaCandidate(
    val expression: SankhyaExpression
)

data class SankhyaParseResult(
    val value: BigInteger,
    val expression: SankhyaExpression,
    val derivation: DerivationResult
)

class SankhyaCandidateFinder {

    fun find(surface: String): List<SankhyaCandidate> {
        val candidates = mutableListOf<SankhyaCandidate>()

        // 1. Check direct primitive match
        val direct = PrimitiveSankhya.fromPratipadika(surface)
        if (direct != null) {
            candidates.add(SankhyaCandidate(SankhyaExpression.Primitive(direct)))
        }

        // 2. Check compound lower + higher matches (e.g. द्वा/द्वि + विंशति)
        val prefixes = mapOf(
            "एक" to PrimitiveSankhya.EKA,
            "द्वि" to PrimitiveSankhya.DVI,
            "द्वा" to PrimitiveSankhya.DVI,
            "त्रि" to PrimitiveSankhya.TRI,
            "त्रयः" to PrimitiveSankhya.TRI,
            "त्रयो" to PrimitiveSankhya.TRI,
            "चतुर्" to PrimitiveSankhya.CHATUR,
            "चतुः" to PrimitiveSankhya.CHATUR,
            "चतुश्" to PrimitiveSankhya.CHATUR,
            "पञ्च" to PrimitiveSankhya.PANCHAN,
            "षट्" to PrimitiveSankhya.SHASH,
            "षड्" to PrimitiveSankhya.SHASH,
            "सप्त" to PrimitiveSankhya.SAPTAN,
            "अष्ट" to PrimitiveSankhya.ASHTAN,
            "अष्टा" to PrimitiveSankhya.ASHTAN,
            "नव" to PrimitiveSankhya.NAVAN
        )

        for ((prefix, lowerPrim) in prefixes) {
            if (surface.startsWith(prefix) && surface.length > prefix.length) {
                val rest = surface.substring(prefix.length)
                val higherPrim = PrimitiveSankhya.fromPratipadika(rest)
                if (higherPrim != null) {
                    candidates.add(
                        SankhyaCandidate(
                            SankhyaExpression.Add(
                                lower = SankhyaExpression.Primitive(lowerPrim),
                                higher = SankhyaExpression.Primitive(higherPrim)
                            )
                        )
                    )
                }
            }
        }

        return candidates
    }
}

class SanskritSankhyaParser(
    private val candidateFinder: SankhyaCandidateFinder = SankhyaCandidateFinder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: DerivationEngine = DerivationEngine()
) {

    fun parse(surface: String): List<SankhyaParseResult> =
        candidateFinder.find(surface)
            .mapNotNull { candidate ->
                val input = derivationFactory.create(candidate.expression)
                val derivation = derivationEngine.derive(input)

                if (derivation.final.surface == surface) {
                    SankhyaParseResult(
                        value = candidate.expression.value,
                        expression = candidate.expression,
                        derivation = derivation
                    )
                } else {
                    null
                }
            }
            .distinctBy { it.value }
}
