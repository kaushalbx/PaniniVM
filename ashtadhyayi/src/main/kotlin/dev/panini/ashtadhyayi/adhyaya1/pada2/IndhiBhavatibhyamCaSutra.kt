package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.SthaniProperties
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 1.2.6 इन्धिभवतिभ्यां च — लिट् is कित् after इन्ध् and भू. */
object IndhiBhavatibhyamCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.6",
    text = "इन्धिभवतिभ्यां च",
    hindiExplanation = "इन्ध् और भू धातुओं से परे लिट् के आदेश कित् होते हैं।",
    type = SutraType.NITYA,
    chapter = 1,
    pada = 2,
    optional = false,
    kramaValue = 120006,
    role = SutraRole.Atidesha,
    action = SutraAction.ATIDESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LIT) return false
        val eligibleRoot = context.terms.any {
            it.kind == TermKind.DHATU && (it.matchesUpadesha("भू") || it.matchesUpadesha("इन्ध्"))
        }
        val ending = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return eligibleRoot && !ending.hasEffectiveMarker(ItMarker.KIT)
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.replaceTerm(
            context.terms.last { it.kind == TermKind.PRATYAYA }.id,
            context.terms.last { it.kind == TermKind.PRATYAYA }.let {
                it.copy(sthaniProps = SthaniProperties(
                    upadesha = it.sthaniProps?.upadesha ?: it.upadesha,
                    itMarkers = it.sthaniProps?.itMarkers.orEmpty() + ItMarker.KIT,
                ))
            },
        ),
        "1.2.6 assigns कित्-status to the लिट् ending after भू or इन्ध्.",
    )
}
