package dev.panini.analysis

import dev.panini.core.LopaType

/** An established affix-elision event; a missing written affix alone does not prove lopa. */
data class TaddhitaLopaEvidence(val lopa: LopaType, val sutra: String)
