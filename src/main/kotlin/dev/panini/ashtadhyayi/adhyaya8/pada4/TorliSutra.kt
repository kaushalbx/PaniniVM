package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.60: tor li.
 * The sounds of ta-varga (t, th, d, dh, n) are replaced by 'l' (or nasalized l̐ for 'n')
 * when immediately followed by 'l'.
 */
object TorliSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.60",
    text = "तोर्लि",
    hindiExplanation = "त-वर्ग (त्, थ्, द्, ध्, न्) के स्थान पर ल-कार आदेश होता है, यदि ल-कार परे हो (न् का अनुनासिक लँ्)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840060,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    private val tuVarga = Varnamala.expandUdit("तु")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || !next.startsWith("ल")) return@any false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@any false
            lastChar in tuVarga
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || !next.startsWith("ल")) return@first false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@first false
            lastChar in tuVarga
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface
        val lastChar = surface.trimEnd('्').last()
        val isNasal = lastChar == 'न' || surface.endsWith("न्")
        val replacement = if (isNasal) "ँल्" else "ल्"

        val newSurface = when {
            surface.endsWith("त्") || surface.endsWith("थ्") || surface.endsWith("द्") || surface.endsWith("ध्") || surface.endsWith("न्") ->
                surface.dropLast(2) + replacement
            surface.endsWith("त") || surface.endsWith("थ") || surface.endsWith("द") || surface.endsWith("ध") || surface.endsWith("न") ->
                surface.dropLast(1) + replacement
            else -> surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.60: Assimilated ta-varga to $replacement before 'l'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, surface.last(), replacement, sutra))) }
    }
}
