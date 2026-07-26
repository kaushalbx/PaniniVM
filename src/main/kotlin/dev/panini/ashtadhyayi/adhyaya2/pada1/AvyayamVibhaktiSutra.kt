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
 * Sūtra 2.1.6: अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासंप्रतिशब्दप्रादुर्भावपश्चाद्यथानुपूर्व्ययौगपद्यसादृश्यसंपत्तिसाकल्यान्तवचनेषु.
 * Prescribes Avyayībhāva compound formation between an Avyaya and a subanta in vibhakti, samīpa, etc. meanings.
 */
object AvyayamVibhaktiSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.1.6",
    text = "अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासंप्रतिशब्दप्रादुर्भावपश्चाद्यथानुपूर्व्ययौगपद्यसादृश्यसंपत्तिसाकल्यान्तवचनेषु",
    hindiExplanation = "विभक्ति, समीप आदि १६ अर्थों में अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. उपकृष्णम्, प्रतिगृहम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            val isAvyaya = context.samjnas.any { it.targetId == first.id && (it.samjna == Samjna.AVYAYA || it.samjna == Samjna.UPASARGA) } || first.id == "upasarga" || first.id == "avyaya" || first.surface in setOf("उप", "प्रति", "यथा", "अनु", "सह")
            val isSubanta = second.kind == TermKind.PRATIPADIKA
            isAvyaya && isSubanta
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val index = (0 until context.terms.size - 1).first { i ->
            val first = context.terms[i]
            val second = context.terms[i + 1]
            val isAvyaya = context.samjnas.any { it.targetId == first.id && (it.samjna == Samjna.AVYAYA || it.samjna == Samjna.UPASARGA) } || first.id == "upasarga" || first.id == "avyaya" || first.surface in setOf("उप", "प्रति", "यथा", "अनु", "सह")
            val isSubanta = second.kind == TermKind.PRATIPADIKA
            isAvyaya && isSubanta
        }

        val first = context.terms[index]
        val second = context.terms[index + 1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.1.6 forms Avyayībhāva compound '${compoundSurface}'."
        )
    }
}
