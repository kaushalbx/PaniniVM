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

/** 7.2.100: The final ṛ of tisṛ and catasṛ is replaced by r before a vowel-initial case ending. */
object AciRaRtahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.100",
    text = "अचि र ऋतः",
    hindiExplanation = "तिसृ और चतसृ अङ्ग के अन्तिम ऋ के स्थान पर स्वरादि विभक्ति के परे र् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720100,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (stem.surface !in setOf("तिसृ", "चतसृ")) return false

        // जस् and शस् expose vowel-initial अस् after their indicatory consonant is removed.
        return affix.id in setOf("sup-au", "sup-jas", "sup-am", "sup-aut", "sup-shas") ||
            affix.upadesha in setOf("औ", "जस्", "अम्", "औट्", "शस्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = stem.surface.dropLast(1) + "्र्"

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.100: Replaced the final ऋ of '${stem.surface}' with र् before a vowel-initial case ending.",
        )
    }
}
