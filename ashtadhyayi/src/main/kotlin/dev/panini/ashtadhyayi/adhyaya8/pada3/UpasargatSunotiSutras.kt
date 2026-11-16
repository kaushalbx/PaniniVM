package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * The स् that begins a verbal form after an upasarga ending in इण् (8.3.57), when the form's root is
 * one of [roots] by Dhātupāṭha upadeśa. The form must begin with the root's स्; an अट् merged into the
 * upasarga by yaṇ is not reached here.
 */
private fun DerivationState.rootInitialSa(roots: Set<String>, excluding: Set<String> = emptySet(),
    onlyAfter: Set<String>? = null): String? =
    (0 until terms.lastIndex).firstNotNullOfOrNull { i ->
        val prefix = terms[i]
        val verb = terms[i + 1]
        if (samjnas.none { it.targetId == prefix.id && it.samjna == Samjna.UPASARGA }) return@firstNotNullOfOrNull null
        if (prefix.upadesha in excluding || (onlyAfter != null && prefix.upadesha !in onlyAfter)) return@firstNotNullOfOrNull null
        val root = verb.sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha } ?: return@firstNotNullOfOrNull null
        val last = prefix.varnas.lastOrNull() ?: return@firstNotNullOfOrNull null
        if (root !in roots || !Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IN, last)) return@firstNotNullOfOrNull null
        verb.id.takeIf { verb.varnas.firstOrNull() == Vyanjana.SA }
    }

/**
 * 8.3.65 उपसर्गात्सुनोतिसुवतिस्यतिस्तौतिस्तोभतिस्थासेनयसेधसिचसञ्जस्वञ्जाम् — after an upasarga ending in इण्,
 * the स् of these roots becomes ष्: अभि + सुनोति → अभिषुणोति, नि + सिञ्चति → निषिञ्चति, अभि + स्तौति → अभिष्टौति
 * (8.4.41). स्था's present तिष्ठति does not begin with स्, so अधितिष्ठति is unchanged. सेनय (a nominal verb)
 * is not modelled.
 */
object UpasargatSunotiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.65", text = "उपसर्गात्सुनोतिसुवतिस्यतिस्तौतिस्तोभतिस्थासेनयसेधसिचसञ्जस्वञ्जाम्",
    hindiExplanation = "इण्-अन्त उपसर्ग से परे सु, सू, सो, स्तु, स्तुभ्, स्था, सेध्, सिच्, सञ्ज्, स्वञ्ज् के सकार को षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830065,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    internal val roots = setOf("षुञ्", "षूङ्", "षू", "षो", "ष्टुञ्", "ष्टुभुँ", "ष्ठा", "षिधुँ", "षिधँ", "षिचँ", "षन्जँ", "ष्वन्जँ")

    override fun matches(context: DerivationState) = context.rootInitialSa(roots) != null
    override fun apply(context: DerivationState): DerivationChange {
        val id = requireNotNull(context.rootInitialSa(roots))
        return DerivationChange(context.replaceTermVarna(id, 0, listOf(Vyanjana.SSA), sutra),
            "8.3.65: the root's स् becomes ष् after an इण्-final upasarga.")
    }
}

/** 8.3.66 सदिरप्रतेः — likewise the स् of सद् (षदॢँ), except after प्रति: निषीदति, but प्रतिसीदति. */
object SadirAprateSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.66", text = "सदिरप्रतेः",
    hindiExplanation = "प्रति को छोड़कर इण्-अन्त उपसर्ग से परे सद् धातु के सकार को षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830066,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val roots = setOf("षदॢँ")
    override fun matches(context: DerivationState) = context.rootInitialSa(roots, excluding = setOf("प्रति")) != null
    override fun apply(context: DerivationState): DerivationChange {
        val id = requireNotNull(context.rootInitialSa(roots, excluding = setOf("प्रति")))
        return DerivationChange(context.replaceTermVarna(id, 0, listOf(Vyanjana.SSA), sutra),
            "8.3.66: the स् of सद् becomes ष् after an upasarga other than प्रति.")
    }
}

/**
 * 8.3.70 परिनिविभ्यः सेवसितसयसिवुसहसुट्स्तुस्वञ्जाम् — after परि, नि, वि the स् of सेव्, सिव्, सह्, स्तु, स्वञ्ज्
 * becomes ष्: परिषेवते, निषेवते, विषहते. सित/सय (nominals) and सुट् are not modelled. The अट्-intervened
 * forms (8.3.71, optional) are not reached.
 */
object PariniviSevaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.70", text = "परिनिविभ्यः सेवसितसयसिवुसहसुट्स्तुस्वञ्जाम्",
    hindiExplanation = "परि, नि, वि से परे सेव्, सिव्, सह्, स्तु, स्वञ्ज् आदि के सकार को षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830070,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val roots = setOf("षेवृँ", "षिवुँ", "षहँ", "ष्टुञ्", "ष्वन्जँ")
    private val after = setOf("परि", "नि", "वि")
    override fun matches(context: DerivationState) = context.rootInitialSa(roots, onlyAfter = after) != null
    override fun apply(context: DerivationState): DerivationChange {
        val id = requireNotNull(context.rootInitialSa(roots, onlyAfter = after))
        return DerivationChange(context.replaceTermVarna(id, 0, listOf(Vyanjana.SSA), sutra),
            "8.3.70: the root's स् becomes ष् after परि/नि/वि.")
    }
}

/** 8.3.77 वेः स्कभ्नातेर्नित्यम् — after वि, the स् of स्कभ् (स्कभिँ) always becomes ष्: विष्कभ्नाति. */
object VehSkabhnatehSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.77", text = "वेः स्कभ्नातेर्नित्यम्",
    hindiExplanation = "वि से परे स्कभ् धातु के सकार को नित्य षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830077,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val roots = setOf("स्कभिँ")
    override fun matches(context: DerivationState) = context.rootInitialSa(roots, onlyAfter = setOf("वि")) != null
    override fun apply(context: DerivationState): DerivationChange {
        val id = requireNotNull(context.rootInitialSa(roots, onlyAfter = setOf("वि")))
        return DerivationChange(context.replaceTermVarna(id, 0, listOf(Vyanjana.SSA), sutra),
            "8.3.77: the स् of स्कभ् becomes ष् after वि.")
    }
}
