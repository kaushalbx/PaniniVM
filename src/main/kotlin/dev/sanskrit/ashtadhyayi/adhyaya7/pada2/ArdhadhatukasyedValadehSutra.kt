package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.derivation.*
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.sutra.*

/** 7.2.35: आर्धधातुकस्येड्वलादेः. */
object ArdhadhatukasyedValadehSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.35", text = "आर्धधातुकस्येड्वलादेः",
    hindiExplanation = "सेट् धातु के बाद वलादि आर्धधातुक प्रत्यय से पहले इट् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false, kramaValue = 720035,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val isTransformedLitEnding = context.effectiveContext.rupa.lakara != Lakara.LIT ||
            (ending.upadesha != null && ending.surface != ending.upadesha)
        val isSipLet = context.allEffectiveTerms.any { it.id == "sip-aorist" }
        return (HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) || isSipLet) &&
            context.terms.any { it.kind == TermKind.DHATU && it.itStatus == ItStatus.SET } &&
            ending.kind == TermKind.PRATYAYA &&
            ending.surface.firstOrNull()?.let { char -> char !in vowels } == true &&
            isTransformedLitEnding &&
            context.allEffectiveTerms.none { it.id == "it-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val itAgama = DerivationTerm("it-agama", "इ", TermKind.AGAMA, upadesha = "इट्")
        return DerivationChange(
            context.copy(
                terms = context.terms.take(dhatuIndex + 1) + itAgama + context.terms.drop(dhatuIndex + 1),
                stage = DerivationStage.IT_PROCESSED,
            ),
            "7.2.35 inserts इट् after the seṭ root before a consonant-initial (valādi) ārddhadhātuka affix.",
        )
    }
}
