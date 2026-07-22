package dev.panini.ashtadhyayi.adhyaya1.pada4

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
 * 1.4.3: yūstryākhyau nadī.
 * Stems ending in long 'ī' or 'ū' which denote the feminine gender are called 'nadī'.
 */
object NadiSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.3",
    text = "यूस्त्र्याख्यौ नदी",
    hindiExplanation = "ईकारान्त और ऊकारान्त नित्य स्त्रीलिंग शब्दों की नदी संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140003,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
            (term.surface.endsWith('ई') || term.surface.endsWith('ी') ||
             term.surface.endsWith('ऊ') || term.surface.endsWith('ू')) &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.NADI }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.PRATIPADIKA &&
                      (it.surface.endsWith('ई') || it.surface.endsWith('ी') ||
                       it.surface.endsWith('ऊ') || it.surface.endsWith('ू')) }
            .map { SamjnaAssignment(it.id, Samjna.NADI) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.4.3 assigns नदी संज्ञा to long i/u feminine stems."
        )
    }
}
