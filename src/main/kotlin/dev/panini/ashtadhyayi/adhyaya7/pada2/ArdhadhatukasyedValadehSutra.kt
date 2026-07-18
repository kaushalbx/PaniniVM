package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.ItStatus
import dev.panini.sutra.*

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
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val isLabhPerfectMiddle = context.effectiveContext.rupa.lakara == Lakara.LIT &&
            dhatu?.upadesha == "डुलभँष्" && ending.upadesha in setOf("थास्", "ध्वम्", "वहि", "महिङ्")
        val isNonKradiPerfect = context.effectiveContext.rupa.lakara == Lakara.LIT &&
            dhatu != null && dhatu.surface !in KrsrbhrvrstudrusrusruvoLitiSutra.KRADI_ROOTS &&
            "1.2.5" in ending.establishedBySutras
        val isTransformedLitEnding = context.effectiveContext.rupa.lakara != Lakara.LIT ||
            ending.surface in setOf(
                "अ", "अतुस्", "उस्", "अथुस्", "व", "म",
                "ए", "आते", "इरे", "से", "आथे", "ध्वे", "वहे", "महे",
            ) || (ending.surface == "थ" && ending.matchesUpadesha("सिप्"))
        val isSipLet = context.allEffectiveTerms.any { it.id == "sip-aorist" }
        return (HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) || isSipLet) &&
            (context.terms.any { it.kind == TermKind.DHATU && it.itStatus == ItStatus.SET } || isLabhPerfectMiddle || isNonKradiPerfect) &&
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
                stage = maxOf(context.stage, DerivationStage.IT_PROCESSED),
            ),
            "7.2.35 inserts इट् after the seṭ root before a consonant-initial (valādi) ārddhadhātuka affix.",
        )
    }
}
