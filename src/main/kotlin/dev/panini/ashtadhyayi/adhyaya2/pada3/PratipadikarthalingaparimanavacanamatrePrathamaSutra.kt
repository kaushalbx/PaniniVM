package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.46 प्रातिपदिकार्थलिङ्गपरिमाणवचनमात्रे प्रथमा.
 * Assigns Prathamā (nominative case) to convey bare nominal stem meaning, gender, measure, or number,
 * or when the kāraka is already expressed (abhihita).
 */
object PratipadikarthalingaparimanavacanamatrePrathamaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.46", text = "प्रातिपदिकार्थलिङ्गपरिमाणवचनमात्रे प्रथमा",
    hindiExplanation = "प्रातिपदिकार्थ, लिङ्ग, परिमाण और वचन का बोध कराने के लिए प्रथमा विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230046,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = emptySet(),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        (context.abhihita || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.PRATHAMA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PRATHAMA,
        KarakaEvidence(number, text, "प्रथमा realizes nominal stem meaning / abhihita karaka (2.3.46)."),
    )
}
