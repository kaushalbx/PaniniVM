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
import dev.panini.sutra.SutraType

/**
 * 8.4.59: vā padāntasya.
 * Word-final Anusvāra is optionally replaced by the parasavarṇa (5th nasal member of the following stop's class)
 * when followed by a yay sound.
 */
object VaPadantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.59",
    text = "वा पदान्तस्य",
    hindiExplanation = "पदान्त अनुस्वार का ययि परे रहते विकल्प से परसवर्ण (वर्ग का ५वाँ अनुनासिक वर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840059,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            if (!curr.endsWith("ं") || next.isEmpty()) return@any false
            val nextChar = next.first()
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, nextChar)) return@any false
            val info = Varnamala.getVargaInfo(nextChar)
            info != null && Varnamala.getVargaMember(info.first, 4) != null
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            if (!curr.endsWith("ं") || next.isEmpty()) return@first false
            val nextChar = next.first()
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, nextChar)) return@first false
            val info = Varnamala.getVargaInfo(nextChar)
            info != null && Varnamala.getVargaMember(info.first, 4) != null
        }

        val targetTerm = context.terms[targetIndex]
        val nextTerm = context.terms[targetIndex + 1]
        val nextChar = nextTerm.surface.first()
        val info = Varnamala.getVargaInfo(nextChar)!!
        val replacement = Varnamala.getVargaMember(info.first, 4)!!.toString()

        val surface = targetTerm.surface
        val newSurface = if (surface.endsWith("ं")) {
            surface.dropLast(1) + replacement + "्"
        } else {
            surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.59: Replaced final Anusvāra with parasavarṇa '$replacement' before yay sound."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'ं', replacement, sutra))) }
    }
}
