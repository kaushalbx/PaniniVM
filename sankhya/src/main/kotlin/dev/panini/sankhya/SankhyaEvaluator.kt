package dev.panini.sankhya

/**
 * Reverse evaluator: reconstructs a [SankhyaExpression] from a sequence of primitive numeral stems or terms.
 * This is the exact inverse of [SankhyaDerivationFactory].
 */
class SankhyaEvaluator {

    /**
     * Evaluates a sequence of primitive stem strings (e.g. ["द्वि", "विंशति"] or ["द्वि", "शत"]) into a [SankhyaExpression].
     */
    fun evaluateStems(stems: List<String>): SankhyaExpression {
        require(stems.isNotEmpty()) { "Cannot evaluate an empty stem list" }

        // Check for frequency marker suffix (कृत्वः / कृत्वसुच्)
        if (stems.size > 1 && (stems.last() == "कृत्वः" || stems.last() == "कृत्वसुच्")) {
            val count = evaluateStems(stems.dropLast(1))
            return SankhyaExpression.Frequency(count = count)
        }

        // Check for distributive suffix (धा)
        if (stems.size > 1 && stems.last() == "धा") {
            val parts = evaluateStems(stems.dropLast(1))
            return SankhyaExpression.Distribution(parts = parts)
        }

        // Check for ordinal suffix (तम, म, तीय, थ, ठ)
        val puranaSuffixes = setOf("तम", "म", "तीय", "थ", "ठ")
        if (stems.size > 1 && stems.last() in puranaSuffixes) {
            val base = evaluateStems(stems.dropLast(1))
            return SankhyaExpression.Purana(base = base)
        }

        // Split by "अधिक" if present as an internal marker (handling optional preceding "अभि" upasarga)
        val adhikaIndex = stems.indexOf("अधिक")
        if (adhikaIndex > 0 && adhikaIndex < stems.size - 1) {
            val remStems = if (stems[adhikaIndex - 1] == "अभि") stems.subList(0, adhikaIndex - 1) else stems.subList(0, adhikaIndex)
            val rem = evaluateStems(remStems)
            val base = evaluateStems(stems.subList(adhikaIndex + 1, stems.size))
            return SankhyaExpression.Adhika(remainder = rem, base = base)
        }

        // Split by "ऊन" or "न्यून" if present as an internal marker
        val unaIndex = stems.indexOfFirst { it == "ऊन" || it == "न्यून" }
        if (unaIndex > 0 && unaIndex < stems.size - 1) {
            val sub = evaluateStems(stems.subList(0, unaIndex))
            val base = evaluateStems(stems.subList(unaIndex + 1, stems.size))
            return SankhyaExpression.Una(subtrahend = sub, base = base)
        }

        // Split by "सहित", "युत", "संयुक्त" for addition
        val sahitaIndex = stems.indexOfFirst { it == "सहित" || it == "युत" || it == "संयुक्त" }
        if (sahitaIndex > 0 && sahitaIndex < stems.size - 1) {
            val left = evaluateStems(stems.subList(0, sahitaIndex))
            val right = evaluateStems(stems.subList(sahitaIndex + 1, stems.size))
            return SankhyaExpression.Add(lower = right, higher = left)
        }

        // Split by "रहित", "वर्जित" for subtraction
        val rahitaIndex = stems.indexOfFirst { it == "रहित" || it == "वर्जित" }
        if (rahitaIndex > 0 && rahitaIndex < stems.size - 1) {
            val left = evaluateStems(stems.subList(0, rahitaIndex))
            val right = evaluateStems(stems.subList(rahitaIndex + 1, stems.size))
            return SankhyaExpression.Una(subtrahend = right, base = left)
        }

        // Multiplicative stem + "गुणित" / "हते", e.g. ["द्वि", "गुणित", "शत"] -> 2 * 100 = 200
        if (stems.size >= 2 && (stems[1] == "गुणित" || stems[1] == "हते")) {
            val coeff = evaluateStems(listOf(stems[0]))
            val rest = if (stems.size >= 3) evaluateStems(stems.subList(2, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.Multiply(coefficient = coeff, magnitude = rest)
        }

        // Division stem + "भक्त" / "हृत", e.g. ["द्वि", "भक्त", "शत"] -> 100 / 2 = 50
        if (stems.size >= 2 && (stems[1] == "भक्त" || stems[1] == "हृत")) {
            val divisor = evaluateStems(listOf(stems[0]))
            val rest = if (stems.size >= 3) evaluateStems(stems.subList(2, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.RationalFraction(numerator = rest.value, denominator = divisor.value)
        }

        // Square: ["वर्ग", "कृत", ...] or ["वर्ग", ...]
        if (stems.isNotEmpty() && stems[0] == "वर्ग") {
            val startIndex = if (stems.size >= 2 && stems[1] == "कृत") 2 else 1
            val operand = if (startIndex < stems.size) evaluateStems(stems.subList(startIndex, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.Square(operand)
        }

        // Cube: ["घन", "कृत", ...] or ["घन", ...]
        if (stems.isNotEmpty() && stems[0] == "घन") {
            val startIndex = if (stems.size >= 2 && stems[1] == "कृत") 2 else 1
            val operand = if (startIndex < stems.size) evaluateStems(stems.subList(startIndex, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.Cube(operand)
        }

        // SquareRoot: ["मूल", ...] or ["पद", ...]
        if (stems.isNotEmpty() && (stems[0] == "मूल" || stems[0] == "पद")) {
            val operand = if (stems.size >= 2) evaluateStems(stems.subList(1, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.SquareRoot(operand)
        }

        // Trigonometric: "ज्या", "कोटि" + "ज्या", "स्पर्श" + "ज्या", "उत्क्रम" + "ज्या"
        if (stems.size >= 2 && (stems[0] == "कोटि" || stems[0] == "कोटी") && stems[1] == "ज्या") {
            val operand = if (stems.size >= 3) evaluateStems(stems.subList(2, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.SHUNYA)
            return SankhyaExpression.Cos(operand)
        }
        if (stems.size >= 2 && stems[0] == "स्पर्श" && stems[1] == "ज्या") {
            val operand = if (stems.size >= 3) evaluateStems(stems.subList(2, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.SHUNYA)
            return SankhyaExpression.Tan(operand)
        }
        if (stems.size >= 2 && stems[0] == "उत्क्रम" && stems[1] == "ज्या") {
            val operand = if (stems.size >= 3) evaluateStems(stems.subList(2, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.SHUNYA)
            return SankhyaExpression.Versin(operand)
        }
        if (stems.isNotEmpty() && stems[0] == "ज्या") {
            val operand = if (stems.size >= 2) evaluateStems(stems.subList(1, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.SHUNYA)
            return SankhyaExpression.Sin(operand)
        }

        // Geometric: "परिधि", "क्षेत्रफल", "कर्ण"
        if (stems.isNotEmpty() && stems[0] == "परिधि") {
            val radius = if (stems.size >= 2) evaluateStems(stems.subList(1, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.CircleCircumference(radius)
        }
        if (stems.isNotEmpty() && stems[0] == "क्षेत्रफल") {
            val radius = if (stems.size >= 2) evaluateStems(stems.subList(1, stems.size)) else SankhyaExpression.Primitive(PrimitiveSankhya.EKA)
            return SankhyaExpression.CircleArea(radius)
        }
        if (stems.size >= 3 && stems[0] == "कर्ण") {
            val bhuja = evaluateStems(listOf(stems[1]))
            val koti = evaluateStems(listOf(stems[2]))
            return SankhyaExpression.Hypotenuse(bhuja, koti)
        }

        // Mixed rational prefixes: सार्ध, सपाद, पादोन
        if (stems.size == 2 && (stems[0] == "सार्ध" || stems[0] == "सपाद" || stems[0] == "पादोन")) {
            val baseExpr = evaluateStems(listOf(stems[1]))
            val baseValue = baseExpr.value
            return when (stems[0]) {
                "सार्ध" -> SankhyaExpression.RationalFraction(numerator = baseValue * 2 + 1, denominator = 2)
                "सपाद" -> SankhyaExpression.RationalFraction(numerator = baseValue * 4 + 1, denominator = 4)
                "पादोन" -> SankhyaExpression.RationalFraction(numerator = baseValue * 4 - 1, denominator = 4)
                else -> error("Invalid prefix")
            }
        }

        // Segmented fraction suffix: e.g. ["त्रि", "तीय", "अंश"] -> 1/3, ["द्वि", "त्रि", "तीय", "अंश"] -> 2/3, ["पाद", "अंश"] -> 1/4
        if (stems.size >= 2 && (stems.last() == "अंश" || stems.last() == "भाग")) {
            val rest = stems.dropLast(1)
            val lastAffix = rest.last()
            val denomLength = if (lastAffix in listOf("तीय", "थ", "ष्ठ", "म", "तम")) 2 else 1
            val denomStems = rest.takeLast(denomLength)
            val numStems = rest.dropLast(denomLength)

            val denomVal = parseDenomStems(denomStems)
            val numVal = if (numStems.isNotEmpty()) evaluateStems(numStems).value else 1L
            return SankhyaExpression.RationalFraction(numerator = numVal, denominator = denomVal)
        }

        // Numerator-Denominator fraction: e.g. ["त्रि", "पाद"] -> 3/4
        if (stems.size == 2) {
            val denomFraction = parseFractionStem(stems[1])
            if (denomFraction != null) {
                val numExpr = evaluateStems(listOf(stems[0]))
                return SankhyaExpression.RationalFraction(
                    numerator = numExpr.value * denomFraction.numerator,
                    denominator = denomFraction.denominator
                )
            }
            val puranaExpr = parsePuranaStemSequence(stems)
            if (puranaExpr != null) return puranaExpr
        }

        if (stems.size == 1) {
            val stem = stems.single()
            val standaloneFraction = parseFractionStem(stem)
            if (standaloneFraction != null) return standaloneFraction

            val standaloneFrequency = parseStandaloneFrequency(stem)
            if (standaloneFrequency != null) return standaloneFrequency

            val standalonePurana = parseStandalonePurana(stem)
            if (standalonePurana != null) return standalonePurana

            val prim = PrimitiveSankhya.fromAnnotatedPratipadika(stem)
                ?: error("Unrecognized primitive numeral stem: '$stem'")
            return SankhyaExpression.Primitive(prim)
        }

        if (stems.size == 2) {
            val first = PrimitiveSankhya.fromAnnotatedPratipadika(stems[0])
                ?: error("Unrecognized primitive numeral stem: '${stems[0]}'")
            val second = PrimitiveSankhya.fromAnnotatedPratipadika(stems[1])
                ?: error("Unrecognized primitive numeral stem: '${stems[1]}'")

            val firstExpr = SankhyaExpression.Primitive(first)
            val secondExpr = SankhyaExpression.Primitive(second)

            return when {
                // Multiplicative: coefficient * magnitude (e.g. द्वि * शत = 200)
                isMagnitude(second) -> SankhyaExpression.Multiply(coefficient = firstExpr, magnitude = secondExpr)
                // Additive: unit + ten (e.g. द्वि + विंशति = 22)
                isTen(second) && isUnit(first) -> SankhyaExpression.Add(lower = firstExpr, higher = secondExpr)
                else -> error("Invalid 2-stem numeral sequence: [${stems[0]}, ${stems[1]}]")
            }
        }

        // For longer sequences, process recursively
        val lastStem = stems.last()
        val lastPrim = PrimitiveSankhya.fromAnnotatedPratipadika(lastStem)
        if (lastPrim != null && isMagnitude(lastPrim)) {
            val coeffExpr = evaluateStems(stems.dropLast(1))
            return SankhyaExpression.Multiply(coefficient = coeffExpr, magnitude = SankhyaExpression.Primitive(lastPrim))
        }

        error("Complex stem sequence evaluation failed for: $stems")
    }

    private fun parseStandaloneFrequency(stem: String): SankhyaExpression.Frequency? = when (stem) {
        "सकृत्" -> SankhyaExpression.Frequency(SankhyaExpression.Primitive(PrimitiveSankhya.EKA))
        "द्विः" -> SankhyaExpression.Frequency(SankhyaExpression.Primitive(PrimitiveSankhya.DVI))
        "त्रिः" -> SankhyaExpression.Frequency(SankhyaExpression.Primitive(PrimitiveSankhya.TRI))
        "चतुः" -> SankhyaExpression.Frequency(SankhyaExpression.Primitive(PrimitiveSankhya.CHATUR))
        else -> null
    }

    private fun parseStandalonePurana(stem: String): SankhyaExpression.Purana? {
        val baseVal = when (stem) {
            "प्रथम" -> PrimitiveSankhya.EKA
            else -> null
        }
        return baseVal?.let { SankhyaExpression.Purana(SankhyaExpression.Primitive(it)) }
    }

    private fun parsePuranaStemSequence(stems: List<String>): SankhyaExpression.Purana? {
        if (stems.isEmpty()) return null
        if (stems.size == 1) return parseStandalonePurana(stems.single())

        val last = stems.last()
        if (last == "तीय" || last == "थ" || last == "ष्ठ" || last == "म" || last == "तम") {
            val baseStems = stems.dropLast(1)
            val baseExpr = if (baseStems.size == 1) {
                val s = baseStems.single()
                when (s) {
                    "द्वि" -> PrimitiveSankhya.DVI
                    "त्रि" -> PrimitiveSankhya.TRI
                    "चतुर्" -> PrimitiveSankhya.CHATUR
                    "पञ्च" -> PrimitiveSankhya.PANCHAN
                    "षष्" -> PrimitiveSankhya.SHASH
                    "सप्त" -> PrimitiveSankhya.SAPTAN
                    "अष्ट" -> PrimitiveSankhya.ASHTAN
                    "नव" -> PrimitiveSankhya.NAVAN
                    "दश" -> PrimitiveSankhya.DASHAN
                    "शत" -> PrimitiveSankhya.SHATA
                    "सहस्र" -> PrimitiveSankhya.SAHASRA
                    else -> PrimitiveSankhya.fromAnnotatedPratipadika(s)
                }
            } else {
                val evaluated = try { evaluateStems(baseStems) } catch (e: Throwable) { null }
                evaluated?.let { PrimitiveSankhya.fromValue(it.value) }
            }
            if (baseExpr != null) return SankhyaExpression.Purana(SankhyaExpression.Primitive(baseExpr))
        }
        return null
    }

    private fun parseFractionStem(stem: String): SankhyaExpression.RationalFraction? = when (stem) {
        "अर्ध" -> SankhyaExpression.RationalFraction(1L, 2L)
        "पाद" -> SankhyaExpression.RationalFraction(1L, 4L)
        else -> null
    }

    private fun parseDenomStems(stems: List<String>): Long {
        if (stems.size == 1 && stems.single() == "पाद") return 4L
        val puranaVal = parsePuranaStemSequence(stems)?.value
        if (puranaVal != null) return puranaVal
        if (stems.size == 1) {
            val primVal = PrimitiveSankhya.fromAnnotatedPratipadika(stems.single())?.value
            if (primVal != null) return primVal
        }
        return 1L
    }

    /**
     * Evaluates a sentence-level Adhika expression given a remainder expression and a base expression.
     */
    fun evaluateAdhika(remainder: SankhyaExpression, base: SankhyaExpression): SankhyaExpression {
        return SankhyaExpression.Adhika(remainder = remainder, base = base)
    }

    /**
     * Evaluates a sentence-level Una expression given a subtrahend expression and a base expression.
     */
    fun evaluateUna(subtrahend: SankhyaExpression, base: SankhyaExpression): SankhyaExpression {
        return SankhyaExpression.Una(subtrahend = subtrahend, base = base)
    }

    private fun isUnit(prim: PrimitiveSankhya): Boolean = prim.value in 1L..9L

    private fun isTen(prim: PrimitiveSankhya): Boolean = prim.value in listOf(10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L)

    private fun isMagnitude(prim: PrimitiveSankhya): Boolean = prim.value in listOf(100L, 1_000L, 10_000L, 100_000L, 1_000_000L, 10_000_000L)
}
