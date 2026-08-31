package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.44: च्लेः सिच्. Substitutes सिच् for the abstract aorist marker च्लि. */
object ClehSicSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.44",
    text = "च्लेः सिच्",
    hindiExplanation = "च्लि के स्थान पर सिच् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310044,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.none { it.upadesha == "च्लि" }) return false
        if (ShalIgupadhadAnitahKsahSutra.matches(context)) return false
        if (NishriDruSruBhyahKarthariChaSutra.matches(context)) return false
        if (PusAdiDyutAdyLdtahParasmaipadesuSutra.matches(context)) return false
        if (ChinKarmaniChaSutra.matches(context)) return false
        return true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        return DerivationChange(
            context.replaceWholeAffix(
                cli.id,
                "सिँच्",
                sutra,
                WholeAffixDesignationPolicy.FreshUpadesha,
                upadesha = "सिच्",
            ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.44 substitutes सिच् for च्लि.",
        )
    }
}
