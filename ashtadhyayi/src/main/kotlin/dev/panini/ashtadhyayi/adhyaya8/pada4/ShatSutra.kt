package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.4.44 शात् — a tu (तोः, continued from 8.4.43) after श is not palatalised by 8.4.40
 * (प्रश्नः, विश्नः, श्नु). It changes nothing by itself; 8.4.40 consults [protects].
 */
object ShatSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.44", text = "शात्",
    hindiExplanation = "शकार से परे तवर्ग को श्चुत्व नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 8, pada = 4, optional = false, kramaValue = 840044,
    role = SutraRole.Nishedha, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI, blocks = setOf("8.4.40"),
), DerivationSutra {
    override val advancesTripadiFrontier = false
    private val tu = setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)

    fun protects(left: Varna, right: Varna): Boolean = left == Vyanjana.SHA && right in tu

    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState): DerivationChange =
        error("8.4.44 restricts 8.4.40 and does not apply on its own")
}
