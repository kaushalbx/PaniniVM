package dev.panini.analysis

import dev.panini.derivation.Purusha
import dev.panini.derivation.Vacana

data class TingAnalysis(
    val pratyaya: String,
    val purusha: Purusha,
    val vacana: Vacana,
    val pada: PadaType,
)
