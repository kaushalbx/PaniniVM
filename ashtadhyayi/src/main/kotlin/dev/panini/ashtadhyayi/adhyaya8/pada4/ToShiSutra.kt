package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.4.43 तोः षि — a tu before ष is not retroflexed by 8.4.41 (सन् + षष्ठः → सन्षष्ठः).
 * It changes nothing by itself; 8.4.41 consults [protects].
 */
object ToShiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.43", text = "तोः षि",
    hindiExplanation = "षकार परे होने पर तवर्ग को ष्टुत्व नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 8, pada = 4, optional = false, kramaValue = 840043,
    role = SutraRole.Nishedha, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI, blocks = setOf("8.4.41"),
), DerivationSutra {
    override val advancesTripadiFrontier = false
    private val tu = setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)

    fun protects(left: Varna, right: Varna): Boolean = left in tu && right == Vyanjana.SSA

    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState): DerivationChange =
        error("8.4.43 restricts 8.4.41 and does not apply on its own")
}
