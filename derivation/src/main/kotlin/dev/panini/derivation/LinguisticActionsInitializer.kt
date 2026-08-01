package dev.panini.derivation

import dev.panini.actions.linguistic.SandhiAction
import dev.panini.actions.linguistic.SubantaDerivationAction
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana

object LinguisticActionsInitializer {
    private var initialized = false

    fun initialize() {
        if (initialized) return
        initialized = true

        val sandhiEngine = SandhiEngine()

        SandhiAction.sandhiHandler = { left: String, right: String ->
            if (left.isEmpty()) right
            else if (right.isEmpty()) left
            else {
                sandhiEngine.join(left, right).final.surface
            }
        }

        SubantaDerivationAction.subantaHandler = { stem: String ->
            val engine = SubantaEngine()
            val request = SubantaDerivationRequest(
                pratipadika = stem,
                vibhakti = Vibhakti.PRATHAMA,
                vacana = Vacana.EKAVACANA,
                stemClass = SubantaStemClass.guess(stem),
            )
            engine.derive(request).final.surface
        }
    }
}
