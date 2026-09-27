package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.core.TypedAffix

fun SupAffix.term(): DerivationTerm = DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha)

/** Matches a typed sup identity, including identity retained through sthānin substitution. */
fun DerivationTerm.matchesSupAffix(affix: SupAffix): Boolean = matchesUpadesha(affix.upadesha)

fun TypedAffix.term(id: String): DerivationTerm =
    DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha)

fun DerivationTerm.matchesAffix(affix: TypedAffix): Boolean = matchesUpadesha(affix.upadesha)

fun DerivationTerm.matchesAnyAffix(vararg affixes: TypedAffix): Boolean = affixes.any(::matchesAffix)

fun SupAffix.Companion.fromContext(context: DerivationalContext): SupAffix? {
    val vibhakti = context.rupa.vibhakti ?: return null
    val vacana = context.rupa.vacana ?: return null
    return SupAffix.entries.singleOrNull { it.vibhakti == vibhakti && it.vacana == vacana }
}

fun TingAffix.term(): DerivationTerm = DerivationTerm(termId, upadesha, TermKind.PRATYAYA, upadesha = upadesha)
