package dev.panini.analysis

import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas

data class DhatuIdentity(
    val surface: String,
    val sakarmaka: Boolean = true,
) {
    val varnas: List<Varna> by lazy(LazyThreadSafetyMode.PUBLICATION) { surface.toVarnas() }
}
