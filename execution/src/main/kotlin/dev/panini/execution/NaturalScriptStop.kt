package dev.panini.execution

import dev.panini.core.Lakara
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.TingantaPada

/** वि + रम् + लोट् + सिप् (विरम): finish the current script. */
object NaturalScriptStop {
    fun matches(invocation: Invocation): Boolean {
        val head = invocation.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return false
        return invocation.vakya.padas.size == 1 &&
            head.dhatu.mulaDhatu == "रम्" &&
            head.dhatu.sanadiPratyayas.isEmpty() &&
            head.upasargas == listOf("वि") &&
            head.lakara == Lakara.LOT && head.ting.text == "सिप्"
    }
}
