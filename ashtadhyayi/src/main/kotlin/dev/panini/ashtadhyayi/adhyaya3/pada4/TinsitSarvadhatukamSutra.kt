package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.113 तिङ् शित् सार्वधातुकम्.
 * Assigns Sārvadhātuka saṃjñā to Tiङ् and Ś-it affixes.
 */
object TinsitSarvadhatukamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.113", text = "तिङ् शित् सार्वधातुकम्",
    hindiExplanation = "तिङ् प्रत्यय तथा श्-इत् प्रत्ययों की 'सार्वधातुक' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 3, pada = 4, optional = false, kramaValue = 340113,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val tingAndShitAffixes = setOf(
        "तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्",
        "त", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्",
        "शप्", "श्यन्", "श्नु", "श", "श्नम्", "श्ना"
    )

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATYAYA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVADHATUKA } &&
            (term.upadesha in tingAndShitAffixes || term.hasEffectiveMarker(ItMarker.SH))
        }

    override fun apply(context: DerivationState): DerivationChange {
        val newSamjnas = context.terms.filter { term ->
            term.kind == TermKind.PRATYAYA &&
            (term.upadesha in tingAndShitAffixes || term.hasEffectiveMarker(ItMarker.SH))
        }.map { SamjnaAssignment(it.id, Samjna.SARVADHATUKA) }.toSet()

        return DerivationChange(
            state = context.withSamjnas(newSamjnas),
            explanation = "3.4.113 assigns Sārvadhātuka saṃjñā to Tiङ् and Ś-it affixes.",
        )
    }
}
