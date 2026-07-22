package dev.panini.analysis

import dev.panini.core.PadaType
import dev.panini.core.Purusha
import dev.panini.core.Vacana

data class TingAnalysis(
    val pratyaya: String,
    val purusha: Purusha,
    val vacana: Vacana,
    val pada: PadaType,
)
