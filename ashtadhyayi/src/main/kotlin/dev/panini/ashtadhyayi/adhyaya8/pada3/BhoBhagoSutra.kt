package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
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

        if (left.varnas.lastOrNull() != Vyanjana.RA) return false

        // 1. Check if preceded by bho, bhago, agho, a, or ā
        val base = left.varnas.dropLast(1)
        val isPrecededByEligible = eligibleBases.any { base.takeLast(it.size) == it } ||
            base.lastOrNull() == Svara.A

        if (!isPrecededByEligible) return false

        // 2. Check if followed by Aś (vowels + voiced consonants)
        val next = right.varnas.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next.devanagari.single())
    }

    override fun apply(context: DerivationState): DerivationChange {
        val leftTerm = context.terms[context.terms.size - 2]
        val next = context.terms.last().varnas.first()
        val result = leftTerm.varnas.dropLast(1) + Vyanjana.YA

        return DerivationChange(
            state = context.substituteTermVarnas(leftTerm.id, result, Vyanjana.RA, listOf(Vyanjana.YA), sutra),
            explanation = "8.3.17 replaces ru with y before voiced sound $next."
        )
    }

    private val eligibleBases: List<List<Varna>> = listOf(
        listOf(Vyanjana.BHA, Svara.O),
        listOf(Vyanjana.BHA, Svara.A, Vyanjana.GA, Svara.O),
        listOf(Svara.A, Vyanjana.GHA, Svara.O),
    )
}
