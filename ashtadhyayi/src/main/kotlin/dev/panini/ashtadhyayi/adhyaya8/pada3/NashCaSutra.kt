package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

internal fun dhutTarget(state: DerivationState, final: Vyanjana, sutra: String): DerivationTerm? {
    for (index in 0 until state.terms.lastIndex) {
        val left = state.terms[index]
        val right = state.terms[index + 1]
        if (left.varnas.lastOrNull() != final || !state.isPadaFinalVarna(left, left.varnas.lastIndex)) continue
        if (right.varnas.firstOrNull() != Vyanjana.SA || sutra in right.establishedBySutras) continue
        if (state.samjnas.none { it.targetId == right.id && it.samjna == Samjna.PADA }) continue
        return right
    }
    return null
}

internal fun introduceDhut(context: DerivationState, right: DerivationTerm, sutra: String): DerivationChange {
    val upadesha = "धुँट्"
    val augment = DerivationTerm("${right.id}-dhut", upadesha, TermKind.AGAMA,
        upadesha = upadesha, createdBySutra = sutra,
        itProcessingPhase = ItProcessingPhase.RAW_UPADESHA, augmentTargetId = right.id)
    return DerivationChange(context.addTerm(augment),
        "$sutra optionally introduces raw dhut as the initial member of the following s-leading pada.")
}

object DahSiDhutSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.29", text = "डः सि धुट्",
    hindiExplanation = "पदान्त डकार के बाद सकार को विकल्प से धुट् आगम होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = true, kramaValue = 830029,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = dhutTarget(context, Vyanjana.DDA, sutra) != null
    override fun apply(context: DerivationState) =
        introduceDhut(context, requireNotNull(dhutTarget(context, Vyanjana.DDA, sutra)), sutra)
}

/** Dhut is a following-pada initial augment; its dh is not a new left padānta. */
object NashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.30", text = "नश्च",
    hindiExplanation = "पदान्त नकार के बाद सकार को विकल्प से धुट् आगम होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = true, kramaValue = 830030,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = dhutTarget(context, Vyanjana.NA, sutra) != null
    override fun apply(context: DerivationState) =
        introduceDhut(context, requireNotNull(dhutTarget(context, Vyanjana.NA, sutra)), sutra)
}
