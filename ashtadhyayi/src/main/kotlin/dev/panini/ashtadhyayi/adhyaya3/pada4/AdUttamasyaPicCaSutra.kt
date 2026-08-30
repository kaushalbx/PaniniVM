package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.core.ItMarker
import dev.panini.core.DhatuGana
import dev.panini.core.Purusha
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.SthaniProperties
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.92: आडुत्तमस्य पिच्च. Adds आट् to first-person LOT endings. */
object AdUttamasyaPicCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.92", text = "आडुत्तमस्य पिच्च",
    hindiExplanation = "लोट् के उत्तमपुरुष प्रत्ययों के आदि में आट् आगम होता है और वे पित् माने जाते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340092,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val ganaReady = when (context.terms.firstOrNull { it.kind == TermKind.DHATU }?.gana) {
            DhatuGana.BHVADI -> context.allEffectiveTerms.any { it.upadesha == "शप्" } ||
                context.terms.first { it.kind == TermKind.DHATU }.surface.lastOrNull() !in setOf('इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ')
            DhatuGana.SVADI -> context.allEffectiveTerms.any { it.id == "shnu" }
            DhatuGana.JUHOTYADI -> context.terms.any { it.id == "abhyasa" }
            else -> true
        }
        return context.effectiveContext.rupa.lakara == Lakara.LOT &&
            context.effectiveContext.rupa.purusha == Purusha.UTTAMA &&
            ganaReady &&
            ending.upadesha in setOf("इट्", "वहि", "महिङ्") &&
            context.allEffectiveTerms.none { it.id == "lot-at-agama" } &&
            context.allEffectiveTerms.none { sutra in it.establishedBySutras } &&
            ending.surface in setOf("ए", "वहे", "महे")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val agama = DerivationTerm(
            id = "lot-at-agama",
            surface = "आट्",
            kind = TermKind.AGAMA,
            upadesha = "आट्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = ending.id,
        )
        val augmented = ending.copy(
            establishedBySutras = ending.establishedBySutras + sutra,
            sthaniProps = SthaniProperties(
                ending.sthaniProps?.upadesha ?: ending.upadesha,
                ending.sthaniProps?.itMarkers.orEmpty() + ending.itMarkers + ItMarker.P,
            ),
        )
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + agama + augmented),
            "3.4.92 supplies आट् to the LOT first-person ending and makes it pit.",
        )
    }
}
