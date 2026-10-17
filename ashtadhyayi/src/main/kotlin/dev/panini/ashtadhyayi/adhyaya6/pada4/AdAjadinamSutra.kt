package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

object AdAjadinamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.72", text = "आडजादीनाम्",
    hindiExplanation = "लुङ्, लङ्, लृङ् में अजादि अङ्ग को उदात्त आट् आगम होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 4, optional = false, kramaValue = 640072,
    role = SutraRole.Apavada, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    priority = SutraPriority.APAVADA, blocks = setOf("6.4.71"),
), DerivationSutra {
    private fun target(state: DerivationState): DerivationTerm? = state.terms.firstOrNull { it.kind == TermKind.DHATU }
        ?.takeIf { it.varnas.firstOrNull() is Svara }
    override fun matches(context: DerivationState): Boolean {
        if (!pastAugmentEnvironment(context)) return false
        val root = target(context) ?: return false
        val augment = context.allEffectiveTerms.firstOrNull { it.id == "at-agama" } ?: return true
        return augment in context.terms && augment.kind == TermKind.AGAMA && augment.upadesha == "अट्" &&
            augment.createdBySutra == "6.4.71" && augment.augmentTargetId == root.id
    }
    override fun apply(context: DerivationState): DerivationChange {
        val old = context.terms.firstOrNull { it.id == "at-agama" }
            ?: return introducePastAugment(context, "आट्", sutra)
        val replaced = context.replaceWholeAffix(old.id, listOf(Svara.AA, Vyanjana.TTA), sutra,
            WholeAffixDesignationPolicy.FreshUpadesha, upadesha = "आट्")
        val fresh = replaced.terms.single { it.id == old.id }
        return DerivationChange(replaced.replaceTerm(old.id, fresh.copy(
            establishedBySutras = old.establishedBySutras - "1.1.46",
            grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, sutra)))),
            "6.4.72 replaces aṭ with fresh āṭ when the verbal base becomes vowel-initial.")
    }
}
