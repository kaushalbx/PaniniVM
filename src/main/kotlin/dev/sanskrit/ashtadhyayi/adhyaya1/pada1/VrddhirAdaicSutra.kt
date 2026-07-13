package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.AllOf
import dev.sanskrit.derivation.AssignSamjnaToTermsContaining
import dev.sanskrit.derivation.AtDerivationStage
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.TermsContainAnyMark
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraExample
import dev.sanskrit.sutra.SutraInput
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraStage
import dev.sanskrit.sutra.SutraType

/**
 * English: Defines the वृद्धि vowels as आ, ऐ, and औ.
 * हिन्दी: आ, ऐ और औ स्वर वृद्धि संज्ञा वाले माने जाते हैं।
 * Code reference: dev.sanskrit.samjna.SvaraSamjna.vrddhi.
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
