package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.37: समासेऽनञ्पूर्वे क्त्वो ल्यप्.
 * In a compound with an upasarga (other than nañ), 'क्त्वा' (ktvā) is replaced by 'ल्यप्' (lyap).
 */
object SamaseAnanpurveKtvoLyapSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.37",
    text = "समासेऽनञ्पूर्वे क्त्वो ल्यप्",
    hindiExplanation = "अवैदिक समास में अनञ् उपसर्ग से उत्तर क्त्वा के स्थान पर ल्यप् आदेश होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710037,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("3.4.21"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val hasUpasarga = context.samjnas.any { it.samjna == Samjna.UPASARGA } || context.terms.any { it.kind == TermKind.PRATIPADIKA && it.id.startsWith("upasarga") }
        val ktvaTerm = context.terms.lastOrNull { it.upadesha == "क्त्वा" || it.id == "ktva_pratyaya" || it.surface == "त्वा" }
        return hasUpasarga && ktvaTerm != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ktvaTerm = context.terms.last { it.upadesha == "क्त्वा" || it.id == "ktva_pratyaya" }
        return DerivationChange(
            state = context.replaceWholeAffix(
                id = ktvaTerm.id,
                replacementId = "lyap_pratyaya",
                surface = "ल्यप्",
                upadesha = "ल्यप्",
                sutra = sutra,
                policy = dev.panini.derivation.WholeAffixDesignationPolicy.FreshUpadesha,
            ),
            explanation = "7.1.37 substitutes ल्यप् (य) for क्त्वा when preceded by an upasarga."
        )
    }
}
