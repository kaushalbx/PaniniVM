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

/**
 * 7.2.84: aṣṭana ā vibhaktau.
 * Substitutes 'ā' at the end of the stem 'aṣṭan' before a case affix.
 */
object AstanAaVibhaktuSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.84",
    text = "अष्टन आ विभक्तौ",
    hindiExplanation = "विभक्ति परे होने पर 'अष्टन्' शब्द के नकार के स्थान पर आकार (आ) आदेश होता है।",
    type = SutraType.VIBHASHA,
    chapter = 7,
    pada = 2,
    optional = true,
    kramaValue = 720084,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.surface != "अष्टन्") return false

        return affix.id.startsWith("sup-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val replacement = "अष्टा"

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = replacement))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.84: Substituted 'aṣṭā' for '${stem.surface}' before case affix."
        )
    }
}
