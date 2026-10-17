package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** Āp here is the Sup pratyāhāra beginning at instrumental ṭā, not feminine āp. */
private fun idamObliquePair(context: DerivationState): Pair<DerivationTerm, DerivationTerm>? {
    val suffix = context.terms.lastOrNull() ?: return null
    val source = suffix.sourceSupAffix ?: return null
    if (source.ordinal < SupAffix.TA.ordinal || suffix.itProcessingPending ||
        suffix.itDesignations.isNotEmpty() || suffix.deferredItDesignations.isNotEmpty()) return null
    // In the sarvanāman genitive plural, suṭ first establishes the actual
    // consonant-initial suffix on which the prefix operation depends.
    if (source == SupAffix.AM_6 && context.droppedTerms.none {
        it.createdBySutra == "7.1.52" && it.mergedIntoTermId == suffix.id
    }) return null
    val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return null
    if (stem.upadesha != "इदम्" || Vyanjana.KA in stem.varnas || stem.pendingBaseComposition != null) return null
    if (stem.varnas.take(2) != listOf(Svara.I, Vyanjana.MA)) return null
    if (context.substitutions.none { it.targetId == stem.id && it.sutra == "7.2.109" }) return null
    return stem to suffix
}

object AnapyakahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.112", text = "अनाप्यकः",
    hindiExplanation = "ककाररहित इदम् के इद् भाग को तृतीयादि विभक्ति परे होने पर अन् आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false,
    kramaValue = 720112, role = SutraRole.Vidhi, action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION, nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"), blocks = setOf("7.3.102", "7.3.103"),
), DerivationSutra {
    override fun matches(context: DerivationState) = idamObliquePair(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (stem, _) = requireNotNull(idamObliquePair(context))
        return DerivationChange(context.replaceTermVarnaRange(stem.id, 0, 2,
            listOf(Svara.A, Vyanjana.NA), mapOf(0 to 0), sutra)
            .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.112 replaces the retained id portion with an before an oblique case suffix.")
    }
}

object HaliLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.113", text = "हलि लोपः",
    hindiExplanation = "हलादि तृतीयादि विभक्ति परे होने पर ककाररहित इदम् के इद् भाग का लोप होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 2, optional = false,
    kramaValue = 720113, role = SutraRole.Apavada, action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION, nimittaScope = NimittaScope.BOTH,
    blocks = setOf("7.2.112", "7.3.102", "7.3.103"), dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState) =
        idamObliquePair(context)?.second?.varnas?.firstOrNull() is Vyanjana
    override fun apply(context: DerivationState): DerivationChange {
        val (stem, _) = requireNotNull(idamObliquePair(context))
        return DerivationChange(context.deleteTermVarnas(stem.id, 0, 2, sutra)
            .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.113 deletes the complete retained id portion before a consonant-initial oblique suffix.")
    }
}
