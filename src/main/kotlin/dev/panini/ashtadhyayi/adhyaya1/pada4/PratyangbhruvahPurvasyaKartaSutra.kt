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

object PratyangbhruvahPurvasyaKartaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.40", text = "प्रत्याङ्भ्रुवः पूर्वस्य कर्ता",
    hindiExplanation = "प्रति-श्रु और आ-श्रु धातुओं के योग में जो पूर्व व्यापार का कर्ता (प्रेरक या याचक) है, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140040,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val tinganta = context.verbNode as? dev.panini.vyakaranam.ast.TingantaPada ?: return false
        val hasPrefix = tinganta.upasargas.any { it == "प्रति" || it == "आ" }
        val isShru = context.baseDhatu?.let { it.upadesha == "श्रु" || it.upadesha == "श्रुँ" || it.sourceSurface == "श्रु" }
                     ?: (tinganta.dhatu.mulaDhatu == "श्रु")
        return hasPrefix && isShru && SemanticRelation.RECIPIENT in context.participant.semanticRelations && Karaka.SAMPRADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the original requestor/promiser under roots prati-shru or a-shru."
        ),
    )
}
