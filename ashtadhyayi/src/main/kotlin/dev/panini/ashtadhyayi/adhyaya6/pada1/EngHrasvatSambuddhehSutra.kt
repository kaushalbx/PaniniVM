package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.SupAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.matchesAnyAffix
import dev.panini.pratyahara.ItMarker
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDirgha
import dev.panini.sutra.*

/**
 * 6.1.69 एङ्ह्रस्वात् सम्बुद्धेः — the hal of a सम्बुद्धि is deleted after an aṅga ending in एङ् or in a
 * short vowel: राम + सुँ → हे राम, हरि + सुँ → हरे + स् (7.3.108) → हे हरे.
 */
object EngHrasvatSambuddhehSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.69", text = "एङ्ह्रस्वात् सम्बुद्धेः",
    hindiExplanation = "एङन्त और ह्रस्वान्त अङ्ग से परे सम्बुद्धि के हल् का लोप होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610069,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private val eng = Pratyahara(Svara.E, ItMarker.NG)

    /** After 6.1.107 merged अम् into the stem, its म् is still the सम्बुद्धि's (6.1.85 अन्तादिवच्च). */
    private fun mergedAm(context: DerivationState): Boolean {
        val stem = context.terms.singleOrNull() ?: return false
        val merged = context.droppedTerms.any { dropped ->
            dropped.sourceSupAffix == SupAffix.SU && context.samjnas.any { it.targetId == dropped.id && it.samjna == Samjna.SAMBUDDHI }
        }
        if (!merged || "6.1.107" !in context.appliedSutras || sutra in context.appliedSutras) return false
        if (stem.varnas.lastOrNull() != Vyanjana.MA) return false
        val before = stem.varnas.getOrNull(stem.varnas.lastIndex - 1) as? Svara ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(eng, before) || before.toDirgha() != before
    }

    override fun matches(context: DerivationState): Boolean {
        if (mergedAm(context)) return true
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (context.samjnas.none { it.targetId == affix.id && it.samjna == Samjna.SAMBUDDHI }) return false
        // The सम्बुद्धि is सुँ or its substitute अम् (7.1.24): its hal is the final consonant.
        if (!(affix.matchesAnyAffix(SupAffix.SU) || affix.sourceSupAffix == SupAffix.SU) || affix.varnas.lastOrNull() !is Vyanjana) return false
        val final = stem.varnas.lastOrNull() as? Svara ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(eng, final) || final.toDirgha() != final
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (mergedAm(context)) {
            val stem = context.terms.single()
            return DerivationChange(context.deleteTermVarnas(stem.id, stem.varnas.lastIndex, 1, sutra),
                "6.1.69 deletes the म् of the सम्बुद्धि अम् merged into ${stem.surface} (6.1.85).")
        }
        val affix = context.terms.last()
        val stem = context.terms[context.terms.lastIndex - 1]
        if (affix.varnas.size > 1) return DerivationChange(
            context.deleteTermVarnas(affix.id, affix.varnas.lastIndex, 1, sutra),
            "6.1.69 deletes the hal of the सम्बुद्धि ${affix.surface} after ${stem.surface}.")
        val carried = context.samjnas.filter { it.targetId == affix.id && it.samjna == Samjna.PADA }
            .map { it.copy(targetId = stem.id) }.toSet()
        return DerivationChange(
            context.removeTerm(affix.id, sutra).withSamjnas(carried).copy(stage = DerivationStage.FINAL),
            "6.1.69 deletes the hal of the सम्बुद्धि after ${stem.surface}.")
    }
}
