package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.1.60: adarśanaṃ lopaḥ.
 * Disappearance (non-perception) of an element is called 'lopa'.
 */
object AdarsanamLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.60",
    text = "अदर्शनं लोपः",
    hindiExplanation = "वर्ण के न दिखने (अदर्शन) को लोप संज्ञा दी जाती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110060,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}

/**
 * 1.1.61: pratyayasya luk-ślū-lupaḥ.
 * Deletion of an affix by the terms 'luk', 'ślu', or 'lup' is also called 'lopa', 
 * but with different persistence properties.
 */
object LukShluLupSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.61",
    text = "प्रत्ययस्य लुक्श्लुलुपः",
    hindiExplanation = "प्रत्यय के लोप को लुक्, श्लु और लुप् भी कहा जाता है (विशिष्ट सन्दर्भों में)।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110061,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
