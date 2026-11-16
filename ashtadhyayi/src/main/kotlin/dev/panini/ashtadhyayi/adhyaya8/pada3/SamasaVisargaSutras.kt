package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.ganapatha.GanaPatha
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/** The left word is a non-final member of a compound, not itself an uttarapada (अनुत्तरपदस्थ). */
private fun DerivationState.isPurvapadaBoundary() =
    DerivationalEnvironment.SAMASA_PURVAPADA in context.environments

private fun DerivationState.replaceVisarga(left: DerivationTerm, by: Varna, sutra: String) =
    replaceTermVarna(left.id, left.varnas.lastIndex, listOf(by), sutra)

private fun DerivationState.followingTerm(left: DerivationTerm): DerivationTerm? =
    terms.dropWhile { it.id != left.id }.drop(1).firstOrNull { it.varnas.isNotEmpty() }

/**
 * 8.3.45 नित्यं समासेऽनुत्तरपदस्थस्य — in a compound, the visarga of an इस्/उस् word that is not in
 * an uttarapada becomes ष् before ku/pu without option: सर्पिष्कुण्डिका, धनुष्कपालम्. Where the स् is
 * not the इस्/उस् affix, 8.3.41 gives the same fixed ष्, so the form needs no guess of the affix.
 */
object NityamSamaseSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.45", text = "नित्यं समासेऽनुत्तरपदस्थस्य",
    hindiExplanation = "समास में उत्तरपद में न स्थित इस्-उस् के विसर्ग को कवर्ग-पवर्ग परे नित्य षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830045,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private fun target(state: DerivationState) = if (!state.isPurvapadaBoundary()) null else
        state.ownSVisargaBeforeKupu()?.takeIf { it.varnas.getOrNull(it.varnas.lastIndex - 1) in setOf(Svara.I, Svara.U) }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, Vyanjana.SSA, sutra),
            "8.3.45 replaces the visarga of the pūrvapada ${left.surface} with ष् before ku/pu.")
    }
}

/**
 * 8.3.46 अतः कृकमिकंसकुम्भपात्रकुशाकर्णीष्वनव्ययस्य — in a compound, the visarga after अ of a
 * non-avyaya pūrvapada becomes स् before (forms of) कृ, कम्, and कंस, कुम्भ, पात्र, कुशा, कर्णी:
 * अयस्कुम्भः, अयस्पात्रम्. A form of कृ/कम् is known only by the dhātu it was derived from
 * (`sourceDhatuId`); an avyaya (स्वरादि, 1.1.37) such as अधस् is excluded.
 */
object AtahKrkamiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.46", text = "अतः कृकमिकंसकुम्भपात्रकुशाकर्णीष्वनव्ययस्य",
    hindiExplanation = "समास में अकार से परे अनव्यय के विसर्ग को कृ, कम्, कंस, कुम्भ, पात्र, कुशा, कर्णी परे सकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830046,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private val nouns = setOf("कंस", "कुम्भ", "पात्र", "कुशा", "कर्णी")
    private val roots = setOf("डुकृञ्", "कमुँ")
    private fun isUttarapada(term: DerivationTerm) = term.upadesha in nouns ||
        term.sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha } in roots
    private fun target(state: DerivationState): DerivationTerm? {
        if (!state.isPurvapadaBoundary()) return null
        val left = state.ownSVisargaBeforeKupu() ?: return null
        if (left.varnas.getOrNull(left.varnas.lastIndex - 1) != Svara.A) return null
        if (GanaPatha.isEligibleMember(2, left.upadesha, left.lexicalUses)) return null
        val right = state.followingTerm(left) ?: return null
        return left.takeIf { isUttarapada(right) }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, Vyanjana.SA, sutra),
            "8.3.46 replaces the visarga of ${left.surface} with स् before कृ, कम्, कंस, कुम्भ, पात्र, कुशा or कर्णी.")
    }
}

/** 8.3.47 अधःशिरसी पदे — in a compound, the visarga of अधस् and शिरस् becomes स् before पद: अधस्पदम्, शिरस्पदम्. */
object AdhahShirasiPadeSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.47", text = "अधःशिरसी पदे",
    hindiExplanation = "समास में अधस् और शिरस् के विसर्ग को पद परे सकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830047,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private val words = setOf("अधः".toVarnas(), "शिरः".toVarnas())
    private fun target(state: DerivationState): DerivationTerm? {
        if (!state.isPurvapadaBoundary()) return null
        val left = state.ownSVisargaBeforeKupu()?.takeIf { it.varnas in words } ?: return null
        return left.takeIf { state.followingTerm(left)?.upadesha == "पद" }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, Vyanjana.SA, sutra),
            "8.3.47 replaces the visarga of ${left.surface} with स् before पद.")
    }
}

/**
 * 8.3.48 कस्कादिषु च — in the words of the कस्कादि gaṇa the visarga before ku/pu becomes स् (ष् after
 * इण्, as listed): कः + कः → कस्कः, भ्रातुः + पुत्रः → भ्रातुष्पुत्रः. The gaṇa's own spelling decides;
 * a word not listed is left to the general rules.
 */
object KaskadishuCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.48", text = "कस्कादिषु च",
    hindiExplanation = "कस्कादि गण के शब्दों में विसर्ग को कवर्ग-पवर्ग परे सकार (इण् के बाद षकार) होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830048,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private const val KASKADI = 256
    private fun target(state: DerivationState): Pair<DerivationTerm, Varna>? {
        for (index in 0 until state.terms.lastIndex) {
            val left = state.terms[index]
            val right = state.followingTerm(left) ?: continue
            if (left.varnas.lastOrNull() != Ayogavaha.VISARGA || right.varnas.first() !in kupuStops) continue
            if (state.isBlockedAtBoundary(sutra, left.id, right.id)) continue
            val tail = state.terms.drop(index + 1).flatMap { it.varnas }
            for (sibilant in listOf(Vyanjana.SA, Vyanjana.SSA)) {
                val word = (left.varnas.dropLast(1) + sibilant + tail).toDevanagari()
                if (GanaPatha.isEligibleMember(KASKADI, word)) return left to sibilant
            }
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, sibilant) = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, sibilant, sutra),
            "8.3.48 gives ${listOf(sibilant).toDevanagari()} for the visarga of ${left.surface} as the कस्कादि word has it.")
    }
}

/**
 * 8.3.38 सोऽपदादौ and 8.3.39 इणः षः — before a ku/pu that does not begin a pada, the visarga becomes
 * स्, and ष् after इण्. By the vārttika (पाशकल्पककाम्येष्विति वक्तव्यम्) only before the affixes
 * पाशप्, कल्पप्, क (कप्/कन्) and काम्यच्: पयस्पाशम्, पयस्काम्यति, सर्पिष्काम्यति.
 */
private val apadadiAffixes = setOf("पाशप्", "कल्पप्", "कप्", "कन्", "काम्यच्")

private fun DerivationState.visargaBeforeApadadiKupu(): DerivationTerm? = (0 until terms.lastIndex).firstNotNullOfOrNull { index ->
    val left = terms[index]
    val right = terms[index + 1]
    left.takeIf {
        right.kind == TermKind.PRATYAYA && right.upadesha in apadadiAffixes &&
            left.varnas.lastOrNull() == Ayogavaha.VISARGA && right.varnas.firstOrNull() in kupuStops
    }
}

private fun DerivationState.afterIn(left: DerivationTerm) =
    left.varnas.getOrNull(left.varnas.lastIndex - 1)?.let { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IN, it) } == true

object SoPadadauSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.38", text = "सोऽपदादौ",
    hindiExplanation = "पद के आदि में न होने वाले कवर्ग-पवर्ग (पाश, कल्प, क, काम्य) परे विसर्ग को सकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830038,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private fun target(state: DerivationState) = state.visargaBeforeApadadiKupu()?.takeIf { !state.afterIn(it) }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, Vyanjana.SA, sutra),
            "8.3.38 replaces the visarga of ${left.surface} with स् before a non-initial ku/pu.")
    }
}

object InahShahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.39", text = "इणः षः",
    hindiExplanation = "इण् से परे विसर्ग को अपदादि कवर्ग-पवर्ग परे षकार होता है।",
    type = SutraType.APAVADA, chapter = 8, pada = 3, optional = false, kramaValue = 830039,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37", "8.3.38"),
), DerivationSutra {
    private fun target(state: DerivationState) = state.visargaBeforeApadadiKupu()?.takeIf { state.afterIn(it) }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceVisarga(left, Vyanjana.SSA, sutra),
            "8.3.39 replaces the visarga of ${left.surface} with ष् after इण् before a non-initial ku/pu.")
    }
}
