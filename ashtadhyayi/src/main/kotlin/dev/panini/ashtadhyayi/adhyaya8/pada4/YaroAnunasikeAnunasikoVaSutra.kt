package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

internal data class YarNasalTarget(val term: DerivationTerm, val replacement: NasalParasavarna)

/**
 * A pada-final yar before an anunāsika. With [inPratyaya], only when that anunāsika begins a pratyaya,
 * which is the domain of the vārttika; the following term's kind decides it, never its spelling.
 */
internal fun yarNasalTarget(context: DerivationState, inPratyaya: Boolean): YarNasalTarget? {
    for ((index, term) in context.terms.withIndex()) {
        if (!context.isPadaFinalVarna(term, term.varnas.lastIndex)) continue
        val source = term.phonologicalText.effectiveVarnas.lastOrNull() ?: continue
        if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAR, source.varna)) continue
        val replacement = source.varna.nasalParasavarna() ?: continue
        if (source.varna == replacement.varna && source.nasalized == replacement.nasalized) continue
        val followingTerm = context.terms.drop(index + 1)
            .firstOrNull { it.phonologicalText.effectiveVarnas.isNotEmpty() } ?: continue
        val follower = followingTerm.phonologicalText.effectiveVarnas.first()
        val nasalStop = (follower.varna as? Vyanjana)?.sthana?.contains(Sthana.NASIKA) == true
        if (!nasalStop && !follower.nasalized) continue
        if (inPratyaya && followingTerm.kind != TermKind.PRATYAYA) continue
        return YarNasalTarget(term, replacement)
    }
    return null
}

/** Optional external-pada nasal assimilation. The suffix-specific obligatory
 * varttika has a separate domain and is not inferred from a following spelling.
 */
object YaroAnunasikeAnunasikoVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.45", text = "यरोऽनुनासिकेऽनुनासिको वा",
    hindiExplanation = "पदान्त यर् का अनुनासिक परे होने पर विकल्प से अनुनासिक आदेश होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840045,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = yarNasalTarget(context, inPratyaya = false) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (term, replacement) = requireNotNull(yarNasalTarget(context, inPratyaya = false))
        return DerivationChange(
            context.replaceTermVarna(term.id, term.varnas.lastIndex, listOf(replacement.varna), sutra,
                nasalizedReplacement = replacement.nasalized),
            "8.4.45 optionally replaces pada-final yar with its nasal counterpart before an anunāsika.",
        )
    }
}

/**
 * Vārttika on 8.4.45: यरोऽनुनासिके प्रत्यये भाषायां नित्यम्. Before an anunāsika that begins a pratyaya the
 * substitution is obligatory: षड् + णाम् → षण्णाम्, वाक् + मय → वाङ्मय.
 */
object YaroAnunasikePratyayeNityamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.45-v1", text = "यरोऽनुनासिके प्रत्यये भाषायां नित्यम्",
    hindiExplanation = "अनुनासिकादि प्रत्यय परे होने पर लौकिक भाषा में यर् को नित्य अनुनासिक होता है (वार्त्तिक)।",
    type = SutraType.APAVADA, chapter = 8, pada = 4, optional = false, kramaValue = 840045,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA,
    // The suffix lies inside the word, so this is word-internal phonology after 8.4.41 ष्टुत्व, not pada-boundary sandhi.
    scope = SutraScope.VARNA, stage = SutraStage.THUK_PHONOLOGY, priority = SutraPriority.APAVADA, blocks = setOf("8.4.45"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = yarNasalTarget(context, inPratyaya = true) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (term, replacement) = requireNotNull(yarNasalTarget(context, inPratyaya = true))
        return DerivationChange(
            context.replaceTermVarna(term.id, term.varnas.lastIndex, listOf(replacement.varna), sutra,
                nasalizedReplacement = replacement.nasalized),
            "यरोऽनुनासिके प्रत्यये भाषायां नित्यम्: obligatory nasal before a pratyaya-initial anunāsika.",
        )
    }
}
