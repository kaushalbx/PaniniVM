package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Samjni
import dev.panini.sutra.SamjniSetDefinitionArtha
import dev.panini.sutra.SamjniSamuhVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.2.46 कृत्तद्धितसमासाश्च.
 * Assigns prātipadika saṃjñā to kṛt-ending, taddhita-ending, and compound stems.
 */
object KrtTaddhitaSamasascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.46", text = "कृत्तद्धितसमासाश्च",
    hindiExplanation = "कृदन्त, तद्धितान्त तथा समास शब्दों की 'प्रातिपदिक' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 2, optional = false, kramaValue = 120046,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra, SamjniSamuhVidhayakaSutra {
    override val artha = SamjniSetDefinitionArtha(
        samjnis = setOf(
            Samjni.KRT_ENDING,
            Samjni.TADDHITA_ENDING,
            Samjni.SAMASA,
        ),
        samjna = Samjna.PRATIPADIKA,
    )

    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("घञ्", "तव्यत्", "अनीयर", "यत", "क्त", "तुमुन्", "क्त्वा") } &&
        context.samjnas.none { it.samjna == Samjna.PRATIPADIKA }

    override fun apply(context: DerivationState): DerivationChange {
        val targetTerm = context.allEffectiveTerms.first()
        val newSamjna = SamjnaAssignment(targetTerm.id, Samjna.PRATIPADIKA)
        return DerivationChange(
            state = context.withSamjnas(setOf(newSamjna)),
            explanation = "1.2.46 assigns prātipadika saṃjñā to kṛt/taddhita/samāsa stems.",
        )
    }
}
