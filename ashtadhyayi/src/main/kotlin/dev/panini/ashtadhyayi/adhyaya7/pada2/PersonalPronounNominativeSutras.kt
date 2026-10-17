package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.*
import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

object MaparyantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.91", text = "मपर्यन्तस्य", hindiExplanation = "आगामी आदेश मकारपर्यन्त अंश के स्थान पर होते हैं।",
    type = SutraType.ADHIKARA, chapter = 7, pada = 2, optional = false, kramaValue = 720091,
    role = SutraRole.Adhikara(endKrama = 720098), action = SutraAction.ADHIKARA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = sutra !in context.activeAdhikaras &&
        context.terms.any { it.kind == TermKind.PRATIPADIKA && it.personalPronounIdentity() != null } && context.terms.any { it.sourceSupAffix != null }
    override fun apply(context: DerivationState) = DerivationChange(context.copy(activeAdhikaras = context.activeAdhikaras + sutra),
        "7.2.91 governs the exact personal-pronoun portion through m.")
}

private fun nominativeTarget(context: DerivationState, original: SupAffix, sutra: String): DerivationTerm? {
    if ("6.4.1" !in context.activeAdhikaras || "7.2.91" !in context.activeAdhikaras) return null
    val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return null
    return stem.takeIf { it.kind == TermKind.PRATIPADIKA && it.personalPronounMaparyantaRange() != null &&
        sutra !in it.establishedBySutras && context.terms.last().sourceSupAffix == original }
}

object TvahauSauSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.94", text = "त्वाहौ सौ", hindiExplanation = "सु परे युष्मदस्मदोः मपर्यन्त अंश को त्व और अह आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false, kramaValue = 720094,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = nominativeTarget(context, SupAffix.SU, sutra) != null
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(nominativeTarget(context, SupAffix.SU, sutra))
        val replacement = if (stem.personalPronounIdentity() == PersonalPronounIdentity.YUSMAD)
            listOf(Vyanjana.TA, Vyanjana.VA, Svara.A) else listOf(Svara.A, Vyanjana.HA, Svara.A)
        return DerivationChange(context.replacePersonalPronounMaparyanta(stem.id, replacement, sutra)
            .copy(stage = DerivationStage.ANGAKARYA), "7.2.94 substitutes tva/aha only through m; the original ad remainder stays.")
    }
}

object YuyavayauJasiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.93", text = "यूयवयौ जसि", hindiExplanation = "जस् परे युष्मदस्मदोः मपर्यन्त अंश को यूय और वय आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false, kramaValue = 720093,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = nominativeTarget(context, SupAffix.JAS, sutra) != null
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(nominativeTarget(context, SupAffix.JAS, sutra))
        val replacement = if (stem.personalPronounIdentity() == PersonalPronounIdentity.YUSMAD)
            listOf(Vyanjana.YA, Svara.UU, Vyanjana.YA, Svara.A) else listOf(Vyanjana.VA, Svara.A, Vyanjana.YA, Svara.A)
        return DerivationChange(context.replacePersonalPronounMaparyanta(stem.id, replacement, sutra)
            .copy(stage = DerivationStage.ANGAKARYA), "7.2.93 substitutes yūya/vaya only through m, preserving the remaining ad.")
    }
}

/** Siddhāntakaumudī's ṭi-lopa interpretation; ā/y domains and duals are excluded. */
object SheseLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.90", text = "शेषे लोपः", hindiExplanation = "आत्व-यत्व से भिन्न शेष विभक्तियों में युष्मदस्मदोः टि का लोप होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 2, optional = false, kramaValue = 720090,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        val source = context.terms.last().sourceSupAffix ?: return false
        return stem.kind == TermKind.PRATIPADIKA && stem.personalPronounIdentity() != null &&
            stem.varnas.lastOrNull() == Vyanjana.DA && stem.varnas.any { it is Svara } &&
            source.vacana != Vacana.DVIVACANA && source.vibhakti in
            setOf(Vibhakti.PRATHAMA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI, Vibhakti.SASTHI)
    }
    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val stem = context.terms[context.terms.lastIndex - 1]
        val start = stem.varnas.indexOfLast { it is Svara }
        return DerivationChange(context.deleteTermVarnas(stem.id, start, stem.varnas.size - start, sutra)
            .copy(stage = DerivationStage.ANGAKARYA), "7.2.90 deletes the remaining ṭi portion in the specified residual cases.")
    }
}
