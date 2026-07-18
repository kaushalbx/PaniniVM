package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.BlockSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 1.1.4 prevents 1.1.3 where its trigger is dhatu loss before an ardhadhatuka affix. */
object NaDhatulopaArdhadhatukeSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.4",
    text = "न धातुलोप आर्धधातुके",
    hindiExplanation = "आर्धधातुक प्रसंग में धातु-लोप से होने वाला गुण-वृद्धि निषिद्ध है।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110004,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.DHATU, SutraInput.PRATYAYA, SutraInput.SEMANTIC_FEATURE),
    dependencies = setOf("1.1.3"),
    stage = SutraStage.ANGAKARYA,
    blocks = setOf("1.1.3"),
    traceTemplateValue = "{sutra} blocks गुण-वृद्धि caused only by dhātu loss.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasDerivationalEnvironment(DerivationalEnvironment.DHATU_LOPA).matches(context) &&
                HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) &&
                context.blockedSutras["1.1.3"] != sutra

    override fun apply(context: DerivationState): DerivationChange =
        BlockSutra(
            "1.1.3",
            sutra,
            "$sutra blocks 1.1.3 in the dhātu-lopa ārdhadhātuka environment."
        ).apply(context)
}
