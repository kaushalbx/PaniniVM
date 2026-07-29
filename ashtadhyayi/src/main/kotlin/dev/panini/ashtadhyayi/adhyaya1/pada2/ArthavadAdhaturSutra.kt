package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.ArthavatSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.2.45: arthavadadhāturapratyayaḥ prātipadikam.
 * A meaningful element that is not a root (dhātu) and not an affix (pratyaya) is a prātipadika.
 */
object ArthavadAdhaturSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.45",
    text = "अर्थवदधातुरप्रत्ययः प्रातिपदिकम्",
    hindiExplanation = "धातु और प्रत्यय को छोड़कर अर्थवान शब्द-स्वरूप की प्रातिपदिक संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 2,
    optional = false,
    kramaValue = 120045,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra, ArthavatSutra {
    override val artha = SamjnaDefinitionArtha(
        samjni = Samjni.MEANINGFUL_NON_DHATU_NON_PRATYAYA,
        samjna = Samjna.PRATIPADIKA,
    )

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRATIPADIKA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.PRATIPADIKA }
            .map { SamjnaAssignment(it.id, Samjna.PRATIPADIKA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.2.45 assigns प्रातिपदिक संज्ञा to meaningful stems."
        )
    }
}
