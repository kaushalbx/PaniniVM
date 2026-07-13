package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
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
 * 1.1.2: adeṅ guṇaḥ.
 * Defines 'a', 'e', and 'o' as having the name 'guṇa'.
 */
object AdengGunaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.2",
    text = "अदेङ्गुणः",
    hindiExplanation = "अत् (ह्रस्व अ) और एङ् (ए, ओ) की गुण संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110002,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    stage = SutraStage.SAMJNA,
    traceTemplateValue = "{sutra} assigns गुण संज्ञा to {target}.",
    examplesValue = listOf(SutraExample(Svara.E.devanagari, "${Svara.E.devanagari} [गुण]")),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.surface.any { it in gunaChars } &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.GUNA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val newSamjnas = context.terms.filter { term ->
            term.surface.any { it in gunaChars }
        }.map { SamjnaAssignment(it.id, Samjna.GUNA) }
        
        return DerivationChange(
            state = context.withSamjnas(newSamjnas.toSet()),
            explanation = "1.1.2 assigns गुण संज्ञा to eligible terms."
        )
    }
}

private val gunaChars = setOf('अ', 'ए', 'ओ', 'े', 'ो')
