package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
            context.replaceWholeAffix(
                id = ending.id,
                surface = "अ",
                sutra = number,
                policy = WholeAffixDesignationPolicy.Consume,
            ),
            "3.4.106 replaces Ātmanepada इट् with अ in liṅ.",
        )
    }
}
