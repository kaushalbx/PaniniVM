package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.SanadiAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.derivation.matchesAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.48: णिश्रिद्रुस्रुभ्यः कर्तरि चङ्.
 *
 * Here णि denotes a dhātu ending in the causative affix णिच्; it is not the
 * preverb नि. The rule substitutes चङ् for the च्लि introduced by 3.1.43 in
 * kartari luṅ after such a derived dhātu or after श्रि, द्रु, or स्रु.
 */
object NiShriDruSruBhyahKartariChangSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.48", text = "णिश्रिद्रुस्रुभ्यः कर्तरि चङ्",
    hindiExplanation = "कर्तरि लुङ् में णिच्-अन्त धातु अथवा श्रि, द्रु, स्रु धातु से पर च्लि के स्थान पर चङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310048,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val directlyNamedRoots = setOf("श्रिञ्", "श्रि", "द्रु", "स्रु")

    private fun hasEligibleDhatu(context: DerivationState): Boolean {
        val hasNic = context.allEffectiveTerms.any { it.matchesAffix(SanadiAffix.NIC) }
        return context.allEffectiveTerms.any { term ->
            term.kind == TermKind.DHATU && (term.upadesha in directlyNamedRoots || hasNic)
        }
    }

    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.effectiveContext.rupa.prayoga == Prayoga.KARTARI &&
            context.terms.any { it.upadesha == "च्लि" } && hasEligibleDhatu(context)

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        return DerivationChange(
            context.replaceWholeAffix(cli.id, "चङ्", sutra,
                WholeAffixDesignationPolicy.FreshUpadesha, upadesha = "चङ्")
                .copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.48 substitutes चङ् for च्लि after a ṇic-derived dhātu or श्रि, द्रु, स्रु in kartari luṅ.",
        )
    }
}
