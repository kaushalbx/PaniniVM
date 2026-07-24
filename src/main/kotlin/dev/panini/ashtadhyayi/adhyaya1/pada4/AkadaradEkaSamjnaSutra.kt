package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.4.1: ā kaḍārād ekā saṃjñā.
 * From here until 2.2.38 (Kaḍārāḥ karmadhāraye), only one name is assigned.
 * In case of conflict, the later rule wins (1.4.2).
 */
object AkadaradEkaSamjnaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.1",
    text = "आ कडारादेका संज्ञा",
    hindiExplanation = "यहाँ से लेकर 'कडाराः कर्मधारये' (2.2.38) सूत्र तक एक ही संज्ञा होती है।",
    type = SutraType.ADHIKARA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140001,
    role = SutraRole.Adhikara(endKrama = 220038),
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = "1.4.1" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.activateAdhikara("1.4.1"),
        explanation = "1.4.1 (Ekā Saṃjñā) jurisdiction activated."
    )
}
