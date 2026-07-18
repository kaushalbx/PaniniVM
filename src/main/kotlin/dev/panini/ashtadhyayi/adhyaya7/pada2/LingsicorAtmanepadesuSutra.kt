package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 7.2.42: लिङ्सिचोरात्मनेपदेषु. */
object LingsicorAtmanepadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.42", text = "लिङ्सिचोरात्मनेपदेषु",
    hindiExplanation = "आत्मनेपद में लिङ् और सिच् प्रत्ययों से पहले विकल्प से इट् आगम होता है।",
    type = SutraType.VIBHASHA, chapter = 7, pada = 2, optional = true, kramaValue = 720042,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val isAtmanepada = ending.upadesha in setOf("त", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्")
        if (!isAtmanepada) return false
        val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" && it.surface == "स्" }
        return sicIndex > 0 && context.allEffectiveTerms.none { it.id == "it-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" }
        val itAgama = DerivationTerm("it-agama", "इ", TermKind.AGAMA, upadesha = "इट्")
        return DerivationChange(
            context.copy(
                terms = context.terms.take(sicIndex) + itAgama + context.terms.drop(sicIndex),
                stage = DerivationStage.IT_PROCESSED,
            ),
            "7.2.42 optionally inserts इट् before सिच् in the Atmanepada.",
        )
    }
}
