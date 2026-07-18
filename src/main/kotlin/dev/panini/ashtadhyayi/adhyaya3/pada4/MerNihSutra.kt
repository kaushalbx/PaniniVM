package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.89: mer niḥ. */
object MerNihSutra : Sutra<DerivationState, DerivationChange>(
    "3.4.89", "मेर्निः", "लोट् में मिप् के स्थान पर आनि आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340089,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT && affix.upadesha == "मिप्" &&
            ((context.stage == DerivationStage.PADA_FORMED && affix.surface != "आनि") ||
                (context.stage == DerivationStage.IT_PROCESSED && affix.surface == "आनि"))
    }
    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(context.replaceTerm(affix.id, affix.copy(surface = "आनि")).copy(stage = DerivationStage.PADA_FORMED), "3.4.89 replaces मिप् in loṭ.")
    }
}
