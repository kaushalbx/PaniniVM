package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.1.97: ato guṇe.
 * When an 'a' (short) is followed by a guṇa vowel (a, e, o) in an affix, 
 * the single substitute for both is the latter vowel (pararūpa).
 */
object AtoGuneSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.97",
    text = "अतो गुणे",
    hindiExplanation = "अपदान्त अकार के बाद गुण संज्ञक वर्ण (अ, ए, ओ) परे होने पर पररूप एकादेश होता है।",
    type = SutraType.APAVADA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610097,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("6.1.101"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (stem.id == "shap" && context.terms.any { it.kind == TermKind.DHATU && it.gana == Gana.ADADI }) return false
        
        // 1. Stem must end in short 'a'
        if (!dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface)) return false
        
        // 2. Affix must start with a Guṇa vowel
        val firstChar = affix.surface.firstOrNull() ?: return false
        val isGuna = firstChar in setOf('अ', 'ए', 'ओ', 'े', 'ो')
        
        return isGuna
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val affix = terms.last()
        
        val firstChar = affix.surface.first()
        val replacement = firstChar.toString()
        
        val lastChar = stem.surface.last()
        val newSurface = if (lastChar !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks) {
            val vowelMark = when (firstChar) {
                'ए' -> "े"
                'ओ' -> "ो"
                else -> ""
            }
            stem.surface + vowelMark + affix.surface.drop(1)
        } else {
            stem.surface.dropLast(1) + replacement + affix.surface.drop(1)
        }
        
        val mergedTerm = stem.copy(
            surface = newSurface,
            sthaniProps = stem.sthaniProps ?: affix.sthaniProps
        )
        val newSamjnas = context.samjnas.map { 
            if (it.targetId == affix.id && it.samjna != Samjna.PRATYAYA) it.copy(targetId = stem.id) else it
        }.toSet()
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + mergedTerm,
                stage = DerivationStage.ANGAKARYA,
                samjnas = newSamjnas
            ).addSubstitution(VarnaSubstitution(stem.id, 'अ', replacement, sutra)),
            explanation = "6.1.97: Pararūpa substitution ($replacement) for a + $firstChar."
        )
    }
}
