package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 6.3.111: ḍhralope pūrvasya dīrgho'ṇaḥ.
 * When 'ḍh' or 'r' has been elided, the preceding short 'aṇ' vowel (a, i, u) becomes long (dīrgha).
 */
object DhralopePurvasyaDirghonahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.111",
    text = "ढ्रलोपे पूर्वस्य दीर्घोऽणः",
    hindiExplanation = "ढ-कार तथा र-कार का लोप होने पर पूर्व 'अण्' (अ, इ, उ) का दीर्घ आदेश होता है (उदा. पुना रमते, हरी रमते)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630111,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        // Triggers after 8.3.14 ḍho ḍhe lopaḥ or 8.3.14 ro ri
        val hasLopa = context.substitutions.any { it.sutra == "8.3.14" || it.sutra == "8.3.15" }
        if (!hasLopa) return false

        return context.terms.any { term ->
            val surface = term.surface
            val lastChar = surface.lastOrNull() ?: return@any false
            val hrasva = Varnamala.getHrasva(lastChar).firstOrNull() ?: lastChar
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AK, hrasva)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = context.terms.indexOfFirst { term ->
            val surface = term.surface
            val lastChar = surface.lastOrNull() ?: return@indexOfFirst false
            val hrasva = Varnamala.getHrasva(lastChar).firstOrNull() ?: lastChar
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AK, hrasva)
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface
        val lastChar = surface.last()

        val replacement = when (lastChar) {
            'अ' -> "आ"
            'इ' -> "ई"
            'उ' -> "ऊ"
            'ि' -> "ी"
            'ु' -> "ू"
            else -> "ा"
        }

        val newSurface = surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "6.3.111: Lengthened preceding aṇ vowel after ḍh/r lopa."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, lastChar, replacement, sutra))) }
    }
}
