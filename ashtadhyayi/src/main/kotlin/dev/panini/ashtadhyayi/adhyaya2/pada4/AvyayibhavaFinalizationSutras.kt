package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 2.4.18 अव्ययीभावश्च — the compound is neuter. */
object AvyayibhavasCaNapumsakamSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.18", text = "अव्ययीभावश्च",
    hindiExplanation = "अव्ययीभाव समास नपुंसकलिङ्ग होता है।",
    type = SutraType.NITYA, chapter = 2, pada = 4, optional = false, kramaValue = 240018,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
    stage = SutraStage.SAMJNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.samasaType == SamasaType.AVYAYIBHAVA &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && "2.4.18" !in it.establishedBySutras }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.PRATIPADIKA }
        val state = context.replaceTerm(stem.id, stem.copy(establishedBySutras = stem.establishedBySutras + sutra))
            .copy(context = context.context.copy(rupa = context.context.rupa.copy(linga = Linga.NAPUMSAKA)))
        return DerivationChange(state, "2.4.18 establishes neuter gender for the avyayībhāva compound.")
    }
}

/** 2.4.82 अव्ययादाप्सुपः — luk of sup after an indeclinable. */
object AvyayadApsupahSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.82", text = "अव्ययादाप्सुपः",
    hindiExplanation = "अव्यय से परे सुप् प्रत्यय का लुक् होता है।",
    type = SutraType.NITYA, chapter = 2, pada = 4, optional = false, kramaValue = 240082,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
    stage = SutraStage.PADA_FORMATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        val sup = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isAvyaya = SamjnaAssignment(stem.id, Samjna.AVYAYA) in context.samjnas
        val neuterHrasvaPending = context.effectiveContext.rupa.linga == Linga.NAPUMSAKA &&
            stem.surface.lastOrNull() in setOf('ा', 'ी', 'ू', 'ॄ')
        return isAvyaya && !neuterHrasvaPending &&
            !(context.context.samasaType == SamasaType.AVYAYIBHAVA && isAdanta(stem.surface)) &&
            sup.droppedBySutra == null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sup = context.terms.last { it.kind == TermKind.PRATYAYA }
        return DerivationChange(
            context.removeTerm(sup.id, sutra).copy(stage = DerivationStage.FINAL),
            "2.4.82 applies luk to sup after the indeclinable.",
        )
    }

    private fun isAdanta(surface: String): Boolean = surface.lastOrNull()?.let { it !in setOf('ा','ि','ी','ु','ू','ृ','ॄ','ॢ','े','ै','ो','ौ','ं','ः','्') } == true
}

/** 2.4.83 नाव्ययीभावादतोऽम्त्वपञ्चम्याः. */
object NavyayibhavadAtoAmtvapancamyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.83", text = "नाव्ययीभावादतोऽम्त्वपञ्चम्याः",
    hindiExplanation = "अदन्त अव्ययीभाव से परे, पञ्चमी को छोड़कर, सुप् के स्थान पर अम् होता है।",
    type = SutraType.APAVADA, chapter = 2, pada = 4, optional = false, kramaValue = 240083,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.PADA_FORMATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        val sup = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return context.context.samasaType == SamasaType.AVYAYIBHAVA && isAdanta(stem.surface) &&
            context.context.rupa.vibhakti != Vibhakti.PANCHAMI && sup.upadesha != "अम्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sup = context.terms.last { it.kind == TermKind.PRATYAYA }
        return DerivationChange(
            context.replaceWholeAffix(sup.id, "अम्", sutra, WholeAffixDesignationPolicy.Consume, upadesha = "अम्"),
            "2.4.83 substitutes अम् for sup after an a-final avyayībhāva.",
        )
    }

    private fun isAdanta(surface: String): Boolean = surface.lastOrNull()?.let { it !in setOf('ा','ि','ी','ु','ू','ृ','ॄ','ॢ','े','ै','ो','ौ','ं','ः','्') } == true
}
