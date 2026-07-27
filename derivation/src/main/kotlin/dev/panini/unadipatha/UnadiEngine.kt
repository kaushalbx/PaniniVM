package dev.panini.unadipatha

import dev.panini.shiksha.Samjna
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraVisibility
import dev.panini.ashtadhyayi.adhyaya7.pada2.AcoNnitiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EcoYavayavahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.VatoYukSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AtaUpadhayahSutra

object UnadiBlockerSutra : Sutra<DerivationState, DerivationChange>(
    number = "unadi", text = "उणादि-बाधः",
    hindiExplanation = "",
    type = SutraType.NITYA, chapter = 0, pada = 0, optional = false, kramaValue = 0,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    visibility = SutraVisibility.ASIDDHAVAT
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = true
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "unadi-blocker")
}

class UnadiEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(
        listOf(
            AcoNnitiSutra,
            EcoYavayavahSutra,
            VatoYukSutra,
            AtaUpadhayahSutra,
            UnadiBlockerSutra
        ) + UnadiPatha.sutras.filterIsInstance<DerivationSutra>()
    )
) {
    fun derive(root: String, suffix: String): String? {
        val initialState = UnadiState(root = root, suffix = suffix)
        val sutra = UnadiPatha.sutras.firstOrNull { it.matches(initialState) } ?: return null

        val unadiChange = sutra.apply(initialState)
        val stateAfterUnadi = unadiChange.state

        val rootTerm = DerivationTerm("dhatu", stateAfterUnadi.root, TermKind.DHATU)
        val suffixTerm = DerivationTerm(
            id = "unadi_${sutra.number}",
            surface = stateAfterUnadi.surface.substringAfter(stateAfterUnadi.root),
            kind = TermKind.PRATYAYA,
            itMarkers = stateAfterUnadi.itMarkers,
            upadesha = stateAfterUnadi.suffix ?: "",
        )

        val blocked = if (sutra.number == "1.3") {
            mapOf("3.1.91" to "unadi", "7.2.115" to "unadi", "7.2.116" to "unadi")
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
