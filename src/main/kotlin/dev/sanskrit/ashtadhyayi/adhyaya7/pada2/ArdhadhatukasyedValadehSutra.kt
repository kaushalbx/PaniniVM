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
    override fun matches(context: DerivationState): Boolean =
        HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) &&
            context.terms.any { it.kind == TermKind.DHATU && it.itStatus == ItStatus.SET } &&
            context.terms.any { it.kind == TermKind.PRATYAYA && it.surface.firstOrNull()?.let { char -> char !in vowels } == true } &&
            context.allEffectiveTerms.none { it.id == "it-agama" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(terms = context.terms.flatMap { term ->
            if (term.kind == TermKind.DHATU) listOf(term, DerivationTerm("it-agama", "इ", TermKind.AGAMA, upadesha = "इट्")) else listOf(term)
        }, stage = DerivationStage.IT_PROCESSED),
        "7.2.35 inserts इट् after a seṭ root before a consonant-initial (valādi) ārddhadhātuka affix.",
    )
}
