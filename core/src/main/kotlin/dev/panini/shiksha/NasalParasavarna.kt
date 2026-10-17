package dev.panini.shiksha

/** A nasal counterpart: stops have a nasal member; y/v/l need token nasalization. */
data class NasalParasavarna(val varna: Vyanjana, val nasalized: Boolean = false)

fun Varna.nasalParasavarna(): NasalParasavarna? = when (this) {
    Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA -> NasalParasavarna(Vyanjana.NGA)
    Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA, Vyanjana.NYA -> NasalParasavarna(Vyanjana.NYA)
    Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA, Vyanjana.NNA -> NasalParasavarna(Vyanjana.NNA)
    Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA -> NasalParasavarna(Vyanjana.NA)
    Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA, Vyanjana.MA -> NasalParasavarna(Vyanjana.MA)
    Vyanjana.YA, Vyanjana.VA, Vyanjana.LA -> NasalParasavarna(this as Vyanjana, nasalized = true)
    else -> null // In particular, ra has no nasal counterpart.
}
