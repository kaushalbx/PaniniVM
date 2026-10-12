package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** A written display object fills the role; sequence results must not duplicate it. */
object PriorResultInjection {
    fun hasExplicitDisplayObject(node: Invocation): Boolean {
        val verb = node.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return false
        if (verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.MUDR) return false
        fun objectEnding(sup: SupPratyaya) = SupAffix.candidates(sup.text).any {
            it.vibhakti == Vibhakti.DVITIYA
        }
        return node.vakya.padas.any { when (it) {
            is SubantaPada -> objectEnding(it.sup)
            is SankhyaPada -> objectEnding(it.sup)
            is SankhyaPuranaPada -> objectEnding(it.sup)
            is SamuccitaSubanta -> it.members.any { member -> objectEnding(member.sup) }
            else -> false
        } }
    }
}
