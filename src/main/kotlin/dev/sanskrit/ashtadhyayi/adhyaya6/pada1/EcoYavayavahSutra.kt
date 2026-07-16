package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 6.1.78: eco'yavāyāvaḥ. Substitute ay, av, āy, āv for e, o, ai, au before a vowel. */
object EcoYavayavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.78",
    text = "एचोऽयवायावः",
    hindiExplanation = "एच् (ए, ओ, ऐ, औ) के बाद अच् (कोई स्वर) आए तो क्रम से अय्, अव्, आय् या आव् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610078,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size - 1) {
            val rightTerm = context.terms[i + 1]
            if (context.effectiveContext.rupa.lakara == Lakara.LET &&
                rightTerm.upadesha == "झि" && context.substitutions.none { it.sutra == "3.4.94" }
            ) continue
            val left = context.terms[i].surface.lastOrNull() ?: continue
            val right = rightTerm.surface.firstOrNull() ?: continue
            if (engine.contains(Pratyahara.EC, left) && engine.contains(Pratyahara.AC, right)) {
                return true
            }
        }
        return false
    }

    override fun apply(context: DerivationState): DerivationChange {
        val engine = Ashtadhyayi.pratyaharaEngine
        for (i in 0 until context.terms.size - 1) {
            val leftTerm = context.terms[i]
            val rightTerm = context.terms[i+1]
            if (context.effectiveContext.rupa.lakara == Lakara.LET &&
                rightTerm.upadesha == "झि" && context.substitutions.none { it.sutra == "3.4.94" }
            ) continue
            val leftChar = leftTerm.surface.lastOrNull() ?: continue
            val rightChar = rightTerm.surface.firstOrNull() ?: continue
            if (engine.contains(Pratyahara.EC, leftChar) && engine.contains(Pratyahara.AC, rightChar)) {
                val replacement = getAdesha(leftChar)
                val base = leftTerm.surface.dropLast(1)
                val s1 = concatDevanagari(base, replacement)
                val newSurface = concatDevanagari(s1, rightTerm.surface)
                val mergedTerm = leftTerm.copy(
                    surface = newSurface,
                    sthaniProps = leftTerm.sthaniProps ?: rightTerm.sthaniProps
                )
                val newTerms = context.terms.subList(0, i) + mergedTerm + context.terms.subList(i + 2, context.terms.size)
                val newSamjnas = context.samjnas.map { 
                    if (it.targetId == rightTerm.id && it.samjna != Samjna.PRATYAYA) it.copy(targetId = leftTerm.id) else it
                }.toSet()
                
                return DerivationChange(
                    state = context.copy(
                        terms = newTerms,
                        droppedTerms = context.droppedTerms + rightTerm.copy(surface = ""),
                        stage = DerivationStage.PADA_FORMED,
                        samjnas = newSamjnas
                    ).addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, replacement, sutra)),
                    explanation = "6.1.78: substituted $replacement for $leftChar before vowel."
                )
            }
        }
        return DerivationChange(context, "6.1.78: No match found")
    }

    private fun concatDevanagari(s1: String, s2: String): String {
        if (s1.isEmpty()) return s2
        if (s2.isEmpty()) return s1
        
        if (s1.endsWith('्')) {
            val firstChar = s2.first()
            if (firstChar == 'अ') {
                return s1.dropLast(1) + s2.drop(1)
            }
            if (firstChar in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) {
                return s1.dropLast(1) + s2
            }
            val matra = getMatra(firstChar)
            if (matra != null) {
                return s1.dropLast(1) + matra + s2.drop(1)
            }
        }
        
        if (s1.last() !in dev.sanskrit.shiksha.Varnamala.independentVowelsOrMarks && s2.startsWith('अ')) {
            return s1 + s2.drop(1)
        }
        
        return s1 + s2
    }

    private fun getMatra(c: Char): Char? = when (c) {
        'आ' -> 'ा'
        'इ' -> 'ि'
        'ई' -> 'ी'
        'उ' -> 'ु'
        'ऊ' -> 'ू'
        'ऋ' -> 'ृ'
        'ॠ' -> 'ॄ'
        'ऌ' -> 'ॢ'
        'ए' -> 'े'
        'ऐ' -> 'ै'
        'ओ' -> 'ो'
        'औ' -> 'ौ'
        else -> null
    }

    private fun getAdesha(c: Char): String = when (c) {
        'ए', 'े' -> "अय्"
        'ओ', 'ो' -> "अव्"
        'ऐ', 'ै' -> "आय्"
        'औ', 'ौ' -> "आव्"
        else -> ""
    }
}
