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
        val presentStemEstablished = context.terms.any { it.id in setOf("shap", "shyan", "shnu", "sha", "tanadi-u", "shna", "nic") } ||
            context.droppedTerms.any { it.id == "shap" || it.upadesha in setOf("श्नम्", "श्लु") }
        if (context.effectiveContext.rupa.lakara != Lakara.LOT || !presentStemEstablished) return false
        val activeJhi = affix.upadesha == "झि" &&
            ((context.stage == DerivationStage.PADA_FORMED && affix.surface != "न्तु") ||
                (context.stage == DerivationStage.IT_PROCESSED && affix.surface == "न्तु"))
        val middleE = affix.surface in setOf("ते", "एते", "आते", "न्ते", "अन्ते", "अते", "एथे", "आथे") &&
            context.substitutions.none { it.sutra == "3.4.90" }
        return activeJhi || middleE
    }
    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = if (affix.upadesha == "झि") {
            if (context.terms.any { it.id == "shna" }) "अन्तु" else "न्तु"
        } else if (affix.upadesha == "झ" && context.terms.any { it.id == "shna" }) {
            "अताम्"
        } else {
            affix.surface.dropLast(1) + "ाम्"
        }
        return DerivationChange(
            context.replaceTerm(affix.id, affix.copy(surface = replacement)).copy(stage = DerivationStage.PADA_FORMED),
            "3.4.90 replaces the LOT ending's ए with आम्.",
        )
    }
}
