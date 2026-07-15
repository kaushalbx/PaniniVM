package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.90: āmet aḥ. */
object AmetahSutra : Sutra<DerivationState, DerivationChange>(
    "3.4.90", "आमेतः", "लोट् में झि के स्थान पर अन्तु आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340090,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        val shapEstablished = context.terms.any { it.id == "shap" } || context.droppedTerms.any { it.id == "shap" }
        return context.effectiveContext.rupa.lakara == Lakara.LOT && affix.upadesha == "झि" && shapEstablished &&
            ((context.stage == DerivationStage.PADA_FORMED && affix.surface != "न्तु") ||
                (context.stage == DerivationStage.IT_PROCESSED && affix.surface == "न्तु"))
    }
    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(context.replaceTerm(affix.id, affix.copy(surface = "न्तु")).copy(stage = DerivationStage.PADA_FORMED), "3.4.90 replaces झि in loṭ.")
    }
}
