package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.ashtadhyayi.adhyaya3.pada1.hasSanadyantaDhatu
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.87: ser hyapic ca. */
object SerHyapicCaSutra : Sutra<DerivationState, DerivationChange>(
    "3.4.87", "सेर्ह्यपिच्च", "लोट् में सिप् के स्थान पर हि आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340087,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT && affix.upadesha == "सिप्" &&
            context.substitutions.none { it.sutra == "3.4.87" } &&
            ((context.stage == DerivationStage.PADA_FORMED && affix.surface.isNotEmpty()) ||
                (context.stage == DerivationStage.IT_PROCESSED && affix.surface.isEmpty()))
    }
    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = if (context.hasSanadyantaDhatu()) "" else when (context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" }?.gana) {
            DhatuGana.ADADI, DhatuGana.JUHOTYADI, DhatuGana.RUDHADI -> "धि"
            DhatuGana.KRYADI -> "हि"
            else -> ""
        }
        return DerivationChange(context.replaceTerm(affix.id, affix.copy(surface = replacement)).copy(stage = DerivationStage.PADA_FORMED), "3.4.87 replaces सिप् in loṭ.")
    }
}
