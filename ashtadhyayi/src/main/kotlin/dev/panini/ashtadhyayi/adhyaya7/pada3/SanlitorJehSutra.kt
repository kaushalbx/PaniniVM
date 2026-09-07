package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 7.3.57: सन्लिटोर्जेः — the non-abhyāsa ज् of जि receives its guttural substitute before सन् or लिट्. */
object SanlitorJehSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.57",
    text = "सन्लिटोर्जेः",
    hindiExplanation = "सन् अथवा लिट् परे जि धातु के अभ्यासोत्तर जकार को गकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730057,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.none { it.id == "abhyasa" }) return false
        val anga = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        val san = context.terms.any { it.kind == TermKind.PRATYAYA && it.upadesha == "सन्" }
        return san && anga.upadesha == "जि" && anga.surface.startsWith("ज")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val anga = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val surface = "ग" + anga.surface.drop(1)
        return DerivationChange(
            context.substituteTermSurface(anga.id, surface, 'ज', "ग", sutra),
            "7.3.57 substitutes guttural ग for the non-abhyāsa ज of जि before सन्.",
        )
    }
}
