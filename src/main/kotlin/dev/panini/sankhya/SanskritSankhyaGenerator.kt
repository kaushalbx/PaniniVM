package dev.panini.sankhya

import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationResult
import dev.panini.derivation.Vacana
import dev.panini.derivation.Vibhakti
import dev.panini.shiksha.Linga
import java.math.BigInteger

class SanskritSankhyaGenerator(
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: DerivationEngine = DerivationEngine()
) {

    private val puranaGenerator by lazy { PuranaSankhyaGenerator(this, derivationEngine) }

    fun generate(value: BigInteger): DerivationResult {
        require(value.signum() >= 0) {
            "Negative numbers are not supported: $value"
        }

        val expression = expressionBuilder.build(value)
        val state = derivationFactory.create(expression)

        return derivationEngine.derive(state)
    }

    fun generateSurface(value: BigInteger): String =
        generate(value).final.surface

    fun generateOrdinalSurface(value: BigInteger): String =
        puranaGenerator.generateOrdinalSurface(value)

    fun generateAdhikaSurface(value: BigInteger): String {
        if (value < BigInteger.valueOf(100)) {
            return generateDeclinedSurface(value)
        }
        val hundreds = value.divide(BigInteger.valueOf(100)).multiply(BigInteger.valueOf(100))
        val remainder = value.mod(BigInteger.valueOf(100))
        if (remainder == BigInteger.ZERO) {
            return generateDeclinedSurface(value)
        }
        val remSurface = generateSurface(remainder)
        val hundredSurface = generateSurface(hundreds)

        val sandhiPrefix = when {
            remSurface.endsWith("इ") -> remSurface.dropLast(1) + "्य"
            remSurface.endsWith("त्रि") -> remSurface.dropLast(3) + "त्र्य"
            remSurface.endsWith("न्") -> remSurface.dropLast(1)
            remSurface.endsWith("त्") -> remSurface.dropLast(1) + "द्"
            else -> remSurface
        }
        return "${sandhiPrefix}धिक$hundredSurface"
    }

    fun generateDeclined(
        value: BigInteger,
        linga: Linga = Linga.PUMS,
        vibhakti: Vibhakti = Vibhakti.PRATHAMA,
        vacana: Vacana = Vacana.EKAVACANA
    ): String {
        if (value == BigInteger.ONE) {
            return when (linga) {
                Linga.PUMS -> when (vibhakti) {
                    Vibhakti.PRATHAMA -> "एकः"
                    Vibhakti.DVITIYA -> "एकम्"
                    Vibhakti.TRTIYA -> "एकेन"
                    Vibhakti.CHATURTHI -> "एकस्मै"
                    Vibhakti.PANCHAMI -> "एकस्मात्"
                    Vibhakti.SASTHI -> "एकस्य"
                    Vibhakti.SAPTAMI -> "एकस्मिन्"
                }
                Linga.STRI -> when (vibhakti) {
                    Vibhakti.PRATHAMA -> "एका"
                    Vibhakti.DVITIYA -> "एकाम्"
                    Vibhakti.TRTIYA -> "एकया"
                    Vibhakti.CHATURTHI -> "एकस्यै"
                    Vibhakti.PANCHAMI -> "एकस्याः"
                    Vibhakti.SASTHI -> "एकस्याः"
                    Vibhakti.SAPTAMI -> "एकस्याम्"
                }
                Linga.NAPUMSAKA -> when (vibhakti) {
                    Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> "एकम्"
                    Vibhakti.TRTIYA -> "एकेन"
                    Vibhakti.CHATURTHI -> "एकस्मै"
                    Vibhakti.PANCHAMI -> "एकस्मात्"
                    Vibhakti.SASTHI -> "एकस्य"
                    Vibhakti.SAPTAMI -> "एकस्मिन्"
                }
            }
        }

        if (value == BigInteger.valueOf(3)) {
            return when (linga) {
                Linga.PUMS -> "त्रयः"
                Linga.STRI -> "तिस्रः"
                Linga.NAPUMSAKA -> "त्रीणि"
            }
        }

        if (value == BigInteger.valueOf(4)) {
            return when (linga) {
                Linga.PUMS -> "चत्वारः"
                Linga.STRI -> "चतस्रः"
                Linga.NAPUMSAKA -> "चत्वारि"
            }
        }

        return generateDeclinedSurface(value)
    }

    fun generateDeclinedSurface(value: BigInteger): String {
        if (value == BigInteger.valueOf(5)) return "पञ्च"
        if (value == BigInteger.valueOf(6)) return "षट्"
        if (value == BigInteger.valueOf(7)) return "सप्त"
        if (value == BigInteger.valueOf(8)) return "अष्ट"
        if (value == BigInteger.valueOf(9)) return "नव"
        if (value == BigInteger.valueOf(10)) return "दश"

        val stem = generateSurface(value)
        return when {
            stem.endsWith("विंशति") || stem.endsWith("षष्टि") || stem.endsWith("सप्तति") ||
            stem.endsWith("अशीति") || stem.endsWith("नवति") -> stem + "ः"
            stem.endsWith("इ") || stem.endsWith("उ") || stem.endsWith("ऋ") -> stem + "ः"
            stem.endsWith("अ") -> stem + "ः"
            stem.endsWith("न्") -> stem.dropLast(1)
            stem.endsWith("त्") -> stem + "ः"
            else -> stem
        }
    }
}
