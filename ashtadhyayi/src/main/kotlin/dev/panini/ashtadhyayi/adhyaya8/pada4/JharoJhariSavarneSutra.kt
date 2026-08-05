package dev.panini.ashtadhyayi.adhyaya8.pada4

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
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || next.isEmpty()) return@any false

            val jharChar1 = curr.trimEnd('्').lastOrNull() ?: return@any false
            val jharChar2 = next.first()

            val stemBeforeJhar1 = curr.trimEnd('्').dropLast(1)
            val precedingChar = stemBeforeJhar1.lastOrNull() ?: return@any false
            val isPrecededByHal = precedingChar == '्' || precedingChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks

            // 8.4.65 does not delete 'c' before 'ch' produced by 8.4.63 śaś cho'ṭi
            if (jharChar1 == 'च' && jharChar2 == 'छ') return@any false

            val isJhar1 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jharChar1)
            val isJhar2 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jharChar2)
            val isSavarna = Varnamala.areSavarna(jharChar1, jharChar2)

            isPrecededByHal && isJhar1 && isJhar2 && isSavarna
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || next.isEmpty()) return@first false

            val jharChar1 = curr.trimEnd('्').lastOrNull() ?: return@first false
            val jharChar2 = next.first()

            val stemBeforeJhar1 = curr.trimEnd('्').dropLast(1)
            val precedingChar = stemBeforeJhar1.lastOrNull() ?: return@first false
            val isPrecededByHal = precedingChar == '्' || precedingChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks

            if (jharChar1 == 'च' && jharChar2 == 'छ') return@first false

            val isJhar1 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jharChar1)
            val isJhar2 = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAR, jharChar2)
            val isSavarna = Varnamala.areSavarna(jharChar1, jharChar2)

            isPrecededByHal && isJhar1 && isJhar2 && isSavarna
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("त्") || surface.endsWith("द्") || surface.endsWith("क्") || surface.endsWith("ग्") || surface.endsWith("च्") -> surface.dropLast(2)
            else -> surface.dropLast(1)
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.65: Elided redundant jhar consonant before savarṇa jhar."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, surface.last(), "", sutra))) }
    }
}
