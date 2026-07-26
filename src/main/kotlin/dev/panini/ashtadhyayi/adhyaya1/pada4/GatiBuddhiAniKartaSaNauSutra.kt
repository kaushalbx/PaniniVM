package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult

import dev.panini.analysis.SemanticRelation

/**
 * Sūtra 1.4.52 गतिबुद्धिप्रत्यवसानार्थशब्दकर्माकर्मकाणामणि कर्ता स णौ.
 * In causative (ṇi) constructions, the agent of non-causative verbs of motion (gati),
 * knowledge (buddhi), eating (pratyavasāna), verbal action (śabdakarma), or intransitive (akarmaka)
 * roots becomes the object (karman) of the causative verb.
 */
object GatiBuddhiAniKartaSaNauSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.52", text = "गतिबुद्धिप्रत्यवसानार्थशब्दकर्माकर्मकाणामणि कर्ता स णौ",
    hindiExplanation = "गत्त्याद्यर्थानाम् अणौ यः कर्ता स णौ कर्मसंज्ञः स्यात्।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140052,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23", "1.4.49"),
) {
    private val hrKrRoots = setOf("हृ", "कृ", "हार", "कार", "हर", "कर")

    override fun matches(context: KarakaRuleContext): Boolean {
        val root = context.dhatu.surface.trimEnd('्', 'ँ')
        val isHrKr = hrKrRoots.any { r -> root.contains(r) || r.contains(root) }
        return context.prayoga == Prayoga.CAUSATIVE &&
            !isHrKr &&
            SemanticRelation.PROMPTER_CAUSE !in context.participant.semanticRelations &&
            Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The non-causative agent of motion/knowledge/eating/speech/intransitive verb becomes Karman in causative (1.4.52)."),
    )
}
