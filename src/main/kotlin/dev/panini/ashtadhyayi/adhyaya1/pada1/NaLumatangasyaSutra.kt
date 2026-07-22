package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.LopaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.63: na lumatāṅgasya.
 * An operation triggered by an affix that was deleted by a 'lu' term (luk, ślu, lup)
 * does NOT take place if it is an aṅga-related operation.
 * This is an exception to 1.1.62.
 */
object NaLumatangasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.63",
    text = "न लुमताङ्गस्य",
    hindiExplanation = "लुमत् (लुक्, श्लु, लुप्) के द्वारा प्रत्यय का लोप होने पर अङ्ग-सम्बन्धी कार्य नहीं होते।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110063,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("1.1.62")
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean = false // Interpretive rule

    override fun apply(context: DerivationState): DerivationChange =
        error("Paribhasha sutra 1.1.63 should not be applied directly.")

    /**
     * Checks if pratyayalakṣaṇa is allowed for a given dropped term in an aṅga context.
     */
    fun allowsPratyayalaksana(droppedTerm: dev.panini.derivation.DerivationTerm, isAngaContext: Boolean): Boolean {
        if (!isAngaContext) return true
        return droppedTerm.deletionType == null || droppedTerm.deletionType == LopaType.LOPA
    }
}
