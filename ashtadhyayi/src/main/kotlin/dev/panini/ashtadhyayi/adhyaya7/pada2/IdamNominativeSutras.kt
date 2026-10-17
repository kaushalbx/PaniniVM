package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

private fun idamBeforeSu(context: DerivationState): DerivationTerm? {
    val suffix = context.terms.lastOrNull() ?: return null
    if (suffix.sourceSupAffix != SupAffix.SU || !suffix.hasCurrentAffix(SupAffix.SU)) return null
    return context.terms.getOrNull(context.terms.lastIndex - 1)?.takeIf {
        it.kind == TermKind.PRATIPADIKA && it.upadesha == "इदम्"
    }
}

/** 7.2.108 preserves final m against tyadādi atva (Kāśikā, SK 343). */
object IdamoMahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.108", text = "इदमो मः",
    hindiExplanation = "सुँ परे होने पर इदम् के अन्त्य मकार को मकार आदेश होता है; यह अत्व का अपवाद है।",
    type = SutraType.APAVADA, chapter = 7, pada = 2, optional = false,
    kramaValue = 720108, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION, nimittaScope = NimittaScope.BOTH,
    blocks = setOf("7.2.102"), dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stem = idamBeforeSu(context) ?: return false
        return context.effectiveContext.rupa.linga != Linga.NAPUMSAKA &&
            stem.varnas.lastOrNull() == Vyanjana.MA &&
            context.substitutions.none { it.targetId == stem.id && it.sutra == sutra }
    }
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(idamBeforeSu(context))
        return DerivationChange(context.retainTermVarna(stem.id, stem.varnas.lastIndex,
            Vyanjana.MA, sutra).copy(stage = DerivationStage.ANGAKARYA),
            "7.2.108 explicitly retains final m in place of the otherwise applicable a substitution.")
    }
}

/** 7.2.110 replaces d with y in the feminine nominative singular. */
object YahSauSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.110", text = "यः सौ",
    hindiExplanation = "स्त्रीलिङ्ग में सुँ परे होने पर इदम् के दकार को यकार आदेश होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 2, optional = false,
    kramaValue = 720110, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION, nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stem = idamBeforeSu(context) ?: return false
        return context.effectiveContext.rupa.linga == Linga.STRI &&
            stem.varnas.getOrNull(1) == Vyanjana.DA &&
            context.substitutions.any { it.targetId == stem.id && it.sutra == "7.2.108" }
    }
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(idamBeforeSu(context))
        return DerivationChange(context.replaceTermVarna(stem.id, 1, listOf(Vyanjana.YA), sutra)
            .copy(stage = DerivationStage.ANGAKARYA), "7.2.110 replaces the d occurrence with y before feminine su.")
    }
}
