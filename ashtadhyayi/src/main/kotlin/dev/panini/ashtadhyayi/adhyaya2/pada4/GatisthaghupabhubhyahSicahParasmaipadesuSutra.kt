package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    blocks = setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.97", "6.1.101"),
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
            context.removeTerm(sic.id, sutra),
            "2.4.77 applies luk to सिच् after भू before a Parasmaipada ending.",
        )
    }
}
