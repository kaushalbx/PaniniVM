package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 6.4.7 lengthens the phonological upadhā of an n-final aṅga before nām. */
object NopadhayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.7", text = "नोपधायाः",
    hindiExplanation = "नकारान्त अङ्ग की उपधा को नाम् परे दीर्घ होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640007,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        return stem.kind == TermKind.PRATIPADIKA && stem.varnas.lastOrNull() == Vyanjana.NA &&
            (stem.varnas.getOrNull(stem.varnas.lastIndex - 1) as? Svara)?.isHrasva == true &&
            context.hasNutAugmentedGenitivePlural(context.terms.last())
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val stem = context.terms[context.terms.lastIndex - 1]
        val index = stem.varnas.lastIndex - 1
        val vowel = stem.varnas[index] as Svara
        return DerivationChange(context.replaceTermVarna(stem.id, index, listOf(vowel.toDirgha()), sutra)
            .copy(stage = DerivationStage.ANGAKARYA), "6.4.7 lengthens the exact upadhā before nuṭ-augmented ām, retaining final n.")
    }
}
