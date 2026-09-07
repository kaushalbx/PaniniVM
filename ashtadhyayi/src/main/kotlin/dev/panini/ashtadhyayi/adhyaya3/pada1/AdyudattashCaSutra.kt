package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.AccentType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SvaraNimittaKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

object AdyudattashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.3", text = "आद्युदात्तश्च", hindiExplanation = "प्रत्यय का आदि स्वर उदात्त होता है।",
    type = SutraType.UTSARGA, chapter = 3, pada = 1, optional = false, kramaValue = 310003,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION, stage = SutraStage.SVARA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.svaraAssignments.none { it.accent == AccentType.UDATTA } &&
            context.svaraNimittas.any { it.kind == SvaraNimittaKind.PRATYAYA }

    override fun apply(context: DerivationState): DerivationChange {
        val vowelIndex = requireNotNull(context.svaraNimittas.first { it.kind == SvaraNimittaKind.PRATYAYA }.vowelIndex)
        return DerivationChange(
            context.assignSvara(vowelIndex, AccentType.UDATTA, number),
            "$text assigns udātta to the initial vowel of the affix."
        )
    }
}
