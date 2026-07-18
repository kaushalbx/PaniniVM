package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.3.17: bho-bhago-agho-apūrvasya yo'śi.
 * Replaces 'ru' (repha) with 'y' when preceded by 'bho', 'bhago', 'agho',
 * or 'a'/'ā', and followed by an 'aś' sound (vowels + voiced consonants).
 */
object BhoBhagoSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.17",
    text = "भोभगोअघोअपूर्वस्य योऽशि",
    hindiExplanation = "भो, भगो, अघो शब्दों के बाद या अ/आ के बाद वाले 'रु' (र्) के स्थान पर 'य्' आदेश होता है, यदि बाद में अश् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830017,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        val surface = left.surface
        if (!surface.endsWith('र')) return false // Target is repha (from ru)

        // 1. Check if preceded by bho, bhago, agho, a, or ā
        val isPrecededByEligible = surface.endsWith("भोर") ||
                                   surface.endsWith("भगोर") ||
                                   surface.endsWith("अघोर") ||
                                   (surface.length >= 2 && (surface[surface.length - 2] == 'अ' || surface[surface.length - 2] == 'ा' || surface[surface.length - 2] == 'आ'))

        if (!isPrecededByEligible) return false

        // 2. Check if followed by Aś (vowels + voiced consonants)
        val nextChar = right.surface.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, nextChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val leftTerm = context.terms[context.terms.size - 2]
        val nextChar = context.terms.last().surface.first()

        val replacement = "य्"
        val newSurface = leftTerm.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface)),
            explanation = "8.3.17: Replaced 'ru' with 'y' before voiced sound '$nextChar'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(leftTerm.id, 'र', replacement, sutra))) }
    }
}
