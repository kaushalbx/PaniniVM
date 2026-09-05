package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.shiksha.ItStatus
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.35: आर्धधातुकस्येड्वलादेः. */
object ArdhadhatukasyedValadehSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.35", text = "आर्धधातुकस्येड्वलादेः",
    hindiExplanation = "सेट् धातु के बाद वलादि आर्धधातुक प्रत्यय से पहले इट् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false, kramaValue = 720035,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
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
        val isLabhAorist = context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            (dhatu?.upadesha?.contains("लभ") == true || dhatu?.surface?.contains("लभ") == true || dhatu?.surface?.contains("स्रम्भ") == true)
        val isTransformedLitEnding = context.effectiveContext.rupa.lakara != Lakara.LIT ||
            ending.surface in setOf(
                "अ", "अतुस्", "उस्", "अथुस्", "व", "म",
                "ए", "आते", "इरे", "से", "आथे", "ध्वे", "वहे", "महे",
            ) || ending.matchesUpadesha("सिप्")
        val isSipLet = context.allEffectiveTerms.any { it.id == "sip-aorist" }
        val isAniKtvaKtaLyap = (ending.upadesha in setOf("क्त्वा", "क्त", "क्तवतुँ", "ल्यप्") || ending.surface == "य") &&
            (dhatu?.surface in setOf("भू", "कृ", "हृ", "जि", "चि", "नी") || dhatu?.upadesha in setOf("भू", "कृ", "हृ", "जि", "चि", "नी", "भूँ", "डुकृञ्", "हृञ्", "चिञ्", "जिञ्", "नीञ्") || ending.upadesha == "ल्यप्" || ending.id == "lyap_pratyaya")
        if (isAniKtvaKtaLyap) return false

        // LIṬ is ārdhadhātuka by 3.4.114 even after the lakāra term has been
        // replaced by a freshly processed tiṅ substitution.
        val isArdhadhatuka = HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) ||
            context.effectiveContext.rupa.lakara == Lakara.LIT || isLabhAorist || isSipLet
        return isArdhadhatuka &&
            (context.terms.any { it.kind == TermKind.DHATU && (it.itStatus == ItStatus.SET || it.itStatus == ItStatus.VET) } || isLabhPerfectMiddle || isNonKradiPerfect || isLabhAorist) &&
            ending.kind == TermKind.PRATYAYA &&
            ending.surface.firstOrNull()?.let { char -> char !in vowels } == true &&
            isTransformedLitEnding &&
            context.allEffectiveTerms.none { it.id == "it-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val target = context.terms[dhatuIndex + 1]
        val itAgama = DerivationTerm(
            "it-agama", "इट्", TermKind.AGAMA,
            upadesha = "इट्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = target.id,
            mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            context.copy(
                terms = context.terms.take(dhatuIndex + 1) + itAgama + context.terms.drop(dhatuIndex + 1),
                stage = maxOf(context.stage, DerivationStage.IT_PROCESSED),
            ),
            "7.2.35 inserts इट् after the seṭ root before a consonant-initial (valādi) ārddhadhātuka affix.",
        )
    }
}
