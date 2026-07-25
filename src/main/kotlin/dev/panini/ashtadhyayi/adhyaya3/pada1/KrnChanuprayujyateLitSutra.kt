package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.40 कृञ्चानुप्रयुज्यते लिटि.
 * Prescribes auxiliary kr/bhu/as after ām periphrastic affix in Liṭ.
 */
object KrnChanuprayujyateLitSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.40", text = "कृञ्चानुप्रयुज्यते लिटि",
    hindiExplanation = "आम् प्रत्यय के बाद कृ, भू, तथा अस् धातुओं का लिट् लकार में अनुप्रयोग होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310040,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT &&
        context.allEffectiveTerms.any { it.upadesha == "आम्" } &&
        "3.1.40" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("3.1.40"),
            explanation = "3.1.40 prescribes auxiliary kr/bhu/as after ām in Liṭ.",
        )
}
