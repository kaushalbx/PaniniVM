package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.ashtadhyayi.adhyaya2.pada4.isGatisthaGhuPaBhu
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 3.4.110 आतः — after an आ-final root whose सिच् takes luk, झि is replaced by जुस् (सिज्लुकि, Kāśikā):
 * अदुः, अस्थुः, अगुः, अपुः. The luk is 2.4.77, nitya for exactly these roots before a parasmaipada
 * ending of लुङ्, so the condition is read from the root before सिच् is introduced; 7.1.3 would
 * otherwise consume झ first.
 */
object AtahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.110", text = "आतः",
    hindiExplanation = "सिच् के लुक् होने पर आकारान्त धातु से परे झि के स्थान पर जुस् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 3, pada = 4, optional = false, kramaValue = 340110,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LUNG) return false
        val ending = context.terms.lastOrNull() ?: return false
        if (ending.upadesha != TingAffix.JHI.upadesha || ending.varnas.firstOrNull() != Vyanjana.JHA) return false
        if (ending.sourceTingAffix?.pada != PadaType.PARASMAIPADA) return false
        val dhatu = context.terms.lastOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.varnas.lastOrNull() == Svara.AA && isGatisthaGhuPaBhu(context, dhatu)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceWholeAffix(ending.id, "उस्", sutra, WholeAffixDesignationPolicy.Consume),
            "3.4.110 replaces झि with जुस् after an आ-final root whose सिच् takes luk.",
        )
    }
}
