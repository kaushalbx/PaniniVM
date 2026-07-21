package dev.panini.sankhya

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna

class SankhyaDerivationFactory {

    fun create(expression: SankhyaExpression): DerivationState {
        val terms = when (expression) {
            is SankhyaExpression.Subtract -> {
                val minuendTerms = createTerms(expression.minuend)
                val ekonaTerm = DerivationTerm(
                    id = "sankhya_ekona",
                    surface = "एकोन",
                    kind = TermKind.PRATIPADIKA,
                    upadesha = "एकोन"
                )
                listOf(ekonaTerm) + minuendTerms
            }
            else -> createTerms(expression)
        }

        val samjnas = terms.flatMap { term ->
            listOf(
                SamjnaAssignment(term.id, Samjna.PRATIPADIKA),
                SamjnaAssignment(term.id, Samjna.SANKHYA)
            )
        }.toSet()

        return DerivationState(
            terms = terms,
            samjnas = samjnas
        )
    }

    private fun createTerms(expression: SankhyaExpression): List<DerivationTerm> {
        val primitives = flatten(expression)
        return primitives.mapIndexed { index, primitive ->
            DerivationTerm(
                id = "sankhya_term_$index",
                surface = primitive.pratipadika,
                kind = TermKind.PRATIPADIKA,
                upadesha = primitive.pratipadika
            )
        }
    }

    fun flatten(expression: SankhyaExpression): List<PrimitiveSankhya> =
        when (expression) {
            is SankhyaExpression.Primitive -> listOf(expression.sankhya)
            is SankhyaExpression.Add -> flatten(expression.lower) + flatten(expression.higher)
            is SankhyaExpression.Subtract -> flatten(expression.minuend)
            is SankhyaExpression.Multiply -> flatten(expression.coefficient) + flatten(expression.magnitude)
        }
}
