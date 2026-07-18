package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.AllOf
import dev.panini.derivation.AssignSamjnaToTermsContaining
import dev.panini.derivation.AtDerivationStage
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.derivation.TermsContainAnyMark
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraExample
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * English: Defines the वृद्धि vowels as आ, ऐ, and औ.
 * हिन्दी: आ, ऐ और औ स्वर वृद्धि संज्ञा वाले माने जाते हैं।
 * Code reference: dev.panini.samjna.SvaraSamjna.vrddhi.
 */
/**
 * 1.1.1 keeps its catalog data and executable definition together.
 * It adds वृद्धि as grammatical state; it does not rewrite the term's surface.
 */
object VrddhirAdaicSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.1",
    text = "वृद्धिरादैच्",
    hindiExplanation = "आ, ऐ और औ को वृद्धि संज्ञा दी जाती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110001,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    stage = SutraStage.SAMJNA,
    traceTemplateValue = "{sutra} assigns वृद्धि संज्ञा to {target}.",
    examplesValue = listOf(SutraExample(Svara.AA.devanagari, "${Svara.AA.devanagari} [वृद्धि]")),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = AllOf(
        AtDerivationStage(DerivationStage.INITIAL),
        TermsContainAnyMark(vrddhiMarks, missingSamjna = Samjna.VRDDHI),
    ).matches(context)

    override fun apply(context: DerivationState): DerivationChange =
        AssignSamjnaToTermsContaining(
            vrddhiMarks,
            Samjna.VRDDHI,
            "वृद्धिरादैच् assigns वृद्धि संज्ञा to terms containing आ, ऐ, or औ."
        ).apply(context)
}

private val vrddhiMarks: Set<String> = setOf(Svara.AA, Svara.AI, Svara.AU).flatMap { svara ->
    listOfNotNull(svara.devanagari, svara.matra)
}.toSet()
