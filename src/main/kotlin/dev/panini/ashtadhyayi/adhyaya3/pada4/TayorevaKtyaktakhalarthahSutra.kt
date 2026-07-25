package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.70 तयोरेव कृत्यक्तखलर्थाः.
 * Prescribes that Kṛtya, Kta, and Khalartha affixes occur only in Karmaṇi and Bhāve.
 */
object TayorevaKtyaktakhalarthahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.70", text = "तयोरेव कृत्यक्तखलर्थाः",
    hindiExplanation = "कृत्य, क्त तथा खलर्थ प्रत्यय केवल भाव तथा कर्म अर्थों में ही होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340070,
    role = SutraRole.Niyama, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        "3.4.70" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("3.4.70"),
            explanation = "3.4.70 restricts kṛtya/kta to karmaṇi/bhāve.",
        )
}
