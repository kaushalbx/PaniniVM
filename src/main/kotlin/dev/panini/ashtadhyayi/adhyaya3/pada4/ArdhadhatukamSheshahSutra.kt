package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.ItMarker
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
 * Sūtra 3.4.114 आर्धधातुकं शेषः.
 * Assigns Ārdhadhātuka saṃjñā to remaining affixes after a root (excluding Tiṅ and Ś-it affixes).
 */
object ArdhadhatukamSheshahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.114", text = "आर्धधातुकं शेषः",
    hindiExplanation = "धातु से परे विहित शेष (सार्वधातुक से भिन्न) प्रत्ययों की 'आर्धधातुक' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 3, pada = 4, optional = false, kramaValue = 340114,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {

    private fun isTingOrShit(term: DerivationTerm): Boolean =
        term.upadesha in setOf("तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्", "त", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्", "शप्", "श्यन्", "श्नु", "श", "श्नम्", "श्ना") ||
        term.hasEffectiveMarker(ItMarker.SH)

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATYAYA &&
            !isTingOrShit(term) &&
            context.samjnas.none { it.targetId == term.id && (it.samjna == Samjna.SARVADHATUKA || it.samjna == Samjna.ARDHADHATUKA) }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val newSamjnas = context.terms.filter { term ->
            term.kind == TermKind.PRATYAYA &&
            !isTingOrShit(term) &&
            context.samjnas.none { it.targetId == term.id && (it.samjna == Samjna.SARVADHATUKA || it.samjna == Samjna.ARDHADHATUKA) }
        }.map { SamjnaAssignment(it.id, Samjna.ARDHADHATUKA) }.toSet()

        return DerivationChange(
            state = context.withSamjnas(newSamjnas),
            explanation = "3.4.114 assigns Ārdhadhātuka saṃjñā to remaining non-Tiṅ/non-Śit affixes.",
        )
    }
}
