package dev.panini.unadipatha

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationResult
import dev.panini.dhatupatha.DhatuPathaRegistration

/**
 * Derivation Engine for Uṇādi stems.
 * Coordinates initial Uṇādi match lookup, bridge state creation, and Aṣṭādhyāyī derivation rule execution.
 */
object UnadiDerivationEngine {

    /**
     * Executes full step-by-step derivation for a given Dhātu and Uṇādi pratyaya.
     */
    fun derive(dhatuSurface: String, pratyaya: String): DerivationResult {
        val dhatu = DhatuPathaRegistration.allDhatus.firstOrNull {
            it.sourceSurface == dhatuSurface || it.upadesha == dhatuSurface
        } ?: UnadiPatha.sutras.flatMap { it.roots }.firstOrNull {
            it.sourceSurface == dhatuSurface || it.upadesha == dhatuSurface
        } ?: error("Dhātu '$dhatuSurface' not found in DhātuPatha or Uṇādipāṭha.")

        val matches = UnadiPatha.findSamjna(dhatu, pratyaya)
        val match = matches.firstOrNull()
            ?: error("No matching Uṇādi sūtra found for ($dhatuSurface, $pratyaya).")

        val initialState = UnadiDerivationBridge.createInitialState(dhatu, match)
        return DerivationEngine(Ashtadhyayi.executableSutras).derive(initialState)
    }
}
