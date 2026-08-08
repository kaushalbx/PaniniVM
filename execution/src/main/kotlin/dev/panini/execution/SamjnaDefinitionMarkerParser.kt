package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

/** Recognizes explicit saṃjñā-definition qualifiers following इति. */
object SamjnaDefinitionMarkerParser {
    private val parser = PaniniParser()

    fun hasExplicitMarker(source: String): Boolean {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return false
        val padas = ukti.vakyas.flatMap { it.padas }
        val itiIndices = padas.indices.filter { index ->
            (padas[index] as? AvyayaPada)?.form == "इति"
        }
        return itiIndices.any { itiIndex ->
            padas.drop(itiIndex + 1).filterIsInstance<SubantaPada>().any { it.isDefinitionMarker() }
        }
    }

    private fun SubantaPada.isDefinitionMarker(): Boolean {
        if (SupAffix.fromUpadesha(sup.text)?.vibhakti != Vibhakti.PRATHAMA) return false
        return when (val base = pratipadika) {
            is MulaPratipadika -> base.text in LEXICAL_MARKERS
            is KridantaPratipadika ->
                base.upasargas == listOf("अप") &&
                    base.dhatu.mulaDhatu == "वद्" &&
                    base.krtPratyaya == "घञ्"
            else -> base.samjnaIdentity() in STRUCTURAL_MARKERS
        }
    }

    private val LEXICAL_MARKERS = setOf("संज्ञा", "अपवाद", "नित्य", "अन्तरङ्ग")
    private val STRUCTURAL_MARKERS = setOf("नि + त्य", "अन्तर् + अङ्ग")
}
