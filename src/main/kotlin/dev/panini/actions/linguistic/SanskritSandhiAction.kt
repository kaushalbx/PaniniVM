package dev.panini.actions.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

/** Sandhi joining (saṃhitā) over text operands using the Panini Ashtadhyayi rules. */
object SanskritSandhiAction : DhatuAction("संहिताकरणम्", "पदानां सन्धियोगः") {

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        if (operands.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val result = operands.drop(1).fold(operands.first()) { acc, next -> applySandhi(acc, next) }
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Joined ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
        )
    }

    private fun applySandhi(left: String, right: String): String {
        val l = if (left.trim().endsWith("म्")) left.trim().dropLast(1) else left.trim()
        val r = if (right.trim().endsWith("म्")) right.trim().dropLast(1) else right.trim()
        if (l.isEmpty()) return right
        if (r.isEmpty()) return left

        val lastChar = l.last()
        val firstChar = r.first()
        val hasVowelEnd = lastChar !in "ािीुूेैोौ्"

        // 1. Savarṇa Dīrgha (अकः सवर्णे दीर्घः 6.1.101)
        if ((hasVowelEnd || lastChar in "अआा") && firstChar in "अआ") {
            val replaceMark = "ा"
            val lStem = if (lastChar == 'ा') l.dropLast(1) else l
            val rStem = r.drop(1)
            return lStem + replaceMark + rStem
        }
        if (lastChar in "इईिी" && firstChar in "इई") {
            val lStem = if (lastChar in "िी") l.dropLast(1) else l
            val rStem = r.drop(1)
            return lStem + "ी" + rStem
        }
        if (lastChar in "उऊुू" && firstChar in "उऊ") {
            val lStem = if (lastChar in "ुू") l.dropLast(1) else l
            val rStem = r.drop(1)
            return lStem + "ू" + rStem
        }

        // 2. Ścutva (स्तोः श्चुना श्चुः 8.4.40 / छत्वम् 8.4.63 - e.g. तत् + शिव -> तच्छिव / तत् + च -> तच्च)
        if ((lastChar == '्' || lastChar in "त्द्") && (r.startsWith("श") || r.startsWith("च") || r.startsWith("छ") || r.startsWith("ज") || r.startsWith("झ") || r.startsWith("ञ"))) {
            val lStem = if (l.endsWith("्")) l.dropLast(2) else l.dropLast(1)
            val subChar = "च्"
            val rStem = if (r.startsWith("श")) "छ" + r.drop(1) else r
            return lStem + subChar + rStem
        }

        // 3. Guṇa (आद्गुणः 6.1.87)
        if ((hasVowelEnd || lastChar in "अआा") && firstChar in "इईउऊऋॠ") {
            val replaceMark = when (firstChar) {
                'इ', 'ई' -> "े"
                'उ', 'ऊ' -> "ो"
                else -> "र्"
            }
            val lStem = if (lastChar == 'ा') l.dropLast(1) else l
            val rStem = r.drop(1)
            return lStem + replaceMark + rStem
        }

        // 4. Yaṇ (इको यणचि 6.1.77)
        if (lastChar in "इीुू" && firstChar in "अआइईउऊएऐओऔ") {
            val semi = if (lastChar in "इी") "्य" else "्व"
            val lStem = if (lastChar in "िीुू") l.dropLast(1) else l
            return lStem + semi + r
        }

        return l + r
    }
}
