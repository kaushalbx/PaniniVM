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

/** Masculine ṛ-stem before au/auṭ: form the strong r-au dual base. */
object RtoAuoSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.131",
    text = "ऋत औऔटौ",
    hindiExplanation = "पुंलिङ्ग ऋकारान्त में प्रथमा-द्वितीया द्विवचन औ/औट् के परे अरौ रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730131,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vacana != Vacana.DVIVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.lastOrNull() in setOf('ऋ', 'ृ') && context.terms.last().upadesha in setOf("औ", "औट्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + "रौ"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.131: Formed the masculine ṛ-stem strong dual अरौ ending before ${affix.upadesha}.",
        )
    }
}
