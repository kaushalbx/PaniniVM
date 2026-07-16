package dev.sanskrit.ashtadhyayi.adhyaya2.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.LopaType
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 2.4.72: अदिप्रभृतिभ्यः शपः. The शप् vikaraṇa receives LUK after Adādi roots. */
object AdiprabhritibhyahShapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.72",
    text = "अदिप्रभृतिभ्यः शपः",
    hindiExplanation = "अदादिगण के धातुओं से परे शप् प्रत्यय का लुक् होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240072,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.ADADI && context.terms.any { it.upadesha == "शप्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shap = context.terms.first { it.upadesha == "शप्" }
        return DerivationChange(
            context.replaceTerm(shap.id, shap.copy(deletionType = LopaType.LUK)).removeTerm(shap.id),
            "2.4.72 applies LUK to शप् after an Adādi root.",
        )
    }
}
