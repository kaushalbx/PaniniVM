package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 1.4.8 पतिः समास एव — पति is घि only inside a compound: भूपतये, but पत्ये and पत्युः alone.
 * It changes nothing by itself; 1.4.7 consults [excludes].
 */
object PatihSamasaEvaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.8", text = "पतिः समास एव",
    hindiExplanation = "पति शब्द की घि संज्ञा केवल समास में होती है।",
    type = SutraType.NISHEDHA, chapter = 1, pada = 4, optional = false, kramaValue = 140008,
    role = SutraRole.Niyama, action = SutraAction.NIYAMA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val pati = listOf(Vyanjana.PA, Svara.A, Vyanjana.TA, Svara.I)

    /** A stem that is पति on its own, not the last member of a compound such as भूपति. */
    fun excludes(term: DerivationTerm): Boolean = term.varnas == pati && term.compoundHeadUpadesha == null

    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState): DerivationChange =
        error("1.4.8 restricts 1.4.7 and does not apply on its own")
}
