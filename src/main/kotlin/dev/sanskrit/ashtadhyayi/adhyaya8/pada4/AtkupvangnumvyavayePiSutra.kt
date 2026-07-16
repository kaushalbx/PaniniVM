package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
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
        if (HasMorphosyntax(vibhakti = Vibhakti.DVITIYA, vacana = Vacana.BAHUVACANA).matches(context)) return false
        // 3.4.105's liṅ substitute रन् retains a dental न्: लभेरन्.
        if (context.terms.any { it.surface == "रन्" }) return false

        // Matches if there is an 'n' preceded by 'r' or 'ṣ' with only allowed intervenors.
        val surface = context.surface
        val (rIndex, nIndex) = targetIndices(context) ?: return false

        // 8.4.37: padāntasya blocks retroflexion at the end of a word (ending in 'न्')
        val isPadanta = nIndex == surface.length - 2 && surface[nIndex + 1] == '्'
        if (isPadanta) return false

        // 8.4.35: No retroflexion if 'n' is followed by a dental consonant (t-varga: t, th, d, dh, n)
        val nextCharIndex = if (nIndex + 1 < surface.length && surface[nIndex + 1] == '्') nIndex + 2 else nIndex + 1
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.gana == Gana.RUDHADI }
        val dhatuEnd = dhatu?.surface?.length ?: -1
        val isStrongRudhadiShnam = nIndex < dhatuEnd &&
            surface.getOrNull(nIndex + 1) != '्' &&
            context.droppedTerms.any { it.upadesha == "श्नम्" }
        val isKryadiShnaNasal = context.terms.any { it.id == "shna" && 'न' in it.surface }
        if (nextCharIndex < surface.length) {
            val nextChar = surface[nextCharIndex]
            if (!isStrongRudhadiShnam && !isKryadiShnaNasal && nextChar in setOf('त', 'थ', 'द', 'ध', 'न')) return false
        }

        val intervenors = surface.substring(rIndex + 1, nIndex)
        return intervenors.all { isAllowed(it) }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // Find the term containing the 'n' and replace it
        val surface = context.surface
        val (_, nIndex) = requireNotNull(targetIndices(context))
        
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

    private fun targetIndices(context: DerivationState): Pair<Int, Int>? {
        val surface = context.surface
        val shna = context.terms.firstOrNull { it.id == "shna" }
        if (shna != null) {
            if ('ण' in shna.surface || 'न' !in shna.surface) return null
            val shnaIndex = context.terms.indexOf(shna)
            val nIndex = context.copy(terms = context.terms.take(shnaIndex)).surface.length
            val triggerIndex = surface.substring(0, nIndex).lastIndexOfAny(setOf('र', 'ष', 'ऋ', 'ृ', 'ॠ', 'ॄ'))
            if (triggerIndex >= 0) return triggerIndex to nIndex
            return null
        }
        val triggerIndex = surface.lastIndexOfAny(setOf('र', 'ष', 'ऋ', 'ृ', 'ॠ', 'ॄ'))
        if (triggerIndex < 0) return null
        val nIndex = surface.indexOf('न', triggerIndex)
        return nIndex.takeIf { it >= 0 }?.let { triggerIndex to it }
    }

    private fun String.lastIndexOfAny(chars: Set<Char>): Int {
        for (i in length - 1 downTo 0) {
            if (this[i] in chars) return i
        }
        return -1
    }
}
