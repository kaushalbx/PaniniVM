package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Varnamala
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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        if (context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.terms.none { it.id == "yasut" || it.id == "siyut" }) return false
        val hasNic = context.terms.any { it.upadesha == "णिच्" }
        val ending = TingAffix.entries.firstOrNull { it.upadesha == context.terms.lastOrNull()?.upadesha }
        val isAdadiStrong = ending != null && dhatu.gana == DhatuGana.ADADI && when (context.effectiveContext.rupa.lakara) {
            Lakara.LAT -> ending in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
            Lakara.LOT -> ending.purusha == Purusha.UTTAMA || ending == TingAffix.TIP
            Lakara.LANG -> ending in setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
            else -> false
        }
        return (((hasNic && !dhatu.blocksNicGuna) && ending != null) || isAdadiStrong) &&
            lightUpadhaIndex(dhatu.surface) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val index = requireNotNull(lightUpadhaIndex(dhatu.surface))
        val source = dhatu.surface[index]
        val replacement = requireNotNull(Varnamala.getGuna(source))
        return DerivationChange(
            state = context.substituteTermSurface(
                dhatu.id, dhatu.surface.replaceRange(index, index + 1, replacement), source, replacement, sutra,
            ).copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.86 applies guṇa to the light upadhā before ṇic or a strong ending.",
        )
    }

    private fun lightUpadhaIndex(surface: String): Int? {
        val finalConsonantStart = surface.length - 2
        if (finalConsonantStart <= 0 || surface.lastOrNull() != '्') return null
        return (finalConsonantStart - 1 downTo 0).firstOrNull {
            surface[it] in setOf('ि', 'ु', 'ृ', 'ॢ')
        }
    }
}
