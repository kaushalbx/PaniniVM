package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 7.2.107: final s → au and separate su-lopa; neither d → s nor vṛddhi. */
object AdasAuSuLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.107", text = "अदस औ सुलोपश्च",
    hindiExplanation = "अदस् के अन्त्य सकार को औ आदेश तथा परवर्ती सु का लोप होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 2, optional = false, kramaValue = 720107,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    blocks = setOf("7.2.102"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        val suffix = context.terms.last()
        return stem.kind == TermKind.PRATIPADIKA && stem.upadesha == "अदस्" && stem.varnas.lastOrNull() == Vyanjana.SA &&
            suffix.sourceSupAffix == SupAffix.SU && suffix.hasCurrentAffix(SupAffix.SU)
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val stem = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        val changed = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.AU), sutra)
        val current = changed.terms.first { it.id == stem.id }
        return DerivationChange(changed.replaceTerm(stem.id,
            current.copy(establishedBySutras = current.establishedBySutras + sutra))
            .removeTerm(suffix.id, sutra).copy(stage = DerivationStage.ANGAKARYA),
            "7.2.107 substitutes au for final s and elides the complete su affix; d and preceding a remain.")
    }
}
