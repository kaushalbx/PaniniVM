package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.derivation.consumeAffixForDrop
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.4.51 णेरनिटि. Deletes णि before a vowel-initial ārdhadhātuka suffix. */
object NerAnitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.51",
    text = "णेरनिटि",
    hindiExplanation = "अनिट् आर्धधातुक प्रत्यय परे होने पर णि का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640051,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
    dependencies = setOf("6.4.1"),
    blocks = setOf("7.3.84"),
), DerivationSutra {
    private val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')

    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras ||
            !HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context)
        ) return false
        val nicIndex = context.terms.indexOfFirst { it.matchesUpadesha("णिच्") && it.surface == "इ" }
        if (nicIndex < 0) return false
        val following = context.terms.drop(nicIndex + 1).firstOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val vowelInitialAfterItProcessing = following.surface.firstOrNull() in vowels ||
            following.matchesUpadesha("घञ्") || following.matchesUpadesha("ल्युट्")
        return vowelInitialAfterItProcessing && context.terms.none { it.id == "it-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nic = context.terms.first { it.matchesUpadesha("णिच्") && it.surface == "इ" }
        return DerivationChange(
            state = context.copy(
                terms = context.terms.filterNot { it.id == nic.id },
                droppedTerms = context.droppedTerms + consumeAffixForDrop(nic, sutra),
                stage = DerivationStage.ANGAKARYA,
            ),
            explanation = "6.4.51 deletes the णि ending before a vowel-initial aniṭ ārdhadhātuka suffix.",
        )
    }
}
