package dev.panini.sankhya

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna

class SankhyaDerivationFactory {

    fun create(expression: SankhyaExpression): DerivationState {
        val terms = createTerms(expression, "root", CompoundPosition.STANDALONE)

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
            is SankhyaExpression.Multiply ->
                createTerms(expression.coefficient, "${path}_multiply_coefficient", CompoundPosition.PURVAPADA) +
                    createTerms(expression.magnitude, "${path}_multiply_magnitude", CompoundPosition.UTTARAPADA)
            is SankhyaExpression.Una ->
                createTerms(expression.subtrahend, "${path}_una_subtrahend", CompoundPosition.PURVAPADA) +
                    DerivationTerm(
                        id = "sankhya_${path}_una",
                        surface = "ऊन",
                        kind = TermKind.PRATIPADIKA,
                        upadesha = "ऊन",
                    ) + createTerms(expression.base, "${path}_una_base", CompoundPosition.UTTARAPADA)
            is SankhyaExpression.Purana ->
                createTerms(expression.base, "${path}_purana", position)
            is SankhyaExpression.Frequency ->
                createTerms(expression.count, "${path}_frequency", position)
            is SankhyaExpression.Distribution ->
                createTerms(expression.parts, "${path}_distribution", position)
            is SankhyaExpression.RationalFraction ->
                listOf(
                    DerivationTerm(
                        id = "sankhya_${path}_fraction",
                        surface = "${expression.numerator}/${expression.denominator}",
                        kind = TermKind.PRATIPADIKA,
                        upadesha = "${expression.numerator}/${expression.denominator}",
                    )
                )
            is SankhyaExpression.Square -> createTerms(expression.operand, "${path}_square", position)
            is SankhyaExpression.Cube -> createTerms(expression.operand, "${path}_cube", position)
            is SankhyaExpression.SquareRoot -> createTerms(expression.operand, "${path}_squareroot", position)
            is SankhyaExpression.Sin -> createTerms(expression.degrees, "${path}_sin", position)
            is SankhyaExpression.Cos -> createTerms(expression.degrees, "${path}_cos", position)
            is SankhyaExpression.Tan -> createTerms(expression.degrees, "${path}_tan", position)
            is SankhyaExpression.Versin -> createTerms(expression.degrees, "${path}_versin", position)
            is SankhyaExpression.Hypotenuse -> createTerms(expression.bhuja, "${path}_hypotenuse_bhuja", position) + createTerms(expression.koti, "${path}_hypotenuse_koti", position)
            is SankhyaExpression.CircleArea -> createTerms(expression.radius, "${path}_circle_area", position)
            is SankhyaExpression.CircleCircumference -> createTerms(expression.radius, "${path}_circle_circumference", position)
        }

    private enum class CompoundPosition { STANDALONE, PURVAPADA, UTTARAPADA }

}
