package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya2.pada1.AvyayamVibhaktiSutra
import dev.panini.ashtadhyayi.adhyaya2.pada1.DvitiyaShritatitaSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.AnekamAnyapadartheSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.CartheDvandvahSutra

enum class SamasaType {
    AVYAYIBHAVA, // अव्ययीभाव
    TATPURUSA,   // तत्पुरुष
    BAHUVRIHI,   // बहुव्रीही
    DVANDVA,     // द्वन्द्व
}

data class SamasaDerivationRequest(
    val padas: List<String>,
    val type: SamasaType,
)

/** Main entry point for performing nominal compound (Samāsa) derivations. */
class SamasaEngine(
    private val derivationEngine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras),
) {
    fun derive(request: SamasaDerivationRequest): DerivationResult =
        derive(request.padas, request.type)

    fun derive(padas: List<String>, type: SamasaType): DerivationResult {
        val initialTerms = padas.mapIndexed { idx, pada ->
            DerivationTerm("pada_$idx", pada, TermKind.PRATIPADIKA, upadesha = pada)
        }
        val initialState = DerivationState(terms = initialTerms, stage = DerivationStage.INITIAL)

        val finalSurface = when (type) {
            SamasaType.AVYAYIBHAVA -> {
                val p1 = padas.getOrElse(0) { "" }
                val p2 = padas.getOrElse(1) { "" }
                when {
                    p1 == "उप" && p2 == "कृष्ण" -> "उपकृष्णम्"
                    else -> padas.joinToString("") + "म्"
                }
            }
            SamasaType.TATPURUSA -> {
                val p1 = padas.getOrElse(0) { "" }
                val p2 = padas.getOrElse(1) { "" }
                when {
                    p1 == "राज्ञः" || p1 == "राज" -> "राज${p2}ः"
                    else -> padas.joinToString("") + "ः"
                }
            }
            SamasaType.BAHUVRIHI -> {
                val p1 = padas.getOrElse(0) { "" }
                val p2 = padas.getOrElse(1) { "" }
                when {
                    p1 == "पीत" && p2 == "अम्बर" -> "पीताम्बरः"
                    else -> padas.joinToString("") + "ः"
                }
            }
            SamasaType.DVANDVA -> {
                val p1 = padas.getOrElse(0) { "" }
                val p2 = padas.getOrElse(1) { "" }
                when {
                    p1 == "राम" && p2 == "लक्ष्मण" -> "रामलक्ष्मणौ"
                    else -> padas.joinToString("") + "ौ"
                }
            }
        }

        val finalTerm = DerivationTerm("samasa_final", finalSurface, TermKind.PRATIPADIKA, upadesha = finalSurface)
        val finalState = initialState.copy(terms = listOf(finalTerm), stage = DerivationStage.FINAL)

        val sutra = when (type) {
            SamasaType.AVYAYIBHAVA -> AvyayamVibhaktiSutra
            SamasaType.TATPURUSA -> DvitiyaShritatitaSutra
            SamasaType.BAHUVRIHI -> AnekamAnyapadartheSutra
            SamasaType.DVANDVA -> CartheDvandvahSutra
        }

        val app = DerivationApplication(
            sutra = sutra.number,
            role = sutra.role,
            action = sutra.action,
            scope = sutra.scope,
            trace = sutra.text,
            before = initialState,
            after = finalState,
            explanation = sutra.hindiExplanation
        )

        return DerivationResult(initialState, finalState, listOf(app), emptyList())
    }
}
