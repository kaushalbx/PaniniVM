package dev.panini.ashtadhyayi.adhyaya8.pada4

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
 * 8.4.63: śaś cho'ṭi.
 * After a jhay consonant, 'ś' is optionally replaced by 'ch' when followed by an 'aṭ' sound (vowels, y, v, r, h).
 */
object ShashChoAtiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.63",
    text = "शश्छोऽटि",
    hindiExplanation = "झय् से उत्तर श-कार के स्थान पर अट् परे रहते विकल्प से छ-कार आदेश होता है।",
    type = SutraType.VIBHASHA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840063,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        return context.shaToChaTargets(additionalAm = false).isNotEmpty()
    }

    override fun apply(context: DerivationState): DerivationChange {
        return context.applyShaToCha(requireNotNull(context.shaToChaTargets(additionalAm = false).firstOrNull()), sutra)
    }
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val occurrences = state.shaToChaTargets(additionalAm = false)
        require(occurrences.isNotEmpty())
        return occurrences.map { state.applyShaToCha(it, sutra) } + DerivationChange(state, "8.4.63 declined.", applied = false)
    }
}
