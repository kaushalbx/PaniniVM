package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
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
        if ("6.4.148" in context.appliedSutras) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isTaddhita = "4.1.76" in context.activeAdhikaras ||
            affix.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्", "आयन्", "एय्", "ईन्", "ईय्", "इय्", "डट्", "तमट्", "तीयै", "टीयै", "डँ", "मयट्")

        val isStriII = affix.upadesha in setOf("ङीप्", "ङीष्", "ङीन्")

        if (!isTaddhita && !isStriII) return false

        return stem.varnas.lastOrNull() in deletableFinals
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val source = requireNotNull(stem.varnas.lastOrNull() as? Svara)
        val stemBase = stem.varnas.dropLast(1)
        val mergedVarnas = when (affix.upadesha) {
            "अण्" -> stemBase + affix.varnas
            "इञ्" -> stemBase + Svara.I
            "आयन्" -> stemBase + ayan
            "एय्" -> stemBase + eya
            "यञ्" -> stemBase + ya
            else -> when (affix.varnas) {
                ayanWithInherentA -> stemBase + ayan
                eyaWithInherentA -> stemBase + eya
                yaWithInherentA -> stemBase + ya
                else -> stemBase + affix.varnas
            }
        }

        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, mergedVarnas.toDevanagari(), source, emptyList(), sutra,
            ).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.4.148: Deleted the final stem vowel before the Taddhita affix.",
        )
    }

    private val deletableFinals: Set<Varna> = setOf(Svara.A, Svara.AA, Svara.I, Svara.II)
    private val ayan: List<Varna> = listOf(Svara.AA, Vyanjana.YA, Svara.A, Vyanjana.NA)
    private val ayanWithInherentA: List<Varna> = ayan + Svara.A
    private val eya: List<Varna> = listOf(Svara.E, Vyanjana.YA)
    private val eyaWithInherentA: List<Varna> = eya + Svara.A
    private val ya: List<Varna> = listOf(Vyanjana.YA, Svara.A)
    private val yaWithInherentA: List<Varna> = ya
}
