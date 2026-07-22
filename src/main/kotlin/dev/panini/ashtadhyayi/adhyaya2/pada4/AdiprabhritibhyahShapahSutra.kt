package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.LopaType
import dev.panini.derivation.TermKind
import dev.panini.core.DhatuGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        return dhatu.gana == DhatuGana.ADADI && context.terms.any { it.upadesha == "शप्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shap = context.terms.first { it.upadesha == "शप्" }
        return DerivationChange(
            context.replaceTerm(shap.id, shap.copy(deletionType = LopaType.LUK)).removeTerm(shap.id),
            "2.4.72 applies LUK to शप् after an Adādi root.",
        )
    }
}
