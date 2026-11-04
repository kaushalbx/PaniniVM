package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.hasEstablishedMeaning
import dev.panini.analysis.isStrongestOrdinaryReading
import dev.panini.analysis.KarakaDhatuDomains.isCausativeConstruction

/**
 * Sūtra 1.4.51 अकथितं च.
 * Assigns Karma-saṃjñā to secondary arguments (gauṇa karman) of dvikarmaka roots
 * Includes Kāśikā's भिक्ष् and Kaumudī's पच्/दण्ड् alongside the common inventory.
 */
object AkathitamCaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.51", text = "अकथितं च",
    hindiExplanation = "अपादानादिभिरविशेषितः कारकः कर्मसंज्ञः स्यात्। दुह्-याच्-रुध्-प्रच्छ्-भिक्ष्-चि-ब्रू-शास् तथा कौमुद्युक्तेषु पच्-दण्ड्-जि-मथ्-मुष्-नी-हृ-कृष्-वह् अर्थेषु।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140051,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.49"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val matchesDhatu = KarakaDhatuDomains.isDvikarmaka(context)
        return matchesDhatu && (!isCausativeConstruction(context) ||
            context.participant.hasEstablishedMeaning(SemanticRelation.SECONDARY_OBJECT)) &&
            context.participant.semanticRelations.any {
                (it == SemanticRelation.SECONDARY_OBJECT || it == SemanticRelation.AMBIGUOUS_DVIKARMAKA_OBJECT) &&
                    context.participant.isStrongestOrdinaryReading(it)
            } &&
            (Karaka.KARMAN in context.candidates ||
                context.participant.hasEstablishedMeaning(SemanticRelation.SECONDARY_OBJECT))
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The secondary argument of a dvikarmaka root is assigned Karma-saṃjñā (1.4.51 अकथितं च)."),
    )
}
