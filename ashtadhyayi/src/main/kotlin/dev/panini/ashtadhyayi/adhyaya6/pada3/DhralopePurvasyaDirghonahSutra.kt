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
        return target(context) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val (targetTerm, index) = requireNotNull(target(context))
        val source = targetTerm.varnas[index] as Svara
        val replacement = source.toDirgha()

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, index, listOf(replacement), sutra),
            explanation = "6.3.111: Lengthened preceding aṇ vowel after ḍh/r lopa."
        )
    }

    private val anVowels = setOf(Svara.A, Svara.I, Svara.U)

    private fun target(context: DerivationState) = context.substitutions.asReversed()
        .filter { it.sutra in setOf("8.3.13", "8.3.14") && it.replacement.isEmpty() }
        .firstNotNullOfOrNull { deletion ->
            val index = deletion.sourceVarnaIndex?.minus(1) ?: return@firstNotNullOfOrNull null
            context.terms.firstOrNull { it.id == deletion.targetId && it.varnas.getOrNull(index) in anVowels }
                ?.let { it to index }
        }
}
