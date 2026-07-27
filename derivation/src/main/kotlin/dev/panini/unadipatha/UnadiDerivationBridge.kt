package dev.panini.unadipatha

import dev.panini.dhatupatha.Dhatu
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna

/**
 * Bridges Uṇādi suffix assignment matches to the Aṣṭādhyāyī DerivationEngine.
 */
object UnadiDerivationBridge {

    /**
     * Constructs an initial DerivationState for a Dhātu and an Uṇādi match.
     */
    fun createInitialState(dhatu: Dhatu, match: UnadiMatch): DerivationState {
        val rootTerm = DerivationTerm(
            id = "root",
            surface = dhatu.sourceSurface.ifEmpty { dhatu.upadesha },
            kind = TermKind.DHATU,
            upadesha = dhatu.upadesha
        )

        val pratyayaTerm = DerivationTerm(
            id = "unadi_${match.sutraNumber}",
            surface = match.pratyayaSurface,
            kind = TermKind.PRATYAYA,
            itMarkers = match.itMarkers,
            upadesha = match.pratyaya
        )

        val samjnas = setOf(
            SamjnaAssignment(rootTerm.id, Samjna.DHATU),
            SamjnaAssignment(pratyayaTerm.id, Samjna.PRATYAYA),
            SamjnaAssignment(pratyayaTerm.id, Samjna.KRT)
        )

        return DerivationState(
            terms = listOf(rootTerm, pratyayaTerm),
            samjnas = samjnas,
            stage = DerivationStage.PRATYAYA_SELECTED
        )
    }
}
