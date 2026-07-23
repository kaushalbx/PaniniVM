package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
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

object KarmanaYamAbhipraitiSampradanamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.32", text = "कर्मणा यमभिप्रैति स सम्प्रदानम्",
    hindiExplanation = "कर्म के द्वारा जिसे अभिप्रेत किया जाता है उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140032,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val hasKarmanCoArgument = context.allParticipants.any { other ->
            other.id != context.participant.id &&
                (SemanticRelation.DESIRED_OBJECT in other.semanticRelations ||
                    other.possibleVibhaktis.contains(Vibhakti.DVITIYA))
        }
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isAngerVerbOrRucOrSprhaOrDharayOrShlagh = normalized.contains("क्रुध") || normalized.contains("द्रुह") ||
                               normalized == "ईर्ष्या" || normalized == "असूया" ||
                               normalized.startsWith("ईर्ष्य") || normalized.startsWith("असूय") ||
                               normalized == "रुच" || normalized == "रोच" || normalized.startsWith("रोच") ||
                               normalized == "स्पृह" || normalized == "स्पृहय" || normalized.startsWith("स्पृह") ||
                               normalized == "धृ" || normalized == "धारय" || normalized.startsWith("धारय") ||
                               normalized == "श्लाघ" || normalized == "ह्नु" || normalized == "स्था" || normalized == "शप" ||
                               normalized.startsWith("श्लाघ") || normalized.startsWith("ह्नु") || normalized.startsWith("तिष्ठ") || normalized.startsWith("शप")
        val isRecipient = SemanticRelation.RECIPIENT in context.participant.semanticRelations
        val isCandidate = Karaka.SAMPRADANA in context.candidates
        return !isAngerVerbOrRucOrSprhaOrDharayOrShlagh && isRecipient && isCandidate && (hasKarmanCoArgument || context.allParticipants.size <= 1)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(number, text, "The participant is the intended recipient of the object (कर्मणा यम् अभिप्रैति)."),
    )
}
