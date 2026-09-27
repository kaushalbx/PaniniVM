package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.ashtadhyayi.adhyaya7.pada1.HrasvanadyapoNutSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SatCaturbhyascaSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.113: yāḍāpaḥ.
 * The augment yāṭ is added to a ṅit case-affix when it follows an aṅga ending in āp.
 */
object YadapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.113",
    text = "याडापः",
    hindiExplanation = "आप्-प्रत्यान्त अङ्ग के बाद ङित् विभक्ति को याट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730113,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 7.1.54-55 supply nuṭ to genitive plural आम् before this later yāṭ rule.
        if (HrasvanadyapoNutSutra.matches(context) || SatCaturbhyascaSutra.matches(context)) return false
        if (affix.establishedBySutras.any { it == "7.1.54" || it == "7.1.55" }) return false

        val isNadiGenitivePlural = context.samjnas.any { it.targetId == stem.id && it.samjna == dev.panini.shiksha.Samjna.NADI } &&
            affix.upadesha == "आम्"
        if (isNadiGenitivePlural) return false

        // 1. The stem must be an actual āp formation, not another pit affix
        // (for example शप्) whose visible remainder happens to be lengthened to ā.
        val isApFormation = stem.upadesha in setOf("टाप्", "डाप्", "चाप्") ||
            "4.1.4" in stem.establishedBySutras
        if (stem.varnas.lastOrNull() != Svara.AA ||
            !isApFormation ||
            !stem.hasEffectiveMarker(ItMarker.P)
        ) return false

        // 2. Affix must be ṅit
        val isNgit = affix.hasEffectiveMarker(ItMarker.NGIT) ||
            affix.upadesha in setOf("ङि", "टा") || affix.varnas == listOf(Svara.AA, Vyanjana.MA)

        // Prevent infinite loop by checking if we already applied 'yā'
        val alreadyApplied = affix.varnas.take(2) in setOf(
            listOf(Vyanjana.YA, Svara.AA),
            listOf(Vyanjana.YA, Svara.AI),
        )

        return isNgit && !alreadyApplied
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (affix.upadesha == "टा") {
            return DerivationChange(
                state = context.substituteTermSurface(
                    stem.id, (stem.varnas.dropLast(1) + Svara.A).toDevanagari(),
                    Svara.AA, listOf(Svara.A), sutra,
                )
                    .replaceWholeAffix(affix.id, listOf(Vyanjana.YA, Svara.AA), sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume)
                    .blockSutra(sutra, sutra)
                    .copy(stage = DerivationStage.PADA_FORMED),
                explanation = "7.3.113: Formed the instrumental singular -या after an āp stem.",
            )
        }
        val newVarnas = when (affix.upadesha) {
            "ङसि", "ङस्" -> listOf(Vyanjana.YA, Svara.AA, Ayogavaha.VISARGA)
            else -> when (affix.varnas) {
            listOf(Vyanjana.NGA, Svara.E), listOf(Svara.A, Svara.E) -> listOf(Vyanjana.YA, Svara.AI)
            listOf(Svara.A, Vyanjana.SA) -> listOf(Vyanjana.YA, Svara.AA, Vyanjana.SA)
            listOf(Svara.AA, Vyanjana.MA) -> listOf(Vyanjana.YA, Svara.AA, Vyanjana.MA)
            else -> listOf(Vyanjana.YA, Svara.AA) + affix.varnas
            }
        }

        return DerivationChange(
            state = context.replaceWholeAffix(affix.id, newVarnas, sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume)
                .blockSutra(sutra, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.113: Added 'yāṭ' augment before ṅit affix and merged."
        )
    }
}
