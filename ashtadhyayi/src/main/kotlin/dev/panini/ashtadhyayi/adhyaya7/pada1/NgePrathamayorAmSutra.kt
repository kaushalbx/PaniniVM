package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.*
import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 7.1.28 substitutes complete am after yuṣmad/asmad in ṅe and the first two cases. */
object NgePrathamayorAmSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.28", text = "ङे प्रथमयोरम्",
    hindiExplanation = "युष्मद् और अस्मद् से परे ङे तथा प्रथमा-द्वितीया विभक्तियों को अम् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 1, optional = false, kramaValue = 710028,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.14", "7.1.17", "7.1.19", "7.1.20", "7.1.23", "7.1.24"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        val suffix = context.terms.last()
        val original = suffix.sourceSupAffix ?: return false
        return stem.kind == TermKind.PRATIPADIKA && stem.personalPronounIdentity() != null &&
            (original == SupAffix.NGE || original.vibhakti in setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) &&
            sutra !in suffix.establishedBySutras
    }
    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val suffix = context.terms.last()
        val changed = context.replaceWholeAffix(suffix.id, listOf(Svara.A, Vyanjana.MA), sutra,
            WholeAffixDesignationPolicy.Consume, upadesha = SupAffix.AM.upadesha)
        val current = changed.terms.last()
        return DerivationChange(changed.replaceTerm(current.id, current.copy(establishedBySutras = current.establishedBySutras + sutra))
            .copy(stage = DerivationStage.ANGAKARYA), "7.1.28 substitutes complete am, retaining the original sup slot.")
    }
}
