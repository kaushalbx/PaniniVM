package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.65: jharo jhari savarṇe.
 * A jhar consonant (stops, sibilants) after a consonant (hal) is optionally elided
 * when followed by a homogeneous jhar consonant (savarṇa jhar).
 */
object JharoJhariSavarneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.65",
    text = "झरो झरि सवर्णे",
    hindiExplanation = "हल् से उत्तर झर् वर्ण का अपने सवर्ण झर् परे रहते विकल्प से लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840065,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas
            val jhar1 = curr.lastOrNull() ?: return@any false
            val jhar2 = next.firstOrNull() ?: return@any false
            val isPrecededByHal = curr.getOrNull(curr.lastIndex - 1) is Vyanjana

            val isJhar1 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jhar1)
            val isJhar2 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jhar2)
            val isSavarna = Varnamala.areSavarna(jhar1, jhar2)

            isPrecededByHal && isJhar1 && isJhar2 && isSavarna
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas
            val jhar1 = curr.lastOrNull() ?: return@first false
            val jhar2 = next.firstOrNull() ?: return@first false
            val isPrecededByHal = curr.getOrNull(curr.lastIndex - 1) is Vyanjana

            val isJhar1 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jhar1)
            val isJhar2 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jhar2)
            val isSavarna = Varnamala.areSavarna(jhar1, jhar2)

            isPrecededByHal && isJhar1 && isJhar2 && isSavarna
        }

        val targetTerm = context.terms[targetIndex]

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, targetTerm.varnas.lastIndex, emptyList(), sutra),
            explanation = "8.4.65: Elided redundant jhar consonant before savarṇa jhar."
        )
    }
}
