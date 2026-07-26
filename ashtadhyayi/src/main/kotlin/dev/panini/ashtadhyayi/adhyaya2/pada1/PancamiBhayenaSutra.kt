package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.37: पञ्चमी भयेन.
 * Prescribes Pañcamī Tatpuruṣa compound between a 5th-case subanta and the word 'bhaya'.
 */
object PancamiBhayenaSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.1.37",
    text = "पञ्चमी भयेन",
    hindiExplanation = "पञ्चम्यन्त समर्थ सुबन्त का 'भय' शब्द के साथ पञ्चमी तत्पुरुष समास होता है (उदा. चोरभयम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210037,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val bhayaWords = setOf("भय", "भयम्", "भीत", "भीति")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            first.kind == TermKind.PRATIPADIKA && second.kind == TermKind.PRATIPADIKA && second.surface in bhayaWords
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val index = (0 until context.terms.size - 1).first { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            first.kind == TermKind.PRATIPADIKA && second.kind == TermKind.PRATIPADIKA && second.surface in bhayaWords
        }

        val first = context.terms[index]
        val second = context.terms[index + 1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa_pancami", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.1.37 forms Pañcamī Tatpuruṣa compound '${compoundSurface}'."
        )
    }
}
