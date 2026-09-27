package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

internal fun DerivationTerm.isNFinal(): Boolean = varnas.lastOrNull() == Vyanjana.NA

internal fun DerivationTerm.replaceFinalAn(replacement: List<Varna>): String {
    require(isNFinal()) { "Strong n-stem substitution requires final n in $surface." }
    val retained = if (varnas.takeLast(2) == listOf(Svara.A, Vyanjana.NA)) {
        varnas.dropLast(2)
    } else {
        varnas.dropLast(1)
    }
    return (retained + replacement).toDevanagari()
}

internal fun DerivationTerm.extendFinalN(extension: List<Varna>): String =
    (varnas + extension).toDevanagari()
