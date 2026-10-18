package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.DerivationalMeaning
import dev.panini.core.TaddhitaAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.3.10 सप्तम्यास्त्रल्.
 * Prescribes tral (त्र) Avyaya-Taddhita locative affix after pronominal stems (e.g. yatra, tatra, kutra, sarvatra).
 */
object SaptamyasTralSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.10", text = "सप्तम्यास्त्रल्",
    hindiExplanation = "सप्तम्यन्त सर्वनाम पद से 'त्रल्' (त्र) अव्ययतद्धित प्रत्यय होता है।",
    type = SutraType.VIBHASHA, chapter = 5, pada = 3, optional = true, kramaValue = 530010,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.context.requestedMeaning == DerivationalMeaning.TATRA_BHAVA &&
        "5.3.1" in context.activeAdhikaras && "5.3.2" in context.activeAdhikaras &&
        context.allEffectiveTerms.any { it.kind == TermKind.PRATIPADIKA } &&
        context.allEffectiveTerms.none { it.sourceTaddhitaAffix == TaddhitaAffix.TRAL }

    override fun apply(context: DerivationState): DerivationChange {
        val tralTerm = DerivationTerm("tral", "त्रल्", TermKind.PRATYAYA, upadesha = "त्रल्",
            createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            sourceTaddhitaAffix = TaddhitaAffix.TRAL)
        return DerivationChange(
            state = context.addTerm(tralTerm),
            explanation = "5.3.10 prescribes त्रल् (त्र) locative Avyaya-Taddhita affix.",
        )
    }
}
