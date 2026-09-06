package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 7.3.52: चजोः कु घिण्ण्यतोः. */
object CajoKuGhinnyatohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.52",
    text = "चजोः कु घिण्ण्यतोः",
    hindiExplanation = "घित् अथवा ण्यत् प्रत्यय परे होने पर अङ्ग के अन्त्य च्-ज् को कवर्गादेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730052,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val affix = context.terms.firstOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return (dhatu.surface.endsWith("च्") || dhatu.surface.endsWith("ज्")) &&
            (affix.matchesUpadesha("घञ्") || affix.matchesUpadesha("ण्यत्"))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val source = dhatu.surface[dhatu.surface.length - 2]
        val replacement = if (source == 'च') "क" else "ग"
        val newSurface = dhatu.surface.dropLast(2) + replacement + "्"
        return DerivationChange(
            context.substituteTermSurface(dhatu.id, newSurface, source, replacement, sutra),
            "7.3.52 substitutes the corresponding guttural for final $source before घञ्/ण्यत्.",
        )
    }
}
