package dev.panini.execution

import dev.panini.core.Lakara
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.TingantaPada

/** Morphological recognition of the unprefixed imperative तिष्ठ used inside a loop. */
object NaturalLoopStop {
    fun matches(invocation: Invocation): Boolean {
        val head = invocation.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return false
        return invocation.vakya.padas.size == 1 &&
            head.dhatu.mulaDhatu == "स्था" &&
            head.dhatu.sanadiPratyayas.isEmpty() &&
            head.upasargas.isEmpty() &&
            head.lakara == Lakara.LOT &&
            head.ting.text == "सिप्"
    }
}
