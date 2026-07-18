package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.102: prathamayoḥ pūrvasavarṇaḥ.
 * In the first two vibhaktis (Prathama and Dvitiya), when an Ak vowel is followed
 * by a vowel, a single substitute homogeneous with the former (pūrvasavarṇa dīrgha) occurs.
 */
object PrathamayohPurvaSavarnahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.102",
    text = "प्रथमयोः पूर्वसवर्णः",
    hindiExplanation = "प्रथमा और द्वितीया विभक्ति के अच् परे होने पर पूर्व-सवर्ण दीर्घ एकादेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610102,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.IT_PROCESSED && context.stage != DerivationStage.PADA_FORMED) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val suffix = context.terms.last()

        val suffixId = suffix.id
        if (suffixId !in setOf("sup-au", "sup-jas", "sup-aut", "sup-sas")) return false

        val leftChar = stem.surface.lastOrNull() ?: return false
        val leftPhoneme = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else leftChar

        // The implemented scope of 6.1.102 is a/ā + vowel.  Ik-final
        // aṅgas take their own यण् path under 6.1.77.
        if (leftPhoneme !in setOf('अ', 'आ', 'ा')) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        if (!engine.contains(dev.panini.pratyahara.Pratyahara.AK, leftPhoneme)) return false

        val rightChar = suffix.surface.firstOrNull() ?: return false
        if (!engine.contains(dev.panini.pratyahara.Pratyahara.AC, rightChar)) return false

        // Ami Purvah (6.1.107) has precedence for sup-am.
        // Nadici (6.1.104) block:
        if ((leftPhoneme == 'अ' || leftPhoneme == 'आ' || leftPhoneme == 'ा') &&
            engine.contains(dev.panini.pratyahara.Pratyahara.IC, rightChar)) {
            return false
        }

        return true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val suffix = context.terms.last()

        val leftChar = stem.surface.last()
        val leftPhoneme = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) 'अ' else leftChar

        val substitute = getDirgha(leftPhoneme)

        val newStemSurface = if (leftChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            stem.surface + substitute
        } else {
            stem.surface.dropLast(1) + substitute
        }

        val remainingSuffix = suffix.surface.drop(1)
        val newSurface = newStemSurface + remainingSuffix

        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = newSurface),
                droppedTerms = context.droppedTerms + suffix.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED
            ).addSubstitution(VarnaSubstitution(stem.id, leftPhoneme, substitute, sutra)),
            explanation = "6.1.102: Combined $leftPhoneme + ${suffix.surface.first()} into long $substitute."
        )
    }

    private fun getDirgha(c: Char): String = when (normalize(c)) {
        'अ' -> "ा"
        'इ' -> "ी"
        'उ' -> "ू"
        'ऋ' -> "ॄ"
        'ऌ' -> "ॄ"
        else -> c.toString()
    }

    private fun normalize(c: Char): Char = when (c) {
        'अ', 'आ', 'ा' -> 'अ'
        'इ', 'ई', 'ि', 'ी' -> 'इ'
        'उ', 'ऊ', 'ु', 'ू' -> 'उ'
        'ऋ', 'ॠ', 'ृ', 'ॄ' -> 'ऋ'
        'ऌ', 'ॢ' -> 'ऌ'
        else -> c
    }
}
