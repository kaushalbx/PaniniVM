package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.shiksha.toVarnas
import dev.panini.core.SanadiAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.sutra.*

/** 7.3.36: the ā-final portion of अर्तिह्रीव्लीरीक्नूयीक्ष्माय्यातां पुङ्णौ. */
object ArtihriPukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.36", text = "अर्तिह्रीव्लीरीक्नूयीक्ष्माय्यातां पुङ्णौ",
    hindiExplanation = "आकारान्त धातु को णिच् परे पुक् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730036, role = SutraRole.Vidhi, action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { it.matchesAffix(SanadiAffix.NIC) } &&
            (context.terms.any { eligibleRoot(it) } || context.terms.any { krIJi(it) }) &&
            context.allEffectiveTerms.none { it.createdBySutra == sutra }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { eligibleRoot(it) || krIJi(it) }
        // 6.1.48 क्रीङ्जीनां णौ: क्री and जि take आ, done here because the replacement rewrites the upadeśa.
        val base = if (krIJi(root)) context.replaceTermVarna(root.id, root.varnas.lastIndex, listOf(Svara.AA), sutra) else context
        val augment = DerivationTerm(
            id = "${root.id}-puk", surface = "पुँक्", upadesha = "पुँक्",
            kind = TermKind.AGAMA, createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = root.id,
        )
        return DerivationChange(base.addTerm(augment), "7.3.36 introduces raw पुक् for placement at the end of the ā-final root before णिच्.")
    }

    private fun krIJi(term: DerivationTerm): Boolean = term.kind == TermKind.DHATU && term.upadesha in setOf("जि", "डुक्रीञ्", "क्री")

    private fun eligibleRoot(term: DerivationTerm): Boolean =
        term.kind == TermKind.DHATU && term.varnas.lastOrNull() == Svara.AA && term.surface !in ShachaYukSutra.roots &&
            listedRootEndsIn(term, setOf(Svara.AA, Svara.E, Svara.AI, Svara.O))
}

/**
 * The listed root of a term, found by its upadeśa or, where a derivation carries the marker-free
 * root (स्था for ष्ठा, मे for मेङ्), by that surface; true when it ends in one of [finals].
 */
internal fun listedRootEndsIn(term: DerivationTerm, finals: Set<Svara>): Boolean = DhatuPatha.all.any {
    (it.upadesha == term.upadesha && it.sourceVarnas.lastOrNull() in finals) ||
        (it.derivationalSurface == term.upadesha &&
            it.derivationalSurface.toVarnas().lastOrNull() in finals)
}

/** 7.3.37: शाच्छासाह्वाव्यावेपां युक्, as in पाययति and ह्वाययति. */
object ShachaYukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.37", text = "शाच्छासाह्वाव्यावेपां युक्",
    hindiExplanation = "शा, छा, सा, ह्वा, व्या, वे, पा को णिच् परे युक् होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730037, role = SutraRole.Vidhi, action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    internal val roots = setOf("शा", "छा", "सा", "ह्वा", "व्या", "वा", "पा")
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { it.matchesAffix(SanadiAffix.NIC) } &&
            context.terms.any { it.kind == TermKind.DHATU && it.surface in roots } &&
            context.allEffectiveTerms.none { it.createdBySutra == sutra }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { it.kind == TermKind.DHATU && it.surface in roots }
        val augment = DerivationTerm(id = "${root.id}-yuk", surface = "युँक्", upadesha = "युँक्",
            kind = TermKind.AGAMA, createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA, augmentTargetId = root.id)
        return DerivationChange(context.addTerm(augment), "7.3.37 introduces युक् after ${root.surface} before ṇic.")
    }
}

/** 6.1.45: आदेच उपदेशेऽशिति, limited here to ṇic, so गै becomes गा and then takes पुक्. */
object AdecaUpadeshaNicSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.45", text = "आदेच उपदेशेऽशिति",
    hindiExplanation = "उपदेश में एच्-अन्त धातु को अशित् प्रत्यय परे आ होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false,
    kramaValue = 610045, role = SutraRole.Vidhi, action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION, stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    private val ecFinals = setOf(Svara.E, Svara.AI, Svara.O)
    /** "In upadeśa": the root as listed must end in एच्, not a form reached by vṛddhi or guṇa. */
    private fun eligible(term: DerivationTerm) = term.kind == TermKind.DHATU && term.varnas.lastOrNull() in ecFinals &&
        listedRootEndsIn(term, ecFinals)
    /** अशिति: the next affix has had its it-letters found, and श् is not among them. */
    private fun followedByAshit(context: DerivationState, root: DerivationTerm): Boolean {
        val next = context.terms.dropWhile { it !== root }.drop(1).firstOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        if (next.matchesAffix(SanadiAffix.NIC)) return true
        // मयते: in a sārvadhātuka lakāra the śit vikaraṇa will stand between root and ending, so wait for it.
        if (context.effectiveContext.rupa.lakara in setOf(dev.panini.core.Lakara.LAT, dev.panini.core.Lakara.LOT,
                dev.panini.core.Lakara.LANG, dev.panini.core.Lakara.LING) &&
            (next.upadesha in setOf("लट्", "लोट्", "लङ्", "लिङ्") || dev.panini.core.TingAffix.entries.any(next::hasCurrentAffix))) return false
        return !next.itProcessingPending && !next.hasEffectiveMarker(dev.panini.core.ItMarker.SH)
    }

    override fun matches(context: DerivationState): Boolean =
        context.terms.any { eligible(it) && followedByAshit(context, it) }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { eligible(it) && followedByAshit(context, it) }
        val replaced = context.replaceTermVarna(root.id, root.varnas.lastIndex, listOf(Svara.AA), sutra)
        // विनिमाय, ध्यातः: before any other aśit affix only the vowel changes.
        if (context.terms.none { it.matchesAffix(SanadiAffix.NIC) }) return DerivationChange(replaced,
            "6.1.45 replaces the final एच् of ${root.surface} with आ before an aśit affix.")
        val needsYuk = root.upadesha in setOf("ह्वेञ्", "ह्वे", "व्येञ्", "व्ये", "वेञ्", "वे", "शो", "छो", "षो", "सो")
        if (needsYuk) return DerivationChange(replaced, "6.1.45 replaces the final एच् of ${root.surface} with आ before ṇic.")
        // Replacing the vowel also rewrites the term's upadeśa, so पुक् (7.3.36) is attached here for this root.
        val puk = DerivationTerm(id = "${root.id}-puk", surface = "पुँक्", upadesha = "पुँक्",
            kind = TermKind.AGAMA, createdBySutra = ArtihriPukSutra.sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA, augmentTargetId = root.id)
        return DerivationChange(replaced.addTerm(puk), "6.1.45 replaces the final एच् of ${root.surface} with आ before ṇic, and पुक् follows.")
    }
}

