package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

data class KarakaResolution(
    val candidates: Set<Karaka>,
    val resolved: Karaka?,
    val possibleVibhaktis: Set<Vibhakti>,
    val evidence: List<KarakaEvidence>,
    val resolvedVibhakti: Vibhakti? = null,
)
