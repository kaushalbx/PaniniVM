package dev.panini.execution

import dev.panini.shiksha.applyInitialVrddhi
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.ast.TaddhitaPratyayaClass
import dev.panini.vyakaranam.parser.PaniniParser

/** A child stem derived from a parent by an apatyam taddhita affix. */
data class InheritanceRelation(
    val childStem: String,
    val parentStem: String,
)

object TaddhitaInheritanceEngine {

    private val parser = PaniniParser()
    fun deriveVriddhiStem(parentStem: String): String = applyInitialVrddhi(parentStem)

    /** Detects an inheritance declaration from its parsed taddhita morphology. */
    fun detectInheritanceAdhikara(domainSegmented: String): InheritanceRelation? {
        val ukti = parser.parseOrNull(domainSegmented.trim().trimEnd('।', '॥', ' ')) ?: return null
        val parent = ukti.grammaticalVakyas().asSequence()
            .flatMap { it.padas.asSequence() }
            .filterIsInstance<SubantaPada>()
            .mapNotNull { it.pratipadika as? MulaPratipadika }
            .firstOrNull { pratipadika ->
                pratipadika.vikaras.filterIsInstance<TaddhitaVikara>()
                    .any { it.pratyayaClass == TaddhitaPratyayaClass.APATYA }
            }
            ?.text
            ?: return null
        return InheritanceRelation(
            childStem = deriveVriddhiStem(parent),
            parentStem = parent,
        )
    }
}
