package dev.panini.sankhya

import dev.panini.derivation.DerivationResult
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Linga
import java.math.BigInteger

class SanskritSankhyaGenerator(
    private val expressionBuilder: SankhyaExpressionBuilder = SankhyaExpressionBuilder(),
    private val derivationFactory: SankhyaDerivationFactory = SankhyaDerivationFactory(),
    private val derivationEngine: SankhyaDerivationEngine = SankhyaDerivationEngine()
) {
    private val puranaGenerator by lazy { PuranaSankhyaGenerator(this) }

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

    /** All grammatically licensed branches, including vibhāṣā alternatives. */
    fun generateVariants(value: BigInteger): List<DerivationResult> {
        require(value.signum() >= 0) { "Negative numbers are not supported: $value" }
        val expression = expressionBuilder.build(value)
        return derivationEngine.deriveAll(derivationFactory.create(expression))
    }

    fun generateOrdinal(value: BigInteger): DerivationResult = puranaGenerator.generate(value)

    fun generateOrdinalVariants(value: BigInteger): List<DerivationResult> = puranaGenerator.generateVariants(value)

    fun generateOrdinalSurface(value: BigInteger): String = generateOrdinal(value).final.surface


    fun generateAdhikaSurface(value: BigInteger): String {
        require(value.signum() >= 0) { "Negative numbers are not supported: $value" }
        return generateSurface(value)
    }

    fun generateDeclined(
        value: BigInteger,
        linga: Linga = Linga.PUMS,
        vibhakti: Vibhakti = Vibhakti.PRATHAMA,
        vacana: Vacana = Vacana.EKAVACANA
    ): String {
        if (value == BigInteger.ONE) {
            require(vacana == Vacana.EKAVACANA) { "एक is singular; requested $vacana" }
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

        if (value == BigInteger.TWO) {
            require(vacana == Vacana.DVIVACANA) { "द्वि is dual; requested $vacana" }
            require(vibhakti == Vibhakti.PRATHAMA) {
                "Declension of द्वि is currently implemented only for prathamā; requested $vibhakti"
            }
            return if (linga == Linga.PUMS) "द्वौ" else "द्वे"
        }

        if (value == BigInteger.valueOf(3)) {
            require(vacana == Vacana.BAHUVACANA) { "त्रि is plural; requested $vacana" }
            require(vibhakti == Vibhakti.PRATHAMA) {
                "Declension of त्रि is currently implemented only for prathamā; requested $vibhakti"
            }
            return when (linga) {
                Linga.PUMS -> "त्रयः"
                Linga.STRI -> "तिस्रः"
                Linga.NAPUMSAKA -> "त्रीणि"
            }
        }

        if (value == BigInteger.valueOf(4)) {
            require(vacana == Vacana.BAHUVACANA) { "चतुर् is plural; requested $vacana" }
            require(vibhakti == Vibhakti.PRATHAMA) {
                "Declension of चतुर् is currently implemented only for prathamā; requested $vibhakti"
            }
            return when (linga) {
                Linga.PUMS -> "चत्वारः"
                Linga.STRI -> "चतस्रः"
                Linga.NAPUMSAKA -> "चत्वारि"
            }
        }

        require(vibhakti == Vibhakti.PRATHAMA) {
            "Numeral declension beyond prathamā is not yet implemented for $value"
        }
        return generateDeclinedSurface(value)
    }

    fun generateDeclinedSurface(value: BigInteger): String {
        val expression = expressionBuilder.build(value)
        val stem = generate(value).final.surface
        return when (expression.headPrimitive().inflectionClass) {
            SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN -> when {
                stem.endsWith("न्") -> stem.dropLast(2)
                stem == "षष्" -> "षट्"
                else -> stem
            }
            SankhyaInflectionClass.FEMININE_I -> "$stemः"
            SankhyaInflectionClass.FEMININE_T -> stem
            SankhyaInflectionClass.NEUTER_A -> "${stem}म्"
            SankhyaInflectionClass.SPECIAL -> when (value) {
                BigInteger.ZERO -> "शून्यम्"
                BigInteger.ONE -> "एकम्"
                BigInteger.TWO -> "द्वे"
                BigInteger.valueOf(3) -> "त्रीणि"
                BigInteger.valueOf(4) -> "चत्वारि"
                else -> stem
            }
        }
    }
}
