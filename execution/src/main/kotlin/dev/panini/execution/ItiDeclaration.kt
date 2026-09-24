package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.invocations

/** Typed structural form of a nominal `… इति …` declaration. */
internal data class ItiDeclaration(
    val declaredPadas: List<Pada>,
    val markerPadas: List<Pada>,
) {
    val nominativeMarker: SubantaPada?
        get() = markerPadas.filterIsInstance<SubantaPada>().singleOrNull()?.takeIf {
            SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.PRATHAMA
        }
}

internal object ItiDeclarationAnalyzer {
    fun analyze(ukti: Ukti): ItiDeclaration? {
        val quotation = ukti.body as? Quotation
        if (quotation != null) {
            return ItiDeclaration(
                quotation.quoted.vakya.padas,
                quotation.reporting.invocations().flatMap { it.vakya.padas },
            )
        }
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val iti = padas.indexOfFirst { (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE }
        if (iti < 0) return null
        return ItiDeclaration(padas.take(iti), padas.drop(iti + 1))
    }
}
