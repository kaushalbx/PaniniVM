package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.85: loṭo laṅvat. Applies laṅ-style endings to loṭ. */
object LotolangvatSutra : Sutra<DerivationState, DerivationChange>(
    "3.4.85", "लोटो लङ्वत्", "लोट् में तस्, थस् और थ के लङ्-समान आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340085,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val replacements = mapOf("तस्" to "ताम्", "थस्" to "तम्", "थ" to "त")

    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        val replacement = replacements[affix.upadesha] ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT && ((context.stage == DerivationStage.PADA_FORMED && affix.surface != replacement) || (context.stage == DerivationStage.IT_PROCESSED && affix.surface == replacement))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = replacements.getValue(requireNotNull(affix.upadesha))
        return DerivationChange(
            context.replaceTerm(affix.id, affix.copy(surface = replacement))
                .copy(stage = DerivationStage.PADA_FORMED),
            "3.4.85 applies the loṭ laṅ-style termination replacement."
        )
    }
}
