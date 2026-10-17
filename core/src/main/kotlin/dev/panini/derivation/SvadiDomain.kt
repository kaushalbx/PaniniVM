package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.shiksha.Samjna

/** Svādi starts at sup (4.1.2) and extends through the nominal suffix domain
 * of chapters four and five. Verbal/kṛt affixes are not included merely
 * because they carry pratyaya designation. */
fun DerivationTerm.isSvadiAffix(): Boolean = kind == TermKind.PRATYAYA &&
    (sourceSupAffix != null || sourceTaddhitaAffix != null ||
        createdBySutra?.substringBefore('.') in setOf("4", "5"))

/** Designation requires settled operative material; śi substitution must be
 * resolved before assigning a designation that excludes sarvanāmasthāna. */
fun DerivationState.isEligibleNonSarvanamasthanaSvadi(affix: DerivationTerm): Boolean {
    if (!affix.isSvadiAffix() || affix.itProcessingPending || affix.itDesignations.isNotEmpty() ||
        affix.deferredItDesignations.isNotEmpty()) return false
    if (samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SARVANAMASTHANA }) return false
    if (affix.upadesha == "शि") return false
    if (effectiveContext.rupa.linga == Linga.NAPUMSAKA) {
        // 7.1.20 is still pending on the original jas/śas; both become śi.
        if (affix.hasCurrentAffix(SupAffix.JAS) || affix.hasCurrentAffix(SupAffix.SAS)) return false
    } else if (affix.hasSarvanamasthanaSupIdentity()) return false
    return true
}
