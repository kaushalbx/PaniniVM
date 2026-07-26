package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.core.TingAffix

fun SupAffix.term(): DerivationTerm = DerivationTerm(id, initialSurface, TermKind.PRATYAYA, itMarkers, upadesha)

fun SupAffix.Companion.fromContext(context: DerivationalContext): SupAffix? {
    val vibhakti = context.rupa.vibhakti ?: return null
    val vacana = context.rupa.vacana ?: return null
    return SupAffix.entries.singleOrNull { it.vibhakti == vibhakti && it.vacana == vacana }
}

fun TingAffix.term(): DerivationTerm = DerivationTerm(termId, upadesha, TermKind.PRATYAYA, upadesha = upadesha)
