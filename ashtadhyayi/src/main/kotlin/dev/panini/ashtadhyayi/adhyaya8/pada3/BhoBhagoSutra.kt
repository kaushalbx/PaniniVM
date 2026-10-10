package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.17: bho-bhago-agho-apūrvasya yo'śi.
 * Compatibility entry point for the canonical implementation.
 * Applicability and mutations must not diverge from the registered rule.
 */
object BhoBhagoSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.17",
    text = "भोभगोअघोअपूर्वस्य योऽशि",
    hindiExplanation = "भो, भगो, अघो शब्दों के बाद या अ/आ के बाद वाले 'रु' (र्) के स्थान पर 'य्' आदेश होता है, यदि बाद में अश् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830017,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = BhoBhagoAghoApurvasyaYoshiSutra.matches(context)

    override fun apply(context: DerivationState): DerivationChange = BhoBhagoAghoApurvasyaYoshiSutra.apply(context)
}
