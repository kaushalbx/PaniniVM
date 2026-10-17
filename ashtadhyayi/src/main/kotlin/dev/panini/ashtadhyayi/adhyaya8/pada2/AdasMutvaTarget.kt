package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Vyanjana

internal data class AdasMutvaTarget(val stem: DerivationTerm, val dIndex: Int)

/** Shared source domain of 8.2.80/81. An incorporated sup's terminal s/r
 * cannot identify the lexical head: eligibility uses the actual a-for-s history. */
internal fun adasMutvaTarget(context: DerivationState): AdasMutvaTarget? {
    val stem = context.terms.firstOrNull() ?: return null
    if (stem.upadesha != "अदस्") return null
    if (context.allEffectiveTerms.none { it.sourceSupAffix != null }) return null
    if (context.substitutions.none { it.targetId == stem.id && it.sutra == "7.2.102" }) return null
    val index = stem.varnas.indexOf(Vyanjana.DA)
    if (index < 0 || index == stem.varnas.lastIndex) return null
    return AdasMutvaTarget(stem, index)
}
