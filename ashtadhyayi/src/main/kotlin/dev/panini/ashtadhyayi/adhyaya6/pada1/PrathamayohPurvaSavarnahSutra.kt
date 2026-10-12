package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.SupAffix
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.firstVarna
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toDirgha
import dev.panini.shiksha.toVarnas
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
    blocks = setOf("6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.IT_PROCESSED && context.stage != DerivationStage.PADA_FORMED) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val suffix = context.terms.last()

        if (listOf(SupAffix.AU, SupAffix.JAS, SupAffix.AUT, SupAffix.SAS).none(suffix::hasCurrentAffix)) return false

        val leftPhoneme = stem.varnas.lastOrNull() as? Svara ?: return false

        // Short i/u duals take purvasavarna, not yan. Other ik domains
        // require their own exceptions and are not broadened here.
        val shortIkDual = leftPhoneme in setOf(Svara.I, Svara.U) &&
            listOf(SupAffix.AU, SupAffix.AUT).any(suffix::hasCurrentAffix)
        if (leftPhoneme !in setOf(Svara.A, Svara.AA) && !shortIkDual) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        if (!engine.contains(Pratyahara.AK, leftPhoneme)) return false

        val rightChar = suffix.varnas.firstOrNull() ?: return false
        if (!engine.contains(Pratyahara.AC, rightChar)) return false

        // Ami Purvah (6.1.107) has precedence for sup-am.
        // Nadici (6.1.104) block:
        if (leftPhoneme in setOf(Svara.A, Svara.AA) && engine.contains(Pratyahara.IC, rightChar)) {
            return false
        }

        return true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val suffix = context.terms.last()

        val stemVarnas = stem.varnas
        val suffixVarnas = suffix.varnas
        val leftPhoneme = stemVarnas.last() as Svara
        val substitute = listOf(leftPhoneme.toDirgha())
        val withLongVowel = if (substitute.single() == leftPhoneme) context
            else context.replaceTermVarna(stem.id, stemVarnas.lastIndex, substitute, sutra)
        return DerivationChange(
            state = withLongVowel
                .deleteTermVarnas(suffix.id, 0, 1, sutra)
                .concatenateAfterInitialVowelCoalescence(stem.id, suffix, sutra)
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.102: Combined $leftPhoneme + ${suffixVarnas.first()} into long ${substitute.toDevanagari()}."
        )
    }
}
