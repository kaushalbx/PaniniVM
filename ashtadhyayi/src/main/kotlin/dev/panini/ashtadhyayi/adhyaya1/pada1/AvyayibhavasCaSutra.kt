package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.SamasaType
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 1.1.41 अव्ययीभावश्च — an avyayībhāva compound receives अव्यय-saṃjñā. */
object AvyayibhavasCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.41", text = "अव्ययीभावश्च",
    hindiExplanation = "अव्ययीभाव समास की अव्यय संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110041,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SAMJNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.samasaType == SamasaType.AVYAYIBHAVA &&
            context.terms.any {
                it.kind == TermKind.PRATIPADIKA && SamjnaAssignment(it.id, Samjna.AVYAYA) !in context.samjnas
            }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
            .map { SamjnaAssignment(it.id, Samjna.AVYAYA) }.toSet()
        return DerivationChange(context.withSamjnas(assignments), "1.1.41 assigns अव्यय-saṃjñā to the avyayībhāva compound.")
    }
}
