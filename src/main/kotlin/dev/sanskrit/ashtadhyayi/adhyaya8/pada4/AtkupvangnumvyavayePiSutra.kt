package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 8.4.2: aṭ-kup-vāṅ-num-vyavāye'pi.
 * Retroflexion of 'n' to 'ṇ' happens even if sounds of Aṭ, Ku (ka-varga), 
 * Pu (pa-varga), Āṅ, or Num intervene between the trigger (r/ṣ) and the target.
 */
object AtkupvangnumvyavayePiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.2",
    text = "अट्कुप्वाङ्नुम्व्यवायेऽपि",
    hindiExplanation = "र् या ष् के बाद न् का ण् होता है, यदि बीच में अट्, क-वर्ग, प-वर्ग, आङ् या नुम् का व्यवधान हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840002,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("8.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Matches if there is an 'n' preceded by 'r' or 'ṣ' with only allowed intervenors.
        val surface = context.surface
        val rIndex = surface.lastIndexOfAny(setOf('र', 'ष'))
        if (rIndex == -1) return false
        
        val nIndex = surface.indexOf('न', rIndex)
        if (nIndex == -1) return false

        val intervenors = surface.substring(rIndex + 1, nIndex)
        return intervenors.all { isAllowed(it) }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // Find the term containing the 'n' and replace it
        val surface = context.surface
        val rIndex = surface.lastIndexOfAny(setOf('र', 'ष'))
        val nIndex = surface.indexOf('न', rIndex)
        
        // We find which term owns the 'n'
        var currentCharCount = 0
        val targetTerm = context.terms.find { term ->
            val start = currentCharCount
            currentCharCount += term.surface.length
            nIndex in start until currentCharCount
        } ?: return DerivationChange(context, "8.4.2: Target 'n' not found in terms.")

        val newSurface = targetTerm.surface.replaceFirst('न', 'ण')
        
        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.2: Retroflexed 'n' to 'ṇ' with allowed intervenors."
        )
    }

    private fun isAllowed(c: Char): Boolean {
        if (c == '्') return true // Virama is transparent
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.AC, c) || // Aṭ includes all vowels
               c in setOf('ह', 'य', 'व', 'र') ||    // Remainder of Aṭ
               c in Varnamala.expandUdit("कु") || 
               c in Varnamala.expandUdit("पु") ||
               c == 'ं' // Num results in Anusvara
    }

    private fun String.lastIndexOfAny(chars: Set<Char>): Int {
        for (i in length - 1 downTo 0) {
            if (this[i] in chars) return i
        }
        return -1
    }
}
