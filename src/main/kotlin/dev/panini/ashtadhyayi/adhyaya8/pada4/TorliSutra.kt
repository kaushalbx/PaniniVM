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
    private data class Match(val termIndex: Int, val isNasal: Boolean)

    private val tuVarga = Varnamala.expandUdit("तु")

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val isNasal = match.isNasal
        val replacement = if (isNasal) "ँल्" else "ल्"

        val surface = targetTerm.surface
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

    private fun findMatch(context: DerivationState): Match? {
        val terms = context.terms
        for (i in 0 until terms.size - 1) {
            val curr = terms[i].surface
            val next = terms[i + 1].surface

            if (curr.isNotEmpty() && next.startsWith("ल")) {
                val lastChar = curr.trimEnd('्').lastOrNull() ?: continue
                if (lastChar in tuVarga) {
                    val isNasal = lastChar == 'न' || curr.endsWith("न्")
                    return Match(i, isNasal)
                }
            }
        }
        return null
    }
}
