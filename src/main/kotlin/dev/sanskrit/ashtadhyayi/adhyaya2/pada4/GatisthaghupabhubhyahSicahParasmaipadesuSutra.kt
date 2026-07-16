package dev.sanskrit.ashtadhyayi.adhyaya2.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 2.4.77: गातिस्थाघुपाभूभ्यः सिचः परस्मैपदेषु. */
object GatisthaghupabhubhyahSicahParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.77",
    text = "गातिस्थाघुपाभूभ्यः सिचः परस्मैपदेषु",
    hindiExplanation = "भू आदि धातुओं से परे सिच् का परस्मैपद में लुक् होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240077,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" }
        if (sicIndex <= 0 || sicIndex == context.terms.lastIndex) return false
        val dhatu = context.terms.subList(0, sicIndex).lastOrNull { it.kind == TermKind.DHATU } ?: return false
        val ending = context.terms.last()
        return dhatu.matchesUpadesha("भू") && ending.id.startsWith("ting-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sic = context.terms.first { it.upadesha == "सिच्" }
        return DerivationChange(
            context.removeTerm(sic.id),
            "2.4.77 applies luk to सिच् after भू before a Parasmaipada ending.",
        )
    }
}
