package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDirgha
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

        return context.terms.any { it.varnas.lastOrNull() in anVowels }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = context.terms.indexOfFirst { it.varnas.lastOrNull() in anVowels }

        val targetTerm = context.terms[targetIndex]
        val source = targetTerm.varnas.last() as Svara
        val replacement = source.toDirgha()
        val result = targetTerm.varnas.dropLast(1) + replacement

        return DerivationChange(
            state = context.substituteTermVarnas(targetTerm.id, result, source, listOf(replacement), sutra),
            explanation = "6.3.111: Lengthened preceding aṇ vowel after ḍh/r lopa."
        )
    }

    private val anVowels = setOf(Svara.A, Svara.I, Svara.U)
}
