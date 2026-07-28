package dev.panini.unadipatha.sutra

import dev.panini.derivation.sutra.DerivationAvastha
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaRegistry
import dev.panini.sutra.runtime.SutraId
import dev.panini.unadipatha.UnadiPatha

/**
 * Complete Uṇādipāṭha catalogue grantha created directly from [UnadiPatha.sutras].
 */
object UnadipathaRuntimeGrantha {
    val id: GranthaId = GranthaId("unadipatha")

    private val localIds: Set<SutraId> = UnadiPatha.sutras.mapTo(linkedSetOf()) {
        SutraId(it.number)
    }

    val grantha: SutraGrantha<DerivationAvastha> = SutraGrantha(
        id = id,
        sutras = UnadiPatha.sutras.map {
            it.toRuntimeSutra()
        },
        exports = localIds,
    )
    val registry: SutraGranthaRegistry = SutraGranthaRegistry(listOf(grantha))
}
