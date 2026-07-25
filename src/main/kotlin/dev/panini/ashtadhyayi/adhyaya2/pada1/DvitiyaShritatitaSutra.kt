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
 * Sūtra 2.1.24: द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः.
 * Prescribes Dvitīyā Tatpuruṣa compound between a 2nd-case subanta and words like śrita, atīta, patita, gata, atyasta, prāpta, āpanna.
 */
object DvitiyaShritatitaSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.1.24",
    text = "द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः",
    hindiExplanation = "द्वितीयान्त समर्थ सुबन्त का श्रित, अतीत, पतित, गत, अत्यस्त, प्राप्त, आपन्न के साथ तत्पुरुष समास होता है (उदा. कृष्णश्रितः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val targetWords = setOf("श्रित", "श्रितः", "अतीत", "अतीतः", "पतित", "पतितः", "गत", "गतः", "अत्यस्त", "प्राप्त", "आपन्न")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            first.kind == TermKind.PRATIPADIKA && second.kind == TermKind.PRATIPADIKA && second.surface in targetWords
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val index = (0 until context.terms.size - 1).first { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            first.kind == TermKind.PRATIPADIKA && second.kind == TermKind.PRATIPADIKA && second.surface in targetWords
        }

        val first = context.terms[index]
        val second = context.terms[index + 1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa_tatpurusha", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.1.24 forms Dvitīyā Tatpuruṣa compound '${compoundSurface}'."
        )
    }
}
