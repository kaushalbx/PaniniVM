package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.55: पुषादिद्युताद्यॢदितः परस्मैपदेषु. */
object PusAdiDyutAdyLrditahParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.55", text = "पुषादिद्युताद्यॢदितः परस्मैपदेषु",
    hindiExplanation = "परस्मैपद लुङ् में दिवादि-पुषादि, भ्वादि-द्युतादि अथवा लृदित् धातु से पर च्लि के स्थान पर अङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310055,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private fun eligibleDhatu(context: DerivationState) = context.allEffectiveTerms.firstOrNull { term ->
        if (term.kind != TermKind.DHATU) return@firstOrNull false
        val pusadi = term.gana == DhatuGana.DIVADI && term.sourceDhatuKrama in 79..97
        val dyutadi = term.gana == DhatuGana.BHVADI && term.sourceDhatuKrama in 842..866
        val lrdit = Svara.L in term.upadeshaVarnas
        pusadi || dyutadi || lrdit
    }

    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.effectiveContext.rupa.pada == PadaType.PARASMAIPADA &&
            context.terms.any { it.upadesha == "च्लि" } &&
            eligibleDhatu(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        return DerivationChange(
            context.replaceWholeAffix(cli.id, "अङ्", sutra,
                WholeAffixDesignationPolicy.FreshUpadesha, upadesha = "अङ्")
                .copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.55 substitutes अङ् for च्लि after an eligible source-attested dhātu in parasmaipada luṅ.",
        )
    }
}
