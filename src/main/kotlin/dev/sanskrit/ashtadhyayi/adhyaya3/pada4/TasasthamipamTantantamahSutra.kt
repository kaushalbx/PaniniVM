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
), DerivationSutra {
    private val ELIGIBLE_ENDINGS = setOf("तस्", "थस्", "थ", "मिप्")

    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        
        val isNit = lastTerm.matchesUpadesha("लङ्") || lastTerm.matchesUpadesha("लृङ्") ||
            lastTerm.matchesUpadesha("लुङ्") || context.effectiveContext.rupa.lakara == Lakara.LING
        val targetUpadesha = lastTerm.upadesha ?: ""
        val eligible = targetUpadesha in ELIGIBLE_ENDINGS
        
        val isAlreadyApplied = when (targetUpadesha) {
            "तस्" -> lastTerm.surface == "ताम्"
            "थस्" -> lastTerm.surface == "तम्"
            "थ" -> lastTerm.surface == "त"
            "मिप्" -> lastTerm.surface == "अम्"
            else -> false
        }
        
        return isNit && eligible && !isAlreadyApplied
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
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = substitute))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.101: Replaced ending ${lastTerm.upadesha} with $substitute."
        )
    }
}
