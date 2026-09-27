package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

internal fun DerivationTerm.isSFinal(): Boolean = varnas.lastOrNull() == Vyanjana.SA

internal fun DerivationTerm.replaceFinalS(replacement: List<Varna>): List<Varna> =
    if (varnas.takeLast(2) == listOf(Svara.A, Vyanjana.SA) &&
        varnas.getOrNull(varnas.lastIndex - 2) is Vyanjana
    ) {
        varnas.dropLast(2) + replacement
    } else {
        val conditionedReplacement = if (varnas.getOrNull(varnas.lastIndex - 1) is Svara) {
            replacement.dropWhile { it == Svara.A }
        } else {
            replacement
        }
        varnas.dropLast(1) + conditionedReplacement
    }

internal fun DerivationTerm.extendFinalS(extension: List<Varna>): List<Varna> =
    varnas + extension
