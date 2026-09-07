package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.sutra.*

object AnudattamPadamEkavarjamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.158", text = "अनुदात्तं पदमेकवर्जम्", hindiExplanation = "एक उदात्त स्वर को छोड़कर पद के अन्य स्वर अनुदात्त होते हैं।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610158,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION, stage = SutraStage.SVARA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.svaraAssignments.any { it.accent == AccentType.UDATTA } &&
            context.svaraAssignments.map { it.vowelIndex }.distinct().size < DevanagariVowelLoci.positions(context.surface).size

    override fun apply(context: DerivationState): DerivationChange {
        val udatta = context.svaraAssignments.single { it.accent == AccentType.UDATTA }.vowelIndex
        val additions = DevanagariVowelLoci.positions(context.surface).indices.filter { it != udatta }
            .map { SvaraAssignment(it, AccentType.ANUDATTA, SvaraAssignmentSource.Sutra(number)) }
        return DerivationChange(context.copy(svaraAssignments = context.svaraAssignments + additions), "$text assigns anudātta to every vowel except the established udātta.")
    }
}
