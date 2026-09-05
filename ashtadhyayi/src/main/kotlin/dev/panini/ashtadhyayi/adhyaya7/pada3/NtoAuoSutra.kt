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

/** Masculine n-stem before au/auṭ: release n and form the dual au ending. */
object NtoAuoSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.138",
    text = "नत औऔटौ",
    hindiExplanation = "पुंलिङ्ग नकारान्त में प्रथमा-द्वितीया द्विवचन औ/औट् के परे नौ रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730138,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vacana != Vacana.DVIVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.endsWith("न्") && context.terms.last().upadesha in setOf("औ", "औट्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(2) + "ानौ"),
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.138: Formed the masculine n-stem dual नौ ending before ${affix.upadesha}.",
        )
    }
}
