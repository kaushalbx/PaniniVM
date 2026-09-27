package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.KrtAffix
import dev.panini.core.SanadiAffix
import dev.panini.core.Purusha
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.matchesAffix
import dev.panini.derivation.matchesAnyAffix
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.86: पुगन्तलघूपधस्य च. */
object PugantalaghupadhasyaCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.86",
    text = "पुगन्तलघूपधस्य च",
    hindiExplanation = "लघु इक् उपधा को सार्वधातुक या आर्धधातुक प्रत्यय से पहले गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730086,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        if (context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.terms.none { it.id == "yasut" || it.id == "siyut" }) return false
        val hasNic = context.terms.any { it.matchesAffix(SanadiAffix.NIC) }
        val ending = TingAffix.entries.firstOrNull { it.upadesha == context.terms.lastOrNull()?.upadesha }
        val isAdadiStrong = ending != null && dhatu.gana == DhatuGana.ADADI && when (context.effectiveContext.rupa.lakara) {
            Lakara.LAT -> ending in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
            Lakara.LOT -> ending.purusha == Purusha.UTTAMA || ending == TingAffix.TIP
            Lakara.LANG -> ending in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
            else -> false
        }
        val beforeArdhadhatuka = HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) &&
            context.terms.dropWhile { it.id != dhatu.id }.drop(1).any {
                it.kind == TermKind.PRATYAYA &&
                    !it.matchesAnyAffix(KrtAffix.KTA, KrtAffix.KTAVATU, KrtAffix.KTVA, KrtAffix.LYAP)
            }
        return ((((hasNic && !dhatu.blocksNicGuna) && ending != null) || isAdadiStrong || beforeArdhadhatuka)) &&
            lightUpadhaIndex(dhatu.varnas) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val index = requireNotNull(lightUpadhaIndex(dhatu.varnas))
        val source = dhatu.varnas[index] as Svara
        val replacement = requireNotNull(Varnamala.getGuna(source))
        val substituted = dhatu.varnas.toMutableList().apply {
            removeAt(index)
            addAll(index, replacement)
        }
        return DerivationChange(
            state = context.substituteTermSurface(
                dhatu.id, substituted.toDevanagari(), source, replacement, sutra,
            ).copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.86 applies guṇa to the light upadhā before ṇic or a strong ending.",
        )
    }

    private fun lightUpadhaIndex(varnas: List<Varna>): Int? =
        (varnas.lastIndex - 1).takeIf { index ->
            index >= 0 && varnas.last() is Vyanjana &&
                varnas[index] in setOf(Svara.I, Svara.U, Svara.R, Svara.L)
        }
}
