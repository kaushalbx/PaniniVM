package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 8.2.30: coḥ kuḥ.
 * Substitutes ku (ka-varga) for cu (ca-varga) 
 * at the end of a pada or before a jhal sound.
 */
object CohKuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.30",
    text = "चोः कुः",
    hindiExplanation = "पदान्त में या झल् वर्ण परे होने पर च-वर्ग के स्थान पर क-वर्ग आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820030,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val surface = context.surface
        if (surface.isEmpty()) return false

        // Check for cu-varga (c, ch, j, jh, ñ)
        val cuChars = setOf('च', 'छ', 'ज', 'झ', 'ञ')
        
        // Nimitta 1: End of Pada
        val lastChar = surface.last()
        if (lastChar in cuChars) return true

        // Nimitta 2: Before Jhal
        for (i in 0 until surface.length - 1) {
            if (surface[i] in cuChars) {
                val nextChar = surface[i + 1]
                if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, nextChar)) {
                    return true
                }
            }
        }
        
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        val surface = context.surface
        val cuChars = setOf('च', 'छ', 'ज', 'झ', 'ञ')
        
        var targetIndex = -1
        // Priority to later occurrences? Tripadi usually applies word-internally first or word-end.
        // For word-end:
        if (surface.last() in cuChars) {
            targetIndex = surface.length - 1
        } else {
            for (i in 0 until surface.length - 1) {
                if (surface[i] in cuChars) {
                    val nextChar = surface[i + 1]
                    if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, nextChar)) {
                        targetIndex = i
                        break
                    }
                }
            }
        }

        val targetChar = surface[targetIndex]
        val vargaInfo = Varnamala.getVargaInfo(targetChar)!!
        val replacement = Varnamala.getVargaMember("कु", vargaInfo.second)!!.toString()

        var offset = 0
        val targetTerm = context.terms.find { term ->
            val start = offset
            offset += term.surface.length
            targetIndex in start until offset
        }!!

        val newSurface = targetTerm.surface.replaceFirst(targetChar.toString(), replacement)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.2.30: Substituted ka-varga '$replacement' for ca-varga '$targetChar'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }
}
