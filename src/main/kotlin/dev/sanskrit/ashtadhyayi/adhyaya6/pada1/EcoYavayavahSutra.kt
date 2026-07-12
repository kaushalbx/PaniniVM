package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
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
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2].surface.lastOrNull() ?: return false
        val right = context.terms.last().surface.firstOrNull() ?: return false
        
        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.EC, left) && engine.contains(Pratyahara.AC, right)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()
        
        val leftChar = leftTerm.surface.last()
        val replacement = getAdesha(leftChar)
        
        val newSurface = leftTerm.surface.dropLast(1) + replacement + rightTerm.surface
        
        return DerivationChange(
            state = context.copy(
                terms = terms.dropLast(2) + leftTerm.copy(surface = newSurface),
                stage = DerivationStage.ANGAKARYA
            ).addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, replacement, sutra)),
            explanation = "6.1.78: substituted $replacement for $leftChar before vowel."
        )
    }

    private fun getAdesha(c: Char): String = when (c) {
        'ए', 'े' -> "अय्"
        'ओ', 'ो' -> "अव्"
        'ऐ', 'ै' -> "आय्"
        'औ', 'ौ' -> "आव्"
        else -> ""
    }
}
