package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.ItMarker
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

private val khay = Pratyahara(Vyanjana.KHA, ItMarker.Y)
private val pum = "पुम्".toVarnas()
private val nrn = "नॄन्".toVarnas()
private val kan = "कान्".toVarnas()

/** A pada-final word [word] before a following term whose varṇas satisfy [following]. */
private fun DerivationState.padaFinalBefore(word: List<Varna>, following: (DerivationTerm, DerivationTerm) -> Boolean): DerivationTerm? {
    for (index in 0 until terms.lastIndex) {
        val left = terms[index]
        val right = terms[index + 1]
        if (left.varnas.filter { it != Ayogavaha.ANUSVARA } != word || !isPadaFinalVarna(left, left.varnas.lastIndex)) continue
        if (following(left, right)) return left
    }
    return null
}

/** 8.3.6 पुमः खय्यम्परे — पुम् takes रु before a खय् followed by an अम्: पुम् + कोकिलः → पुंस्कोकिलः. */
internal fun pumRutvaTarget(state: DerivationState) = state.padaFinalBefore(pum) { _, right ->
    val first = right.varnas.getOrNull(0)
    val second = right.varnas.getOrNull(1)
    first != null && second != null && Ashtadhyayi.pratyaharaEngine.contains(khay, first) &&
        Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AM, second)
}

/** 8.3.10 नॄन् पे — नॄन् takes रु before प: नॄन् + पाहि → नॄँः पाहि. */
internal fun nrnRutvaTarget(state: DerivationState) = state.padaFinalBefore(nrn) { _, right ->
    right.varnas.firstOrNull() == Vyanjana.PA
}

/** 8.3.12 कानाम्रेडिते — कान् takes रु before its āmreḍita (the repeated कान्, 8.1.2): कांस्कान्. */
internal fun kanRutvaTarget(state: DerivationState) = state.padaFinalBefore(kan) { left, right ->
    right.varnas == kan
}

/** The sources of रु in 8.3.6–8.3.12 that 8.3.2 and 8.3.4 also see. */
internal fun otherRutvaTarget(state: DerivationState): DerivationTerm? =
    pumRutvaTarget(state) ?: nrnRutvaTarget(state) ?: kanRutvaTarget(state)

object PumahKhayyamPareSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.6", text = "पुमः खय्यम्परे",
    hindiExplanation = "अम्-पर खय् परे होने पर पुम् को रु होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830006,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = pumRutvaTarget(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(pumRutvaTarget(context))
        return DerivationChange(context.substituteFinalVarnaWithRutva(term.id, sutra),
            "8.3.6 substitutes रुँ for the म् of पुम् before khay followed by am.")
    }
}

object NrnPeSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.10", text = "नॄन् पे",
    hindiExplanation = "पकार परे होने पर नॄन् के न् को रु होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830010,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = nrnRutvaTarget(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(nrnRutvaTarget(context))
        return DerivationChange(context.substituteFinalVarnaWithRutva(term.id, sutra),
            "8.3.10 substitutes रुँ for the न् of नॄन् before प.")
    }
}

object KanAmreditaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.12", text = "कानाम्रेडिते",
    hindiExplanation = "आम्रेडित परे होने पर कान् के न् को रु होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830012,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState) = kanRutvaTarget(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(kanRutvaTarget(context))
        return DerivationChange(context.substituteFinalVarnaWithRutva(term.id, sutra),
            "8.3.12 substitutes रुँ for the न् of कान् before its āmreḍita.")
    }
}

/**
 * Vārttika संपुंकानां सो वक्तव्यः (on 8.3.5): the visarga that the रु of सम्, पुम् and कान् became is
 * replaced by स्, not by the ᳵ/ᳶ of 8.3.37: पुंस्कोकिलः, कांस्कान्. It acts on the visarga of 8.3.15, so it
 * is ordered with 8.3.34 (the स् of visarga), not at 8.3.5, in the Tripādī sequence.
 */
object SampumkanamSoVaktavyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.5-v1", text = "संपुंकानां सो वक्तव्यः",
    hindiExplanation = "सम्, पुम्, कान् के रु से बने विसर्ग को सकार होता है (वार्त्तिक)।",
    type = SutraType.APAVADA, chapter = 8, pada = 3, optional = false, kramaValue = 830034,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA,
    blocks = setOf("8.3.34", "8.3.35", "8.3.36", "8.3.37", "8.3.48"),
), DerivationSutra {
    private val sources = setOf("8.3.5", "8.3.6", "8.3.12")
    private fun target(state: DerivationState) = state.terms.dropLast(1).firstOrNull { term ->
        term.varnas.lastOrNull() == Ayogavaha.VISARGA &&
            state.substitutions.any { it.targetId == term.id && it.sutra in sources }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(term.id, term.varnas.lastIndex, listOf(Vyanjana.SA), sutra),
            "संपुंकानां सो वक्तव्यः replaces the visarga of ${term.surface} with स्.")
    }
}
