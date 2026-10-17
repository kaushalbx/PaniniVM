package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

private val prashan = listOf(Vyanjana.PA, Vyanjana.RA, Svara.A, Vyanjana.SHA, Svara.AA, Vyanjana.NA)

/** The co-prescribed 8.3.2/4 operation sees the source of the imminent ru
 * substitution (Kāśikā 8.3.2), before it is superseded by 8.3.7. */
private fun chaviRutvaTarget(context: DerivationState): DerivationTerm? {
    for (index in 0 until context.terms.lastIndex) {
        val left = context.terms[index]
        val right = context.terms[index + 1]
        if (left.varnas.lastOrNull() != Vyanjana.NA || left.upadeshaVarnas == prashan) continue
        if (!context.isPadaFinalVarna(left, left.varnas.lastIndex)) continue
        val first = right.varnas.getOrNull(0) ?: continue
        val second = right.varnas.getOrNull(1) ?: continue
        if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.CHAV, first) &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AM, second)) return left
    }
    return null
}

private fun VarnaToken.isAnunasika(): Boolean = nasalized ||
    (varna as? Vyanjana)?.sthana?.contains(Sthana.NASIKA) == true

object AtranunasikahPurvasyaTuVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.2", text = "अत्रानुनासिकः पूर्वस्य तु वा",
    hindiExplanation = "इस रुप्रकरण में रु के स्थानिन् से पूर्व वर्ण का विकल्प से अनुनासिक होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830002,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private data class Target(val term: DerivationTerm, val varna: Varna, val nasalized: Boolean)
    private fun target(context: DerivationState): Target? {
        val term = chaviRutvaTarget(context) ?: return null
        val previous = term.phonologicalText.effectiveVarnas.getOrNull(term.varnas.lastIndex - 1) ?: return null
        if (previous.isAnunasika()) return null
        if (previous.varna is Svara) return Target(term, previous.varna, true)
        val replacement = previous.varna.nasalParasavarna() ?: return null
        return Target(term, replacement.varna, replacement.nasalized)
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (term, varna, nasalized) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(term.id, term.varnas.lastIndex - 1,
            listOf(varna), sutra, nasalizedReplacement = nasalized),
            "8.3.2 optionally nasalizes the occurrence preceding the source of 8.3.7 ru.")
    }
}

object AnunasikatParoAnusvarahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.4", text = "अनुनासिकात् परोऽनुस्वारः",
    hindiExplanation = "पूर्व वर्ण के अनुनासिक न होने के पक्ष में उसके बाद अनुस्वार आगम होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830004,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? {
        val term = chaviRutvaTarget(context) ?: return null
        val previous = term.phonologicalText.effectiveVarnas.getOrNull(term.varnas.lastIndex - 1) ?: return null
        return term.takeIf { !previous.isAnunasika() && previous.varna != Ayogavaha.ANUSVARA }
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(target(context))
        return DerivationChange(context.insertTermVarnas(term.id, term.varnas.lastIndex,
            listOf(Ayogavaha.ANUSVARA), sutra),
            "8.3.4 adds anusvāra after the non-nasalized occurrence preceding the source of ru.")
    }
}

object NashChavyAprashanSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.7", text = "नश्छव्यप्रशान्",
    hindiExplanation = "प्रशान् को छोड़कर पदान्त न् को अम्-पर छव् के सामने रु आदेश होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830007,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = chaviRutvaTarget(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(chaviRutvaTarget(context))
        return DerivationChange(context.substituteFinalVarnaWithRutva(term.id, sutra),
            "8.3.7 substitutes fresh रुँ for pada-final न् before chav followed by am, except praśān.")
    }
}
