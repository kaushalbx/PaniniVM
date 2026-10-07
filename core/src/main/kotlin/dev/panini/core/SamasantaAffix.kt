package dev.panini.core

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

/** Grammatical samāsānta identity, distinct from the affix's operative sounds. */
enum class SamasantaAffix(
    override val upadesha: String,
    val varnas: List<Varna>,
    override val itMarkers: Set<ItMarker>,
) : TypedAffix {
    KAP("कप्", listOf(Vyanjana.KA, Svara.A), setOf(ItMarker.P));

    override val initialSurface: String get() = varnas.toDevanagari()
}
