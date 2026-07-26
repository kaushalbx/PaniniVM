package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.10: yathāsaṃkhyamanudeśaḥ samānām.
 * Substitutes are mapped to their sources in order when the counts match.
 */
object YathasamkhyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.10",
    text = "यथासंख्यमनुदेशः समानाम्",
    hindiExplanation = "समान संख्या वाले उद्देश्य और विधेय का सम्बन्ध क्रम के अनुसार होता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130010,
    role = SutraRole.Paribhasha(),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean = false

    override fun apply(context: DerivationState): DerivationChange =
        error("Paribhasha sutra 1.3.10 should not be applied directly.")

    /**
     * Maps an item at a specific index in a source list to the item at the same index in the target list.
     */
    fun <T, R> map(item: T, sources: List<T>, targets: List<R>): R? {
        val index = sources.indexOf(item)
        return if (index != -1 && index < targets.size) targets[index] else null
    }
}
