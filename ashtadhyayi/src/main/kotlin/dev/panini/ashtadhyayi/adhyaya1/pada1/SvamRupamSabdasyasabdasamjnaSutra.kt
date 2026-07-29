package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.InterpretivePrinciple
import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.ParibhashaVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.68 स्वं रूपं शब्दस्याशब्दसंज्ञा.
 * Specifies that a grammatical term denotes its own phonetic form unless it is a technical saṃjñā.
 */
object SvamRupamSabdasyasabdasamjnaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.68", text = "स्वं रूपं शब्दस्याशब्दसंज्ञा",
    hindiExplanation = "व्याकरणशास्त्र में शब्द अपने ही रूप (ध्वनि) का बोधक होता है, जब तक कि वह शास्त्रीय संज्ञा न हो।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110068,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.DERIVATION,
), DerivationSutra, ParibhashaVidhayakaSutra {
    override val artha = InterpretivePrincipleArtha(InterpretivePrinciple.SELF_FORM_REFERENCE)

    override fun matches(context: DerivationState): Boolean =
        "1.1.68" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.68"),
            explanation = "1.1.68 establishes self-form representation principle for terms.",
        )
}
