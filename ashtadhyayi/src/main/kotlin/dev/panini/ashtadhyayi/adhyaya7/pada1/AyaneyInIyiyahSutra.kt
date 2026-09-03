package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignationConsumption
import dev.panini.derivation.ItDesignationRemap
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
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
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val firstChar = pratyaya.surface.firstOrNull() ?: return false
        if (firstChar !in setOf('फ', 'ढ', 'ख', 'छ', 'घ')) return false
        val designations = pratyaya.itDesignations + pratyaya.deferredItDesignations
        return designations.any { it.sutra == "1.3.3" && it.endExclusive == pratyaya.surface.length }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val pratyayaIndex = context.terms.indexOfLast { it.kind == TermKind.PRATYAYA }
        val pratyaya = context.terms[pratyayaIndex]

        val firstChar = pratyaya.surface.first()
        val rest = pratyaya.surface.drop(1)
        val replacement = when (firstChar) {
            'फ' -> "आयन"
            'ढ' -> "एय्"
            'ख' -> "ईन्"
            'छ' -> "ईय्"
            'घ' -> "इय्"
            else -> pratyaya.surface
        }
        val newSurface = replacement + rest
        val initialDesignation = (pratyaya.itDesignations + pratyaya.deferredItDesignations).singleOrNull {
            it.start == 0 && it.endExclusive == 1 && it.designatedText == firstChar.toString()
        }
        val remaps = (pratyaya.itDesignations + pratyaya.deferredItDesignations)
            .filterNot { initialDesignation != null && it == initialDesignation }
            .map { designation ->
                ItDesignationRemap(
                    oldStart = designation.start,
                    oldEndExclusive = designation.endExclusive,
                    newStart = designation.start + replacement.length - 1,
                    newEndExclusive = designation.endExclusive + replacement.length - 1,
                )
            }
        val updatedPratyaya = pratyaya.replaceWholeAffix(
            replacementSurface = newSurface,
            replacementUpadesha = pratyaya.upadesha,
            sutra = sutra,
            policy = WholeAffixDesignationPolicy.PreserveAndRemap(
                remaps = remaps,
                consumed = initialDesignation?.let {
                    listOf(ItDesignationConsumption(it.start, it.endExclusive))
                }.orEmpty(),
            ),
        )

        val newTerms = context.terms.toMutableList()
        newTerms[pratyayaIndex] = updatedPratyaya

        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "7.1.2 substitutes $replacement for initial '$firstChar' of pratyaya.",
        )
    }
}
