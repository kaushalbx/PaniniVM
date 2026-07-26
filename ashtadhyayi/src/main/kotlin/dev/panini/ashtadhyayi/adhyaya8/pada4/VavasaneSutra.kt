package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val finalConsonant = getFinalConsonant(lastTerm.surface) ?: return false
        if (finalConsonant in setOf('च', 'ट', 'त', 'क', 'प', 'श', 'ष', 'स', 'ह')) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, finalConsonant)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val finalConsonant = getFinalConsonant(lastTerm.surface)!!

        val potentialSubstitutes = setOf("च", "ट", "त", "क", "प")
        val substitute = SthaneAntaratamahSutra.selectBest(finalConsonant, potentialSubstitutes)

        val newSurface = if (lastTerm.surface.endsWith('र')) {
            lastTerm.surface.dropLast(1) + substitute
        } else {
            lastTerm.surface.dropLast(2) + substitute + '्'
        }

        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.56: Optionally devoiced $finalConsonant to $substitute at avasāna."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(lastTerm.id, finalConsonant, substitute, sutra))) }
    }

    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        apply(state),
        DerivationChange(state, "8.4.56: Declined optional devoicing at avasāna.", applied = false)
    )

    private fun getFinalConsonant(surface: String): Char? {
        if (surface.isEmpty()) return null
        if (surface.endsWith('र')) return 'र'
        if (surface.endsWith('्') && surface.length >= 2) {
            return surface[surface.length - 2]
        }
        return null
    }
}
