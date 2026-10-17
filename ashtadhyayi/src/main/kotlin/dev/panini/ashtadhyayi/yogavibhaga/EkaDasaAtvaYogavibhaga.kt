package dev.panini.ashtadhyayi.yogavibhaga

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*
import dev.panini.sutra.runtime.SutraSource

/** Nyāsa on 6.3.46 explicitly derives eka + daśa āttva from the division `āt`. */
object EkaDasaAtvaYogavibhaga : Sutra<DerivationState, DerivationChange>(
    number = "yogavibhaga:6.3.46:eka-dasa-atva", text = "आत्",
    hindiExplanation = "न्यासोक्त योगविभाग से एक और दश के संख्यासमास में आत्त्व होता है।",
    type = SutraType.UTSARGA, chapter = 6, pada = 3, optional = false, kramaValue = 630046,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override val source = SutraSource.Yogavibhaga("6.3.46", text, "काशिकाविवरणपञ्जिका (न्यास)",
        "https://sa.wikisource.org/wiki/काशिकाविवरणपञ्जिका/षष्ठोऽध्यायः/तृतीयः_पादः")

    override fun matches(context: DerivationState): Boolean {
        val left = context.terms.getOrNull(0) ?: return false
        val right = context.terms.getOrNull(1) ?: return false
        return left.compositionDomain == TermCompositionDomain.SANKHYA &&
            right.compositionDomain == TermCompositionDomain.SANKHYA &&
            left.varnas == listOf(Svara.E, Vyanjana.KA, Svara.A) &&
            right.varnas == listOf(Vyanjana.DA, Svara.A, Vyanjana.SHA, Svara.A)
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val left = context.terms.first()
        return DerivationChange(context.replaceTermVarna(left.id, 2, listOf(Svara.AA), sutra),
            "Nyāsa's 6.3.46 yogavibhāga substitutes ā for the final a of eka before daśa in numeral composition.")
    }
}
