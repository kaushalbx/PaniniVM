package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.148: yasyeti ca.
 * Deletion of final 'i' or 'a' of a 'bha' stem before 'ī' or a Taddhita affix.
 */
object YasyetiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.148",
    text = "यस्येति च",
    hindiExplanation = "भ-संज्ञक अङ्ग के अन्त्य 'इ' या 'अ' का लोप होता है, 'ई' या तद्धित प्रत्यय परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640148,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DHATU,
    dependencies = setOf("6.4.1", "1.4.18")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false
        if (context.substitutions.any { it.sutra == "6.4.148" }) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isTaddhita = "4.1.76" in context.activeAdhikaras ||
            affix.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्", "आयन्", "एय्", "ईन्", "ईय्", "इय्", "डट्", "तमट्", "तीयै", "टीयै", "डँ", "मयट्")

        val isStriII = affix.upadesha in setOf("ङीप्", "ङीष्", "ङीन्")

        if (!isTaddhita && !isStriII) return false

        if (stem.surface.endsWith('्')) return false

        return dev.panini.shiksha.Varnamala.endsWithA(stem.surface) ||
            dev.panini.shiksha.Varnamala.endsWithAA(stem.surface) ||
            stem.surface.endsWith('इ') || stem.surface.endsWith('ि') || stem.surface.endsWith('ी')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.size - 2
        val stem = context.terms[stemIndex]
        val affix = context.terms.last()

        val stemBase = when {
            stem.surface.endsWith('ा') || stem.surface.endsWith('ि') || stem.surface.endsWith('ी') -> stem.surface.dropLast(1)
            else -> stem.surface
        }

        val affixSurface = affix.surface
        val mergedSurface = when {
            affixSurface.startsWith("इञ्") || affixSurface.startsWith("इ") -> stemBase + "ि"
            affixSurface.startsWith("आयन्") || affixSurface.startsWith("आयन") -> stemBase + "ायन्"
            affixSurface.startsWith("एय्") || affixSurface.startsWith("एय") -> stemBase + "ेय्"
            affixSurface.startsWith("यञ्") || affixSurface.startsWith("य") -> stemBase + "्य"
            else -> if (stemBase.endsWith('्') && affixSurface.firstOrNull() in
                setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) {
                stemBase.dropLast(1) + affixSurface
            } else {
                stemBase + affixSurface
            }
        }

        val updatedTerm = DerivationTerm(
            id = stem.id,
            surface = mergedSurface,
            kind = TermKind.PRATIPADIKA,
            upadesha = stem.upadesha,
        )

        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + updatedTerm,
                stage = DerivationStage.PADA_FORMED,
            ),
            explanation = "6.4.148: Deleted final stem vowel before Taddhita affix, producing '$mergedSurface'.",
        )
    }
}
