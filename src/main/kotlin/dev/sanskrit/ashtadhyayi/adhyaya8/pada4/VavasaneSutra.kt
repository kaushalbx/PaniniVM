package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 8.4.56: vāvasāne.
 * At the end of a word (avasāna), a jhal sound is optionally replaced by a car sound (devoiced).
 * This makes the devoicing optional at the end of a sentence or pause.
 */
object VavasaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.56",
    text = "वावसाने",
    hindiExplanation = "अवसान (विराम) में झल् वर्णों के स्थान पर विकल्प से चर् आदेश होता है।",
    type = SutraType.VIBHASHA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840056,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Condition: End of word (Avasāna)
        if (context.stage != DerivationStage.PADA_FORMED && context.stage != DerivationStage.FINAL) return false
        
        val lastTerm = context.terms.lastOrNull() ?: return false
        val lastChar = lastTerm.surface.lastOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val lastChar = lastTerm.surface.last()
        
        val potentialSubstitutes = setOf("च", "ट", "त", "क", "प")
        val substitute = SthaneAntaratamahSutra.selectBest(lastChar, potentialSubstitutes)
        
        val newSurface = lastTerm.surface.dropLast(1) + substitute
        
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.56: Optionally devoiced $lastChar to $substitute at avasāna."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(lastTerm.id, lastChar, substitute, sutra))) }
    }

    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        apply(state),
        DerivationChange(state, "8.4.56: Declined optional devoicing at avasāna.", applied = false)
    )
}
