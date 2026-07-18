package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.1.4: पूर्वोऽभ्यासः. */
object PurvoBhyasahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.4", text = "पूर्वोऽभ्यासः", hindiExplanation = "द्विर्वचन में पूर्व भाग की अभ्यास संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 6, pada = 1, optional = false, kramaValue = 610004,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any { it.id == "abhyasa" } && context.samjnas.none { it.targetId == "abhyasa" && it.samjna == Samjna.ABHYASA }
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context.withSamjnas(setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA))), "6.1.4 assigns अभ्यास saṃjñā to the preceding duplicated term.")
}
