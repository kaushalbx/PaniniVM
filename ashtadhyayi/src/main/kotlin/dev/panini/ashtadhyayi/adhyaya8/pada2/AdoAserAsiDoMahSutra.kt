package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.isHrasva
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Exact mutva after preceding affix/vowel work. 8.2.81 independently overrides
 * the general u grade in its narrower plural-e domain. */
object AdoAserAsiDoMahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.80",
    text = "अदसोऽसेर्दादु दो मः",
    hindiExplanation = "अदस् के दकार को मकार और उससे परे वर्ण को मात्रा के अनुसार उकार या ऊकार आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820080,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = adasMutvaTarget(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (stem, index) = requireNotNull(adasMutvaTarget(context))
        val following = stem.varnas[index + 1]
        val vowel = when {
            following is Svara && !following.isHrasva -> Svara.UU
            else -> Svara.U
        }
        return DerivationChange(
            state = context.replaceTermVarnaOccurrences(stem.id,
                mapOf(index to Vyanjana.MA, index + 1 to vowel), sutra),
            explanation = "8.2.80 replaces d with m and its following occurrence with the corresponding u grade."
        )
    }
}
