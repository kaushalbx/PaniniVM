package dev.panini.ashtadhyayi.adhyaya7.pada2

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

/** 7.2.103: किमः कः — क substitutes for किम् before a case affix. */
object KimahKahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.103",
    text = "किमः कः",
    hindiExplanation = "विभक्ति परे किम् के स्थान पर क आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720103,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val hasSup = context.terms.last().id.startsWith("sup-") || context.droppedTerms.any { it.id.startsWith("sup-") }
        return hasSup && stem.upadesha == "किम्" && stem.surface == "किम्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            state = context.substituteTermSurface(stem.id, "क", 'म', "क", sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.103 substitutes क for किम् before a case affix.",
        )
    }
}
