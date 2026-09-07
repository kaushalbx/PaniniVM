package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.105: āṅi cāpaḥ.
 * The final 'ā' of an aṅga ending in 'āp' is replaced by 'e'
 * before the affix 'āṅ' (ṭā) and in the dual 'os'.
 */
object AngiCapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.105",
    text = "आङि चापः",
    hindiExplanation = "आप्-प्रत्यान्त अङ्ग के अन्त्य 'आ' का एकार होता है, आङ् (टा) और ओस् परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730105,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in 'ā' (representing Āp)
        if (!stem.surface.endsWith('ा') && !stem.surface.endsWith('आ')) return false

        // 2. Affix must be 'āṅ' (ṭā) or 'os'
        // 'ṭā' results in 'in' after 7.1.12 for a-stems, but for ā-stems it remains 'ā' or is handled here.
        // The upadeśa of 'ṭā' is often referred to as 'āṅ' in commentary.
        return affix.upadesha == "टा" || affix.upadesha == "ओस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val replacement = "े"

        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.substituteTermSurface(stem.id, newSurface, lastChar, replacement, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.105: Replaced final 'ā' with 'e' before 'āṅ/os'."
        )
    }
}
