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

object AdhishingsthasamKarmaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.46", text = "अधिशीङ्स्थासां कर्म",
    hindiExplanation = "अधि उपसर्ग पूर्वक शीङ्, स्था और आस् धातुओं के आधार की कर्मसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140046,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val tinganta = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return false
        val hasPrefix = tinganta.upasargas.any { it == "अधि" }
        val isSheeSthaAs = context.baseDhatu?.let {
            it.upadesha in setOf("शीङ्", "शी", "स्था", "आस", "आस्")
        } ?: (tinganta.dhatu.mulaDhatu in setOf("शी", "स्था", "आस्", "शीङ्", "आस"))
        return hasPrefix && isSheeSthaAs && SemanticRelation.LOCATION in context.participant.semanticRelations && Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(
            number,
            text,
            "The location of shee/stha/as prefixed with adhi is designated as karma."
        ),
    )
}
