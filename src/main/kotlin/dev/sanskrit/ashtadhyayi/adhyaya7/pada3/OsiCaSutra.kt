package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.3.104: osi ca. 
 * Substitutes 'e' for the final 'a' of an aṅga before the dual affix 'os'.
 */
object OsiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.104",
    text = "ओसि च",
    hindiExplanation = "ओस् परे होने पर अकारान्त अङ्ग के अन्त्य अकार का एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730104,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false
        
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        return (stem.surface.endsWith('अ') || stem.surface.endsWith('ा')) && 
                affix.upadesha == "ओस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val oldChar = stem.surface.last()
        val newSurface = stem.surface.dropLast(1) + "े"
        
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.104: Substituted 'e' for final 'a' before 'os'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(stem.id, oldChar, "े", sutra))) }
    }
}
