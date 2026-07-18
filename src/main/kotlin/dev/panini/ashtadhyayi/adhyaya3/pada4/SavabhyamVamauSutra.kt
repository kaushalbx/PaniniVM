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
import kotlin.collections.get

/** 3.4.91: savābhyāṃ vāmau. */
object SavabhyamVamauSutra : Sutra<DerivationState, DerivationChange>(
    "3.4.91", "सवाभ्यां वामौ", "लोट् में वस् और मस् के स्थान पर आव् और आम् आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340091,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val replacements = mapOf("वस्" to "आव", "मस्" to "आम")

    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        if (context.effectiveContext.rupa.lakara != Lakara.LOT) return false
        val replacement = replacements[affix.upadesha]
        val active = replacement != null &&
            ((context.stage == DerivationStage.PADA_FORMED && affix.surface != replacement) ||
                (context.stage == DerivationStage.IT_PROCESSED && affix.surface == replacement))
        val middle = (affix.upadesha == "थास्" && affix.surface == "से") ||
            (affix.upadesha == "ध्वम्" && affix.surface == "ध्वे")
        return active || middle
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = when (affix.upadesha) {
            "थास्" -> "स्व"
            "ध्वम्" -> "ध्वम्"
            else -> replacements.getValue(requireNotNull(affix.upadesha))
        }
        return DerivationChange(context.replaceTerm(affix.id, affix.copy(surface = replacement)).copy(stage = DerivationStage.PADA_FORMED), "3.4.91 supplies the loṭ first-person termination.")
    }
}
