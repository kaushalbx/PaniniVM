package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 5.3.1 प्राग्दिशो विभक्तिः: the affixes through 5.3.26 have vibhakti designation. */
object PragDisoVibhaktihSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.1", text = "प्राग्दिशो विभक्तिः",
    hindiExplanation = "दिक्शब्दविहित प्रत्यय से पूर्व के तसिलादि प्रत्यय विभक्ति-संज्ञक होते हैं।",
    type = SutraType.ADHIKARA, chapter = 5, pada = 3, optional = false, kramaValue = 530001,
    role = SutraRole.Adhikara(530026), action = SutraAction.ADHIKARA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.context.requestedMeaning != null && "4.1.76" in context.activeAdhikaras &&
            number !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(activeAdhikaras = context.activeAdhikaras + number),
        "5.3.1 activates the prāgdiśīya vibhakti domain.",
    )
}
