package dev.panini.derivation

import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.execution.LinguisticServices

object LinguisticActionsInitializer {
    fun services(): LinguisticServices {
        val sandhiEngine = SandhiEngine()
        val subantaEngine = SubantaEngine()
        return LinguisticServices(
            joinSandhi = { left, right ->
                when {
                    left.isEmpty() -> right
                    right.isEmpty() -> left
                    else -> sandhiEngine.join(left, right).final.surface
                }
            },
            deriveSubanta = { stem ->
                subantaEngine.derive(
                    SubantaDerivationRequest(
                        pratipadika = stem,
                        vibhakti = Vibhakti.PRATHAMA,
                        vacana = Vacana.EKAVACANA,
                        // The stem's own gender: ज्ञान gives ज्ञानम्, not ज्ञानः.
                        linga = dev.panini.linganushasanam.PaninianPratipadikaLexicon.findPratipadika(stem)?.linga?.singleOrNull()
                            ?: dev.panini.core.Linga.PUMS,
                    ),
                ).final.surface
            },
        )
    }
}
