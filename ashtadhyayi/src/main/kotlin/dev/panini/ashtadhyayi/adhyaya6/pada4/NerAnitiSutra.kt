package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.SanadiAffix
import dev.panini.core.KrtAffix
import dev.panini.derivation.matchesAffix
import dev.panini.derivation.matchesAnyAffix
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.derivation.consumeAffixForDrop
import dev.panini.shiksha.Svara
import dev.panini.shiksha.isDirgha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.4.51 णेरनिटि. Deletes णि in the supported aniṭ ārdhadhātuka environments. */
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
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras ||
            !HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context)
        ) return false
        val nicIndex = context.terms.indexOfFirst { it.matchesAffix(SanadiAffix.NIC) && it.surface == "इ" }
        if (nicIndex < 0) return false
        val following = context.terms.drop(nicIndex + 1).firstOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val vowelInitialAfterItProcessing = following.varnas.firstOrNull() is Svara ||
            following.matchesAnyAffix(KrtAffix.GHAN, KrtAffix.LYUT)
        // ल्यप् is consonant-initial but aniṭ. The heavy preceding syllable
        // avoids the distinct light-syllable अय् replacement of 6.4.56, which
        // is not yet implemented here.
        val base = context.terms.take(nicIndex).lastOrNull { it.kind == TermKind.DHATU }
        val vowelIndex = base?.varnas?.indexOfLast { it is Svara } ?: -1
        val heavyLyap = following.matchesAffix(KrtAffix.LYAP) && vowelIndex >= 0 &&
            ((base!!.varnas[vowelIndex] as Svara).isDirgha || base.varnas.size - vowelIndex - 1 >= 2)
        return (vowelInitialAfterItProcessing || heavyLyap) && context.terms.none { it.id == "it-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nic = context.terms.first { it.matchesAffix(SanadiAffix.NIC) && it.surface == "इ" }
        return DerivationChange(
            state = context.copy(
                terms = context.terms.filterNot { it.id == nic.id },
                droppedTerms = context.droppedTerms + consumeAffixForDrop(nic, sutra),
                stage = DerivationStage.ANGAKARYA,
            ),
            explanation = "6.4.51 deletes the णि ending before a supported aniṭ ārdhadhātuka suffix.",
        )
    }
}
