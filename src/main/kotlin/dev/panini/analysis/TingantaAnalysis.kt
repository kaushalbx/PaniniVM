package dev.panini.analysis

import dev.panini.derivation.Lakara
import dev.panini.dhatupatha.Dhatu

data class TingantaAnalysis(
    val dhatu: Dhatu?,
    val unresolvedDhatu: String?,
    val sanadiPratyayas: List<String>,
    val vikarana: String?,
    val lakara: Lakara?,
    val ting: TingAnalysis?,
    val unresolvedIdentifier: String? = null,
) {
    init {
        if (unresolvedIdentifier != null) {
            require(dhatu == null)
            require(unresolvedDhatu == null)
            require(sanadiPratyayas.isEmpty())
            require(vikarana == null)
            require(lakara == null)
            require(ting == null)
        }
    }

    val isFullyResolved: Boolean
        get() =
            dhatu != null &&
                    lakara != null &&
                    ting != null
}
