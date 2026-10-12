package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana

/** Exact completed pronoun endings, shared by the general rule and its exceptions. */
internal object SupSubstitutionVarnas {
    val smat: List<Varna> = listOf(Vyanjana.SA, Vyanjana.MA, Svara.AA, Vyanjana.TA)
    val smin: List<Varna> = listOf(Vyanjana.SA, Vyanjana.MA, Svara.I, Vyanjana.NA)
    val smai: List<Varna> = listOf(Vyanjana.SA, Vyanjana.MA, Svara.AI)
    val pronounEndings = setOf(smat, smin, smai)
}
