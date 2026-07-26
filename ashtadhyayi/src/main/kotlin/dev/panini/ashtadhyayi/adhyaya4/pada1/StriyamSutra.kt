package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 4.1.3: स्त्रियाम्.
 * Adhikāra sūtra governing feminine suffix rules through 4.1.75.
 */
object StriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.3",
    text = "स्त्रियाम्",
    hindiExplanation = "स्त्रीत्व (स्त्रीलिङ्ग) की विवक्षा में प्रातिपदिक से परे प्रत्यय होते हैं।",
    type = SutraType.ADHIKARA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410003,
    role = SutraRole.Adhikara(410075),
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        return !context.activeAdhikaras.contains(number)
    }

    override fun apply(context: DerivationState): DerivationChange {
        return DerivationChange(
            state = context.copy(activeAdhikaras = context.activeAdhikaras + number),
            explanation = "4.1.3 activates Striyām adhikāra for feminine suffixation."
        )
    }
}

typealias StriyamAdhikaraSutra = StriyamSutra
