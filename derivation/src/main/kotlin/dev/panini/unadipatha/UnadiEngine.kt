package dev.panini.unadipatha

import dev.panini.shiksha.Samjna
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.ashtadhyayi.adhyaya7.pada2.AcoNnitiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EcoYavayavahSutra

class UnadiEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(
        listOf(
            AcoNnitiSutra,
            EcoYavayavahSutra,
            UnadiYugAgamaSutra,
            UnadiTutAgamaSutra,
            UnadiMutAgamaSutra,
            UnadiAdjustmentsSutra
        )
    )
) {
    fun derive(root: String, suffix: String): String? {
        val initialState = UnadiState(root = root, suffix = suffix)
        val sutra = UnadiPatha.sutras.firstOrNull { it.matches(initialState) } ?: return null

        val unadiChange = sutra.apply(initialState)
        val stateAfterUnadi = unadiChange.state

        val rootTerm = DerivationTerm("dhatu", root, TermKind.DHATU)
        val suffixTerm = DerivationTerm(
            id = "unadi_${sutra.number}",
            surface = stateAfterUnadi.surface.substringAfter(root),
            kind = TermKind.PRATYAYA,
            itMarkers = stateAfterUnadi.itMarkers,
            upadesha = stateAfterUnadi.suffix ?: "",
        )

        val blocked = if (sutra.number == "1.3") {
            mapOf("3.1.91" to "unadi", "7.2.115" to "1.1.adjust")
        } else {
            mapOf("3.1.91" to "unadi")
        }

        val ashtadhyayiState = DerivationState(
            terms = listOf(rootTerm, suffixTerm),
            samjnas = setOf(
                SamjnaAssignment(rootTerm.id, Samjna.DHATU),
                SamjnaAssignment(suffixTerm.id, Samjna.PRATYAYA)
            ),
            activeAdhikaras = setOf("6.4.1"),
            blockedSutras = blocked,
            stage = DerivationStage.PRATYAYA_SELECTED,
        )

        val result = derivationEngine.derive(ashtadhyayiState)
        return result.final.surface.takeIf { it.isNotEmpty() }
    }
}
