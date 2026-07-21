package dev.panini.sankhya

import dev.panini.derivation.DerivationEngine
import java.math.BigInteger

class PuranaSankhyaGenerator(
    private val generator: SanskritSankhyaGenerator = SanskritSankhyaGenerator(),
    private val derivationEngine: DerivationEngine = DerivationEngine()
) {

    private val specialOrdinals = mapOf(
        BigInteger.ONE to "प्रथम",
        BigInteger.TWO to "द्वितीय",
        BigInteger.valueOf(3) to "तृतीय",
        BigInteger.valueOf(4) to "चतुर्थ",
        BigInteger.valueOf(6) to "षष्ठ"
    )

    fun generateOrdinalSurface(value: BigInteger): String {
        require(value.signum() > 0) { "Ordinals require positive integer: $value" }

        specialOrdinals[value]?.let { return it }

        val basePratipadika = PrimitiveSankhya.fromValue(value)?.pratipadika
            ?: generator.generate(value).final.surface

        return when {
            basePratipadika.endsWith("न्") -> basePratipadika.dropLast(2) + "म"
            basePratipadika.endsWith("अ") -> basePratipadika + "म"
            basePratipadika.endsWith("ति") -> basePratipadika + "तम"
            basePratipadika.endsWith("त्") -> basePratipadika + "तम"
            else -> basePratipadika + "म"
        }
    }
}
