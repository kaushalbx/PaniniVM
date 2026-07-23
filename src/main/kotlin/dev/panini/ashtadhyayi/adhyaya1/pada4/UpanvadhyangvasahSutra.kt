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

object UpanvadhyangvasahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.48", text = "उपान्वध्याङ्वसः",
    hindiExplanation = "उप, अनु, अधि, आङ् उपसर्ग पूर्वक वस् धातु के आधार की कर्मसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140048,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isPrefixedVas = normalized == "उपवस्" || normalized == "अनुवस्" || normalized == "अधिवस्" || normalized == "आवस्" ||
                            normalized.startsWith("उपवस") || normalized.startsWith("अनुवस") || normalized.startsWith("अधिवस") || normalized.startsWith("आवस")
        return isPrefixedVas && SemanticRelation.LOCATION in context.participant.semanticRelations && Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(
            number,
            text,
            "The location of dwelling verb vas prefixed with upa, anu, adhi, or aa is designated as karma."
        ),
    )
}
