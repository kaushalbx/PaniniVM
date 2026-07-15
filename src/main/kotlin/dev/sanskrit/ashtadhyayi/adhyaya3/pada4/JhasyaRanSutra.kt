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

/** 3.4.105: jhasya raṇ. Replaces the Ātmanepada liṅ ending झ with रन्. */
object JhasyaRanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.105",
    text = "झस्य रन्",
    hindiExplanation = "लिङ् में आत्मनेपद के झ प्रत्यय के स्थान पर रन् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340105,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.stage != DerivationStage.INITIAL &&
            ending.matchesUpadesha("झ") &&
            ending.surface != "रन्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "रन्")),
            "3.4.105 replaces Ātmanepada झ with रन् in liṅ.",
        )
    }
}
