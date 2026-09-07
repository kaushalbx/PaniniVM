package dev.panini.ashtadhyayi.adhyaya6.pada1

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

object NnityadirNityamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.197", text = "ञ्नित्यादिर्नित्यम्", hindiExplanation = "ञित् अथवा णित् प्रत्यय में आदि स्वर नित्य उदात्त होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610197,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.PRATYAYA, stage = SutraStage.SVARA,
    blocks = setOf("3.1.3", "3.1.4"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.svaraAssignments.none { it.accent == AccentType.UDATTA } &&
            context.svaraNimittas.any { it.kind == SvaraNimittaKind.NIT_OR_NGIT }

    override fun apply(context: DerivationState): DerivationChange {
        val vowelIndex = requireNotNull(context.svaraNimittas.first { it.kind == SvaraNimittaKind.NIT_OR_NGIT }.vowelIndex)
        return DerivationChange(context.assignSvara(vowelIndex, AccentType.UDATTA, number), "$text assigns udātta to the initial vowel of the affix.")
    }
}
