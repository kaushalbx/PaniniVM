package dev.panini.shiksha

/** The unaspirated voiceless car member of a stop's own class. Nasals,
 * semivowels and sibilants are not converted into an arbitrary dental stop. */
fun Varna.carStopCounterpart(): Vyanjana? = when (this) {
    Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA -> Vyanjana.KA
    Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA -> Vyanjana.CA
    Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA -> Vyanjana.TTA
    Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA -> Vyanjana.TA
    Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA -> Vyanjana.PA
    else -> null
}
