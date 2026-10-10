package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.core.TypedAffix
import dev.panini.core.FrequencyAffix
import dev.panini.shiksha.SanskritText
import dev.panini.shiksha.toSanskritText

/** Projects pronunciation-only upadesha occurrences once, at the term boundary, not in a rule. */
fun FrequencyAffix.rawTerm(id: String, createdBySutra: String): DerivationTerm {
    val tokens = upadesha.toSanskritText().effectiveVarnas
    require(pronunciationOnlyVarnaIndices.all { it in tokens.indices })
    val segments = pronunciationOnlyVarnaIndices.sorted().map { index ->
        val span = requireNotNull(tokens[index].sourceSpan)
        require(span.start < span.endExclusive)
        NonOperativeUpadeshaSegment(span.start, span.endExclusive,
            upadesha.substring(span.start, span.endExclusive), NonOperativeUpadeshaFunction.UCCARANARTHA)
    }
    return DerivationTerm(id,
        SanskritText(tokens.filterIndexed { index, _ -> index !in pronunciationOnlyVarnaIndices }).render(),
        TermKind.PRATYAYA, upadesha = upadesha, createdBySutra = createdBySutra,
        nonOperativeUpadeshaSegments = segments, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
}

fun SupAffix.term(): DerivationTerm = DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha,
    sourceSupAffix = this)

/** Matches a typed sup identity, including identity retained through sthānin substitution. */
fun DerivationTerm.matchesSupAffix(affix: SupAffix): Boolean = matchesUpadesha(affix.upadesha)

fun TypedAffix.term(id: String): DerivationTerm =
    DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha,
        sourceSupAffix = this as? SupAffix, sourceTingAffix = this as? TingAffix)

fun DerivationTerm.matchesAffix(affix: TypedAffix): Boolean =
    matchesUpadesha(affix.upadesha) || affix.alternateUpadeshas.any(::matchesUpadesha)

/** Current affix only: unlike sthānin-aware matching, this cannot reselect a replaced affix. */
fun DerivationTerm.hasCurrentAffix(affix: TypedAffix): Boolean =
    kind == TermKind.PRATYAYA && (upadesha == affix.upadesha || upadesha in affix.alternateUpadeshas)

fun DerivationTerm.matchesAnyAffix(vararg affixes: TypedAffix): Boolean = affixes.any(::matchesAffix)

fun DerivationTerm.matchesAnyAffix(affixes: Iterable<TypedAffix>): Boolean = affixes.any(::matchesAffix)

fun SupAffix.Companion.fromContext(context: DerivationalContext): SupAffix? {
    val vibhakti = context.rupa.vibhakti ?: return null
    val vacana = context.rupa.vacana ?: return null
    return SupAffix.entries.singleOrNull { it.vibhakti == vibhakti && it.vacana == vacana }
}

fun TingAffix.term(): DerivationTerm = DerivationTerm(termId, upadesha, TermKind.PRATYAYA, upadesha = upadesha,
    sourceTingAffix = this)
