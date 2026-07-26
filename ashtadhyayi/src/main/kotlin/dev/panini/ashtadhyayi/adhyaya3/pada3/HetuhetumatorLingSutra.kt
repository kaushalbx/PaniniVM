package dev.panini.ashtadhyayi.adhyaya3.pada3

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
 * Sūtra 3.3.156 हेतुहेतुमतोर्लिङ्.
 * Prescribes liṅ mood in cause-and-effect relationship.
 */
object HetuhetumatorLingSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.156", text = "हेतुहेतुमतोर्लिङ्",
    hindiExplanation = "हेतु और हेतुमत् (कारण और कार्य) का सम्बन्ध प्रकट होने पर धातुओं से 'लिङ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330156,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LING &&
        "3.3.156" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("3.3.156"),
            explanation = "3.3.156 prescribes लिङ् mood in cause-effect.",
        )
}
