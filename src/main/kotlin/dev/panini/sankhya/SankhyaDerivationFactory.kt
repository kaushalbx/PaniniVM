package dev.panini.sankhya

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna

class SankhyaDerivationFactory {

    fun create(expression: SankhyaExpression): DerivationState {
        val terms = when (expression) {
            is SankhyaExpression.Ekona -> {
                val baseTerms = createTerms(expression.base, "ekona_base", CompoundPosition.UTTARAPADA)
                val ekonaTerm = DerivationTerm(
                    id = "sankhya_ekona",
                    surface = "एकोन",
                    kind = TermKind.PRATIPADIKA,
                    upadesha = "एकोन"
                )
                listOf(ekonaTerm) + baseTerms
            }
            else -> createTerms(expression, "root", CompoundPosition.STANDALONE)
        }

        val samjnas = terms.flatMap { term ->
            buildList {
                add(SamjnaAssignment(term.id, Samjna.PRATIPADIKA))
                if (!term.id.endsWith("_adhika")) add(SamjnaAssignment(term.id, Samjna.SANKHYA))
            }
        }.toSet()

        return DerivationState(
            terms = terms,
            samjnas = samjnas
        )
    }

    private fun createTerms(
        expression: SankhyaExpression,
        path: String,
        position: CompoundPosition,
    ): List<DerivationTerm> =
        when (expression) {
            is SankhyaExpression.Primitive -> listOf(
                DerivationTerm(
                id = "sankhya_$path",
                surface = when (position) {
                    CompoundPosition.STANDALONE -> expression.sankhya.pratipadika
                    CompoundPosition.PURVAPADA -> expression.sankhya.purvapada
                    CompoundPosition.UTTARAPADA -> expression.sankhya.uttarapada
                },
                kind = TermKind.PRATIPADIKA,
                upadesha = expression.sankhya.pratipadika
                )
            )
            is SankhyaExpression.Add ->
                createTerms(expression.lower, "${path}_add_lower", CompoundPosition.PURVAPADA) +
                    createTerms(expression.higher, "${path}_add_higher", CompoundPosition.UTTARAPADA)
            is SankhyaExpression.Adhika ->
                createTerms(expression.remainder, "${path}_adhika_remainder", CompoundPosition.PURVAPADA) +
                    DerivationTerm(
                        id = "sankhya_${path}_adhika",
                        surface = "अधिक",
                        kind = TermKind.PRATIPADIKA,
                        upadesha = "अधिक",
                    ) + createTerms(expression.base, "${path}_adhika_base", CompoundPosition.UTTARAPADA)
            is SankhyaExpression.Ekona -> listOf(
                DerivationTerm(
                    id = "sankhya_${path}_ekona",
                    surface = "एकोन",
                    kind = TermKind.PRATIPADIKA,
                    upadesha = "एकोन"
                )
            ) + createTerms(expression.base, "${path}_ekona_base", CompoundPosition.UTTARAPADA)
            is SankhyaExpression.Multiply ->
                createTerms(expression.coefficient, "${path}_multiply_coefficient", CompoundPosition.PURVAPADA) +
                    createTerms(expression.magnitude, "${path}_multiply_magnitude", CompoundPosition.UTTARAPADA)
        }

    private enum class CompoundPosition { STANDALONE, PURVAPADA, UTTARAPADA }

    fun flatten(expression: SankhyaExpression): List<PrimitiveSankhya> =
        when (expression) {
            is SankhyaExpression.Primitive -> listOf(expression.sankhya)
            is SankhyaExpression.Add -> flatten(expression.lower) + flatten(expression.higher)
            is SankhyaExpression.Adhika -> flatten(expression.remainder) + flatten(expression.base)
            is SankhyaExpression.Ekona -> flatten(expression.base)
            is SankhyaExpression.Multiply -> flatten(expression.coefficient) + flatten(expression.magnitude)
        }
}
