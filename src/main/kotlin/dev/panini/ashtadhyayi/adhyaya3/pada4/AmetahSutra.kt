package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.derivation.TermKind
import dev.panini.core.DhatuGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val activeJhi = affix.upadesha == "झि" && affix.surface !in setOf("न्तु", "अन्तु", "अतु")
        val middleE = affix.surface in setOf("ते", "एते", "आते", "न्ते", "अन्ते", "अते", "एथे", "आथे") &&
            context.substitutions.none { it.sutra == "3.4.90" }
        return activeJhi || middleE
    }
    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = if (affix.upadesha == "झि") {
            when (context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" }?.gana) {
                DhatuGana.JUHOTYADI -> "अतु"
                DhatuGana.ADADI, DhatuGana.SVADI, DhatuGana.RUDHADI, DhatuGana.TANADI, DhatuGana.KRYADI -> "अन्तु"
                else -> "न्तु"
            }
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
