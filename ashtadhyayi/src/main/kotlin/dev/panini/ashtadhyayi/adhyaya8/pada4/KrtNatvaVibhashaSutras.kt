package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/*
 * The optional ṇatva of 8.4.30, 8.4.31 and 8.4.33. Like the other vibhāṣā rules of this pada they are
 * declined by default and offered as alternatives; 8.4.2 and 8.4.14 leave these cases to them.
 */

/** 8.4.30 णेर्विभाषा — after णि, the kṛt न following an upasarga's र्/ष् is optionally ण: प्रयापणम्/प्रयापनम्. */
object NerVibhashaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.30", text = "णेर्विभाषा",
    hindiExplanation = "णिजन्त धातु से परे कृत् के न् को उपसर्गस्थ निमित्त से विकल्प से ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840030,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    override val appliesByDefault = false
    override fun matches(context: DerivationState) =
        AtkupvangnumvyavayePiSutra.eligible(context) && AtkupvangnumvyavayePiSutra.krtVibhasha(context) == number
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState) = listOf(AtkupvangnumvyavayePiSutra.retroflex(state, number),
        DerivationChange(state, "8.4.30 declined.", applied = false))
}

/** 8.4.31 हलश्चेजुपधात् — likewise after a consonant-initial root with an इच् penultimate: प्रकोपणम्/प्रकोपनम्. */
object HalashCejupadhatSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.31", text = "हलश्चेजुपधात्",
    hindiExplanation = "हलादि इजुपध धातु से परे कृत् के न् को विकल्प से ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840031,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    override val appliesByDefault = false
    override fun matches(context: DerivationState) =
        AtkupvangnumvyavayePiSutra.eligible(context) && AtkupvangnumvyavayePiSutra.krtVibhasha(context) == number
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState) = listOf(AtkupvangnumvyavayePiSutra.retroflex(state, number),
        DerivationChange(state, "8.4.31 declined.", applied = false))
}

/** The roots of 8.4.33 by upadeśa: णिसिँ (निंस्), णिक्षँ (निक्ष्), णिदिँ (निन्द्). */
internal fun isNinsaNiksaNinda(term: DerivationTerm): Boolean {
    val upadesha = term.sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha } ?: term.upadesha
    return upadesha in setOf("णिसिँ", "णिक्षँ", "णिदिँ")
}

/** 8.4.33 वा निंसनिक्षनिन्दाम् — for these roots the 8.4.14 ṇatva is optional: प्रणिन्दनम्/प्रनिन्दनम्. */
object VaNinsaNiksaNindamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.33", text = "वा निंसनिक्षनिन्दाम्",
    hindiExplanation = "निंस्, निक्ष् और निन्द् धातुओं के न् को उपसर्गस्थ निमित्त से विकल्प से ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840033,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState): Pair<String, Int>? = (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
        val verb = state.terms[i + 1]
        if (!isNinsaNiksaNinda(verb)) return@firstNotNullOfOrNull null
        UpasargadAsamasePiSutra.nasalAfterUpasarga(state, state.terms[i], verb, UpasargadAsamasePiSutra.rootNasal(verb))
            ?.let { verb.id to it }
    }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val (id, index) = requireNotNull(target(state))
        return listOf(
            DerivationChange(state.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
                "8.4.33: optional ṇatva of the root's न after an upasarga."),
            DerivationChange(state, "8.4.33 declined.", applied = false))
    }
}
