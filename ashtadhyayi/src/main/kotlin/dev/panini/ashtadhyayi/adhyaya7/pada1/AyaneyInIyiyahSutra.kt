package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignationConsumption
import dev.panini.derivation.ItDesignationRemap
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 7.1.2: āyane-yī-nī-y-iyaḥ pha-ḍha-kha-cha-ghāṁ pratyayādīnām.
 * Substitutes āyan, ey, īn, īy, iy for initial ph, ḍh, kh, ch, gh of affixes.
 */
object AyaneyInIyiyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.2",
    text = "ायनेयीनीयियः फढखछघां प्रत्ययादीनाम्",
    hindiExplanation = "प्रत्यय के आदि फ्, ढ्, ख्, छ्, घ् के स्थान पर क्रमशः आयन्, एय्, ईन्, ईय्, इय् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710002,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    stage = SutraStage.IT_PROCESSING,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // This rule continues the taddhita domain; it must not rewrite the
        // initial ku-it of a kṛt suffix such as घञ् before 1.3.9 deletes it.
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        if (pratyaya.varnas.firstOrNull() !in initialSubstitutes) return false
        val designations = pratyaya.itDesignations + pratyaya.deferredItDesignations
        return designations.any { it.sutra == "1.3.3" && pratyaya.varnas.lastIndex in it.varnaIndices }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val pratyayaIndex = context.terms.indexOfLast { it.kind == TermKind.PRATYAYA }
        val pratyaya = context.terms[pratyayaIndex]

        val first = pratyaya.varnas.first()
        val replacementVarnas = initialSubstitutes.getValue(first)
        val replacement = replacementVarnas.toDevanagari()
        val replacedCount = if (pratyaya.varnas.getOrNull(1) == Svara.A) 2 else 1
        val remainder = pratyaya.varnas.drop(replacedCount)
        val initialDesignation = (pratyaya.itDesignations + pratyaya.deferredItDesignations).singleOrNull {
            it.varnaIndices == setOf(0) && pratyaya.varnas.getOrNull(1) == Svara.A
        }
        val updatedPratyaya = pratyaya.replaceWholeAffixWithVarnaMapping(
            replacementVarnas = replacementVarnas + remainder,
            survivingPositions = (replacedCount until pratyaya.varnas.size).associateWith {
                it - replacedCount + replacementVarnas.size
            },
            consumedDesignations = setOfNotNull(initialDesignation),
            sutra = sutra,
        )

        val newTerms = context.terms.toMutableList()
        newTerms[pratyayaIndex] = updatedPratyaya

        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "7.1.2 substitutes $replacement for initial ${first.devanagari} of pratyaya.",
        )
    }

    private val initialSubstitutes: Map<Varna, List<Varna>> = mapOf(
        Vyanjana.PHA to listOf(Svara.AA, Vyanjana.YA, Svara.A, Vyanjana.NA, Svara.A),
        Vyanjana.DDHA to listOf(Svara.E, Vyanjana.YA),
        Vyanjana.KHA to listOf(Svara.II, Vyanjana.NA),
        Vyanjana.CHA to listOf(Svara.II, Vyanjana.YA),
        Vyanjana.GHA to listOf(Svara.I, Vyanjana.YA),
    )
}
