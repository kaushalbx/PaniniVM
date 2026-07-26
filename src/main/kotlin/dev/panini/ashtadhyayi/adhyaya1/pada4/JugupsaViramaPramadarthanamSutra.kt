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

/**
 * Sūtra/Vārttika 1.4.25 जुगुप्साविरामप्रमादार्थानामुपसंख्यानम्.
 * Assigns Apādāna-saṃjñā to the point of origin/reference for verbs expressing aversion (jugupsā),
 * stopping (virāma), or negligence (pramāda).
 */
object JugupsaViramaPramadarthanamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.24V", text = "जुगुप्साविरामप्रमादार्थानामुपसंख्यानम्",
    hindiExplanation = "जुगुप्सा, विराम तथा प्रमादार्थक धातुओं के योग में अपादान संज्ञा होती है। (कात्यायनवार्तिकम्, 1.4.24 पर)",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140024 + 1,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23", "1.4.24"),
) {
    private val targetRoots = setOf("जुगुप्स्", "रम्", "मद्", "जुगुप्सते", "विराम", "प्रमाद्यति")

    override fun matches(context: KarakaRuleContext): Boolean {
        val root = context.dhatu.surface.trimEnd('्', 'ँ')
        val matchesRoot = targetRoots.any { r -> root.contains(r) || r.contains(root) }
        return matchesRoot && Karaka.APADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(number, text, "Assigns Apādāna-saṃjñā for jugupsā/virāma/pramāda constructions (1.4.25)."),
    )
}
