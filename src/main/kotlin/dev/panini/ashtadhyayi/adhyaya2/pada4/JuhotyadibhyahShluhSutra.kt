package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.LopaType
import dev.panini.derivation.TermKind
import dev.panini.core.DhatuGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.4.75: जुहोत्यादिभ्यः श्लुः. The शप् vikaraṇa receives ślu after Juhotyādi roots. */
object JuhotyadibhyahShluhSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.75",
    text = "जुहोत्यादिभ्यः श्लुः",
    hindiExplanation = "जुहोत्यादि-गण के धातुओं से परे शप् प्रत्यय का श्लु होता है।",
    type = SutraType.APAVADA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240075,
    role = SutraRole.Apavada,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("3.1.68"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == DhatuGana.JUHOTYADI && context.terms.any { it.upadesha == "शप्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shap = context.terms.first { it.upadesha == "शप्" }
        return DerivationChange(
            context.replaceTerm(shap.id, shap.copy(deletionType = LopaType.SHLU)).removeTerm(shap.id),
            "2.4.75 applies ślu to शप् after a Juhotyādi root.",
        )
    }
}
