package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.76: तद्धिताः. Adhikāra Sūtra governing Taddhita affixes in Adhyāyas 4 and 5. */
object TaddhitahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.76",
    text = "तद्धिताः",
    hindiExplanation = "अधिकार सूत्र: यहाँ से आगे पञ्चम अध्याय के अन्त तक कहे जाने वाले प्रत्यय 'तद्धित' संज्ञक होते हैं।",
    type = SutraType.ADHIKARA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410076,
    role = SutraRole.Adhikara(540000),
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        "4.1.76" !in context.activeAdhikaras && context.context.requestedMeaning != null

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.copy(activeAdhikaras = context.activeAdhikaras + "4.1.76"),
            explanation = "4.1.76 (Taddhitāḥ) adhikāra activated.",
        )
}
