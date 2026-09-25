package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

internal val rToSu: List<Varna> = listOf(Svara.AA)
internal val rToAu: List<Varna> = listOf(Svara.A, Vyanjana.RA, Svara.AU)
internal val rToJas: List<Varna> = listOf(Svara.A, Vyanjana.RA, Svara.A, Ayogavaha.VISARGA)
internal val rToAm: List<Varna> = listOf(Svara.A, Vyanjana.RA, Svara.A, Vyanjana.MA)
internal val rToShas: List<Varna> = listOf(Svara.RR, Vyanjana.NA)
internal val rToNgas: List<Varna> = listOf(Svara.U, Ayogavaha.VISARGA)
internal val rToNgi: List<Varna> = listOf(Svara.A, Vyanjana.RA, Svara.I)

internal fun DerivationTerm.withFinalReplacement(replacement: List<Varna>): String =
    (varnas.dropLast(1) + replacement).toDevanagari()
