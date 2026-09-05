package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Neuter s-stem nominative/accusative dual: form the sī ending. */
object StanapumsakayorDvivacaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.149",
    text = "सतो नपुंसकयोर्द्विवचने",
    hindiExplanation = "नपुंसकलिङ्ग सकारान्त में प्रथमा-द्वितीया द्विवचन में सी रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730149,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.NAPUMSAKA ||
            context.effectiveContext.rupa.vacana != Vacana.DVIVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)
        ) return false
        return context.terms[context.terms.size - 2].surface.endsWith("स्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(2) + "सी"),
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.149: Formed the neuter s-stem nominative/accusative dual सी ending.",
        )
    }
}
