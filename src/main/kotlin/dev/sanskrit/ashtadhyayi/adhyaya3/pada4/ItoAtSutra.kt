package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.106: iṭo 't. Replaces the Ātmanepada liṅ ending इट् with अ. */
object ItoAtSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.106",
    text = "इटोऽत्",
    hindiExplanation = "लिङ् में आत्मनेपद के इट् प्रत्यय के स्थान पर अ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340106,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.stage != DerivationStage.INITIAL &&
            ending.matchesUpadesha("इट्") &&
            (ending.surface.contains('इ') || ending.surface.contains('ट'))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "अ")),
            "3.4.106 replaces Ātmanepada इट् with अ in liṅ.",
        )
    }
}
