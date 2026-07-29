package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.SamjnaVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.1: bhūvādayo dhātavaḥ.
 * Elements starting with 'bhū' are called 'dhātu' (verbal roots).
 */
object BhuvadayoDhatavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.1",
    text = "भूवादयो धातवः",
    hindiExplanation = "भू आदि क्रियावाचक शब्दों की धातु संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130001,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DHATU,
), DerivationSutra, SamjnaVidhayakaSutra {
    override val artha = SamjnaDefinitionArtha(
        samjni = Samjni.BHU_ADI_VERBAL_ROOT,
        samjna = Samjna.DHATU,
    )

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.DHATU &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.DHATU }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.DHATU }
            .map { SamjnaAssignment(it.id, Samjna.DHATU) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.3.1 assigns धातु संज्ञा to verbal roots."
        )
    }
}
