package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.isDirgha
import dev.panini.sutra.*

/** 7.1.23: luk of nominative/accusative singular after a neuter stem. */
object SvamorNapumsakatSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.23", text = "स्वमोर्नपुंसकात्",
    hindiExplanation = "नपुंसकलिङ्ग अङ्ग के बाद सु और अम् का लुक् होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false, kramaValue = 710023,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
    dependencies = setOf("6.4.1"),
    blocks = setOf("7.2.102", "7.2.103", "7.2.106", "7.2.107"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras || context.effectiveContext.rupa.linga != Linga.NAPUMSAKA) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        val affix = context.terms.last()
        // 1.2.47 must establish the neuter stem before selecting between
        // general luk and the short-a exception 7.1.24.
        if ((stem.varnas.lastOrNull() as? Svara)?.isDirgha == true && stem.varnas == stem.upadeshaVarnas) return false
        return stem.kind == TermKind.PRATIPADIKA && affix.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            listOf(SupAffix.SU, SupAffix.AM).any { affix.sourceSupAffix == it && affix.hasCurrentAffix(it) } &&
            "7.1.24" !in affix.establishedBySutras
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        return DerivationChange(context.removeTerm(context.terms.last().id, sutra).copy(stage = DerivationStage.ANGAKARYA),
            "7.1.23 elides the complete neuter su/am affix by luk, retaining its lifecycle record.")
    }
}
