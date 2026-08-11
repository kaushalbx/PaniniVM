package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.107: adasa au sulopaś ca.
 * In masculine and feminine, before nominative singular 'su', substitutes 'au' for the final letter of 'adas' and elides 'su'.
 * (With 8.2.80 d->s, yields 'asau').
 */
object AdasAuSulopascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.107",
    text = "अदस औ सुलोपश्च",
    hindiExplanation = "पुंल्लिङ्ग और स्त्रीलिङ्ग में प्रथमा एकवचन 'सुँ' विभक्तौ परे होने पर 'अदस्' के अन्त्य वर्ण का 'औ' और 'सुँ' का लोप होता है (निष्पन्न रूप असौ)।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720107,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val linga = context.effectiveContext.rupa.linga
        if (linga !in setOf(Linga.PUMS, Linga.STRI)) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isAdas = stem.upadesha == "अदस्" || stem.surface in setOf("अदस्", "अद")
        if (!isAdas) return false

        val isSu = affix.id == "sup-su" || affix.upadesha == "सुँ" || affix.surface == "स्"
        return isSu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val newTerms = context.terms.dropLast(1)

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = "असौ"))
                .copy(terms = newTerms, stage = DerivationStage.ANGAKARYA)
                .copy(droppedTerms = context.droppedTerms + affix.copy(surface = "")),
            explanation = "7.2.107 & 8.2.80: Substituted 'asau' for 'adas' before nominative singular su."
        )
    }
}
