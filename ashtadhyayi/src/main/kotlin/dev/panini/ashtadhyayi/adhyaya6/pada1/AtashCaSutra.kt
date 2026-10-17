package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

object AtashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.90", text = "आटश्च", hindiExplanation = "आट् से परे अच् के साथ वृद्धि एकादेश होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false, kramaValue = 610090,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.87", "6.1.88", "6.1.92", "6.1.97", "6.1.101"),
), DerivationSutra {
    private fun target(state: DerivationState): Pair<DerivationTerm, DerivationTerm>? = state.terms.zipWithNext().firstOrNull { (augment, root) ->
        augment.kind == TermKind.AGAMA && augment.upadesha == "आट्" &&
            augment.varnas == listOf(Svara.AA) && augment.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            augment.augmentTargetId == root.id && !augment.mergeIntoAugmentTarget && "1.1.46" in augment.establishedBySutras &&
            root.varnas.firstOrNull() is Svara &&
            ((augment.createdBySutra == "6.4.72" && root.kind == TermKind.DHATU) ||
                (augment.createdBySutra == "7.3.112" && root.kind == TermKind.PRATYAYA &&
                    root.hasEffectiveMarker(dev.panini.core.ItMarker.NGIT)))
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (augment, root) = requireNotNull(target(context))
        val replacement = requireNotNull(Varnamala.getVrddhi(root.varnas.first() as Svara))
        var prepared = context
        for (rule in blocks) prepared = prepared.blockAtBoundary(rule, augment.id, root.id, sutra)
        return DerivationChange(prepared.coalescePrecedingAugmentVowel(root.id, augment.id, replacement, sutra)
            .copy(stage = DerivationStage.PADA_FORMED), "6.1.90 coalesces prescribed āṭ with the following vowel as vṛddhi.")
    }
}
