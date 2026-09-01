package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.LetFormation
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.34: सिब्बहुलं लेटि. The request selects this optional aorist-subjunctive formation. */
object SibbahulamLetiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.34", text = "सिब्बहुलं लेटि",
    hindiExplanation = "लेट् पर होने पर धातु से बहुलतः सिप् प्रत्यय होता है।",
    type = SutraType.VIBHASHA, chapter = 3, pada = 1, optional = false, kramaValue = 310034,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LET &&
            context.effectiveContext.letFormation == LetFormation.SIP_AORIST &&
            context.terms.lastOrNull()?.id?.startsWith("ting-") == true &&
            context.allEffectiveTerms.none { it.id == "sip-aorist" }

    override fun apply(context: DerivationState): DerivationChange {
        // In this Vedic suffix इ is उच्चारणार्थ and प् is स्वरार्थ; the effective
        // suffix is therefore स् (as in जुष् + इट् + स् + अट् + त्).
        val sip = DerivationTerm(
            "sip-aorist", "सिँप्", TermKind.PRATYAYA,
            upadesha = "सिँप्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        val base = context.terms.dropLast(1).map { term ->
            // The vārttika on 3.1.34 treats सिप् as ṇit optionally in Chandas;
            // तॄ therefore has the attested vṛddhi stem तार् in तारिषत्.
            if (term.kind == TermKind.DHATU && term.upadesha == "तॄ") term.copy(surface = "तार्") else term
        }
        return DerivationChange(
            context.copy(terms = base + sip + context.terms.last()),
            "3.1.34 introduces the optional सिप् aorist suffix before the LET ending.",
        )
    }
}
