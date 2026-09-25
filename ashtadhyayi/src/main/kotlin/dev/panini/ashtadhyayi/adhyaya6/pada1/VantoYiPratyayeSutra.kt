package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.79: vānto yi pratyaye.
 * Substitutes 'av' and 'āv' (vānta) for 'o' and 'au' respectively
 * when followed by a suffix (pratyaya) beginning with 'y'.
 */
object VantoYiPratyayeSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.79",
    text = "वान्तो यि प्रत्यये",
    hindiExplanation = "यकारादि प्रत्यय परे होने पर ओकार को 'अव्' और औकार को 'आव्' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    nimittaScope = NimittaScope.EXTERNAL
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Affix must be a Pratyaya and start with 'y'
        val isYPratyaya = affix.varnas.firstOrNull() == Vyanjana.YA &&
                        context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
        if (!isYPratyaya) return false

        // 2. Stem must end in 'o' or 'au'
        return stem.varnas.lastOrNull() in setOf(Svara.O, Svara.AU)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val source = stem.varnas.last() as Svara
        val replacement: List<Varna> = when (source) {
            Svara.O -> listOf(Svara.A, Vyanjana.VA)
            Svara.AU -> listOf(Svara.AA, Vyanjana.VA)
            else -> error("6.1.79 matched a non-ec stem")
        }
        val newSurface = (stem.varnas.dropLast(1) + replacement).toDevanagari()

        return DerivationChange(
            state = context.substituteTermSurface(stem.id, newSurface, source, replacement, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.79: Substituted '${replacement.toDevanagari()}' for '$source' before y-initial affix."
        )
    }
}
