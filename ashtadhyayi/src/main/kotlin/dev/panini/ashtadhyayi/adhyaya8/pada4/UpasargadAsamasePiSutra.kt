package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.4.14 उपसर्गादसमासेऽपि णोपदेशस्य — after an upasarga containing र्/ष्, the न of a dhātu taught
 * with ण (णमँ, णीञ्, णदँ …) becomes ण, even outside a compound: प्र + नमति → प्रणमति, परा + नयति → पराणयति.
 * The dhātu is known by its Dhātupāṭha upadeśa (`sourceDhatuId`), never by spelling: नन्दति (टुनदिँ) keeps न.
 * Sounds between र्/ष् and न must be अट्, कु, पु, आङ् or नुम् (8.4.2).
 */
object UpasargadAsamasePiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.14", text = "उपसर्गादसमासेऽपि णोपदेशस्य",
    hindiExplanation = "उपसर्ग में स्थित र्/ष् से परे णोपदेश धातु के न् को समास न होने पर भी ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840014,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private data class Target(val termId: String, val index: Int)

    internal fun isNopadesha(term: DerivationTerm): Boolean {
        val upadesha = term.sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha }
            ?: term.upadesha.takeIf { term.kind == TermKind.DHATU }
        return upadesha?.startsWith("ण") == true
    }

    /** The root's own initial न: the form starts with the root, or with the अट् augment and the root. */
    internal fun rootNasal(verb: DerivationTerm): Int? {
        // A form starting with the upasarga नि (प्र + निनदति) has नि's न first, not the root's.
        val root = verb.sourceDhatuId?.let { DhatuPatha.find(it) }
        if (verb.varnas.getOrNull(0) == Vyanjana.NA && verb.varnas.getOrNull(1) == dev.panini.shiksha.Svara.I &&
            root?.sourceSurface?.startsWith("नि") != true) return null
        return listOf(0, 1).firstOrNull { at ->
        (at == 0 || verb.varnas.firstOrNull() == dev.panini.shiksha.Svara.A) && verb.varnas.getOrNull(at) == Vyanjana.NA
    }
    }

    /** र्/ष् of an upasarga, then only 8.4.2 intervenors, then the न at [nasal] of the next term. */
    internal fun nasalAfterUpasarga(state: DerivationState, prefix: DerivationTerm, verb: DerivationTerm, nasal: Int?,
        requireUpasarga: Boolean = true): Int? {
        if (requireUpasarga && state.samjnas.none { it.targetId == prefix.id && it.samjna == Samjna.UPASARGA }) return null
        val trigger = prefix.varnas.indexOfLast { it == Vyanjana.RA || it == Vyanjana.SSA }
        if (trigger < 0) return null
        val between = prefix.varnas.drop(trigger + 1)
        val target = nasal ?: return null
        return target.takeIf { (between + verb.varnas.take(target)).all(::intervenes) }
    }

    internal fun intervenes(varna: Varna): Boolean =
        Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, varna) || varna in ku || varna in pu ||
            varna == dev.panini.shiksha.Ayogavaha.ANUSVARA

    private fun target(state: DerivationState): Target? = (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
        val prefix = state.terms[i]
        val verb = state.terms[i + 1]
        if (!isNopadesha(verb) || isNinsaNiksaNinda(verb)) return@firstNotNullOfOrNull null
        nasalAfterUpasarga(state, prefix, verb, rootNasal(verb))?.let { Target(verb.id, it) }
    }

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val t = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(t.termId, t.index, listOf(Vyanjana.NNA), sutra),
            "8.4.14: the न of a णोपदेश dhātu after an upasarga's र्/ष् becomes ण.")
    }

    private val ku = setOf(Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA)
    private val pu = setOf(Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA, Vyanjana.MA)
}
