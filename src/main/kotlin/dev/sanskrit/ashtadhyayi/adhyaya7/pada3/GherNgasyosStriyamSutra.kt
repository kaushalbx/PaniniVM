package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** Feminine Ghi stem before singular ṅasi/ṅas: form the yāḥ ending. */
object GherNgasyosStriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.128",
    text = "घेर्ङसिङसोः स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग घि-अन्त में एकवचन ङसि और ङस् के परे याः/वाः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730128,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.STRI ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha in setOf("ङसि", "ङस्") &&
            stem.surface.lastOrNull() in setOf('इ', 'ि', 'ए', 'े', 'उ', 'ु', 'ओ', 'ो')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val ending = when (stem.surface.last()) {
            'इ', 'ि', 'ए', 'े' -> "्याः"
            'उ', 'ु', 'ओ', 'ो' -> "्वाः"
            else -> error("GherNgasyosStriyamSutra matched a non-ik stem")
        }
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + ending),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.128: Formed the feminine Ghi singular ङसि/ङस् त्याः ending.",
        )
    }
}
