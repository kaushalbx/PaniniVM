package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraInput
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraStage
import dev.sanskrit.sutra.SutraType

/** 1.1.8 assigns anunasika to terms that explicitly carry a nasalisation mark. */
object MukhanasikavacanoAnunasikahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.8",
    text = "मुखनासिकावचनोऽनुनासिकः",
    hindiExplanation = "मुख और नासिका दोनों से उच्चरित वर्ण अनुनासिक हैं।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110008,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    stage = SutraStage.SAMJNA,
    traceTemplateValue = "{sutra} assigns अनुनासिक to explicitly nasalised sound material.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any { term ->
        term.surface.any { it in nasalisationMarks } && SamjnaAssignment(
            term.id,
            Samjna.ANUNASIKA
        ) !in context.samjnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments =
            context.terms.filter { it.surface.any { mark -> mark in nasalisationMarks } }
                .map { SamjnaAssignment(it.id, Samjna.ANUNASIKA) }.toSet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "$sutra assigns अनुनासिक to nasalised material."
        )
    }
}

private val nasalisationMarks = setOf('ँ', 'ं')
