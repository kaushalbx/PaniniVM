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
                        stemClass = SubantaStemClass.guess(stem),
                    ),
                ).final.surface
            },
        )
    }
}
