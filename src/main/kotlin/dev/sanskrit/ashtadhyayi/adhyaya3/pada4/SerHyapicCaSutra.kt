package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        val replacement = when (context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" }?.gana) {
            Gana.ADADI, Gana.JUHOTYADI, Gana.RUDHADI -> "धि"
            Gana.KRYADI -> "हि"
            else -> ""
        }
        return DerivationChange(context.replaceTerm(affix.id, affix.copy(surface = replacement)).copy(stage = DerivationStage.PADA_FORMED), "3.4.87 replaces सिप् in loṭ.")
    }
}
