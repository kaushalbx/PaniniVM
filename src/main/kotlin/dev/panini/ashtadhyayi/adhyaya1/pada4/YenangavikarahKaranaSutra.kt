package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult
import dev.panini.vyakaranam.analysis.SemanticRelation

/**
 * Sūtra 1.4.39 येनाङ्गविकारः.
 * The body-part through which a physical deformity is manifested is designated as Karaṇa (instrument).
 * e.g. काणेन पश्यति — "he sees with the (one) eye" (the defective eye is the karaṇa of sight).
 */
object YenangavikarahKaranaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.39", text = "येनाङ्गविकारः",
    hindiExplanation = "येन अङ्गेन अङ्गी विकृतो लक्ष्यते तस्य करणसंज्ञा भवति।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140039,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean =
        SemanticRelation.BODY_DEFORMITY in context.participant.semanticRelations &&
            Karaka.KARANA in context.candidates

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARANA,
        KarakaEvidence(
            number,
            text,
            "The body-part through which a physical deformity is indicated is the karaṇa (1.4.39 येनाङ्गविकारः).",
        ),
    )
}
