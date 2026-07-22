package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

/** Sandhi joining (saṃhitā) over text operands. */
object SanskritSandhiAction : DhatuAction {
    const val ID = "samhita.karana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        if (operands.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val result = operands.drop(1).fold(operands.first()) { acc, next -> applySandhi(acc, next) }
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Joined ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
        )
    }

    private fun applySandhi(left: String, right: String): String {
        val l = left.trim()
        val r = right.trim()
        if (l.isEmpty()) return r
        if (r.isEmpty()) return l

        val lastChar = l.last()
        val firstChar = r.first()

        // 1. Vowel Sandhi (Ac-sandhi)
        // a/ā endings: अ, आ, or implicit vowel on consonant without halanta
        val extracted = extractFinalVowel(l)
        if (extracted != null) {
            val (lBase, lVowel) = extracted
            val combined = combineVowels(lVowel, firstChar)
            if (combined != null) {
                return lBase + combined + r.substring(1)
            }
        }

        // 2. Anusvara Sandhi (m + consonant -> ṃ + consonant)
        if (l.endsWith("म्") || l.endsWith("म")) {
            val stem = if (l.endsWith("म्")) l.dropLast(2) else l.dropLast(1)
            if (isConsonant(firstChar)) {
                return stem + "ं" + r
            }
        }

        // 3. Stutva / Cchutva (t + c -> cc)
        if (l.endsWith("त्") && firstChar == 'च') {
            return l.dropLast(2) + "च्च" + r.substring(1)
        }

        return l + r
    }

    private fun extractFinalVowel(text: String): Pair<String, Char>? {
        if (text.endsWith("अ") || text.endsWith("आ") || text.endsWith("इ") || text.endsWith("ई") ||
            text.endsWith("उ") || text.endsWith("ऊ") || text.endsWith("ऋ")
        ) {
            return text.dropLast(1) to text.last()
        }
        if (text.endsWith("ा")) return text.dropLast(1) to 'आ'
        if (text.endsWith("ि")) return text.dropLast(1) to 'इ'
        if (text.endsWith("ी")) return text.dropLast(1) to 'ई'
        if (text.endsWith("ु")) return text.dropLast(1) to 'उ'
        if (text.endsWith("ू")) return text.dropLast(1) to 'ऊ'
        if (text.endsWith("ृ")) return text.dropLast(1) to 'ऋ'
        if (text.endsWith("्")) return null // Halanta ending

        // Devanagari consonant with implicit schwa ('अ')
        val lastChar = text.last()
        if (isConsonant(lastChar)) {
            return text to 'अ'
        }
        return null
    }

    private fun combineVowels(v1: Char, v2: Char): String? = when {
        // Savarna-dirgha
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'अ' || v2 == 'आ') -> "ा"
        (v1 == 'इ' || v1 == 'ई') && (v2 == 'इ' || v2 == 'ई') -> "ी"
        (v1 == 'उ' || v1 == 'ऊ') && (v2 == 'उ' || v2 == 'ऊ') -> "ू"
        // Guna
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'इ' || v2 == 'ई' || v2 == 'ि' || v2 == 'ी') -> "े"
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'उ' || v2 == 'ऊ' || v2 == 'ु' || v2 == 'ू') -> "ो"
        // Vriddhi
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'ए' || v2 == 'ऐ') -> "ै"
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'ओ' || v2 == 'औ') -> "ौ"
        else -> null
    }

    private fun isConsonant(c: Char): Boolean = c in '\u0915'..'\u0939'
}

/** Morphological subanta derivation from nominal prātipadika stem. */
object SanskritSubantaDerivationAction : DhatuAction {
    const val ID = "padanishpatti"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val stem = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Nominal derivation requires a prātipadika stem in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return try {
            val engine = dev.panini.derivation.SubantaEngine()
            val request = dev.panini.derivation.SubantaDerivationRequest(
                pratipadika = stem,
                vibhakti = Vibhakti.PRATHAMA,
                vacana = Vacana.EKAVACANA,
                stemClass = dev.panini.derivation.SubantaStemClass.guess(stem),
            )
            val result = engine.derive(request).final.surface
            ExecutionResult.Success(
                result,
                operation.id,
                listOf(
                    "Selected operation ${operation.id}.",
                    "Derived subanta for prātipadika '$stem'.",
                    "Produced $result.",
                ),
            )
        } catch (e: Exception) {
            ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Subanta derivation failed for stem '$stem': ${e.message}",
                listOf("Selected operation ${operation.id}."),
            )
        }
    }
}
