package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/** Finds the root directly before one of the named vikaraṇas, once its varṇas match a listed root. */
private fun DerivationState.rootBefore(roots: Collection<List<Varna>>, vikaranas: Set<String>): DerivationTerm? {
    val index = terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
    if (index < 0) return null
    val root = terms[index]
    val following = terms.getOrNull(index + 1) ?: return null
    return root.takeIf { it.varnas in roots && vikaranas.any(following::matchesUpadesha) }
}

private fun DerivationState.notYetApplied(root: DerivationTerm, sutra: String) =
    substitutions.none { it.targetId == root.id && it.sutra == sutra }

private fun lengthen(context: DerivationState, root: DerivationTerm, sutra: String): DerivationState {
    val index = root.varnas.indexOfFirst { it is Svara }
    val long = when (root.varnas[index]) {
        Svara.A -> Svara.AA
        Svara.I -> Svara.II
        Svara.U -> Svara.UU
        else -> root.varnas[index]
    }
    return context.replaceTermVarna(root.id, index, listOf(long), sutra)
}

/** 7.3.74: शमामष्टानां दीर्घः श्यनि. शाम्यति, क्लाम्यति, माद्यति. */
object ShamamAshtanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.74", text = "शमामष्टानां दीर्घः श्यनि",
    hindiExplanation = "शम् आदि आठ धातुओं को श्यन् परे दीर्घ होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730074, role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val roots = listOf("शम्", "तम्", "दम्", "श्रम्", "भ्रम्", "क्षम्", "क्लम्", "मद्").map { it.toVarnas() }
    override fun matches(context: DerivationState) =
        context.rootBefore(roots, setOf("श्यन्"))?.let { context.notYetApplied(it, sutra) } == true
    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.rootBefore(roots, setOf("श्यन्")))
        return DerivationChange(lengthen(context, root, sutra), "7.3.74 lengthens the vowel of the root before श्यन्.")
    }
}

/** 7.3.75 ष्ठिवुक्लम्याचमां शिति, and 7.3.76 क्रमः परस्मैपदेषु: ष्ठीवति, क्रामति. */
object ShitiDirghaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.75", text = "ष्ठिवुक्लम्याचमां शिति",
    hindiExplanation = "ष्ठिव्, क्लम् और क्रम् (परस्मैपद) को शित् परे दीर्घ होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730075, role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    // क्रम् is listed as parasmaipada in this Dhātupāṭha, so 7.3.76's pada condition holds for every entry.
    private val roots = listOf("ष्ठिव्", "ष्ठिवु", "क्रम्").map { it.toVarnas() }
    override fun matches(context: DerivationState) =
        context.rootBefore(roots, setOf("शप्", "श्यन्"))?.let { context.notYetApplied(it, sutra) } == true
    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.rootBefore(roots, setOf("शप्", "श्यन्")))
        return DerivationChange(lengthen(context, root, sutra), "7.3.75-76 lengthen the vowel of the root before a śit vikaraṇa.")
    }
}

/** 7.3.79: ज्ञाजनोर्जा. जानाति, जायते. */
object JnaJanorJaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.79", text = "ज्ञाजनोर्जा",
    hindiExplanation = "ज्ञा और जन् को शित् परे जा आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730079, role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val roots = listOf("ज्ञा", "जन्").map { it.toVarnas() }
    private val ja = "जा".toVarnas()
    override fun matches(context: DerivationState) =
        context.rootBefore(roots, setOf("श्ना", "श्यन्"))?.let { context.notYetApplied(it, sutra) } == true
    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.rootBefore(roots, setOf("श्ना", "श्यन्")))
        return DerivationChange(context.replaceTermVarnaRange(root.id, 0, root.varnas.size, ja, emptyMap(), sutra),
            "7.3.79 replaces the root by जा before a śit vikaraṇa.")
    }
}

/** 7.1.59: शे मुचादीनाम्. मुञ्चति, लुम्पति, सिञ्चति, विन्दति, लिम्पति, कृन्तति, खिन्दति, पिंशति. */
object MucadiNumSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.59", text = "शे मुचादीनाम्",
    hindiExplanation = "मुच् आदि धातुओं को श परे नुम् होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false,
    kramaValue = 710059, role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val stems = mapOf("मुच्" to "मुञ्च्", "लुप्" to "लुम्प्", "विद्" to "विन्द्", "लिप्" to "लिम्प्",
        "सिच्" to "सिञ्च्", "कृत्" to "कृन्त्", "खिद्" to "खिन्द्", "पिश्" to "पिंश्")
        .map { (root, stem) -> root.toVarnas() to stem.toVarnas() }.toMap()
    override fun matches(context: DerivationState) =
        context.rootBefore(stems.keys, setOf("श"))?.let { context.notYetApplied(it, sutra) } == true
    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.rootBefore(stems.keys, setOf("श")))
        return DerivationChange(context.replaceTermVarnaRange(root.id, 0, root.varnas.size, stems.getValue(root.varnas), emptyMap(), sutra),
            "7.1.59 adds the nasal before the root's final consonant before श.")
    }
}

