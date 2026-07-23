package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.49: ṣaṣṭhī sthāneyogā.
 * Interpretative rule: A Genitive case inflection in a rule expresses the relation "in place of" (sthāne).
 */
object SasthiSthaneyogaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.49",
    text = "षष्ठी स्थानेयोगा",
    hindiExplanation = "अनियतसम्बन्धविशेषा षष्ठी स्थानेयोगेत्युपतिष्ठते।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110049,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.49 paribhāṣā applied.")
}
