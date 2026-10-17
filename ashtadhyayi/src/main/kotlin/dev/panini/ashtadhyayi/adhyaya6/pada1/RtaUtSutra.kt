package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 6.1.111: short ṛ + the a of ṅasi/ṅas share u, rapara by 1.1.51. */
object RtaUtSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.111", text = "ऋत उत्",
    hindiExplanation = "ऋकार और ङसि/ङस् के अकार के स्थान पर उकार एकादेश, उरण् रपरः से रपर, होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 1, optional = false,
    kramaValue = 610111, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION, priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        return stem.kind == TermKind.PRATIPADIKA && stem.varnas.lastOrNull() == Svara.R &&
            suffix.itProcessingPhase == ItProcessingPhase.PROCESSED && suffix.varnas.firstOrNull() == Svara.A &&
            listOf(SupAffix.NGASI, SupAffix.NGAS).any {
                suffix.sourceSupAffix == it && suffix.hasCurrentAffix(it)
            }
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val stem = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        val index = stem.varnas.lastIndex
        return DerivationChange(context
            .replaceTermVarna(stem.id, index, listOf(Svara.U, Vyanjana.RA), sutra)
            .deleteTermVarnas(suffix.id, 0, 1, sutra)
            .concatenateAfterInitialVowelCoalescence(stem.id, suffix, sutra,
                coalescedLeftVarnaIndices = index..index)
            .copy(stage = DerivationStage.PADA_FORMED),
            "6.1.111 coalesces ṛ and affix-a to u, rapara by 1.1.51; the remaining s is retained for 8.2.24.")
    }
}
