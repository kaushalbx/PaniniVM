package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.4.101: tasasthāmipāṃ tāntantāmāḥ.
 * In a Nit lakāra, the endings tas, thas, tha, and mip are replaced by tām, tam, ta, and am.
 */
object TasasthamipamTantantamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.101",
    text = "तस्थस्थमिपां तान्तन्तामः",
    hindiExplanation = "ङित् लकार के परस्मैपद प्रत्ययों तस्, थस्, th और मिप् के स्थान पर क्रमशः ताम्, तम्, त और अम् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340101,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("7.2.80"),
), DerivationSutra {
    private val ELIGIBLE_ENDINGS = setOf("तस्", "थस्", "थ", "मिप्")

    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false

        val isNit = context.effectiveContext.rupa.lakara in setOf(
            Lakara.LANG, Lakara.LRNG, Lakara.LUNG, Lakara.LING,
        )
        val targetUpadesha = lastTerm.upadesha ?: ""
        val eligible = targetUpadesha in ELIGIBLE_ENDINGS
        val substitutionRecorded = context.substitutions.any { it.sutra == sutra && it.targetId == lastTerm.id }

        val isAlreadyApplied = when (targetUpadesha) {
            "तस्" -> lastTerm.surface == "ताम्"
            "थस्" -> lastTerm.surface == "तम्"
            "थ" -> lastTerm.surface == "त"
            "मिप्" -> lastTerm.surface == "अम्"
            else -> false
        }

        return isNit && eligible && !isAlreadyApplied && !substitutionRecorded
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val substitute = when (lastTerm.upadesha) {
            "तस्" -> "ताम्"
            "थस्" -> "तम्"
            "थ" -> "त"
            "मिप्" -> "अम्"
            else -> lastTerm.surface
        }
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = substitute, itMarkers = emptySet()))
                .addSubstitution(dev.panini.derivation.VarnaSubstitution(lastTerm.id, lastTerm.surface.first(), substitute, sutra))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.101: Replaced ending ${lastTerm.upadesha} with $substitute."
        )
    }
}
